package organica.com.eje.ges.anexos.visor;

import java.io.IOException;
import java.io.PrintWriter;

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
import freemarker.template.Template;

public class Filtro extends MyHttpServlet
{

    public Filtro()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doGet de vosor.Filtro");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
        {
            mensaje.devolverPaginaSinSesion(resp, "Visor de Anexos", "Tiempo de Sesi\363n expirado...");
        } else
        {
            String strMsg = "";

            SimpleHash modelRoot = new SimpleHash();
            Consulta consul = new Consulta(conexion);
            String sql = "SELECT DISTINCT unid_id AS id, unid_desc AS descrip FROM eje_ges_unidades where vigente='S' order by descrip";
            consul.exec(sql);
            modelRoot.put("unidades", consul.getSimpleList());
            consul.close();
            super.retTemplate(resp,"Gestion/Anexos/Visor/filtros.htm",modelRoot);
        }
        OutMessage.OutMessagePrint("Fin de doGet");
        connMgr.freeConnection("portal", conexion);
    }

    private Usuario user;
    private Tools tool;
}