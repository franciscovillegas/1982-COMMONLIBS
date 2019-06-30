package portal.com.eje.serhumano.uploadimg;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.admin.CapManager;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

 public class UploadingOrganicaShowServlet extends MyHttpServlet 
   implements javax.servlet.Servlet {
    private Usuario user;

	public UploadingOrganicaShowServlet() 
	{  super(); }   	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException 
	{  
	   final String MPREFIX = "[UploadingOrganicaShowServlet][doGet]";
       OutMessage.OutMessagePrint(MPREFIX + " Entrando a subir Organica...");
       try 
       {  
          SimpleHash modelRoot = new SimpleHash();
          
          modelRoot.put("errors", new SimpleList());
          modelRoot.put("messages", new SimpleList());
          insTracking(request, "Carga Masiva Orgánica".intern(),null );
          Connection conexion = connMgr.getConnection("portal");
          CapManager cm = new CapManager(conexion);
          SimpleList lista = new SimpleList();
          lista = cm.GetListaUsuariosCargo();
          modelRoot.put("lista", lista);
          
          connMgr.freeConnection("portal", conexion);
          
          user = SessionMgr.rescatarUsuario(request);
          if (!user.esValido()) 
          {
        	 super.mensaje.devolverPaginaSinSesion(response,"ERROR de Sesi&oacute;n","Su sesi&oacute;n ha expirado o no se ha logeado.");
             return;
          }      
          super.retTemplate(response,"Gestion/PreCargaOrganica.htm",modelRoot);
       }
       catch (Exception ex) 
       {
    	  ex.printStackTrace();
          throw new ServletException(ex.toString(), ex);
       }
       OutMessage.OutMessagePrint(MPREFIX + " Saliendo de Subida Organica...");
	}  	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException 
	{
		doGet(request, response);
	}
	
}