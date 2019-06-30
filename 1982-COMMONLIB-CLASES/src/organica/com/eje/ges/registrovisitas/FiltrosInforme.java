package organica.com.eje.ges.registrovisitas;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class FiltrosInforme extends MyHttpServlet
{

    public FiltrosInforme()
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
        OutMessage.OutMessagePrint("\n**********ntro a cargar Ruts Visitados y Motivos para informe de visitas*****");
        user = Usuario.rescatarUsuario(req);
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        if(user.esValido())
        {
            Consulta consul = new Consulta(conexion);
            String sql = "SELECT DISTINCT eje_ges_visita.motivo_id AS id,eje_ges_visita_motivo.mot_desc AS motivo FROM eje_ges_visita INNER JOIN eje_ges_visita_motivo ON eje_ges_visita.motivo_id = eje_ges_visita_motivo.mot_id ORDER BY motivo";
            consul.exec(sql);
            SimpleHash simplehash1;
            for(; consul.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("id", consul.getString("id"));
                simplehash1.put("mot", consul.getString("motivo"));
            }

            modelRoot.put("motivos", simplelist);
            consul.close();
            Consulta consul2 = new Consulta(conexion);
            sql = "SELECT distinct eje_ges_visita.rut_contacto AS rut,eje_ges_trabajador.digito_ver AS dig FROM eje_ges_visita INNER JOIN eje_ges_trabajador ON eje_ges_visita.rut_contacto = eje_ges_trabajador.rut";
            consul2.exec(sql);
            simplelist = new SimpleList();
            for(; consul2.next(); simplelist.add(simplehash1))
            {
                simplehash1 = new SimpleHash();
                simplehash1.put("rut", consul2.getString("rut"));
                simplehash1.put("rutx", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(Tools.setFormatNumber(consul2.getString("rut")))))).append("-").append(consul2.getString("dig")))));
            }

            modelRoot.put("ruts", simplelist);
            consul2.close();
            ControlAcceso control = new ControlAcceso(user);
            modelRoot.put("cerrar_sesion", user.getPermisos().cantidadPermisos() == 1 && control.tienePermiso("df_vigilante"));
            if(req.getParameter("boton") != null)
                modelRoot.put("cerrar_sesion", "1");
            super.retTemplate(resp,"Gestion/RegistroVisitas/ingreso.htm",modelRoot);
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"Gestion/RegistroVisitas/mensaje.htm",modelRoot);
    }

    private Usuario user;
    private Mensaje mensaje;
}