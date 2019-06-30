package portal.com.eje.tools.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Modificado para cargar y ejecutar metodos de objetos
 * @author Pancho
 * */
public class ClaseGenericaJDK8 extends ClaseGenericaJDK6 {
	 
	@Override
	public Object getNew(Class clase,Class<?>[] StrArgsClass,Object[] StrArgs) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Constructor constructor = null;
		try {
			 constructor = clase.getDeclaredConstructor(StrArgsClass);
		}
		catch(NoSuchMethodException e) {
			for(Constructor c : clase.getDeclaredConstructors()) {
				if(c.getParameterTypes().equals(StrArgsClass)) {
					constructor = c;
					break;
				}
			}
		}
		
		if(constructor == null) {
			throw new NoSuchMethodException(clase.getCanonicalName()+".<init>");
		}
		
		Object objeto = constructor.newInstance(StrArgs);
		return objeto;
	}
}