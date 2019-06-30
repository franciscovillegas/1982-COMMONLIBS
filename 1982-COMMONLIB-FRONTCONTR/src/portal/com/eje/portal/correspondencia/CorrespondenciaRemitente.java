package portal.com.eje.portal.correspondencia;

import java.sql.SQLException;

import cl.eje.model.generic.portal.Eje_correspondencia_remitente;
import portal.com.eje.cache.Cache;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Util;

public class CorrespondenciaRemitente {

	public static CorrespondenciaRemitente getInstance() {
		return Util.getInstance(CorrespondenciaRemitente.class);
	}

	/**
	 * Retorna el registro de la tabla Eje_correspondencia_remitente responsable del
	 * EModulo.<br/>
	 * Nunca retorna null.<br/>
	 * 
	 * @author Pancho
	 * @since 19-10-2018
	 */
	public Eje_correspondencia_remitente getRemitente(EModulos modulos) {

		Eje_correspondencia_remitente remitente = null;
		try {

			Boolean existe = CorrespondenciaRemitenteManager.getInstance().existe(modulos);
			if (!existe) {
				CorrespondenciaRemitenteManager.getInstance().createRemitente(modulos);
			}

			remitente = CorrespondenciaRemitenteManager.getInstance().getRemitente(modulos);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (remitente == null) {
			remitente = new Eje_correspondencia_remitente();
		}
		return remitente;
	}

}
