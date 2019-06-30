package portal.com.eje.portal.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import cl.ejedigital.tool.strings.ArrayFactory;
import portal.com.eje.tools.ClaseGenerica;
/**
 * @deprecated
 * */
public class StaticFactory implements IFactory {

	private boolean stateOnOff;
	private  Map<String, SingleFactoryObject> instances;
	private ClaseGenerica cg;
	
	StaticFactory(Class<? extends Map> clazz) {
		cg = new ClaseGenerica();
		
		//instances = new WeakHashMap<String, SingleFactoryObject>();
		instances = cg.getNewFromClass(clazz);
		
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
		
		SingleFactoryObject o = instances.get(key.toString());
		
		if(o == null) {
			synchronized(DefaultFactory.class) {
				o = instances.get(key.toString());
				if(o == null) {
					try {
						Class<?>[] defs = {};
						Object[] objects = {};
						
						
						o = new SingleFactoryObject(cg.getNew(c.getCanonicalName(), defs, objects));
						
						if(!getStateOnOff()) {
							System.out.println(o);
							return (T)o;
						}
						instances.put(key.toString(), o);
						
					} catch (ClassNotFoundException e) {
						System.out.println(e);
					} catch (NoSuchMethodException e) {
						System.out.println(e);
					} catch (InstantiationException e) {
						System.out.println(e);
					} catch (IllegalAccessException e) {
						System.out.println(e);
					} catch (InvocationTargetException e) {
						System.out.println(e);
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
	
	/*
	private synchronized void put(String key, Object o) {
		if(stateOnOff) {
			instances.put(key, new  SFObject(o));
		}
	}
	
	private synchronized Object get(String key) {
		if(instances.get(key)!=null) {
			return instances.get(key).getObject();
		}
		
		return null;
	}
	*/
 
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
		//System.out.println("[@@@ "+StaticFactory.class.getCanonicalName()+"  StaticFactory estado: "+onOff+"]");
		
		this.stateOnOff = onOff;
		if(!onOff) {
			this.instances.clear();
		}

	}



 
 
}
