package organica.com.eje.ges.simrenta;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.Indicador.Periodo;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class MantConvCaja extends MyHttpServlet
{

    public MantConvCaja()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de MantConvCaja");
        user = Usuario.rescatarUsuario(req);
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermiso("df_mant_convcaja"))
        {
            mensaje.devolverPaginaMensage(resp, "", "Usted no tiene permiso para Realizar esta Acci\363n...");
        } else
        {
            Consulta consul = new Consulta(conexion);
            Periodo peri = new Periodo(conexion);
            String sql = String.valueOf(String.valueOf((new StringBuilder("DELETE FROM eje_ges_asigcaja WHERE (periodo = ")).append(peri.getPeriodo()).append(")")));
            consul.insert(sql);
            String pIds[] = req.getParameterValues("ids");
            int largo = pIds == null ? 0 : pIds.length;
            for(int x = 0; x < largo; x++)
                if(req.getParameter("elim_".concat(String.valueOf(String.valueOf(pIds[x])))) == null)
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_asigcaja (periodo, cod_familia, cod_condicion, monto) VALUES (")).append(peri.getPeriodo()).append(", '").append(req.getParameter("fam_".concat(String.valueOf(String.valueOf(pIds[x]))))).append("', '").append(req.getParameter("conv_".concat(String.valueOf(String.valueOf(pIds[x]))))).append("', ").append(req.getParameter("monto_".concat(String.valueOf(String.valueOf(pIds[x]))))).append(")")));
                    consul.insert(sql);
                }

            consul.close();
            generaPagina(req, resp, true, user, conexion);
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doGet de MantConvCaja");
        user = Usuario.rescatarUsuario(req);
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermiso("df_mant_convcaja"))
            mensaje.devolverPaginaMensage(resp, "", "Usted no tiene permiso para Realizar esta Acci\363n...");
        else
            generaPagina(req, resp, false, user, conexion);
        OutMessage.OutMessagePrint("Fin de doGet");
        connMgr.freeConnection("portal", conexion);
    }

    public void generaPagina(HttpServletRequest req, HttpServletResponse resp, boolean reload, Usuario user, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro a generaPagina");
        String html = req.getParameter("htm");
        if(html == null)
            html = "MantConvCaja.htm";
        OutMessage.OutMessagePrint("html --> ".concat(String.valueOf(String.valueOf(html))));
        SimpleHash modelRoot = new SimpleHash();
        Consulta consul = new Consulta(conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT cod_familia AS familia, cod_condicion AS convenio, monto FROM eje_ges_asigcaja WHERE (periodo = ")).append((new Periodo(conexion)).getPeriodo()).append(")").append(" ORDER BY cod_familia, cod_condicion")));
        consul.exec(sql);
        modelRoot.put("reload", reload);
        modelRoot.put("datos", consul.getSimpleList());
        modelRoot.put("cant_filas", String.valueOf(consul.getFilasSimpleList()));
        consul.close();
        super.retTemplate(resp,"Gestion/SimRenta/" + html,modelRoot);
        OutMessage.OutMessagePrint("Fin de generaPagina");
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}