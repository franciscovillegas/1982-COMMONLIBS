package portal.com.eje.portal.cargo;

import portal.com.eje.portal.factory.SingleFactory;
import portal.com.eje.portal.factory.SingleFactoryType;

public class CargoLocator   {

 
	public static ICargo getInstance() {
		return SingleFactory.getFactory(SingleFactoryType.UTIL).getInstance(Cargo.class);
	}
	
}
