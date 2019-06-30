package portal.com.eje.serhumano.usermanager;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.tools.servlet.GetParam;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

// Referenced classes of package portal.com.eje.serhumano.usermanager:
//            AssignAccesToRol

public class S_AssignAccesToRol extends MyHttpServlet
{

    public S_AssignAccesToRol()
    {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection conexion = super.connMgr.getConnection("portal");
        SimpleHash modelRoot = new SimpleHash();
        Consulta consul4 = null;
        String rol = "";
        String acceso = "";
        AssignAccesToRol ar = new AssignAccesToRol(conexion);
        if(req.getParameter("grabar") != null)
        {
            System.err.println("grabar ".concat(String.valueOf(String.valueOf(req.getParameter("grabar")))));
            String grabar = req.getParameter("grabar");
            if(grabar.equals("si"))
            {
                ar.DeleteAllAccessToRol();
                consul4 = ar.ConsulAllRolAccess();
                do
                {
                    if(!consul4.next())
                        break;
                    rol = consul4.getString("rol");
                    acceso = consul4.getString("acc_id");
                    if(req.getParameter(String.valueOf(String.valueOf((new StringBuilder("check_")).append(rol).append("_").append(acceso)))) != null)
                    {
                        System.err.println(String.valueOf(String.valueOf((new StringBuilder("grabar check_")).append(rol).append("_").append(acceso))));
                        ar.AddAccessToRol(rol, acceso);
                    }
                } while(true);
                consul4.close();
            }
        }
        modelRoot.put("getParam", new GetParam(req));
        modelRoot.put("roles", ar.SimpleListRol());
        modelRoot.put("accesos", ar.SimpleListAccess());
        Consulta consul = null;
        Consulta consul2 = null;
        Consulta consul3 = null;
        SimpleList simplelist1 = new SimpleList();
        SimpleList simplelist2 = new SimpleList();
        rol = "1";
        for(consul3 = ar.ConsulListaRol(); consul3.next(); consul2.close())
        {
            SimpleHash simplehash1 = new SimpleHash();
            rol = consul3.getString("rol");
            simplehash1.put("rol", rol);
            consul = ar.ConsulAccess();
            simplelist2 = new SimpleList();
            SimpleHash simplehash2;
            for(; consul.next(); simplelist2.add(simplehash2))
            {
                simplehash2 = new SimpleHash();
                simplehash2.put("rol", rol);
                acceso = consul.getString("acc_id");
                simplehash2.put("access", acceso);
                simplehash2.put("acc_desc", consul.getString("acc_glosa"));
                consul2 = ar.ConsulRolAccess(rol, acceso);
                consul2.next();
                if(consul2.getInt("cant") > 0)
                    simplehash2.put("checked", "checked");
            }

            simplehash1.put("datos", simplelist2);
            simplelist1.add(simplehash1);
        }

        modelRoot.put("detallerol", simplelist1);
        consul.close();
        consul3.close();
        try
        {
        	super.retTemplate(resp,"usermanager/assignaccestorol.htm",modelRoot);
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
        modelRoot.put("getError", ar.getError());
        super.connMgr.freeConnection("portal", conexion);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }
}