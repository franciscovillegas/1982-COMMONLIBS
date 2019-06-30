package cl.eje.view.servlets.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.eje.helper.servletmapping.IServletRegistration;
import cl.eje.helper.servletmapping.TriadaCallZonePage;
import cl.eje.helper.servletmapping.TriadaParamsBySlashsTool;
import cl.eje.view.servlets.IServletSimple;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class ServletPagi extends MyHttpServlet implements IServletRegistration, IServletSimple {
	private final String servletName = "Pagi";
	private static final long serialVersionUID = 1L;

	@Override
	public void setMapping(Dynamic myServlet) {
		myServlet.addMapping(new StringBuilder().append("/").append(servletName).append("/*").toString());
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TriadaParamsBySlashsTool.getIntance().recreaParametrosFromSlashs(this, req, resp, servletName, false, TriadaCallZonePage.getIntance());

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TriadaParamsBySlashsTool.getIntance().recreaParametrosFromSlashs(this, req, resp, servletName, false, TriadaCallZonePage.getIntance());
	}

	@Override
	public String getServletName() {
		return servletName;
	}

}
