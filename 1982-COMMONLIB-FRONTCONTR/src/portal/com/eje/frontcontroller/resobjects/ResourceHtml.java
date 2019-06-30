package portal.com.eje.frontcontroller.resobjects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.NotImplementedException;

import cl.ejedigital.tool.misc.WeakCacheLocator;
import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.tool.strings.UrlTool;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.ioutils.IOUtilFreemarker;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.httpservlet.MyTemplateUbication;
import portal.com.eje.tools.ClaseGenerica;
 


public class ResourceHtml implements IResourceManipulate {
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private IResourceListener	resourceListener;
	//private String TEMPLATE_PATH;
	private Map<String,Object> mapFile;
	//private IOClaseWeb io;
	
	public ResourceHtml() {
		//TEMPLATE_PATH = "/templates";
        
	}
	
	public static ResourceHtml getInstance() {
		return Util.getInstance(ResourceHtml.class);
	}
	
	
	public void setResourceListener(IResourceListener resourceListener) {
		this.resourceListener = resourceListener;
	}
	
	public void init(MyHttpServlet myhttp, HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;
		
		//this.io = new IOClaseWeb(myhttp, req, resp);
	}

	public ServletOutputStream getWriter() throws IOException, ServletException  {
	 
		//Usuario user = SessionMgr.rescatarUsuario(req);
        String pHtm = req.getParameter("htm");
        
        if(pHtm != null && pHtm.indexOf("?") != -1) {
        	String[] partes = pHtm.split("\\?");
        	
        	if(partes != null && partes.length >= 1) {
        		pHtm = partes[0];
        	}
        }
        
        ServletOutputStream out = null;
        
    	resp.setContentType("text/html; charset=ISO-8859-1");
        SimpleHash modelRoot = new SimpleHash();
        
        IOClaseWeb io = new IOClaseWeb(null, req, resp);
        modelRoot = IOUtilFreemarker.getInstance().getGenericModelRoot(io, modelRoot);
        
    	Template template;
		try {
			template = getTemplate(req,pHtm);
		}
		catch (SQLException e) {
			throw new ServletException(e);
		}
		
    	StringWriter stringWriter = new StringWriter();
       	PrintWriter print = new PrintWriter(stringWriter);        
        try {
			template.process(modelRoot, print);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
      

        out = resp.getOutputStream();
        out.print(ResourceTool.getInstance().getHeader(this.mapFile, EnumTypeCode.html));
        out.print("\n");
        out.print(stringWriter.toString());
        
        return out;
	}
	
	/**
	 * @deprecated 2016-03-29
	 * Es necesario entregarle la petición para poder obtener de buena forma los templates
	 */
	
	public Template getTemplate(String pHtm) throws IOException, ServletException, SQLException {
//		System.out.println("@@[BAD GetTemplate] "+pHtm);
		
		return getTemplate(null, pHtm);
	}
	
	public Template getTemplate( HttpServletRequest req, String pHtm ) throws IOException, ServletException, SQLException {
		return getTemplate(MyTemplateUbication.SrcAndWebContent, req, pHtm);
	}
	
	public Template getTemplateFromBase( HttpServletRequest req, String pHtm ) throws IOException, ServletException, SQLException {
		return getTemplate(MyTemplateUbication.SrcAndWebContent, req, pHtm, false);
	}
	
	public Template getTemplate(MyTemplateUbication ubicacion, HttpServletRequest req, String pHtm ) throws IOException, ServletException, SQLException {
		return getTemplate(ubicacion, req, pHtm, true);
	}
	
	public Template getTemplate(MyTemplateUbication ubicacion, HttpServletRequest req, String pHtm, boolean startFromTemplatePath ) throws IOException, ServletException, SQLException {
		Map<String, Object> o = getObjectTemplate(ubicacion, req, pHtm, startFromTemplatePath);
		if(o != null) {
			return (Template) o.get("Template");
		}
		
		return null;
	}
	
	public Map<String,Object> getObjectTemplate(MyTemplateUbication ubicacion, HttpServletRequest req, String pHtm ) throws IOException, ServletException, SQLException {
		return getObjectTemplate(ubicacion, req, pHtm, true);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,Object> getObjectTemplate(MyTemplateUbication ubicacion, HttpServletRequest req, String pHtm , boolean startFromTemplatePath) throws IOException, ServletException, SQLException {

		
		if(startFromTemplatePath && 
				( !pHtm.startsWith("/templates")  || !pHtm.startsWith("templates")) ) {
			if(pHtm.startsWith("/")) {
				pHtm = new StringBuilder().append("/templates").append(pHtm).toString();	
			}
			else {
				pHtm = new StringBuilder().append("/templates/").append(pHtm).toString();
			}
			
		}
		
		
		pHtm = UrlTool.getInstance().putSlashAtBeginnig(pHtm);
			
		 
		WeakHashMap templateCache = WeakCacheLocator.getInstance(getClass());
		Map objectTemplate = (Map) templateCache.get(pHtm);
   
    	if(objectTemplate == null) {
    		File fileTemplate  = null;
    		
    		try {
    			mapFile = ResourceMapping.getInstance().getObjectFile(MyTemplateUbication.SrcAndWebContent, req, pHtm);
    			fileTemplate = (File)mapFile.get("File");
    		}
    		catch (FileNotFoundException e) {
    			if(resourceListener != null) {
    				resourceListener.fileNotFound(pHtm);
    			}
    			System.out.println("FileNotFoundException "+pHtm);
//    			ResourceMapping.getInstance().getObjectFile(MyTemplateUbication.SrcAndWebContent, req, "/templates/estaticos/error/404.htm");
//    			
//    			fileTemplate  = ResourceMapping.getInstance().getFile(req, "/templates/certificados/team.html");
    			fileTemplate  = ResourceMapping.getInstance().getFile(req, "/templates/estaticos/error/404.htm");
    		}
    		
    		try {
    			ClaseGenerica cg = Util.getInstance(ClaseGenerica.class);
    			Class[] clazz = {File.class};
    			Object[] params = {fileTemplate};
    			//cg.cargaConstructor("freemarker.template.Template", clazz, params);
    			Object o = cg.getNew("freemarker.template.Template", clazz, params);
    			if(mapFile != null) {
    				mapFile.put("Template", (Template)o);	
    			}
    		}
    		catch (ClassNotFoundException e) {
    			throw new NotImplementedException("PARA JDK ACTUAL");
    		}
    		catch (NoSuchMethodException e) {
    			throw new NotImplementedException("PARA JDK ACTUAL");
    		}
    		catch (InstantiationException e) {
    			throw new NotImplementedException("PARA JDK ACTUAL");
    		}
    		catch (IllegalAccessException e) {
    			throw new NotImplementedException("PARA JDK ACTUAL");
    		}
    		catch (InvocationTargetException e) {
    			throw new NotImplementedException("PARA JDK ACTUAL");
    		}
    		finally {
    			templateCache.put(pHtm, objectTemplate);
    		}
			
		}
    	
    	
    	return mapFile;
	}

	public String getTemplatePath() {
		return "/templates";
	}
	
	
	
}
