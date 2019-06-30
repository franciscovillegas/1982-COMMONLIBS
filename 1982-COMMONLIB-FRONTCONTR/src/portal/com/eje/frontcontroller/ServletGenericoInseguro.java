package portal.com.eje.frontcontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.frontcontroller.util.DoTransactionModulo;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class ServletGenericoInseguro extends MyHttpServlet {

	private static final long serialVersionUID = -2582203148239359822L;
	private static String doPost = "doPost";
	private static String doGet = "doGet";
	
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    	DoTransactionModulo.getInstance().doTransactionServlet(this, req, resp, doPost, false);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {
    	DoTransactionModulo.getInstance().doTransactionServlet(this, req, resp, doGet, false);
    }
}