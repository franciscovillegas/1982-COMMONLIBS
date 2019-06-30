package cl.ejedigital.tool.misc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CacheContainer {
	private int seconds;
	private int maxCola;
	private Map<String, CacheObject> map; 
	
	public CacheContainer(int secondMaxToLiveEveryObject, int maxCola) {
		this.seconds = secondMaxToLiveEveryObject;
		this.maxCola = maxCola;
		this.map = new LinkedHashMap<String, CacheObject>();
	}	
	
	public Object get(Object key) {
		if(map.get(key) != null) {
			if(map.get(key).getSecondOld() > seconds) {
				map.remove(key);
			}
		}
		
		if( map.get(key) == null) {
			return null;
		}
		else {
			return map.get(key).getObject();
		}
	}

	public void put(String key, Object o) {
		
		CacheObject cObject = new CacheObject(o);
		map.put(key, cObject );
		
		if(map.size() >=maxCola) {
			Set<String> set = map.keySet();
			String keyPrimero = null;
			for(String s: set) {
				keyPrimero = s;
				break;
			}
		
			map.remove(keyPrimero);
		}
	}
	
}
