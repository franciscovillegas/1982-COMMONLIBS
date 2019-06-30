package portal.com.eje.portal.organica;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.organica.ifaces.IEncargadoRelativo;

public class EncargadoRelativaLocator {
	
	private EncargadoRelativaLocator() {
		
	}
	
	public static IEncargadoRelativo getInstance() {
		return Util.getInstance(EncargadoRelativo.class);
	}
	
}
