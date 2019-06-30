package portal.com.eje.portal.factory;

import java.util.Map;

/**
 * Se debe usar Util.
 * @deprecated
 * */
public class Ctr {

	/**
	 * Se debe usar Util.
	 * @deprecated
	 * */
	public static <T> T getInstance(Class<T> c) {
		return SingleFactory.getFactory(SingleFactoryType.CONTROLLER).getInstance(c);
	}

	/**
	 * Se debe usar Util.
	 * @deprecated
	 * */
	public static <T> T getInstance(String c) {
		return SingleFactory.getFactory(SingleFactoryType.CONTROLLER).getInstance(c);
	}
	
	/**
	 * Se debe usar Util.
	 * @deprecated
	 * */
	public static Map<String, SingleFactoryObject> getObjects() {
		return SingleFactory.getFactory(SingleFactoryType.CONTROLLER).getObjects();
	}

}
