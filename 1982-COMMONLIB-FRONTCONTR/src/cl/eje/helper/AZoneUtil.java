package cl.eje.helper;


 
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import cl.eje.helper.servletmapping.TriadaThingVo;
import cl.eje.view.sencha.Version;
import cl.eje.view.servlets.IServletSimple;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.tools.EnumTool;
import portal.com.eje.tools.paquetefactory.PaqueteFactory;

public abstract class AZoneUtil implements IZoneUtil {
	protected final static String SLASH="/";
	protected final static String CARACTER_PREGUNTA="?";
	protected final static String CARACTER_EQUALS="=";
	protected final static String CARACTER_AMP="&";
	protected final static String PAQUETE_TO_FIND = "cl.eje.view";
	protected Logger logger = Logger.getLogger(this.getClass()); 
	
	public AZoneUtil() {
 
	}
	
	public void get(IOClaseWeb io) throws Throwable {
		System.out.println("get");
	}
	
	public void upd(IOClaseWeb io) throws Throwable {
		System.out.println("upd");
	}
	
	public void add(IOClaseWeb io) throws Throwable {
		System.out.println("add");
	}
	
	public void del(IOClaseWeb io) throws Throwable {
		System.out.println("del");
	}  
	
	public static String buildUrl(String servletName, Class<? extends AZoneUtil> clase, EnumAccion accion) {
		TriadaThingVo tri = getTriadaFromClass(clase);
		
		StringBuilder str = new StringBuilder();
		str.append(servletName).append("?")
		.append("modulo=").append(tri.getModulo())
		.append("&thing=").append(tri.getThing())
		.append("&accion=").append(accion.name());
		
		return str.toString();
	}
	
	public static TriadaThingVo getTriadaFromClass(Class<? extends AZoneUtil> clase) {
		String pquete = "cl.eje.view.sencha";
		
		String cn = clase.getCanonicalName();
		Assert.isTrue(cn.startsWith(pquete) , "Debe pertencer al paquete cl.eje.view.sencha<LETRA>");
		String sinPquete = cn.substring(pquete.length(), cn.length());
		sinPquete = sinPquete.substring(sinPquete.indexOf(".")+1, sinPquete.length());
		
		String modulo = sinPquete.substring(0, sinPquete.indexOf("."));
		String thing = sinPquete.substring(sinPquete.indexOf(".")+1, sinPquete.length());
		
		TriadaThingVo tri = new TriadaThingVo();
		tri.setModulo(modulo);;
		tri.setThing(thing);;
		
		return tri;
	}
	
	public static Class<?> getClassFromUrl(String url) {
		Class<?> retorno = null;
		if(url != null) {
			MultiValueMap<String, String> map =
		            UriComponentsBuilder.fromUriString(url).build().getQueryParams();
			
			String m = map.get("modulo").get(0);
			String t = map.get("thing").get(0);
			
			if(m != null && t != null) {
				retorno = getClass(m, t);
			}
		}
		
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	protected static Class<? extends AZoneUtil> getClass(String modulo, String thing) {
		Class<? extends AZoneUtil> retorno = null;
		
		if(modulo != null && thing != null) {
			String version = modulo.split("_")[0];
			Version v = EnumTool.getEnumByNameIngoreCase(Version.class, version, Version.PORDEFECTO);
			
			StringBuilder str = new StringBuilder();
			str.append(v.getPaquete()).append(".").append(modulo).append(".").append(thing);
			
			try {
				retorno = (Class<? extends AZoneUtil>) Class.forName(str.toString());
			}
			catch(Exception e) {
				
			}
		}

		return retorno;
	}
	 
	@SuppressWarnings("unchecked")
	public static Class<? extends AZoneUtil> getClassFromUrlFolderFormat(String url) {
		Class<? extends AZoneUtil> retorno = null;
		
		List<IServletSimple> sevlets = PaqueteFactory.getInstance().getObjects(PAQUETE_TO_FIND, IServletSimple.class);
		for(IServletSimple ss : sevlets) {
			String sName = ss.getServletName();
			
			if(url.startsWith(ss.getServletName())) {
				 
				if(url != null && url.contains(sName)) {
					try {
						url = url.substring(url.indexOf(sName), url.length() );
						String[] partes = url.split(SLASH);
						if(partes != null && partes.length >= 3) {
							String modulo = partes[1];
							String thing = partes[2];
							
							retorno = (Class<? extends AZonePage>) getClass(modulo, thing);
						}
					}
					catch(Exception e) {
						
					}
				}
			}
		}
		
		
		return retorno;
	}
}
