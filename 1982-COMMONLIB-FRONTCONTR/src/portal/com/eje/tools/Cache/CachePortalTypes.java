package portal.com.eje.tools.Cache;

import cl.ejedigital.tool.cache.CacheTypes;
import portal.com.eje.cache.Cache;

/**
 * 
 * Mal implementado, debe ocuparse Cache
 * 
 * @deprecated
 * @author Pancho
 * @see Cache
 * 
 * 
 * */
public class CachePortalTypes extends CacheTypes {
	public final static CachePortalTypes CACHEOTHER = new CachePortalTypes("portalCacheOther");
	public final static CachePortalTypes CACHETEMPLATE = new CachePortalTypes("portalCacheTemplate");
	public final static CachePortalTypes CACHEIMAGE = new CachePortalTypes("portalCacheImage");
	public final static CachePortalTypes CACHESTYLE = new CachePortalTypes("portalCacheStyle");
	public final static CachePortalTypes CACHEJS = new CachePortalTypes("portalCacheJs");
	public final static CachePortalTypes CACHEMAPPINGRESOURCES = new CachePortalTypes("portalCacheResourcesMapping");
	public final static CachePortalTypes CACHEORGANICA = new CachePortalTypes("portalCacheOrganica");
	
	/**
	 * 
	 * Mal implementado, debe ocuparse Cache
	 * 
	 * @deprecated
	 * @author Pancho
	 * @see Cache
	 * 
	 * 
	 * */
	protected CachePortalTypes(String name) {
		super(name);
	}

	
}
