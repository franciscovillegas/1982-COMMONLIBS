package portal.com.eje.tools.deprecates;

import portal.com.eje.cache.Cache;
import portal.com.eje.portal.factory.Util;

class AssertWarnCached extends AssertWarn {

	public static AssertWarnCached getInstance() {
		return Util.getInstance(AssertWarnCached.class);
	}
	
	@Override
	public void advice(Class<?> claseAvisadora, String msg) {
		
		try {
			Class<?>[] def = {Class.class, String.class};
			Object[] params = {claseAvisadora, msg};
			
			Cache.weak(this, "advice", def, params, void.class, true);
		} catch (SecurityException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
}
