package organica.com.eje.ges.expediente;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class TopExpediente extends MyHttpServlet
{

    public TopExpediente()
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
        OutMessage.OutMessagePrint("\nEntro al doPost de Administracion del tiempo");
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        SimpleHash modelRoot = new SimpleHash();
        user = Usuario.rescatarUsuario(req);
        String strRut = req.getParameter("rut");
        if(strRut == null)
            strRut = user.getRutConsultado();
        else
            modelRoot.put("brut", strRut);
        super.retTemplate(resp,"Gestion/Expediente/top_expedientes.html",modelRoot);
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", Conexion);
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"Gestion/Expediente/mensaje.htm",modelRoot);
    }

    private Usuario user;
    private Mensaje mensaje;
}