package portal.com.eje.portal.factory;

import java.util.Map;

/**
 * @deprecated
 * */
public class Static {
	/**
	 * @deprecated
	 * */
	public static <T> T getInstance(Class<T> c) {
		return StaticFactoryLocator.getFactory().getInstance(c);
	}
	/**
	 * @deprecated
	 * */
	public static <T> T getInstance(String c) {
		return StaticFactoryLocator.getFactory().getInstance(c);
	}
	/**
	 * @deprecated
	 * */
	public static Map<String, SingleFactoryObject> getObjects() {
		return StaticFactoryLocator.getFactory().getObjects();
	}
}
