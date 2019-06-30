package cl.eje.view.servlets.json;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.eje.helper.servletmapping.IServletRegistration;
import cl.eje.helper.servletmapping.TriadaParamsBySlashsTool;
import cl.eje.view.servlets.IServletSimple;
import portal.com.eje.frontcontroller.reqwrapper.MyServletRequestWrapper;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class ServletJson extends MyHttpServlet implements IServletRegistration, IServletSimple {
	private final String servletName = "Json";
	private static final long serialVersionUID = 1L;

	@Override
	public void setMapping(Dynamic myServlet) {
		myServlet.addMapping(new StringBuilder().append("/Json/*").toString());
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req = MyServletRequestWrapper.addHeaderJSONResponse(req);
		
		TriadaParamsBySlashsTool.getIntance().recreaParametrosFromSlashs(this, req, resp, servletName, true);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req = MyServletRequestWrapper.addHeaderJSONResponse(req);
		
		TriadaParamsBySlashsTool.getIntance().recreaParametrosFromSlashs(this, req, resp, servletName, true);
	}

	@Override
	public String getServletName() {
		return servletName;
	}
	
}

