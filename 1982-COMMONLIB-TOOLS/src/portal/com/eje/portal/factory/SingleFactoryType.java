package portal.com.eje.portal.factory;

public enum SingleFactoryType {
	MODULO(EnumCacheMethod.SOFT),
	UTIL(EnumCacheMethod.SOFT),
	MANAGER(EnumCacheMethod.SOFT),
	CONTROLLER(EnumCacheMethod.SOFT),
	WEAK(EnumCacheMethod.WEAK);
	
	private EnumCacheMethod cacheMethod;

	private SingleFactoryType(EnumCacheMethod cacheMethod) {
		this.cacheMethod = cacheMethod;
	}

	public EnumCacheMethod getCacheMethod() {
		return cacheMethod;
	}

 
	
}
