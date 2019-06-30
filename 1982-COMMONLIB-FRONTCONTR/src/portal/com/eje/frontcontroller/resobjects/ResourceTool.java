package portal.com.eje.frontcontroller.resobjects;

import java.util.Calendar;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.tool.properties.PropertiesTools;
import portal.com.eje.tools.Cache.CachePortalTypes;

public class ResourceTool {
	private  PropertiesTools tools;
	private static ResourceTool instance;
	
	private ResourceTool() {
		tools = new PropertiesTools();
	}
	
	public static ResourceTool getInstance() {
		if(instance == null) {
			synchronized(ResourceTool.class) {
				if(instance == null) {
					instance = new ResourceTool();
				}	
			}
		}
		
		return instance;
	}
	
	
	/**
	 * 
	 * Metodo que cachea una respuesta HttpServletResponse, en base a la configuracion de "cache.properties", para ello solo es necesario
	 * pasarle como parámetro el key referente, de esta forma se revisa en el archivo <b>cache.properties</b> si el parametro <b>key</b> es 
	 * igual a <b>true</b>, en caso que así fuera se cachea la respuesta.
	 * 
	 * @author eje
	 * @see HttpServletResponse
	 * 
	 * */
	public void setCacheHeader(HttpServletResponse resp, CachePortalTypes key, int calenderType, int cuantiti) {
		
		if(isCacheActive(key)) {
			Calendar inTwoMonths = Calendar.getInstance();
	        inTwoMonths.add(calenderType, cuantiti);

	        resp.setDateHeader("Expires", inTwoMonths.getTimeInMillis());
		}
		
	}
	
	public boolean isCacheActive(CachePortalTypes key) {
		if(tools.existsBundle("cache")) {
			
			if(key != null && "true".equals(tools.getString(ResourceBundle.getBundle("cache"), "cache.".concat(key.toString().toString()).concat(".enabled") , "false"))) {
				return true;
			}
		}
		
		return false;
	}
	
	public String getExtension(HttpServletRequest req) {
		String ext = "";
		if( req.getQueryString() != null && req.getQueryString().length() > 0) {
			ext = req.getQueryString().substring(req.getQueryString().lastIndexOf(".") + 1, req.getQueryString().length()).toLowerCase();
			if(ext.indexOf("&") != -1) {
				ext = ext.substring(0,  ext.indexOf("&") );
			}

        }
        else if(req.getParameter("omap") != null && req.getParameter("omap").length() > 0){
        	ext = req.getParameter("omap").substring(req.getParameter("omap").lastIndexOf(".") + 1, req.getParameter("omap").length()).toLowerCase();
        	if(ext.indexOf("&") != -1) {
				ext = ext.substring(0,  ext.indexOf("&") );
			}
			
        
        }
		
		return ext;
	}
	
	public String getObject(HttpServletRequest req) {
		String obj = null;
		if(req.getParameter("omap") == null) {
			obj = req.getQueryString();
        }
        else {
        	 obj = req.getParameter("omap");
        }
		
		if(obj.indexOf("&") != -1) {
			obj = obj.substring(0,  obj.indexOf("&") );
		}
		
		return obj;
	}
	
	/**
	 * 
	 * Lo mismo que el metodo {@link #setCacheHeader(HttpServletResponse, String, int, int) setCacheHeader}
	 * la diferencia es que por defecto se entrega un periodo de 2 meses como cacheo
	 * 
	 * @author eje
	 * 
	 * */
	public void setCacheHeader(HttpServletResponse resp, CachePortalTypes key) {
		setCacheHeader(resp, key, Calendar.DAY_OF_MONTH, 1);
	}
	
	public String getHeader(Map<String, Object> mapFile) {
		return getHeader(mapFile, EnumTypeCode.html);
	}
	
	public String getHeader(Map<String, Object> mapFile, EnumTypeCode e) {
		StringBuilder strOut = new StringBuilder();
        strOut.append(e.getCommentIni()).append("\n");
        strOut.append(e.getCommentMid()).append("****** Ejedigital S.A. 2016 ******\n\n");
        if(mapFile != null) {
        	Set<String> set = mapFile.keySet();
        	for(String s : set) {
        		if(!"File".equals(s)) {
        			strOut.append(e.getCommentMid()).append(s).append(":").append(mapFile.get(s)).append("\n");	
        		}
        	}
        }
        strOut.append(e.getCommentEnd()).append("\n");
        
        return strOut.toString();
	}

}
