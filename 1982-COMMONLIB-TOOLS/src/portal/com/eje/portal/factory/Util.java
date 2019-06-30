package portal.com.eje.portal.factory;

import java.util.Map;

public class Util  {

	public static <T> T getInstance(Class<T> c) {
		return SingleFactory.getFactory(SingleFactoryType.UTIL).getInstance(c);
	}

	public static <T> T getInstance(String c) {
		return SingleFactory.getFactory(SingleFactoryType.UTIL).getInstance(c);
	}

	public static Map<String, SingleFactoryObject> getObjects() {
		return SingleFactory.getFactory(SingleFactoryType.UTIL).getObjects();
	}
}
