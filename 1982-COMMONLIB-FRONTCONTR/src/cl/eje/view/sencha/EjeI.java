package cl.eje.view.sencha;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.eje.view.servlets.IServletSimple;
import portal.com.eje.frontcontroller.reqwrapper.MyServletRequestWrapper;

public class EjeI extends HttpServlet implements IServletSimple {
	private final String servletName = "EjeI";
	private final String ejeCoreI = "EjeCoreI?claseweb=cl.eje.view.sencha.ConfSingleI&accept=application/json";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req = MyServletRequestWrapper.addHeaderJSONResponse(req);
		
		RequestDispatcher d = req.getRequestDispatcher(ejeCoreI);
		d.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req = MyServletRequestWrapper.addHeaderJSONResponse(req);
		
		RequestDispatcher d = req.getRequestDispatcher(ejeCoreI);
		d.forward(req, resp);
	}
	
	@Override
	public String getServletName() {
		return servletName;
	}
	
}
