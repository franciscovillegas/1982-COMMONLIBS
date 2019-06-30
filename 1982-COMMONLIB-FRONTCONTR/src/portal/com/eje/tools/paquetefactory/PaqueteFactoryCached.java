package portal.com.eje.tools.paquetefactory;

import java.util.List;

import portal.com.eje.cache2.Cache;
import portal.com.eje.portal.factory.Util;

/**
 * Retorna objetos ubicados en paquetes
 * 
 * @since 27-05-2019
 * */
class PaqueteFactoryCached extends PaqueteFactory {
	
	public static PaqueteFactoryCached getInstance() {
		return Util.getInstance(PaqueteFactoryCached.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getObjects(String vpaquete, Class<T> interfaze) {
		Class<?>[] def = {String.class, Class.class};
		Object[] params = {vpaquete, interfaze};
		return Cache.weak(this, "getObjects", def, params, List.class, true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getObjects(String vpaquete, Class<T> interfaze, IFiltroJavaBean<T> filtro) {
		Class<?>[] def = {String.class, Class.class, IFiltroJavaBean.class};
		Object[] params = {vpaquete, interfaze, filtro};
		return Cache.weak(this, "getObjects", def, params, List.class, true);
	}
}
