package portal.com.eje.portal.correspondencia;

import cl.eje.model.generic.portal.Eje_correspondencia_remitente;
import portal.com.eje.cache.Cache;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Util;

public class CorrespondenciaRemitenteCached {

	public static CorrespondenciaRemitenteCached getInstance() {
		return Util.getInstance(CorrespondenciaRemitenteCached.class);
	}

	/**
	 * Retorna el registro de la tabla Eje_correspondencia_remitente responsable del EModulo.<br/>
	 * Nunca retorna null.<br/>
	 * 
	 * @author Pancho
	 * @since 19-10-2018
	 * */
	public Eje_correspondencia_remitente getRemitente(EModulos modulos) {

		Eje_correspondencia_remitente remitente = null;
		try {
			Class<?>[] def = { EModulos.class };
			Object[] params = { modulos == null ? EModulos.getThisModulo() : modulos };
			remitente = Cache.weak(CorrespondenciaRemitente.getInstance(), "getRemitente", def, params, Eje_correspondencia_remitente.class);
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return remitente;
	}

}
