package portal.com.eje.frontcontroller.resobjects;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;


public interface IResourceManipulate {
	
	public void init(MyHttpServlet myhttp, HttpServletRequest req, HttpServletResponse resp);
	
	public ServletOutputStream getWriter() throws IOException, ServletException;
	
	public void setResourceListener(IResourceListener resourceListener);
	
}
