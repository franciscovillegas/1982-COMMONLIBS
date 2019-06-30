package portal.com.eje.portal.trabajador;

import portal.com.eje.portal.factory.SingleFactory;
import portal.com.eje.portal.factory.SingleFactoryType;

public class TrabajadorDataLocator {
	
	public static ITrabajadorData getInstance() {
		return SingleFactory.getFactory(SingleFactoryType.UTIL).getInstance(TrabajadorData.class);
	}
	
	
}
