package cl.eje.helper.servletmapping;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public interface ITriadaCaller {

	public void call(TriadaThingVo vo, MyHttpServlet http, HttpServletRequest req, HttpServletResponse resp, String servletName, boolean checkSession) throws ServletException, IOException;
}
