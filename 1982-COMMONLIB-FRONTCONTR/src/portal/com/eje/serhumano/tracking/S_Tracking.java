package portal.com.eje.serhumano.tracking;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.Usuario;

public class S_Tracking extends MyHttpServlet
{

    public S_Tracking()
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
        Connection connection = connMgr.getConnection("portal");
        if(connection != null)
            Registrar(httpservletrequest, httpservletresponse, connection);
        else
            mensaje.devolverPaginaMensage(httpservletresponse, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        connMgr.freeConnection("portal", connection);
    }

    public void Registrar(HttpServletRequest httpservletrequest1, HttpServletResponse httpservletresponse1, Connection connection1)
        throws IOException
    {
    }

    private Usuario user;
}