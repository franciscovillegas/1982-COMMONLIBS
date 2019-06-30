package portal.com.eje.frontcontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.frontcontroller.reqwrapper.MyServletRequestWrapper;
import portal.com.eje.frontcontroller.util.DoTransactionModulo;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class ServletGenerico extends MyHttpServlet {
	private String doPost = "doPost";
	private String doGet = "doGet";
	
	private static final long serialVersionUID = 6389300878249144612L;

 
	
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    	//req = MyServletRequestWrapper.addHeaderJSONResponse(req);
    	
    	DoTransactionModulo.getInstance().doTransactionServlet(this, req, resp, doPost, true);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {
    	//req = MyServletRequestWrapper.addHeaderJSONResponse(req);
 
    	DoTransactionModulo.getInstance().doTransactionServlet(this, req, resp, doGet, true);
    }

    
}


