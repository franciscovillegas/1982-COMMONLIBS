package portal.com.eje.serhumano.usermanager;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.tools.servlet.GetParam;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.usermanager:
//            RolManager

public class S_MakeRoles extends MyHttpServlet
{

    public S_MakeRoles()
    {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection conexion = super.connMgr.getConnection("portal");
        SimpleHash modelRoot = new SimpleHash();
        String alert = "";
        String codigo = req.getParameter("codigo");
        String nemo = req.getParameter("nemotecnico");
        String nombre = req.getParameter("nombre");
        System.err.println("cod ".concat(String.valueOf(String.valueOf(req.getParameter("codigo")))));
        System.err.println("nemo ".concat(String.valueOf(String.valueOf(req.getParameter("nemotecnico")))));
        System.err.println("nom ".concat(String.valueOf(String.valueOf(req.getParameter("nombre")))));
        modelRoot.put("getParam", new GetParam(req));
        RolManager rolmanager = new RolManager(conexion);
        if(codigo != "" && nemo != "" && codigo != null && nemo != null)
        {
            if(rolmanager.AddRol(codigo, nombre, nemo))
                alert = "Rol se ingreso con \351xito";
            else
                alert = "Error al ingresar los datos";
            modelRoot.put("alert", alert);
        }
        try
        {
        	super.retTemplate(resp,"usermanager/make_rol.htm",modelRoot);
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
        modelRoot.put("getError", rolmanager.getError());
        super.connMgr.freeConnection("portal", conexion);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }
}