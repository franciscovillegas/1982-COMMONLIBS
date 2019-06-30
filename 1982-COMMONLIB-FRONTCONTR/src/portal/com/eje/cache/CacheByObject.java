package portal.com.eje.cache;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.thoughtworks.xstream.XStream;

import cl.ejedigital.tool.cache.SoftCache;
import cl.ejedigital.tool.misc.MappingFactory;
import cl.ejedigital.tool.misc.SoftCacheLocator;
import cl.ejedigital.tool.misc.WeakCacheLocator;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.parametro.ParametroWithCache;
import portal.com.eje.portal.vo.VoTool;

/**
 * Cacheador de Objetos, Cachea los retornos de los métodos, es multidimensional en relación a las intancias de un mismo objeto.<br/>
 * 
 * Supon que tenemos tres ojetos Persona, y cada uno tiene un método get historial de novias, <br/>
 * El Cache se hará en los tres objetos de forma indistinta.
 * 
 * @since 24-10-2018
 * @author Pancho
 */
public class CacheByObject {
	private Logger logger = Logger.getLogger(CacheByObject.class);

	public static CacheByObject getInstance() {
		return Util.getInstance(CacheByObject.class);
	}

	public static <T> T weak(Object o, String metodoName, Class<?>[] def, Object[] params, Class<T> retornoClass) throws SecurityException, NoSuchMethodException {
		return getInstance().weakCache(o, metodoName, def, params, false, retornoClass, null, false);
	}

	public static <T> T weak(Object o, String metodoName, Class<?>[] def, Object[] params, Class<T> retornoClass, boolean isSuperMethod) throws SecurityException, NoSuchMethodException {
		return getInstance().weakCache(o, metodoName, def, params, false, retornoClass, null, isSuperMethod);
	}
	
	/**
	 * clear Cache debería ser un método
	 * @deprecated
	 *
	 * */
	public static <T> T weak(Object o, String metodoName, Class<?>[] def, Object[] params, boolean clearCache, Class<T> retornoClass) throws SecurityException, NoSuchMethodException {
		return getInstance().weakCache(o, metodoName, def, params, clearCache, retornoClass, null, false);
	}

	public static <T> T soft(Object o, String metodoName, Class<?>[] def, Object[] params, Class<T> retornoClass) throws SecurityException, NoSuchMethodException {
		return getInstance().softCache(o, metodoName, def, params, false, retornoClass, null, false);
	}

	public static <T> T soft(Object o, String metodoName, Class<?>[] def, Object[] params, Class<T> retornoClass, boolean isSuperMethod) throws SecurityException, NoSuchMethodException {
		return getInstance().softCache(o, metodoName, def, params, false, retornoClass, null, isSuperMethod);
	}
	/**
	 * clear Cache debería ser un método
	 * @deprecated
	 *
	 * */
	public static <T> T soft(Object o, String metodoName, Class<?>[] def, Object[] params, boolean clearCache, Class<T> retornoClass) throws SecurityException, NoSuchMethodException {
		return getInstance().softCache(o, metodoName, def, params, clearCache, retornoClass, null, false);
	}

	/**
	 * clear Cache debería ser un método
	 * @deprecated
	 *
	 * */
	
	public static <T> T tenSeconds(Object o, String metodoName, Class<?>[] def, Object[] params, Class<T> retornoClass) throws SecurityException, NoSuchMethodException {
		return getInstance().softCache(o, metodoName, def, params, false, retornoClass, 10, false);
	}
	
	public static <T> T tenSeconds(Object o, String metodoName, Class<?>[] def, Object[] params, Class<T> retornoClass, boolean isSuperMethod) throws SecurityException, NoSuchMethodException {
		return getInstance().softCache(o, metodoName, def, params, false, retornoClass, 10, isSuperMethod);
	}
	
	public static <T> T fiveSeconds(Object o, String metodoName, Class<?>[] def, Object[] params, Class<T> retornoClass) throws SecurityException, NoSuchMethodException {
		return getInstance().softCache(o, metodoName, def, params, false, retornoClass, 5, false);
	}
	
