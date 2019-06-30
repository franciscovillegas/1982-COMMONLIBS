package organica.com.eje.ges.expediente;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class Capacitacion extends MyHttpServlet
{

    public Capacitacion()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        doPost(req, resp);
        connMgr.freeConnection("portal", Conexion);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de Capacitacion");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        if(strRut == null)
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Capacitacion", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_exp_capa", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Capacitacion", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut, digito_ver,nombre, cargo, fecha_ingreso, id_foto,e_mail,anexo FROM view_ges_InfoRut  WHERE (rut = ")).append(strRut).append(")")));
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Capacitacion", "Informaci\363n No Disponible...");
            } else
            {
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("rut", consul.getString("rut"));
                modelRoot.put("rut2", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(strRut))))).append("-").append(consul.getString("digito_ver")))));
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("cargo", consul.getString("cargo"));
                modelRoot.put("fecha_ing", valida.validarFecha(consul.getValor("fecha_ingreso")));
                modelRoot.put("foto", consul.getString("id_foto"));
                modelRoot.put("mail", consul.getString("e_mail"));
                modelRoot.put("anexo", consul.getString("anexo"));
                Consulta consul2 = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT * FROM eje_ges_capacitaciones WHERE (rut = ")).append(strRut).append(")").append(" ORDER BY fecha_inicio")));
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
                consul2.exec(sql);
                SimpleList simplelist = new SimpleList();
                SimpleHash simplehash1;
                for(; consul2.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("curso", valida.validarDato(consul2.getString("curso")));
                    simplehash1.put("org", valida.validarDato(consul2.getString("organismo")));
                    simplehash1.put("fec_ini", valida.validarFecha(consul2.getValor("fecha_inicio")));
                    simplehash1.put("horas", valida.validarDato(consul2.getString("horas")));
                    simplehash1.put("fec_term", valida.validarFecha(consul2.getValor("fecha_termino")));
                    simplehash1.put("resultado", valida.validarDato(consul2.getString("resultado")));
                    simplehash1.put("base_tem", valida.validarDato(consul2.getString("base_tematica")));
                }

                modelRoot.put("cursos", simplelist);
                consul2.close();
                Consulta consul3 = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT TOP 5 * FROM eje_ges_evaluaciones WHERE (rut = ")).append(strRut).append(") ORDER BY periodo desc")));
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
                consul3.exec(sql);
                simplelist = new SimpleList();
                for(; consul3.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("periodo", consul3.getString("periodo"));
                    simplehash1.put("resultado", consul3.getString("resultado"));
                }

                modelRoot.put("evaluaciones", simplelist);
                consul2.close();
                super.retTemplate(resp,"Gestion/Expediente/Capacitacion.htm",modelRoot);
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