package portal.com.eje.datosPlantilla;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;

public class ServletInformeCreado extends MyHttpServlet
{

    public ServletInformeCreado()
    {
        try
        {
            jbInit();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        user = SessionMgr.rescatarUsuario(req);
        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "ERROR de Sesi&oacute;n", "Su sesi&oacute;n ha expirado o no se ha logeado.");
        } else
        {
            SimpleHash modelRoot = new SimpleHash();
            modelRoot.put("archivo", req.getParameter("informe"));
            super.retTemplate(resp,"informegestion/informeok.htm",modelRoot);
            OutMessage.OutMessagePrint("\n**** FIN doGet: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        }
    }

    private void jbInit()
        throws Exception
    {
    }

    private Usuario user;
}