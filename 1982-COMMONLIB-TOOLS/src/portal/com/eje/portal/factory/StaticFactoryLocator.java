package portal.com.eje.portal.factory;

import java.util.HashMap;
/**
 * @deprecated
 * */
public class StaticFactoryLocator {
	public static IFactory instance;
	
	private StaticFactoryLocator() {
		
	}
	
	public static IFactory getFactory() {
		if(instance == null) {
			synchronized(Static.class) {
				if(instance == null) {
					instance = new StaticFactory(HashMap.class);
				}	
			}
		}
		
		return instance;
	}
	
}
