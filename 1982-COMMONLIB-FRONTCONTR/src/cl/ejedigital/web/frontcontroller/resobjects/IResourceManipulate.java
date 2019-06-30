package cl.ejedigital.web.frontcontroller.resobjects;

/**
 * @deprecated
 * @since 2015-06-23
 * @author Francisco
 * */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface IResourceManipulate {
	
	public void init(HttpServletRequest req, HttpServletResponse resp);
	
	public ServletOutputStream getWriter() throws IOException, ServletException;
	
	public void setResourceListener(IResourceListener resourceListener);
	
}
