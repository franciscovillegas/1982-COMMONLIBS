package organica.com.eje.ges.expediente;

import java.io.IOException;
import java.util.GregorianCalendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.Indicador.Periodo;
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

public class RemuHistoria extends MyHttpServlet
{

    public RemuHistoria()
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
        OutMessage.OutMessagePrint("\nEntro al doPost de RemuHistoria");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = req.getParameter("rut");
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Remuneracion (Hist\363tico)", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermisoJerarquico(conexion, "df_exp_remu", strRut))
        {
            mensaje.devolverPaginaMensage(resp, "Remuneracion (Hist\363tico)", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT nombre, rut, digito_ver FROM view_ges_InfoRut WHERE (rut = ")).append(strRut).append(")")));
            OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            if(!consul.next())
            {
                mensaje.devolverPaginaMensage(resp, "Remuneracion (Hist\363tico)", "Este rut no existe...");
            } else
            {
                GregorianCalendar Fecha = new GregorianCalendar();
                SimpleHash modelRoot = new SimpleHash();
                modelRoot.put("nombre", consul.getString("nombre"));
                modelRoot.put("rut", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(consul.getString("rut")))))).append("-").append(consul.getString("digito_ver")))));
                modelRoot.put("fecha_hoy", valida.validarFecha(Fecha.getTime()));
                Consulta consul2 = new Consulta(conexion);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT top 12 rut, periodo, mes, tot_haberes, tot_descuentos, liquido FROM eje_ges_remuneracion_historia WHERE (rut = ")).append(strRut).append(")").append(" ORDER BY periodo desc, mes desc")));
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
                consul2.exec(sql);
                SimpleList simplelist = new SimpleList();
                SimpleHash simplehash1;
                for(; consul2.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    Periodo peri = new Periodo(conexion, consul2.getString("periodo"));
                    simplehash1.put("year", peri.getYear());
                    simplehash1.put("mes", peri.getMes());
                    simplehash1.put("periodo", valida.validarDato(consul2.getString("periodo")));
                    simplehash1.put("mes", valida.validarDato(consul2.getString("mes")));
                    simplehash1.put("tot_haberes", Tools.setFormatNumber(valida.validarDato(consul2.getString("tot_haberes"))));
                    simplehash1.put("tot_descuentos", Tools.setFormatNumber(valida.validarDato(consul2.getString("tot_descuentos"))));
                    simplehash1.put("liquido", Tools.setFormatNumber(consul2.getInt("tot_haberes") - consul2.getInt("tot_descuentos")));
                }

                modelRoot.put("remuneracion", simplelist);
                consul2.close();
                super.retTemplate(resp,"Gestion/Expediente/RemuHistoria.htm",modelRoot);
            }
            consul.close();
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private Usuario user;
    private Mensaje mensaje;
}