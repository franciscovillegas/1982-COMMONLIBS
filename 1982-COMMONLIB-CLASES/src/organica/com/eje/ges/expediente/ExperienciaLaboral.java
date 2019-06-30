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

public class ExperienciaLaboral extends MyHttpServlet
{

    public ExperienciaLaboral()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        doPost(req, resp);
        connMgr.freeConnection("portal", Conexion);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de ExperienciaLaboral");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        if(strRut == null || strRut.equals(""))
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Experiencia Laboral", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_exp_explaboral", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Experiencia Laboral", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            String sql = "SELECT rut, digito_ver,nombre, cargo, fecha_ingreso, id_foto, anexo, e_mail  FROM view_ges_InfoRut  WHERE (rut = " + strRut + ")";
            OutMessage.OutMessagePrint("--> " + sql);
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Experiencia Laboral", "Informaci\363n no Disponible...");
            } else
            {
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("rut2", Tools.setFormatNumber(strRut) + "-" + consul.getString("digito_ver"));
                modelRoot.put("rut", strRut);
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("cargo", consul.getString("cargo"));
                modelRoot.put("fecha_ing", valida.validarFecha(consul.getValor("fecha_ingreso")));
                modelRoot.put("foto", consul.getString("id_foto"));
                modelRoot.put("anexo", consul.getString("anexo"));
                modelRoot.put("mail", consul.getString("e_mail"));
                Consulta consul2 = new Consulta(conexion);
                sql = "SELECT empresa,desde,hasta,cargo FROM view_experiencia_laboral WHERE (rut = " + strRut + ")" + " ORDER BY desde";
                OutMessage.OutMessagePrint("--> " + sql);
                consul2.exec(sql);
                SimpleList simplelist = new SimpleList();
                SimpleHash simplehash1;
                for(; consul2.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("empresa", valida.validarDato(consul2.getString("empresa")));
                    simplehash1.put("desde", valida.validarDato(consul2.getString("desde")));
                    simplehash1.put("hasta", valida.validarDato(consul2.getString("hasta")));
                    simplehash1.put("cargo", valida.validarDato(consul2.getString("cargo")));
                }

                modelRoot.put("experiencia", simplelist);
                consul2.close();
                modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
                super.retTemplate(resp,"Gestion/Expediente/ExperienciaLaboral.htm",modelRoot);
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