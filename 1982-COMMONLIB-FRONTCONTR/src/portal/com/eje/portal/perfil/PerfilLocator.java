package portal.com.eje.portal.perfil;

import portal.com.eje.portal.factory.SingleFactory;
import portal.com.eje.portal.factory.SingleFactoryType;

public class PerfilLocator {

	public static IPerfil getInstance() {
		return SingleFactory.getFactory(SingleFactoryType.UTIL).getInstance(Perfil.class);
	}
}
