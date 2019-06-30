package cl.ejedigital.web.frontcontroller.resobjects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.tool.cache.Cache;
import cl.ejedigital.tool.cache.CacheLocator;
import cl.ejedigital.web.CacheWebTypes;
import cl.ejedigital.web.PropertiesManipulateByRequest;
import cl.ejedigital.web.parametroconfig.ConfigParametro;
import cl.ejedigital.web.parametroconfig.ConfigParametroKey;
import cl.ejedigital.web.parametroconfig.ConfigParametroManager;
import cl.ejedigital.web.usuario.IUsuario;
import cl.ejedigital.web.usuario.SingletonSessionMgr;
import cl.ejedigital.web.usuario.SingletonUsuarioUtils;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

/**
 * @deprecated
 * @since 2015-06-23
 * @author Francisco
 * */
public class ResourceHtml implements IResourceManipulate {
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private Cache templateCache;
	private IResourceListener	resourceListener;
	private String TEMPLATE_PATH;
	
	/**
	 * Por defecto es /templates
	 * */
	public ResourceHtml() {
		this("templates/");
	}
	
	/**
	 * Por defecto es /templates
	 * */
	public ResourceHtml(String pathTemplates) {
		TEMPLATE_PATH = pathTemplates;
        templateCache = CacheLocator.getInstance(CacheWebTypes.CACHETEMPLATE);
	}
	
	public void setResourceListener(IResourceListener resourceListener) {
		this.resourceListener = resourceListener;
	}
	
	public void init(HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;	
	}

	public ServletOutputStream getWriter() throws IOException, ServletException  {
        String pHtm = req.getParameter("htm");
        ServletOutputStream out = null;
        resp.setContentType("text/html");
        resp.setHeader("Expires", "0");
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        SimpleHash modelRoot = new SimpleHash();
        
        IUsuario user = SingletonSessionMgr.getInstance().rescatarUsuario(req);
        
        if(user != null ) {
	        if(!user.esValido() || pHtm == null) {
	            pHtm = "estaticos/mensajes/user_sin_sesion.htm";
	        } 
	        else {
	            PropertiesManipulateByRequest prop = new PropertiesManipulateByRequest(modelRoot);
		        prop.manipulate(req);
		        
		        modelRoot.put("usuario", SingletonUsuarioUtils.getInstance().toHash(user) );
		        modelRoot.put("rut", String.valueOf(user.getDatosTrabajador().getRut()) );	        
		        modelRoot.put("GetProp", new GetProp());	   
	        }
        }
        
        modelRoot.put("GetParam", new GetParam(req));  
        modelRoot.put("context", req.getContextPath());
        
    	Template template;
		try {
			template = getTemplate(pHtm);
		}
		catch (SQLException e) {
			throw new ServletException(e);
		}
		
    	StringWriter stringWriter = new StringWriter();
       	PrintWriter print = new PrintWriter(stringWriter);        
        template.process(modelRoot, print);
        
        out = resp.getOutputStream();
        out.write(stringWriter.toString().getBytes());
	        
    
    	
    	
        return out;
	}
	
	public Template getTemplate(String pHtm) throws IOException, ServletException, SQLException {
		
		
		String templateName = TEMPLATE_PATH + "/" + pHtm;
    	Template template = (Template) templateCache.get(templateName);
		
   
    	if(template == null) {
    		File fileTemplate  = null;
    		
    		try {
    			fileTemplate = ResourceMapping.getInstance().getFile(templateName);
    		}
    		catch (FileNotFoundException e) {
    			if(resourceListener != null) {
    				resourceListener.fileNotFound(pHtm);
    			}
    			fileTemplate  = ResourceMapping.getInstance().getFile("/error/templates/404.htm");
    		}
			template = new Template(fileTemplate);
			templateCache.put(templateName,template);
			
		}
    	
    	return template;
	}

	public String getTemplatePath() {
		return TEMPLATE_PATH;
	}
	

}

class GetParam implements TemplateMethodModel {

	public GetParam(HttpServletRequest req) {
		request = req;
	}

	public boolean isEmpty() {
		return false;
	}

	public TemplateModel exec(List args) {
		return new SimpleScalar(request.getParameter(args.get(0).toString()));
	}

	private HttpServletRequest	request;
}

class GetProp implements TemplateMethodModel {

	public GetProp() {}

	public boolean isEmpty() {
		return false;
	}

	public TemplateModel exec(List args) {

		ConfigParametro param =
			ConfigParametroManager.getInstance().getConfigParametro(new ConfigParametroKey(args.get(0).toString()));
		return new SimpleScalar(param.getSelectedKey());

	}

}
