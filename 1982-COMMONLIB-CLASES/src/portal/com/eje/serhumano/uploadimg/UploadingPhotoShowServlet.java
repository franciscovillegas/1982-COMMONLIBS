package portal.com.eje.serhumano.uploadimg;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;

public class UploadingPhotoShowServlet extends MyHttpServlet implements javax.servlet.Servlet {
	private Usuario user;

	public UploadingPhotoShowServlet() {
		super();
	}   	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		final String MPREFIX = "[UploadingPhotoShowServlet][doGet]";
		try {
			//PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			response.setHeader("Expires", "0");
			response.setHeader("Pragma", "no-cache");

			SimpleHash modelRoot = new SimpleHash();
			modelRoot.put("errors", new SimpleList());
			modelRoot.put("messages", new SimpleList());
			user = SessionMgr.rescatarUsuario(request);
			if (!user.esValido()) {
				super.mensaje.devolverPaginaSinSesion(response,"ERROR de Sesi&oacute;n","Su sesi&oacute;n ha expirado o no se ha logeado.");
				return;
			}      
			
			IOClaseWeb io = new IOClaseWeb(this, request, response);
			super.retTemplate(io, "upload_img/upload_photo.htm", modelRoot);
		}
		catch (Exception ex) {      
			ex.printStackTrace();
			throw new ServletException(ex.toString(), ex);
		}
	}  	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}