package portal.com.eje.frontcontroller.resobjects;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class ResourceNull implements IResourceManipulate {
	private HttpServletResponse resp;
	private HttpServletRequest req;
	
	public void init(MyHttpServlet myhttp, HttpServletRequest req, HttpServletResponse resp) {
		this.resp = resp;
		this.req = req;
	}

	public ServletOutputStream getWriter() throws IOException, ServletException {
		// TODO Auto-generated method stub
		return resp.getOutputStream();
	}

	public void setResourceListener(IResourceListener resourceListener) {
		// TODO Auto-generated method stub
		
	}
	

}
