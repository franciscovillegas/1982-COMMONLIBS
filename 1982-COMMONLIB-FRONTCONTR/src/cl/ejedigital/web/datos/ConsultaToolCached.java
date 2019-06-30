package cl.ejedigital.web.datos;

import org.apache.log4j.Logger;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.singleton.Singleton;
import portal.com.eje.cache.Cache;
import portal.com.eje.portal.factory.Util;
 


/**
 * ConsultaToolCached tiene el principal propósito cached las llamadas a ConsultaTool
 * 
 * @author Francisco
 * @since 25-10-2018
 * 
 * */

public class ConsultaToolCached  implements Singleton, IConsultaTool {	
	private Logger logger = Logger.getLogger(getClass());
 
	
	private ConsultaToolCached() {

	}
	
	/**
	 * Retorna la única instancia de ConsultaToolCached
	 * 
	 * */
	
	public static ConsultaToolCached getInstance() {
		return Util.getInstance(ConsultaToolCached.class);
	}
	
	@Override
	public ConsultaData getData(String jndi, CharSequence sql, Object[] paramsArray) {

		ConsultaData data = null;
		try {
			Class<?>[] def = {String.class, CharSequence.class, Object[].class};
			Object[] params = {jndi, sql, paramsArray};
			data = Cache.weak(ConsultaTool.getInstance(), "getData", def, params, ConsultaData.class, true);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
}
