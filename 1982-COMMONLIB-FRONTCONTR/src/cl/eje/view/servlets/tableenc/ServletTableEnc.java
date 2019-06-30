package cl.eje.view.servlets.tableenc;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.eje.view.servlets.IServletSimple;


public class ServletTableEnc extends HttpServlet implements IServletSimple {
	private final String servletName = "Table";
	private final String ejeCore = "EjeS?modulo=ejeb_generico_vo&thing=TableEnc&accion=get";
	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		RequestDispatcher d = req.getRequestDispatcher(ejeCore);
		d.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher d = req.getRequestDispatcher(ejeCore);
		d.forward(req, resp);
	}
	
	@Override
	public String getServletName() {
		return servletName;
	}
	
}
