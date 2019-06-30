package cl.ejedigital.web.frontcontroller.resobjects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.tool.cache.Cache;
import cl.ejedigital.tool.cache.CacheLocator;
import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.web.CacheWebTypes;

/**
 * @deprecated
 * @since 2015-06-23
 * @author Francisco
 * */
public class ResourceScript implements IResourceManipulate {
	private Cache javascriptCache;
	private HttpServletResponse resp;
	private HttpServletRequest req;
	private IResourceListener	resourceListener;
	
	public ResourceScript() {
		javascriptCache = CacheLocator.getInstance(CacheWebTypes.CACHEJS);
	}
	
	public void setResourceListener(IResourceListener resourceListener) {
		this.resourceListener = resourceListener;
	}
	
	public void init(HttpServletRequest req, HttpServletResponse resp) {
		this.resp = resp;
		this.req = req;
	}

	public ServletOutputStream getWriter() throws IOException, ServletException {
		ServletOutputStream out = null;
		
		String jsPath = "/"+req.getQueryString();
		StringBuilder js = (StringBuilder)javascriptCache.get(jsPath);
	
		if(js == null) {
			File jsFile = null;
			
			try {
				jsFile = ResourceMapping.getInstance().getFile(jsPath);
				
			} catch (FileNotFoundException e) {
				if(resourceListener != null) {
    				resourceListener.fileNotFound(jsPath);
    			}
				throw e;
			}
			catch (SQLException e) {
				throw new ServletException(e);
			}
			
			MyString my = new MyString();
			js = my.readFile(jsFile, "UTF-8");
			javascriptCache.put(jsPath,js);
		
		}
		
		resp.setContentType("text/javascript");
		out = resp.getOutputStream();
		out.write(js.toString().getBytes());
		
		return out;
	}
	
	

}
