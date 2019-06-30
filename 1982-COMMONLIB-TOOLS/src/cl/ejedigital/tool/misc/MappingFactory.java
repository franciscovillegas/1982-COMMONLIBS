package cl.ejedigital.tool.misc;

import java.util.HashMap;
import java.util.Map;

import portal.com.eje.portal.factory.SingleFactoryObject;
import portal.com.eje.portal.factory.Static;

public class MappingFactory {
	private Map<Class, SingleFactoryObject> caches;
	
	private MappingFactory() {
		caches = new HashMap<Class, SingleFactoryObject>();	
	}
	
	public static MappingFactory getInstance() {
		return Static.getInstance(MappingFactory.class);
	}
	
	public static MappingLocator getFactory(Class e) {
		MappingFactory instance = Static.getInstance(MappingFactory.class);
		
		if(instance.caches.get(e) == null) {
			synchronized (MappingFactory.class) {
				if(instance.caches.get(e) == null) {
					SingleFactoryObject object = new SingleFactoryObject(new MappingLocator());
					
					instance.caches.put(e, object);
				}
			}
		}
		
		SingleFactoryObject object = instance.caches.get(e);
		object.countVecesLlamada();
		return (MappingLocator)object.getObject();
	}
	
	public static void clearFactory(Class e) {
		getFactory(e).clear();
 
	}
	
	public Map<Class, SingleFactoryObject> getObjects() {
		return new HashMap(caches);
	}
	
}
