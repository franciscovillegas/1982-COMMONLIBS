package portal.com.eje.portal.tool;

import portal.com.eje.cache.Cache;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Util;

class AplicationsToolCached extends AplicationsTool {

	public static AplicationsToolCached getInstance() {
		return Util.getInstance(AplicationsToolCached.class);
	}
	
	@Override
	public String getDatabase(String conectionJNDI) {
		String retorno = null;
		try {
			Class<?>[] def = {String.class};
			Object[] params = {conectionJNDI};
			retorno = Cache.weak(this, "getDatabase", def, params, String.class, true);
		} catch (SecurityException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	@Override
	public String getDatabase(EModulos modulos, String conectionJNDI) {
		String retorno = null;
		try {
			Class<?>[] def = {EModulos.class, String.class};
			Object[] params = {modulos, conectionJNDI};
			retorno = Cache.weak(this, "getDatabase", def, params, String.class, true);
		} catch (SecurityException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	@Override
	public String getDatabaseNameFromUrl(String url) {
		String retorno = null;
		try {
			Class<?>[] def = {String.class};
			Object[] params = {url};
			retorno = Cache.weak(this, "getDatabaseNameFromUrl", def, params, String.class, true);
		} catch (SecurityException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
}
