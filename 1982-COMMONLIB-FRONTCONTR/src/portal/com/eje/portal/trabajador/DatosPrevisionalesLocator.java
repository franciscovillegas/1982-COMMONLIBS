package portal.com.eje.portal.trabajador;

import portal.com.eje.portal.factory.SingleFactory;
import portal.com.eje.portal.factory.SingleFactoryType;

public class DatosPrevisionalesLocator {
	
	public static IManagerDatosPrevisionales getInstance() {
		return SingleFactory.getFactory(SingleFactoryType.UTIL).getInstance(ManagerDatosPrevisionales.class);
	}
	
}
