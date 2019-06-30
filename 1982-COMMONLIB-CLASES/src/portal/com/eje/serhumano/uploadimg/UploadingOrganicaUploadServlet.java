package portal.com.eje.serhumano.uploadimg;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.Usuario;

public class UploadingOrganicaUploadServlet extends MyHttpServlet implements javax.servlet.Servlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
	  
	  RequestDispatcher d = request.getRequestDispatcher("CargaOrganica");
	  d.forward(request,response);

  }
}