package portal.com.eje.serhumano.uploadimg;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.admin.CapManager;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;

import com.oreilly.servlet.MultipartRequest;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;

public class UploadingGerentesUploadServlet extends MyHttpServlet implements Servlet {
	
	private Usuario user; 

	public UploadingGerentesUploadServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
  		throws ServletException, IOException {
		
		SimpleHash modelRoot = new SimpleHash();
		SimpleList errors = new SimpleList();
		SimpleList messages = new SimpleList();
		modelRoot.put("errors", errors);
		modelRoot.put("messages", messages);
	  
		Connection conexion = connMgr.getConnection("portal");
		CapManager cm = new CapManager(conexion);
		SimpleList lista = new SimpleList();
		lista = cm.GetListaUsuariosCargo();
		modelRoot.put("lista", lista);
      
		connMgr.freeConnection("portal", conexion);
	  
		user = SessionMgr.rescatarUsuario(request);
		if (!user.esValido()) {  
			super.mensaje.devolverPaginaSinSesion(response,"ERROR de Sesi&oacute;n","Su sesi&oacute;n ha expirado o no se ha logeado.");
			return;
		}       
	  
		response.sendRedirect("/firstfactors/portalrrhh/servlet/CargaGerentes");
	  
		super.retTemplate(response,"Gestion/PreCargaGerentes.htm",modelRoot);
	}
}