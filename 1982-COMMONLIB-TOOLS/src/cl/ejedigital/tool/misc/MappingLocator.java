package cl.ejedigital.tool.misc;

import java.util.HashMap;
import java.util.Map;

import portal.com.eje.portal.factory.SingleFactoryObject;
import portal.com.eje.portal.factory.Static;

/**
 * Clases de caches estaticos
 * @since 05-07-2018
 * 
 * */
public class MappingLocator  {
	
	 
//	private Map<String,Map<Object, Object>> fabrica;
//	private Map<String,CacheContainer> fabricaCaches;
	
	private Map<String,SingleFactoryObject> fabrica;
	private Map<String,SingleFactoryObject> fabricaCaches;
	
	MappingLocator() {
//		fabrica = new HashMap<String, Map<Object,Object>>();
//		fabricaCaches = new HashMap<String, CacheContainer>();
		
		fabrica = new HashMap<String, SingleFactoryObject>();
		fabricaCaches = new HashMap<String, SingleFactoryObject>();
		
	}
	
	public static MappingLocator getInstance() {
		return MappingFactory.getFactory(MappingLocator.class);
	}
	
	public 	Map<Object,Object> getMapping(String nameMapping) {
		if(fabrica.get(nameMapping) == null) {
			synchronized (MappingLocator.class) {
				if(fabrica.get(nameMapping) == null) {
					
					Map map = new HashMap<Object,Object>();
					SingleFactoryObject object = new SingleFactoryObject(map);
					fabrica.put(nameMapping, object);
				}
			}
		}
		
		SingleFactoryObject object = fabrica.get(nameMapping);
		object.countVecesLlamada();
		
		return (Map<Object,Object>)object.getObject();
	}
	
	public CacheContainer getCache(String nameCache, int secondMaxToLiveEveryObjec, int maxCola) {
		if(fabricaCaches.get(nameCache) == null) {
			synchronized (MappingLocator.class) {
				if(fabricaCaches.get(nameCache) == null) {
					
					CacheContainer cache = new CacheContainer(secondMaxToLiveEveryObjec, maxCola);
					
					SingleFactoryObject object = new SingleFactoryObject(cache);
					fabricaCaches.put(nameCache, object);
					
					System.out.println("["+String.valueOf(this.getClass())+ "] Cache:"+ nameCache+" maxSecondsToLive:"+secondMaxToLiveEveryObjec+ "  maxObjects:"+maxCola);									
				}
			}
		}
		
		SingleFactoryObject object = fabricaCaches.get(nameCache);
		object.countVecesLlamada();
		
		return (CacheContainer)object.getObject();
	}
	
	public Map<String, SingleFactoryObject> getObjects() {
		HashMap map =  new HashMap(fabrica);
		map.putAll(fabricaCaches);
		
		return map;
	}
	
	public void clear() {
		fabrica.clear();
		fabricaCaches.clear();
	}
}
