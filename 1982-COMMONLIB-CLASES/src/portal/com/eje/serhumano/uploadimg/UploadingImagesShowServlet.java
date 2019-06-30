package portal.com.eje.serhumano.uploadimg;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

/**
 * Servlet implementation class for Servlet: UploadingImagesShowServlet
 *
 */
 public class UploadingImagesShowServlet extends MyHttpServlet 
   implements javax.servlet.Servlet {
   private Usuario user;

  /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public UploadingImagesShowServlet() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    final String MPREFIX = "[UploadingImagesShowServlet][doGet]";

    OutMessage.OutMessagePrint(MPREFIX + " Entrando...");

    try {
      
      SimpleHash modelRoot = new SimpleHash();
      modelRoot.put("errors", new SimpleList());
      modelRoot.put("messages", new SimpleList());

      user = SessionMgr.rescatarUsuario(request);
      if (!user.esValido()) {
        super.mensaje.devolverPaginaSinSesion(response,"ERROR de Sesi&oacute;n","Su sesi&oacute;n ha expirado o no se ha logeado.");
        return;
      }      
      
      super.retTemplate(response,"upload_img/upload_images.htm",modelRoot);

    }
    catch (Exception ex) {      
      ex.printStackTrace();
      throw new ServletException(ex.toString(), ex);
    }

    OutMessage.OutMessagePrint(MPREFIX + " Saliendo...");
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}   	  	    
}