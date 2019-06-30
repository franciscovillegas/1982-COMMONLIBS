package cl.eje.generico;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.tools.ClaseGenerica;
import portal.com.eje.tools.security.Encrypter;

public class RemoteBase {
	protected String urlSW;
	protected Encrypter enc;	
	
	public RemoteBase() {
		enc = new Encrypter();
	}
	
	public RemoteBase(String urlSW) {
		this.urlSW = urlSW;
		enc = new Encrypter();
	}
	
	public Object getObject(String xml_encriptado) throws Exception {
		XStream xs = new XStream();
		String xml = enc.decrypt(xml_encriptado);
		Object o =  xs.fromXML( xml );
		
		if( o instanceof Boolean ) {
			return (Boolean) o;
		}
		else if(o instanceof Map) {
			return (Map) o;
		}
		else if( o instanceof Process  ) {
			return (Process) o;
		}
		else if( o instanceof byte[]  ) {
			return (byte[]) o;
		}
		else if( o instanceof List  ) {
			return (List) o;
		}
		else if( o instanceof Long ) {
			return (Long) o;
		}
		else if( o instanceof File ) {
			return (File) o;
		}
		else if( o instanceof File[] ) {
			return (File[]) o;
		}
		else if(o instanceof ConsultaData) {
			return (ConsultaData) o;	
		}
		else if(o instanceof Exception) {
			Exception e = (Exception) o;
			
			Exception eFinal = new Exception("PROBLEMA REMOTO", e);
			throw eFinal;
		}
		else if(o instanceof RuntimeException) {
			RuntimeException e = (RuntimeException) o;
			
			Exception eFinal = new Exception("PROBLEMA REMOTO", e);
			throw eFinal;
		}
		else {
			if(o == null) {
				System.out.println("[PROBLEMA GRAVE] el objeto no puede ser null ");
			}
			else {
				System.out.println("[PROBLEMA GRAVE] al parecer el tipo de objeto no puede ser parseado por no estar definido como reconocible."+o.getClass());	
			}
			
			return null;
		}
		
	}
	
	public String getXml(Object object) {
		XStream xs = new XStream();
		String xml =  xs.toXML( object );
		xml = enc.encrypt(xml);
		
		return xml;
	}
	

	public Object getProxy(Class toConstruct) {
		try {
			if(toConstruct != null) {
				if(this.urlSW == null) {
					Object[] params = {};
					Class[] defs = {};
					
					ClaseGenerica cg = new ClaseGenerica();
					cg.cargaConstructor(toConstruct.getCanonicalName(), defs, params);
					return cg.getObject();
					
				}
				else {
					Object[] params = {this.urlSW, true};
					Class[] defs = {String.class, Boolean.class};
					
					ClaseGenerica cg = new ClaseGenerica();
					cg.cargaConstructor(toConstruct.getCanonicalName(), defs, params);
					return cg.getObject();
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
