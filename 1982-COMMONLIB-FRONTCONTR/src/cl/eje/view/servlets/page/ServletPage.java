package cl.eje.view.servlets.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.eje.helper.servletmapping.IServletRegistration;
import cl.eje.helper.servletmapping.TriadaCallZonePage;
import cl.eje.helper.servletmapping.TriadaParamsBySlashsTool;
import cl.eje.helper.servletmapping.TriadaThingVo;
import cl.eje.view.servlets.IServletSimple;
import portal.com.eje.frontcontroller.util.DoTransactionModulo;
import portal.com.eje.frontcontroller.util.GetterAZoneUtilFromClass;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class ServletPage extends MyHttpServlet implements IServletSimple, IServletRegistration {
	private final String servletName = "Page";
	private static final long serialVersionUID = 1L;

	@Override
	public void setMapping(Dynamic myServlet) {
		myServlet.addMapping(new StringBuilder().append("/Page/*").toString());
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TriadaParamsBySlashsTool.getIntance().recreaParametrosFromSlashs(this, req, resp, servletName, true, TriadaCallZonePage.getIntance());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TriadaParamsBySlashsTool.getIntance().recreaParametrosFromSlashs(this, req, resp, servletName, true, TriadaCallZonePage.getIntance());
	}

	@Override
	public String getServletName() {
		return servletName;
	}
}
