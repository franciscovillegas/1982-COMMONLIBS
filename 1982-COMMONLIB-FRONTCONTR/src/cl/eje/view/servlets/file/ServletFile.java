package cl.eje.view.servlets.file;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.eje.helper.servletmapping.IServletRegistration;
import cl.eje.helper.servletmapping.ITriadaCaller;
import cl.eje.helper.servletmapping.TriadaParamsBySlashsTool;
import cl.eje.view.servlets.IServletSimple;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class ServletFile extends MyHttpServlet implements IServletRegistration, IServletSimple {
	private final String servletName = "File";
	private static final long serialVersionUID = 1L;
	private final ITriadaCaller callSLoadFile = new TriadaCallSLoadFile();

	@Override
	public void setMapping(Dynamic myServlet) {
		myServlet.addMapping(new StringBuilder().append("/").append(getServletName()).append("/*").toString());
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	 
		TriadaParamsBySlashsTool.getIntance().recreaParametrosFromSlashs(this, req, resp, servletName, true, callSLoadFile);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
 		
		TriadaParamsBySlashsTool.getIntance().recreaParametrosFromSlashs(this, req, resp, servletName, true, callSLoadFile);
	}

	@Override
	public String getServletName() {
		return servletName;
	}

}

