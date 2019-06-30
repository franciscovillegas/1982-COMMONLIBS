package portal.com.eje.serhumano.admin.tools;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.ControlAcceso;
import portal.com.eje.serhumano.user.ControlAccesoTM;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.servlet.GetParam;
import portal.com.eje.tools.servlet.GetProp;
import freemarker.template.SimpleHash;

public class S_Tool extends MyHttpServlet
{

    public S_Tool()
    {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Usuario user = SessionMgr.rescatarUsuario(req);
        String pHtm = req.getParameter("htm");
        if(!user.esValido())
            pHtm = "user/user_sin_sesion.htm";
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("GetParam", new GetParam(req));
        modelRoot.put("GetProp", new GetProp(ResourceBundle.getBundle("db")));
        modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
        modelRoot.put("usuario", user.toHash());
        try
        {
        	super.retTemplate(resp,pHtm,modelRoot);
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
}