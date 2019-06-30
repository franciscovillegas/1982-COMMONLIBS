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

public class DetalleDeudas extends MyHttpServlet
{

    public DetalleDeudas()
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
        OutMessage.OutMessagePrint("\nEntro al doPost de DetalleDeudas");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Detalle de Deuda", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_exp_pres", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Detalle de Deuda", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            String strMovimiento = req.getParameter("mov");
            Consulta consul = new Consulta(conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT info.nombre, info.rut, info.digito_ver, ctas.deuda, ctas.monto_inicial FROM eje_ges_cuentas_corrientes ctas INNER JOIN view_ges_InfoRut info ON ctas.rut = info.rut WHERE (ctas.movimiento = ")).append(strMovimiento).append(") AND (info.rut = ").append(strRut).append(")")));
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Detalle de Deuda", "Informaci\363n no disponible...");
            } else
            {
                GregorianCalendar Fecha = new GregorianCalendar();
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("rut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(consul.getString("rut")))))).append("-").append(consul.getString("digito_ver")))));
                modelRoot.put("deuda", consul.getString("deuda"));
                modelRoot.put("fecha_hoy", valida.validarFecha(Fecha.getTime()));
                modelRoot.put("monto", consul.getString("monto_inicial"));
                Consulta deudas = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT * FROM eje_ges_cuentas_corrientes_det WHERE (movimiento = ")).append(strMovimiento).append(") AND (rut = ").append(strRut).append(")").append(" ORDER BY fecha")));
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
                deudas.exec(sql);
                SimpleList simplelist = new SimpleList();
                SimpleHash simplehash1;
                for(; deudas.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("movimiento", deudas.getString("movimiento"));
                    simplehash1.put("n_cta", String.valueOf(deudas.getInt("cuota")));
                    simplehash1.put("fecha", valida.validarFecha(deudas.getValor("fecha")));
                    simplehash1.put("estado", valida.validarDato(deudas.getString("estado")));
                }

                modelRoot.put("deudas", simplelist);
                deudas.close();
                super.retTemplate(resp,"Gestion/Expediente/DetalleDeudas.htm",modelRoot);
            }
            consul.close();
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("datafolde_nueva", conexion);
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}