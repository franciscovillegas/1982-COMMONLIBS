package cl.eje.view.sencha;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.frontcontroller.reqwrapper.MyServletRequestWrapper;

public class EjeS extends HttpServlet {
	private final String ejeCore = "EjeCore?claseweb=cl.eje.view.sencha.ConfSingleS&accept=application/json";
	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//req = MyServletRequestWrapper.addHeaderJSONResponse(req);
		
		RequestDispatcher d = req.getRequestDispatcher(ejeCore);
		d.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//req = MyServletRequestWrapper.addHeaderJSONResponse(req);
		
		RequestDispatcher d = req.getRequestDispatcher(ejeCore);
		d.forward(req, resp);
	}
}
