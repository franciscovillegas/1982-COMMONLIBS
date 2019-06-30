package organica.com.eje.ges.expediente;

import java.io.IOException;
import java.util.GregorianCalendar;
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

public class VariacionSueldo extends MyHttpServlet
{

    public VariacionSueldo()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de VariacionSueldo");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Variacion de Sueldo", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_exp_remu", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Variacion de Sueldo", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT nombre, rut, digito_ver FROM view_ges_InfoRut WHERE (rut = ")).append(strRut).append(")")));
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Variacion de Sueldo", "Este rut no existe...");
            } else
            {
                GregorianCalendar Fecha = new GregorianCalendar();
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("rut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(consul.getString("rut")))))).append("-").append(consul.getString("digito_ver")))));
                modelRoot.put("fecha_hoy", valida.validarFecha(Fecha.getTime()));
                Consulta consul2 = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT * FROM eje_ges_historia_sueldos WHERE (rut = ")).append(strRut).append(")").append(" ORDER BY fecha desc")));
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
                consul2.exec(sql);
                SimpleList simplelist = new SimpleList();
                SimpleHash simplehash1;
                for(; consul2.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("fecha", valida.validarFecha(consul2.getValor("fecha")));
                    simplehash1.put("periodo", valida.validarDato(consul2.getString("periodo")));
                    simplehash1.put("haber_antes", valida.validarDato(consul2.getString("haber_antes")));
                    simplehash1.put("haber_despues", valida.validarDato(consul2.getString("haber_despues")));
                    simplehash1.put("neto_antes", valida.validarDato(consul2.getString("neto_antes")));
                    simplehash1.put("neto_despues", valida.validarDato(consul2.getString("neto_despues")));
                    simplehash1.put("por", valida.validarDato(consul2.getString("por_incremento")));
                    simplehash1.put("motivo", valida.validarDato(consul2.getString("motivo")));
                }

                modelRoot.put("variacion", simplelist);
                consul2.close();
                super.retTemplate(resp,"Gestion/Expediente/VariacionSueldo.htm",modelRoot);
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