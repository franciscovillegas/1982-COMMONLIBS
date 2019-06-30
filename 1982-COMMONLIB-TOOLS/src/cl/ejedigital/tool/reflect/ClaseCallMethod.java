package cl.ejedigital.tool.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClaseCallMethod {

	public static Object ejecutaMetodo(Object objeto, String metodo,Class[] StrArgsClass,Object[] StrArgs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Object resultado = null;
		try {
			if(objeto!=null) {
				Method runObj;
				runObj = objeto.getClass().getMethod(metodo, StrArgsClass);
				resultado = runObj.invoke(objeto,StrArgs);
			}
		}
		catch(SecurityException e) {
			throw e;
		}
		catch(NoSuchMethodException e) {
			throw e;
		}
		catch(IllegalArgumentException e) {
			throw e;
		}
		catch(IllegalAccessException e) {
			throw e;
		}
		catch(InvocationTargetException e) {
			throw e;
		}
		return resultado;
	}
	
}
