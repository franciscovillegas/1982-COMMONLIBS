package cl.ejedigital.web.freemaker.ifaces;

import cl.ejedigital.web.fmrender.FmRenderers;
import freemarker.template.SimpleHash;

public interface IFreemakerSetHash {

	/**
	 * Introduce todos los valores de un JavaBean, en un hash
	 * @author Pancho
	 * @since 24ñ-06-2019
	 *  */
	public <T>void setDataFromVo(SimpleHash modelRoot, T t);
		
	/**
	 * Introduce todos los valores de un JavaBean, en un hash
	 * @author Pancho
	 * @since 24ñ-06-2019
	 *  */
	public <T>void setDataFromVo(SimpleHash modelRoot, T t, FmRenderers<T> rendes, int counter);
}
