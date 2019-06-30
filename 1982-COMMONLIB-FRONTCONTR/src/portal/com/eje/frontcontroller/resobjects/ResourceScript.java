package portal.com.eje.frontcontroller.resobjects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import cl.ejedigital.tool.cache.Cache;
import cl.ejedigital.tool.cache.CacheLocator;
import cl.ejedigital.tool.strings.MyString;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.httpservlet.MyTemplateUbication;
import portal.com.eje.tools.Cache.CachePortalTypes;


public class ResourceScript implements IResourceManipulate {
	private Cache javascriptCache;
	private HttpServletResponse resp;
	private HttpServletRequest req;
	private IResourceListener	resourceListener;
	
	public ResourceScript() {
		javascriptCache = CacheLocator.getInstance(CachePortalTypes.CACHEJS);
	}
	
	public void setResourceListener(IResourceListener resourceListener) {
		this.resourceListener = resourceListener;
	}
	
	public void init(MyHttpServlet myhttp, HttpServletRequest req, HttpServletResponse resp) {
		this.resp = resp;
		this.req = req;
	}

	public ServletOutputStream getWriter() throws IOException, ServletException {
		ServletOutputStream out = null;
		
		String jsPath = "/"+ResourceTool.getInstance().getObject(req);
		StringBuilder js = (StringBuilder)javascriptCache.get(jsPath);
	
		if(js == null) {
			Map<String, Object> jsObject = null;
			File jsFile = null;
			
			try {
				jsObject = ResourceMapping.getInstance().getObjectFile(MyTemplateUbication.SrcAndWebContent, req, jsPath);
				jsFile = (File)jsObject.get("File");
				
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
			js = new StringBuilder(ResourceTool.getInstance().getHeader(jsObject, EnumTypeCode.javascript));
			js.append(my.readFile(jsFile, "ISO-8859-1"));
			javascriptCache.put(jsPath,js);
		
		}
		
		ResourceTool.getInstance().setCacheHeader(resp, CachePortalTypes.CACHEJS);
		
		resp.setContentType("text/javascript; charset=ISO-8859-1");
		out = resp.getOutputStream();
		IOUtils.write(js.toString().getBytes(), out);
		
		return out;
	}
	
	

}
