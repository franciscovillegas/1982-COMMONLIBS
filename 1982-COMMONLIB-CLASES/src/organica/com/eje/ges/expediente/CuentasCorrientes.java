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

public class CuentasCorrientes extends MyHttpServlet
{

    public CuentasCorrientes()
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
        OutMessage.OutMessagePrint("\nEntro al doPost de CuentasCorrientes");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        if(strRut == null)
            strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Prestamos", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_exp_pres", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Prestamos", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut,digito_ver,nombre, cargo, sueldo, fecha_ingreso, id_foto,anexo,unidad,e_mail,division,area FROM view_ges_InfoRut WHERE (rut = ")).append(strRut).append(")")));
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Prestamos", "Informaci\363n no disponible...");
            } else
            {
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("rut2", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(consul.getString("rut")))))).append("-").append(consul.getString("digito_ver")))));
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("anexo", valida.validarDato(consul.getString("anexo"), "NR"));
                modelRoot.put("mail", valida.validarDato(consul.getString("e_mail"), "NR"));
                modelRoot.put("unidad", consul.getString("unidad"));
                modelRoot.put("area", consul.getString("area"));
                modelRoot.put("division", consul.getString("division"));
                modelRoot.put("cargo", consul.getString("cargo"));
                modelRoot.put("sueldo", consul.getString("sueldo"));
                modelRoot.put("fecha_ing", valida.validarFecha(consul.getValor("fecha_ingreso")));
                modelRoot.put("foto", consul.getString("id_foto"));
                Consulta deudas = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT ctas.movimiento, ctas.deuda, ctas.fecha, ctas.monto_inicial, ctas.total_cuotas, ctas.cuotas_pendientes, ctas.valor_cuota, ctas.saldo FROM eje_ges_cuentas_corrientes ctas WHERE (ctas.rut = ")).append(strRut).append(")").append(" ORDER BY ctas.fecha")));
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
                deudas.exec(sql);
                SimpleList simplelist = new SimpleList();
                SimpleHash simplehash1;
                for(; deudas.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("movimiento", deudas.getString("movimiento"));
                    simplehash1.put("deuda", deudas.getString("deuda"));
                    simplehash1.put("fecha", valida.validarFecha(deudas.getValor("fecha")));
                    simplehash1.put("monto", deudas.getString("monto_inicial"));
                    simplehash1.put("n_ctas", valida.validarDato(deudas.getString("total_cuotas")));
                    simplehash1.put("n_ctas_pend", valida.validarDato(deudas.getString("cuotas_pendientes"), "0"));
                    simplehash1.put("valor_cta", valida.validarDato(deudas.getString("valor_cuota"), "0"));
                    simplehash1.put("saldo", valida.validarDato(deudas.getString("saldo"), "0"));
                }

                modelRoot.put("deudas", simplelist);
                deudas.close();
                super.retTemplate(resp,"Gestion/Expediente/CartolaCtaCte.htm",modelRoot);
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