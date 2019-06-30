package portal.com.eje.serhumano.certificados;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;

public class S_Impresion extends MyHttpServlet
{

    public S_Impresion()
    {
    }

    protected void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        doGet(httpservletrequest, httpservletresponse);
    }

    protected void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        java.sql.Connection connection = connMgr.getConnection("portal");
        String certificado = httpservletrequest.getParameter("certificado");
        if(connection != null)
        {
            user = SessionMgr.rescatarUsuario(httpservletrequest);
            SimpleHash modelRoot = new SimpleHash();
            super.retTemplate(httpservletresponse,"certificados/impresion.htm",modelRoot); 
            insTracking(httpservletrequest, "Impresion de Certificado".intern(), certificado);
            OutMessage.OutMessagePrint("\n**** Fin Impresion: " + getClass().getName());
        } else
        {
            mensaje.devolverPaginaMensage(httpservletresponse, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        connMgr.freeConnection("portal", connection);
    }

    private Usuario user;
    private ResourceBundle proper;
}