package portal.com.eje.pdf;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class ServletTransformPdf extends HttpServlet{
  

    public ServletTransformPdf() {
        //TEMPLATE_PATH = "/";
    }
    protected void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
    		throws IOException, ServletException {
    	
    		doGet(httpservletrequest,httpservletresponse);
    }
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
	throws ServletException, IOException {
    	
            RequestDispatcher d = req.getRequestDispatcher("EjeCore?claseweb=portal.com.eje.pdf.CWebTransformPdf");
            d.forward(req,resp);
            
            
	}
   
}
