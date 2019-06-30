package portal.com.eje.portal.factory;

import java.util.HashMap;
import java.util.Map;

public class DefaultFactories implements IFactories {
	private Map<SingleFactoryType,IFactory> instances;
	
	DefaultFactories() {
		instances = new HashMap<SingleFactoryType, IFactory>();
 
	}
	
	@Override
	public IFactory getInstance(SingleFactoryType type) {
		if(instances.get(type) == null) {
			synchronized (DefaultFactories.class) {
				if(instances.get(type) == null) {
					 instances.put(type, new DefaultFactory(type));	
					 
				}
			}
		}
		
		return instances.get(type);
	}
 
	
}