	public static <T> T fiveSeconds(Object o, String metodoName, Class<?>[] def, Object[] params, Class<T> retornoClass, boolean isSuperMethod) throws SecurityException, NoSuchMethodException {
		return getInstance().softCache(o, metodoName, def, params, false, retornoClass, 5, isSuperMethod);
	}
	
	public static void resetCache(Class o) {
		getInstance().reset(o);
	}
	
	/**
	 * El retorno siempre es un Object y no primitivas
	 * 
	 * @author Pancho
	 * @since 19-10-2018
	 * 
	 */
	@SuppressWarnings("unchecked")
	private <T> T weakCache(Object o, String metodoName, Class<?>[] def, Object[] params, boolean clearCache, Class<T> retornoClass, Integer maxSeconds, boolean isSuperMethod) throws SecurityException, NoSuchMethodException {
		Assert.notNull(o);
		Assert.notNull(metodoName);
		Assert.notNull(def);
		Assert.notNull(params);

		WeakHashMap<String, Object> cache = WeakCacheLocator.getInstance(o.getClass());
		String key = CacheTool.getInstance().getKeyObjectMethodDefParams(o, metodoName, def, params).toString();
		if (clearCache) {
			cache.put(key, null);
		}

		ObjectConAntiguedad contenedor = (ObjectConAntiguedad) cache.get(key);
		if (contenedor == null || (contenedor != null && maxSeconds != null && (contenedor.getCro().GetMilliseconds() / 1000) >= maxSeconds)) {
			logger.debug(key+" [building object] "+metodoName);
			
			
			contenedor = CacheTool.getInstance().buildRetorno(o, metodoName, def, params, key, (Map)cache, isSuperMethod);
			
		}
		else {
			logger.debug(key+" [existing object "+contenedor.getCro().getTimeHHMMSS_milli()+"] "+metodoName);
		}

		return (T) (contenedor != null ? contenedor.getObject() : null);
	}

	/**
	 * El retorno siempre es un Object y no primitivas
	 * 
	 * @author Pancho
	 * @since 19-10-2018
	 * 
	 */
	@SuppressWarnings("unchecked")
	private <T> T softCache(Object o, String metodoName, Class<?>[] def, Object[] params, boolean clearCache, Class<T> retornoClass, Integer maxSeconds, boolean isSuperMethod) throws SecurityException, NoSuchMethodException {
		Assert.notNull(o);
		Assert.notNull(metodoName);
		Assert.notNull(def);
		Assert.notNull(params);

		SoftCache cache = SoftCacheLocator.getInstance(o.getClass());
		String key = CacheTool.getInstance().getKeyObjectMethodDefParams(o, metodoName, def, params ).toString();
		if (clearCache) {
			cache.put(key, null);
		}

		ObjectConAntiguedad contenedor = (ObjectConAntiguedad) cache.get(key);
		if (contenedor == null || (contenedor != null && maxSeconds != null && (contenedor.getCro().GetMilliseconds() / 1000) >= maxSeconds)) {
			logger.debug(key+" [building object] "+metodoName);
			
			contenedor = CacheTool.getInstance().buildRetorno(o, metodoName, def, params, key, (Map)cache, isSuperMethod);
		}
		else {
			logger.debug(key+" [existing object "+contenedor.getCro().getTimeHHMMSS_milli()+"] "+metodoName);
		}
		
		return (T) (contenedor != null ? contenedor.getObject() : null);
	}

	private void reset(Class clase) {
		SoftCacheLocator.getInstance(clase).clear();
		WeakCacheLocator.getInstance(clase).clear();
		MappingFactory.getFactory(clase).getMapping("default").clear();
	}
	
	/**
	 * Clona el objeto
	 */
	private Object clone(Object retornoFinal) {
		XStream xtream = new XStream();
		return (xtream.fromXML(xtream.toXML(retornoFinal)));
	}

	

}
