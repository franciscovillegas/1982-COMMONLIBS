package cl.ejedigital.tool.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @deprecated
 * @see portal.com.eje.tool.Clase
 * */
public class ClaseGenerica {
	private Object objeto;
	private Class clase;
	
	public ClaseGenerica() {
		objeto = null;
		clase = null;
	}
	
	public Object getObjeto(){
		return objeto;
	}
	
	public void cargaConstructor(String path,Class<?>[] StrArgsClass,Object[] StrArgs) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		try {
			objeto = new Object();	
			clase = Class.forName(path);
			Constructor StrArgsConstructor;
			StrArgsConstructor = clase.getConstructor(StrArgsClass);
			objeto = StrArgsConstructor.newInstance(StrArgs);        				
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
	
	public Object ejecutaMetodo(String metodo,Class[] StrArgsClass,Object[] StrArgs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Object resultado = null;
		try {
			if(objeto!=null) {
				Method runObj;
				runObj = clase.getMethod(metodo, StrArgsClass);
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