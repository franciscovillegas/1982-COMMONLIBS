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
public class ResourceStyle implements IResourceManipulate {
	private Cache styleCache;
	private HttpServletResponse resp;
	private HttpServletRequest req;
	private IResourceListener	resourceListener;
	
	public ResourceStyle() {
		styleCache = CacheLocator.getInstance(CacheWebTypes.CACHESTYLE);
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
				
		String stylePath = "/"+req.getQueryString();
		StringBuilder style = (StringBuilder)styleCache.get(stylePath);
		
		if(style == null) {
			File styleFile = null;
			
			try {
				styleFile = ResourceMapping.getInstance().getFile(stylePath);
				
			} catch (FileNotFoundException e) {
				if(resourceListener != null) {
    				resourceListener.fileNotFound(stylePath);
    			}
				throw e;
			}
			catch (SQLException e) {
				throw new ServletException(e);
			}
			
			
			MyString my = new MyString();
			style = my.readFile(styleFile, "UTF-8");
			styleCache.put(stylePath,style);

		}
		resp.setContentType("text/css");
		out = resp.getOutputStream();
		out.write(style.toString().getBytes());
	
		
		return out;
    }




}
