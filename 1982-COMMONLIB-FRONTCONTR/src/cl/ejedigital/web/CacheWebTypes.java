package cl.ejedigital.web;

import cl.ejedigital.tool.cache.CacheTypes;


public class CacheWebTypes extends CacheTypes {
	public final static CacheWebTypes CACHETEMPLATE = new CacheWebTypes("portalCacheTemplate");
	public final static CacheWebTypes CACHEIMAGE = new CacheWebTypes("portalCacheImage");
	public final static CacheWebTypes CACHESTYLE = new CacheWebTypes("portalCacheStyle");
	public final static CacheWebTypes CACHEJS = new CacheWebTypes("portalCacheJs");
	public final static CacheWebTypes CACHEMAPPINGRESOURCES = new CacheWebTypes("portalCacheResourcesMapping");
	
	protected CacheWebTypes(String name) {
		super(name);
	}

	
}
