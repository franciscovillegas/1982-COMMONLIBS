package portal.com.eje.serhumano.user;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.admin.CapManager;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class ListaUsuarios extends MyHttpServlet {

    public ListaUsuarios()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
//    	MMA 20170112
//	    java.sql.Connection conexion = connMgr.getConnection("portal");
	    Connection conexion = getConnMgr().getConnection("portal");
        MuestraDatos(req,resp,conexion);
//    	MMA 20170112
//        connMgr.freeConnection("portal", conexion);
        getConnMgr().freeConnection("portal", conexion);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req,resp);
    }

    public void MuestraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Consh)
        throws ServletException, IOException
    {
        Usuario user = SessionMgr.rescatarUsuario(req);
        CapManager cm = new CapManager(Consh);
        SimpleList lista = new SimpleList();
        super.setExcel(resp,"listaUsuario.xls");
        
        SimpleHash modelRoot = new SimpleHash();
        if(user.esValido())
        {
                lista = cm.GetListaUsuarios();
                modelRoot.put("lista", lista);
                super.retTemplate(resp,"user/listaUsuarios.htm",modelRoot);
        } 
        else 
        {
            mensaje.devolverPaginaMensage(resp, "Time Out", "El tiempo de sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
    }


}