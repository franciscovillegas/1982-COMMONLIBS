package cl.ejedigital.tool.cache;

import java.util.HashMap;

import cl.ejedigital.tool.singleton.Singleton;

public class CacheLocator implements Singleton {
	private static HashMap<String,Cache> instance;
	
	private CacheLocator(){
		
	}
	
	private static HashMap<String,Cache> getCacheBag() {
		if(instance == null) {
			synchronized(CacheLocator.class) {
				if(instance == null) {
					instance = new HashMap<String,Cache>();
				}
			}
		}
		
		return instance;
	}
	
	public static Cache getInstance(CacheTypes type) {
		HashMap<String,Cache> cacheBag = getCacheBag();
		
		if(cacheBag.get(type.toString()) == null) {
			synchronized(cacheBag) {
				if(cacheBag.get(type.toString()) == null) {
					cacheBag.put(type.toString(),new Cache(type.toString()));
				}
			}
		}
		
		return cacheBag.get(type.toString());
	}
}
