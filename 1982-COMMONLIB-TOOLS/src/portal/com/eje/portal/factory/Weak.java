package portal.com.eje.portal.factory;

public class Weak {

	public static <T> T getInstance(Class<T> c) {
		return SingleFactory.getFactory(SingleFactoryType.WEAK).getInstance(c);
	}
	
}
