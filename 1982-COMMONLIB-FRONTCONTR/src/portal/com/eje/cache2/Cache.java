package portal.com.eje.cache2;

import org.apache.log4j.Logger;

import cl.ejedigital.tool.misc.MappingFactory;
import cl.ejedigital.tool.misc.SoftCacheLocator;
import cl.ejedigital.tool.misc.WeakCacheLocator;
import portal.com.eje.portal.factory.Util;

/**
 * Cacheador de Objetos, Cachea los retornos de los métodos, NO ES multidimensional en relación a las intancias de un mismo objeto.<br/>
 * 
 * Supon que tenemos tres ojetos Persona, y cada uno tiene un método get historial de novias, <br/>
 * El Cache se hará sobre una instancia de las tres instancias
 * 
 * @since 24-10-2018
 * @author Pancho
 */
public class Cache extends portal.com.eje.cache.Cache {
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(Cache.class);

	public static Cache getInstance() {
		return Util.getInstance(Cache.class);
	}

 

	public static <T> T weak(Object o, String metodoName, Class<?>[] def, Object[] params, Class<T> retornoClass, boolean isSuperMethod)  {
		T retorno = null;
		try {
			 retorno = getInstance().weakCache(o, metodoName, def, params, false, retornoClass, null, isSuperMethod);
		} catch (SecurityException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}

	 

	public static <T> T soft(Object o, String metodoName, Class<?>[] def, Object[] params, Class<T> retornoClass, boolean isSuperMethod)   {
		T retorno = null;
		try {
			retorno =getInstance().softCache(o, metodoName, def, params, false, retornoClass, null, isSuperMethod);
		} catch (SecurityException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	 
	 
	
	public static <T> T tenSeconds(Object o, String metodoName, Class<?>[] def, Object[] params, Class<T> retornoClass, boolean isSuperMethod)   {
		T retorno = null;
		try {
			retorno = getInstance().softCache(o, metodoName, def, params, false, retornoClass, 10, isSuperMethod);
		} catch (SecurityException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	 
	
	public static <T> T fiveSeconds(Object o, String metodoName, Class<?>[] def, Object[] params, Class<T> retornoClass, boolean isSuperMethod)   {
		T retorno = null;
		
		try {
			retorno = getInstance().softCache(o, metodoName, def, params, false, retornoClass, 5, isSuperMethod);
		} catch (SecurityException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
//	public static void resetCache(Class<?> clase) {
//		getInstance().reset(clase);
//	}
//	 
//	private void reset(Class<?> clase) {
//		SoftCacheLocator.getInstance(clase).clear();
//		WeakCacheLocator.getInstance(clase).clear();
//		MappingFactory.getFactory(clase).getMapping("default").clear();
//	}

}
