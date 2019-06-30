package cl.ejedigital.tool.misc;

import java.util.HashMap;
import java.util.Map;

import cl.ejedigital.tool.cache.SoftCache;
import portal.com.eje.portal.factory.SingleFactoryObject;
import portal.com.eje.portal.factory.Static;
 

/**
 * Clase que genera cache weak
 * 
 * @author Pancho
 * @since 05-07-2018
 * 
 * */
public class SoftCacheLocator {
 
	private Map<Class,SingleFactoryObject> cache;
	
	private SoftCacheLocator() {
		cache = new SoftCache();
	}
	
	private static SoftCacheLocator getInstance() {
		return Static.getInstance(SoftCacheLocator.class);
 
	}
	
	public static SoftCache getInstance(Class clazz) {
		SoftCacheLocator instance = getInstance();
		
		SingleFactoryObject o = instance.cache.get(clazz);
		if( o == null) {
			 SoftCache cache =  new SoftCache();
			o = new SingleFactoryObject(cache);
			getInstance().cache.put(clazz, o);
		}
		
		 
		return (SoftCache) o.getObject();	
	}
	
	public Map<String, SingleFactoryObject> getObjects() {
		return new HashMap(cache);
	}
}
