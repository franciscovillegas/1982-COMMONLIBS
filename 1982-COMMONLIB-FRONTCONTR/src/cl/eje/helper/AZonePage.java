package cl.eje.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cl.eje.helper.servletmapping.TriadaThingVo;
import cl.eje.view.servlets.IServletSimple;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.factory.Weak;

public abstract class AZonePage extends AZoneUtil {


	public AZonePage() {
 
	}
	
	@Override
	public abstract void get(IOClaseWeb io) throws Throwable;
	
	/**
	 * FolderFormat es el formato de capetas donde primero está el servlet y lo siguen dos carpetas, modulo y thing
	 * @author Pancho
	 * */
	public static String buildUrlFolderFormat(Class<? extends IServletSimple> classServletSimple, Class<? extends AZonePage> clase) {
		return buildUrlFolderFormat(classServletSimple, clase, null);
	}
	
	/**
	 * FolderFormat es el formato de capetas donde primero está el servlet y lo siguen dos carpetas, modulo y thing
	 * @author Pancho
	 * */
	public static String buildUrlFolderFormat(Class<? extends IServletSimple> classServletSimple, Class<? extends AZonePage> clase,String param, String value) {
		Map<String,String> params = new HashMap<>();
		if(param != null) {
			params.put(param, value);
		}
		return buildUrlFolderFormat(classServletSimple, clase, params);
	}
	
	/**
	 * FolderFormat es el formato de capetas donde primero está el servlet y lo siguen dos carpetas, modulo y thing
	 * @author Pancho
	 * */
	public static String buildUrlFolderFormat(Class<? extends IServletSimple> classServletSimple, Class<? extends AZonePage> clase, Map<String,String> params)  {
		IServletSimple servletSimple = Weak.getInstance(classServletSimple);
		TriadaThingVo tri = getTriadaFromClass(clase);
		
		StringBuilder str = new StringBuilder();
		str.append(servletSimple.getServletName()).append(SLASH).append(tri.getModulo()).append(SLASH).append(tri.getThing()).append(SLASH).append(CARACTER_PREGUNTA);
		
		StringBuilder paramStr = new StringBuilder();
		if(params != null) {
			for(Entry<String, String> entry : params.entrySet()) {
				if(paramStr.length() != 0) {
					paramStr.append(CARACTER_AMP);
				}
				try {
					paramStr.append(entry.getKey()).append(CARACTER_EQUALS).append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				 
			}
			
			str.append(paramStr);
		}
		
		
		return str.toString();
	}
	
}
