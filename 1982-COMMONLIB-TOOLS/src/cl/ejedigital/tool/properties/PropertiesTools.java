package cl.ejedigital.tool.properties;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import portal.com.eje.portal.factory.Util;

public class PropertiesTools implements IPropertiesTools {

	public static IPropertiesTools getInstance() {
		return Util.getInstance(PropertiesTools.class);
	}
	
	public boolean existsBundle(String bundleName) {
		ResourceBundle bundle = null;
		try {
			bundle = ResourceBundle.getBundle(bundleName);
		} catch (java.lang.NullPointerException e) {
			
		} catch (MissingResourceException e) {

		}

		return bundle != null;
	}

	public String getString(ResourceBundle bundle, String key, String value) {
		String ret = value;
		
		if(bundle != null) {
			try {
				ret = bundle.getString(key);
			} catch (NullPointerException e) {
				
			} catch (MissingResourceException e) {
				
			} catch (ClassCastException e) {
				
			}		
		}
		
		return ret;
	}

	public int getInt(ResourceBundle bundle, String key, int value) {
		int ret = value;
		
		if(bundle != null) {
			try {
				ret = Integer.parseInt(bundle.getString(key));
			} catch (NullPointerException e) {
				
			} catch (MissingResourceException e) {
				
			} catch (ClassCastException e) {
				
			} catch (NumberFormatException e) {
				
			}
		}
		
		return ret;
	}


	public List<String> getKeys(ResourceBundle bundle, String keyBase) {
		List<String> lista = new ArrayList<String>();
		if(bundle != null) {
			try {
				Enumeration<?> enu = bundle.getKeys();
				while(enu.hasMoreElements()) {
					String key = (String)enu.nextElement();
					if(key.startsWith(keyBase)) {
						lista.add(key.substring(keyBase.length(),key.length()));	
					}
				}
			} catch (NullPointerException e) {
				
			} catch (MissingResourceException e) {
				
			} catch (ClassCastException e) {
				
			}		
		}
		
		return lista;
	}


	public List<String> getKeys(ResourceBundle bundle) {
		return getKeys(bundle, "");
	}

}
