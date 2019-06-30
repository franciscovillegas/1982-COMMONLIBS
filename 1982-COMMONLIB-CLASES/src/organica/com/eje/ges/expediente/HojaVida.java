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

public class HojaVida extends MyHttpServlet
{

    public HojaVida()
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
        connMgr.freeConnection("portal", conexion);
        OutMessage.OutMessagePrint("\nEntro al doPost de Hoja de Vida");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        if(strRut == null || strRut.equals(""))
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Hoja de Vida", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_exp_hojavida", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Hoja de Vida", "Usted no tiene permiso para ver esta informaci\363n...");
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
                sql = "SELECT hv.rut, hv.empresa, hv.rut_resp, hv.tipo,hv.fecha,hv.comment, hv.titulo, trab1.nombre,trab2.nombre AS resp FROM eje_ges_hoja_vida_trab hv INNER JOIN eje_ges_trabajador trab1 ON hv.rut = trab1.rut AND hv.empresa = trab1.empresa INNER JOIN eje_ges_trabajador trab2 ON hv.rut_resp = trab2.rut   WHERE (hv.rut = " + strRut + ") " + "order by hv.fecha desc";
                OutMessage.OutMessagePrint("==Hoja de Vida===--> " + sql);
                consul2.exec(sql);
                SimpleList simplelist = new SimpleList();
                int tipo = 0;
                int cont = 1;
                int tipo1 = 0;
                int tipo2 = 0;
                int tipo3 = 0;
                String labTipo = "";
                SimpleHash simplehash1;
                for(; consul2.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    tipo = Integer.parseInt(consul2.getString("tipo"));
                    switch(tipo)
                    {
                    case 30: // '\036'
                        labTipo = "Felicitaciones";
                        tipo1++;
                        break;

                    case 40: // '('
                        labTipo = "Plan de Mejoramiento";
                        tipo2++;
                        break;

                    default:
                        labTipo = "Amonestaciones";
                        tipo3++;
                        break;
                    }
                    simplehash1.put("tipo", labTipo);
                    simplehash1.put("titulo", valida.validarDato(consul2.getString("titulo")));
                    simplehash1.put("comentario", valida.validarDato(consul2.getString("comment")));
                    simplehash1.put("fecha", valida.validarFecha(consul2.getValor("fecha")));
                    simplehash1.put("resp", valida.validarDato(consul2.getString("resp")));
                    simplehash1.put("r_resp", valida.validarDato(consul2.getString("rut_resp")));
                    simplehash1.put("num", String.valueOf(cont));
                    cont++;
                }

                modelRoot.put("feli", String.valueOf(tipo1));
                modelRoot.put("plan", String.valueOf(tipo2));
                modelRoot.put("amon", String.valueOf(tipo3));
                modelRoot.put("detalle", simplelist);
                consul2.close();
                modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
                super.retTemplate(resp,"Gestion/Expediente/HojadeVida.htm",modelRoot);
            }
            consul.close();
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        connMgr.freeConnection("portal", conexion);
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}