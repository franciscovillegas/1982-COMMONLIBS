package portal.com.eje.frontcontroller.resobjects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.httpservlet.MyTemplateUbication;
import portal.com.eje.serhumano.user.ControlAcceso;
import portal.com.eje.serhumano.user.ControlAccesoTM;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.Cache.CachePortalTypes;
import portal.com.eje.tools.servlet.GetHtml;
import portal.com.eje.tools.servlet.GetImagenEmpresa;
import portal.com.eje.tools.servlet.GetParam;
import portal.com.eje.tools.servlet.GetParametro;
import portal.com.eje.tools.servlet.GetProp;
import cl.ejedigital.tool.cache.Cache;
import cl.ejedigital.tool.cache.CacheLocator;
import cl.ejedigital.tool.properties.PropertiesTools;
import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.web.FreemakerTool;
import cl.ejedigital.web.PropertiesManipulateByRequest;
import freemarker.template.SimpleHash;
import organica.tools.Tools;


public class ResourceStyle implements IResourceManipulate {
	private Cache styleCache;
	private HttpServletResponse resp;
	private HttpServletRequest req;
	private IResourceListener	resourceListener;
	
	public ResourceStyle() {
		styleCache = CacheLocator.getInstance(CachePortalTypes.CACHESTYLE);
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
				
		String stylePath = ResourceTool.getInstance().getObject(req);
		String style = getStyle(stylePath);
		
		ResourceTool.getInstance().setCacheHeader(resp, CachePortalTypes.CACHESTYLE);
		resp.setContentType("text/css");
		out = resp.getOutputStream();
		out.write(style.getBytes());
	
		
		return out;
    }

	public String getStyle(String stylePath) throws FileNotFoundException, ServletException {
		stylePath = "/"+stylePath;
		StringBuilder style = (StringBuilder)styleCache.get(stylePath);
		
		if(style == null) {
			Map<String,Object> mapFile;
			File styleFile = null;
			
			try {
				mapFile = ResourceMapping.getInstance().getObjectFile(MyTemplateUbication.SrcAndWebContent, req, stylePath);
				styleFile = (File)mapFile.get("File");
				//System.out.println(styleFile.getAbsolutePath());
				
			} catch (FileNotFoundException e) {
				if(resourceListener != null) {
    				resourceListener.fileNotFound(stylePath);
    			}
				throw e;
			}
			catch (SQLException e) {
				throw new ServletException(e);
			}
			
			Usuario user = SessionMgr.rescatarUsuario(req);
			//MyString my = new MyString();
			FreemakerTool tool = new FreemakerTool();
			SimpleHash modelRoot = new SimpleHash();	
		
			//style = my.readFile(styleFile, "UTF-8");
			
			if(user.esValido()) {
			 
				
	            PropertiesManipulateByRequest prop = new PropertiesManipulateByRequest(modelRoot);
		        prop.manipulate(req);
		        
		        modelRoot.put("Control", new ControlAccesoTM(new ControlAcceso(user)));
		        modelRoot.put("usuario", user.toHash());
		        
		        modelRoot.put("rut", user.getRutId());	      
		        modelRoot.put("GetHtml", new GetHtml(modelRoot ,req));
		        modelRoot.put("GetParam", new GetParam(req));

		        modelRoot.put("GetProp", new GetProp(ResourceBundle.getBundle("db")));
		        modelRoot.put("GetImagenEmpresa", new GetImagenEmpresa(user));
		        
		        PropertiesTools properTools = new PropertiesTools();
		        if(properTools.existsBundle("analytics")) {
	            	modelRoot.put("googleAnalytics",properTools.getString(ResourceBundle.getBundle("analytics"),"google.key",""));
	            }
		        
		        Calendar cal = Calendar.getInstance();
		        int yy = cal.get(Calendar.YEAR);
		        int mm = cal.get(Calendar.MONTH)+1;
		        int dd = cal.get(Calendar.DAY_OF_MONTH);	
		        modelRoot.put("fechaActual", dd + " de " + Tools.RescataMes(mm) + " del " + yy);

	        }
		        
	        modelRoot.put("GetParametro", new GetParametro());
	        modelRoot.put("correo", user.getEmailFromTablaConfirmacion());
	        modelRoot.put("context", req.getContextPath());
			
	        try {
	        	StringBuilder stringWriter = new StringBuilder(tool.templateProcess( styleFile , modelRoot));
				style = new StringBuilder(ResourceTool.getInstance().getHeader(mapFile, EnumTypeCode.css));
	        	style.append(stringWriter.toString());
	 
			} catch (IOException e) {
				e.printStackTrace();
			}
	         
			styleCache.put(stylePath,style);

		}
		
		return style.toString();
	}



}
