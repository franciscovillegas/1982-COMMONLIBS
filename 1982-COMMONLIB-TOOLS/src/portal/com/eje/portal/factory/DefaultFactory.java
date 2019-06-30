package portal.com.eje.portal.factory;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;

import cl.ejedigital.tool.misc.SoftCacheLocator;
import cl.ejedigital.tool.misc.WeakCacheLocator;
import cl.ejedigital.tool.strings.ArrayFactory;
import portal.com.eje.tools.ClaseGenerica;
 

public class DefaultFactory implements IFactory {
	private SingleFactoryType type;
	private boolean stateOnOff;
	private Map<Object,Object> instances;
	private ClaseGenerica cg;
	
	@SuppressWarnings("unchecked")
	DefaultFactory(SingleFactoryType type) {
		this.type = type;
		cg = new ClaseGenerica();
		
		switch (type.getCacheMethod()) {
		case SOFT:
			instances = SoftCacheLocator.getInstance(getClass());
			break;
		case WEAK:
			instances = WeakCacheLocator.getInstance(getClass());
			break;
		case PHANTOM:
			throw new NotImplementedException();
		default:
			break;
		}
		this.type = type;
		//instances = cg.getNewFromClass(cl.ejedigital.tool.cache.SoftCache.class);
		
		
		setStateOnOff(true);
	}
	

	@Override
	public <T> T getInstance(String c) {
		if(c != null) {
			 
			try {
				Class clazz = Class.forName(c);
				return (T) this.getInstance(clazz);
				
			} catch (ClassNotFoundException e) {
				System.out.println(e);
			}
		}
		return null;
	}
	
	public <T extends Object> T getInstance(Class<T>  c) {
		
		if(c == null) {
			return null;
		}

		StringBuilder key = new StringBuilder();
		key.append(c.getCanonicalName());
		
		SingleFactoryObject o = (SingleFactoryObject)instances.get(key.toString());
		
		if(o == null) {
			synchronized(DefaultFactory.class) {
				o = (SingleFactoryObject)instances.get(key.toString());
				if(o == null) {
					
					try {
						Class<?>[] defs = {};
						Object[] objects = {};
						 
						Object newObject =cg.getNewFromClass(c);
						o = new SingleFactoryObject(newObject);
						
						if(!getStateOnOff()) {
							System.out.println(o);
							return (T)o;
						}
						instances.put(key.toString(), o);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		
		if( o != null) {
			o.countVecesLlamada();
			return (T) (o).getObject();
		}
		else {
			return null;
		}
		
	}
	
 
 
	private Object toString(Object[] objects) {
		ArrayFactory af = new ArrayFactory();
		
		if(objects.length > 1) {
			for(Object o : objects) {
				af.add(o.toString());
			}
		}
		else {
			af.add("");
		}
		
		return af.getArrayInteger();
	}

	private String toString(Class<?>[] defs) {
		ArrayFactory af = new ArrayFactory();
		
		if(defs.length > 1) {
			for(Class<?> c : defs) {
				af.add(c.getCanonicalName());
			}
		}
		else {
			af.add("");
		}
		
		return af.getArrayInteger();
	}

	@Override
	public Map<String, SingleFactoryObject> getObjects() {
		return new HashMap(instances);
	}

	@Override
	public boolean getStateOnOff() {
		return stateOnOff;
	}

	@Override
	public void setStateOnOff(boolean onOff) {
		//System.out.println("[@@@ "+DefaultFactory.class.getCanonicalName()+" "+type+" estado: "+onOff+"]");
		
		this.stateOnOff = onOff;
		if(!onOff) {
			this.instances.clear();
		}

	}

}
