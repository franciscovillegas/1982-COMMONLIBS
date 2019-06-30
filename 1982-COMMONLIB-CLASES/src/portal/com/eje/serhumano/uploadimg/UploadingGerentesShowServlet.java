package portal.com.eje.serhumano.uploadimg;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;

public class UploadingGerentesShowServlet extends MyHttpServlet implements Servlet {
	 
	private Usuario user;

	public UploadingGerentesShowServlet() {  
		super(); 
	}   	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		
		final String MPREFIX = "[UploadingGerentesShowServlet][doGet]";
		OutMessage.OutMessagePrint(MPREFIX + " Entrando a subir Gerentes...");
		try {  
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			response.setHeader("Expires", "0");
			response.setHeader("Pragma", "no-cache");
			Template template = getTemplate("Gestion/PreCargaGerentes.htm");
			SimpleHash modelRoot = new SimpleHash();
			insTracking(request, "Carga de Gerentes".intern(),null );
			modelRoot.put("errors", new SimpleList());
			modelRoot.put("messages", new SimpleList());
			 
			//Connection conexion = connMgr.getConnection("portal");
			//CapManager cm = new CapManager(conexion);
			//SimpleList lista = new SimpleList();
			//lista = cm.GetListaUsuariosCargo();
			//modelRoot.put("lista", lista);
          
			//connMgr.freeConnection("portal", conexion);
          
			user = SessionMgr.rescatarUsuario(request);
			if (!user.esValido()) {
				super.mensaje.devolverPaginaSinSesion(response,"ERROR de Sesi&oacute;n","Su sesi&oacute;n ha expirado o no se ha logeado.");
				return;
			}      
			super.retTemplate(response,"Gestion/PreCargaGerentes.htm",modelRoot);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.toString(), ex);
		}
		OutMessage.OutMessagePrint(MPREFIX + " Saliendo de Subida Organica...");
	}  	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		doGet(request, response);
	}
	
}