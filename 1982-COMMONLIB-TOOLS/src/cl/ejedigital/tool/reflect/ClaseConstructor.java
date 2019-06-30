package cl.ejedigital.tool.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClaseConstructor {

	public static <T> T getNewFromClass(Class<T> clazz,Class<?>[] StrArgsClass,Object[] StrArgs)  {
		Object o = null;
		try {
			o = getNew(clazz.getCanonicalName(), StrArgsClass , StrArgs);
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
		
		return (T) o;
	}
 

	public static Object getNew(String path,Class<?>[] StrArgsClass,Object[] StrArgs) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		try {
			Class clase = Class.forName(path);
			Constructor StrArgsConstructor = clase.getConstructor(StrArgsClass);
			Object objeto = StrArgsConstructor.newInstance(StrArgs);
			return objeto;
		} 
		catch (ClassNotFoundException e) {
			throw e;
		}
		catch (SecurityException e) {
			throw e;
		}
		catch (NoSuchMethodException e) {
			throw e;
		}
		catch (IllegalArgumentException e) {
			throw e;
		}
		catch (InstantiationException e) {
			throw e;
		}
		catch (IllegalAccessException e) {
			throw e;
		}
		catch (InvocationTargetException e) {
			throw e;
		}
	}
}
