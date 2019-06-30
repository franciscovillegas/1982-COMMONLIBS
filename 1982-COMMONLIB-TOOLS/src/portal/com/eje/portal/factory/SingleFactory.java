package portal.com.eje.portal.factory;

public class SingleFactory {
	private static IFactories instance;
	
	private SingleFactory() {
		
	}
	
	public static IFactory getFactory(SingleFactoryType type) {
		if(instance == null) {
			synchronized(SingleFactory.class) {
				if(instance == null) {
					instance = new DefaultFactories();
				}
			}
		}
				
		return instance.getInstance(type); 
	}
	
	
}
