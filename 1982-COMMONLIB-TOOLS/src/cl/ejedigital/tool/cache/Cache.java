package cl.ejedigital.tool.cache;

import java.util.ResourceBundle;

import cl.ejedigital.tool.properties.PropertiesTools;


/**
 * @deprecated
 * @see portal.com.eje.Cache
 * */

public class Cache extends SoftCache {
	private ICacheListener listener;
	private boolean conCache;
	
	public Cache(String name) {
		PropertiesTools tool = new PropertiesTools();
		if(tool.existsBundle("cache")) {
			ResourceBundle proper = ResourceBundle.getBundle("cache");
			String booleano = tool.getString(proper,"cache."+name+".enabled","true");
			
			if("true".equals(booleano)) {
				System.out.println("[CACHE:"+name+"] ENABLED");
				conCache = true;
			}
			else {
				System.out.println("[CACHE:"+name+"] DISABLED");
				conCache = false;
			}
		}
		else {
			System.out.println("[CACHE:"+name+"] ENABLED");
		}
	}
	
	@Override
	public synchronized Object put(Object arg0, Object arg1) {
		if(listener != null) {
			listener.put(arg0,arg1);
		}

		if(conCache) {
			return super.put(arg0,arg1);
		}
		else {
			return true;
		}
	}
	
	
	@Override
	public synchronized Object get(Object arg0) {
		if(listener != null) {
			listener.get(arg0);
		}
		
		if(conCache) {
			return super.get(arg0);
		}
		else {
			return null;
		}
	}
	
	@Override
	public synchronized Object remove(Object arg0) {
		if(listener != null) {
			listener.remove(arg0);
		}
		
		if(conCache) {
			return super.remove(arg0);
		}
		else {
			return true;
		}
	}
	
	public void addListener(ICacheListener listener) {
		this.listener = listener;
	}
	
	
}
