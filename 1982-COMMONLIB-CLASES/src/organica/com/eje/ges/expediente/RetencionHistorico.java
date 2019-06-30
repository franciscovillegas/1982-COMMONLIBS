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
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class RetencionHistorico extends MyHttpServlet
{

    public RetencionHistorico()
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
        OutMessage.OutMessagePrint("\nEntro al doPost de RetencionHistorico");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Retencion Judicial (Hist\363rico)", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_exp_remu", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Retencion Judicial (Hist\363rico)", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT nombre, rut, digito_ver FROM view_ges_InfoRut WHERE (rut = ")).append(strRut).append(")")));
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Retencion Judicial (Hist\363rico)", "Este rut no existe...");
            } else
            {
                GregorianCalendar Fecha = new GregorianCalendar();
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("rut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(consul.getString("rut"))))).append("-").append(consul.getString("digito_ver")))));
                modelRoot.put("fecha_hoy", valida.validarFecha(Fecha.getTime()));
                Consulta consul2 = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT jh.periodo, p.peri_ano, jh.mes, jh.corr, jh.benef_rut, jh.benef_nom, jh.monto, jh.moneda FROM eje_ges_retencion_judicial_historia jh INNER JOIN eje_ges_periodo p ON jh.periodo = p.peri_id WHERE (jh.rut = ")).append(strRut).append(")")));
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
                consul2.exec(sql);
                SimpleList simplelist = new SimpleList();
                SimpleHash simplehash1;
                for(; consul2.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("mes", valida.validarDato(consul2.getValor("mes")));
                    simplehash1.put("periodo", valida.validarDato(consul2.getString("peri_ano")));
                    simplehash1.put("num", valida.validarDato(consul2.getString("corr")));
                    simplehash1.put("benef_rut", valida.validarDato(consul2.getString("benef_rut")));
                    simplehash1.put("benef_nom", valida.validarDato(consul2.getString("benef_nom")));
                    simplehash1.put("monto", valida.validarDato(consul2.getValor("monto")));
                    simplehash1.put("moneda", valida.validarDato(consul2.getString("moneda")));
                }

                modelRoot.put("retencion", simplelist);
                consul2.close();
                super.retTemplate(resp,"Gestion/Expediente/RetencionHistorico.htm",modelRoot);
            }
            consul.close();
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }


    private Usuario user;
    private Mensaje mensaje;
}