package portal.com.eje.tools.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.tools.ifaces.IClaseGenerica;

/**
 * Modificado para cargar y ejecutar metodos de objetos
 * @author Pancho
 * */
public class ClaseGenericaJDK6 implements IClaseGenerica {
	protected Object objeto;
	protected Class clase;
	
	
	@Override
	public Object getObject() {
		// TODO Auto-generated method stub
		return objeto;
	}
	/**
	 * No es buena para nada que el objeto en cuestion quede dentro de los metodos miembros
	 * 
	 * @author Pancho
	 * @deprecated
	 * */
	public void cargaConstructor(String path,Class<?>[] StrArgsClass,Object[] StrArgs) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		try {
			objeto = getNew(path, StrArgsClass, StrArgs);
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
	
	public <T> T getNewFromClass(Class<T> clazz)  {
		Class[] defs= {};
		Object[] params = {};
		return getNewFromClass(clazz, defs, params);
	}
	
	public <T> T getNewFromClass(Class<T> clazz,Class<?>[] StrArgsClass,Object[] StrArgs)  {
		Object o = null;
		try {
			o = getNew(clazz , StrArgsClass , StrArgs);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		} catch (NoSuchMethodException e) {
			System.out.println(e);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return (T) o;
	}
 
	public Object getNew(String path) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Class[] defs = {};
		Object[] params = {};
		return getNew(path, defs, params);
	}
	public Object getNew(String path,Class<?>[] StrArgsClass,Object[] StrArgs) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
	 
		Class clase = Class.forName(path);
		 
		return getNew(clase, StrArgsClass, StrArgs);
		 
	}
	
	public Object getNew(Class clase,Class<?>[] StrArgsClass,Object[] StrArgs) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		
		Constructor constructor = clase.getDeclaredConstructor(StrArgsClass);
		
		
		if(constructor == null) {
			throw new NoSuchMethodException(clase.getCanonicalName()+".<init>()");
		}
		
		if(!constructor.isAccessible()) {
			constructor.setAccessible(true);
		}
		
		//Constructor StrArgsConstructor = clase.getConstructor(StrArgsClass);
		
		
		
		Object objeto = constructor.newInstance(StrArgs);
		return objeto;
		 
	}
	
	/**
	 * No es buena para nada que el objeto en cuestion quede dentro de los metodos miembros
	 * 
	 * @author Pancho
	 * @deprecated
	 * */
	@SuppressWarnings("rawtypes")
	public Object ejecutaMetodo(String metodo,Class[] StrArgsClass,Object[] StrArgs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return ejecutaMetodo(this.objeto, metodo, StrArgsClass, StrArgs);
	}
	
	@SuppressWarnings("rawtypes")
	public Object ejecutaMetodo(Object objeto, String metodo, Class[] StrArgsClass, Object[] StrArgs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Object resultado = null;

		if(objeto == null) {
			throw new NullPointerException("objeto no puede ser null");
		}
		if(metodo == null) {
			throw new NullPointerException("metodo no puede ser null");
		}
		if(StrArgsClass == null) {
			throw new NullPointerException("StrArgsClass no puede ser null");
		}
		if(StrArgs == null) {
			throw new NullPointerException("StrArgs no puede ser null");
		}
		
		if (objeto != null) {
			Method m = objeto.getClass().getDeclaredMethod(metodo, StrArgsClass);
			resultado = ejecutaMetodo(objeto , m, StrArgsClass, StrArgs);

		}

		return resultado;
	}
	
	public Object ejecutaMetodo(Object objeto, Method method, Class<?>[] StrArgsClass, Object[] StrArgs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if (!method.isAccessible()) {
			method.setAccessible(true);
		}
		
		return method.invoke(objeto, StrArgs);
	}

}