package portal.com.eje.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.thoughtworks.xstream.XStream;

import cl.ejedigital.tool.misc.JavaVersion;

public class MyXstream extends XStream {

	public static MyXstream getXstream() {
		MyXstream xs = new MyXstream();
		
		if ( JavaVersion.getVersion() > 1.6 ) {
			//					XStream.setupDefaultSecurity(xs); // to be removed after 1.5
			//					xs.allowTypesByWildcard(new String[] {
			//					    "**"
			//					});
			//MyXstream.privateSetupDefaultSecurity(xs);
			MyXstream.privateAllowTypesByWildcard(xs, new String[] {
					"portal.eje.cache.Cache"
			});
			
			
		}
		
		return xs;
	}
	
	private static void privateAllowTypesByWildcard(XStream xs, String[] wilcards) {
		try {
			Class<?>[] def = {String[].class};
			Method setupDefaultSecurity = XStream.class.getDeclaredMethod("allowTypesByWildcard" , def);
			if(!setupDefaultSecurity.isAccessible()) {
				setupDefaultSecurity.setAccessible(true);
			}
			
			if(setupDefaultSecurity != null) {
				Object[] params = {wilcards};
				ClaseGenerica.getInstance().ejecutaMetodo(xs, setupDefaultSecurity, def, params);
				

			}
		} catch (SecurityException e) {
			 
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	private static void privateSetupDefaultSecurity(XStream xs) {
		try {
			Class<?>[] def = {XStream.class};
			Method setupDefaultSecurity = XStream.class.getDeclaredMethod("setupDefaultSecurity" , def);
			if(!setupDefaultSecurity.isAccessible()) {
				setupDefaultSecurity.setAccessible(true);
			}
			
			if(setupDefaultSecurity != null) {
				Object[] params = {xs};
				ClaseGenerica.getInstance().ejecutaMetodo(XStream.class, setupDefaultSecurity, def, params);
				

			}
		} catch (SecurityException e) {
			 
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Clonador de Objetos
	 * 
	 * @author Pancho
	 * @since 27-02-2019
	 * */
	public <T> T clone(T object) {
		
		XStream x = getXstream();
		return (T) x.fromXML( x.toXML(object) );
	}
}
