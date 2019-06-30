package portal.com.eje.frontcontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class IOClaseWebSimulator extends IOClaseWeb {
	protected IOClaseWeb parentSimulator;
	
	public IOClaseWebSimulator(IOClaseWeb parentSimulator, TransactionConnection cons, MyHttpServlet myHttpServlet, HttpServletRequest req, HttpServletResponse resp) {
		super(myHttpServlet, req, resp);
		
		this.parentSimulator = parentSimulator;
		super.cons = cons;
	}
	
	public IOClaseWeb getParentSimulator() {
		return this.parentSimulator;
	}
 
}
