package cl.eje.view.servlets.file;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.eje.helper.servletmapping.ITriadaCaller;
import cl.eje.helper.servletmapping.TriadaThingVo;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class TriadaCallSLoadFile implements ITriadaCaller {

	@Override
	public void call(TriadaThingVo vo, MyHttpServlet http, HttpServletRequest req, HttpServletResponse resp, String servletName, boolean checkSession) throws ServletException, IOException {
		RequestDispatcher d = req.getRequestDispatcher("SLoadFile?enc=true");
		d.forward(req, resp);
		
	}

}
