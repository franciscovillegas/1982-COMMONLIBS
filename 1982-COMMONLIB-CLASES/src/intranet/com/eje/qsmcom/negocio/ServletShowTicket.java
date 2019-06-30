package intranet.com.eje.qsmcom.negocio;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

/**
 * Servlet implementation class for Servlet: ServletControlTarjeta
 *
 */
 public class ServletShowTicket extends MyHttpServlet {
	 
	 public ServletShowTicket() {
		super();
	}   	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
        response.setContentType("text/html");
        Template template = getTemplate("intranet/qsmcom/info/showTicket.html");
        SimpleHash modelRoot = new SimpleHash();
        PrintWriter out = response.getWriter();
        String idReq = request.getParameter("id");        
        String rut = request.getParameter("rut");
        try {
			if (idReq != null) {	
				modelRoot.put("id",idReq);				
				modelRoot.put("rut",rut);
			}		
	    } 
        catch (Exception e) {
				e.printStackTrace();
		}
	    template.process(modelRoot, out);
	    out.flush();
	    out.close();
	}  	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
        doGet(request,response);
	}   	  	    
}