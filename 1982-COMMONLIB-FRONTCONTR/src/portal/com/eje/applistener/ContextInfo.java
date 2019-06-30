package portal.com.eje.applistener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.fileupload.FileService;
import portal.com.eje.portal.factory.Static;


public class ContextInfo {
	private Logger logger = Logger.getLogger(ContextInfo.class);
	
    protected String pathBase; 
	protected String servletContextName;
	
	private ContextInfo() {
		servletContextName ="";
	}
	
	public  static ContextInfo getInstance() {
		return Static.getInstance(ContextInfo.class);
	}
	
	public void setServletContext(ServletContext sContext) {
		if(sContext != null) {
			 this.pathBase = sContext.getRealPath("/");
			 String fullPath = Validar.getInstance().validarDato(sContext.getContextPath(),"");
			String[] partes = fullPath.split("/");
			if(partes.length >= 4) {
				this.servletContextName = "/"+partes[2]+"/"+partes[3];
			}
		}
		else {
			try {
				File f = new File(FileService.class.getProtectionDomain().getCodeSource().getLocation().toURI()); 
				this.pathBase = f.getParent() +  File.separator ;
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		
		logger.info("PATH BASE:"+this.pathBase);
		logger.info("CONTEXT NAME:"+this.servletContextName);
	}
	
	/**
	 * retorna el path real sea de un app web o de un app
	 * */
	public String getRealPath(String path) {
		if(path != null && (!path.equals(File.separator) && !path.equals("/")) ) {
			return this.pathBase+ path;	
		}
		else {
			return this.pathBase;
		}
	}
	
	/**
	 * retorna /cliente/modulo, no necesita ser chequeado en los parámetros de la BD
	 * */
	public String getServletContextName() {
		return this.servletContextName;
	}
}
