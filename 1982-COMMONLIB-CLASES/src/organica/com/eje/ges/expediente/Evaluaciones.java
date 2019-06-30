package organica.com.eje.ges.expediente;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.ControlAccesoTM;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class Evaluaciones extends MyHttpServlet
{

    public Evaluaciones()
    {
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de Evaluaciones");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        if(strRut == null || strRut.equals(""))
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Evaluaciones", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_evaluacion", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Evaluaciones", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            String sql = "SELECT rut, nombre, cargo, fecha_ingreso,digito_ver,id_foto,area,e_mail,id_unidad, unidad,division FROM view_ges_InfoRut WHERE (rut = " + strRut + ")";
            OutMessage.OutMessagePrint("--> " + sql);
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Evaluaciones", "Informaci\363n No Disponible...");
            } else
            {
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("mail", consul.getString("e_mail"));
                modelRoot.put("rut", strRut);
                modelRoot.put("rut2", Tools.setFormatNumber(strRut) + "-" + consul.getString("digito_ver"));
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("cargo", consul.getString("cargo"));
                modelRoot.put("fecha_ing", valida.validarFecha(consul.getValor("fecha_ingreso")));
                modelRoot.put("foto", consul.getString("id_foto"));
                modelRoot.put("unidad", consul.getString("unidad"));
                modelRoot.put("id_unidad", consul.getString("id_unidad"));
                modelRoot.put("area", consul.getString("area"));
                modelRoot.put("division", consul.getString("division"));
                Consulta consul2 = new Consulta(conexion);
                sql = "SELECT rut, empresa, tipo_eval, periodo, nota, eval_final,escala, evaluador,id_tip_eval,id_eval_final FROM vista_evaluaciones WHERE (rut = " + consul.getString("rut") + ") " + "order by periodo desc";
                OutMessage.OutMessagePrint("==Evaluaciones===--> " + sql);
                consul2.exec(sql);
                SimpleList simplelist = new SimpleList();
                SimpleHash simplehash1;
                for(; consul2.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("id1", valida.validarDato(consul2.getString("id_tip_eval")));
                    simplehash1.put("tipo_e", valida.validarDato(consul2.getString("tipo_eval")));
                    simplehash1.put("peri", valida.validarDato(consul2.getString("periodo")));
                    simplehash1.put("escala", valida.validarDato(consul2.getValor("escala")));
                    simplehash1.put("nota", Tools.setFormatNumber(valida.validarDato(consul2.getString("nota")), 2));
                    simplehash1.put("id2", valida.validarDato(consul2.getString("id_eval_final")));
                    simplehash1.put("final", valida.validarDato(consul2.getString("eval_final")));
                    simplehash1.put("evaluador", valida.validarDato(consul2.getString("evaluador")));
                }

                modelRoot.put("evaluaciones", simplelist);
                consul2.close();
                modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
                super.retTemplate(resp,"Gestion/Expediente/Evaluaciones.htm",modelRoot);
            }
            consul.close();
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}