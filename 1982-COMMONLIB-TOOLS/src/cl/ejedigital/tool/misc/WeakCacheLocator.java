package cl.ejedigital.tool.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import portal.com.eje.portal.factory.SingleFactoryObject;
import portal.com.eje.portal.factory.Static;
 

/**
 * Clase que genera cache weak
 * 
 * @author Pancho
 * @since 05-07-2018
 * 
 * */
public class WeakCacheLocator {
 
	private Map<Class,SingleFactoryObject> cache;
	
	private WeakCacheLocator() {
		cache = SoftCacheLocator.getInstance(getClass());
	}
	
	private static WeakCacheLocator getInstance() {
		return Static.getInstance(WeakCacheLocator.class);
 
	}
	
	public static WeakHashMap getInstance(Class clazz) {
		WeakCacheLocator instance = getInstance();
		
		SingleFactoryObject o = instance.cache.get(clazz);
		if( o == null) {
			WeakHashMap<Object,Object > cache = new WeakHashMap<Object,Object >();
			o = new SingleFactoryObject(cache);
			instance.cache.put(clazz, o);	 
		}
		
		return (WeakHashMap) o.getObject();	
	}
	
	public Map<String, SingleFactoryObject> getObjects() {
		return new HashMap(cache);
	}
}
