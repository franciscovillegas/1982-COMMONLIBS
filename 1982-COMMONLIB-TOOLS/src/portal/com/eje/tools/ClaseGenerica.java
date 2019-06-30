package portal.com.eje.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cl.ejedigital.tool.misc.JavaVersion;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.tools.ifaces.IClaseGenerica;
import portal.com.eje.tools.impl.ClaseGenericaJDK6;

/**
 * Modificado para cargar y ejecutar metodos de objetos
 * 
 * @author Pancho
 */
public class ClaseGenerica implements IClaseGenerica {
	private IClaseGenerica impl;

	public static ClaseGenerica getInstance() {
		return Util.getInstance(ClaseGenerica.class);
	}

	public ClaseGenerica() {
		Double version = JavaVersion.getVersion();

		//if (version < 1.8) {
			impl = new ClaseGenericaJDK6();
		//} else {
			//impl = new ClaseGenericaJDK8();
		//}
	}

	public Object getObject() {
		return impl.getObject();
	}

	/**
	 * No es buena para nada que el objeto en cuestion quede dentro de los metodos
	 * miembros
	 * 
	 * @author Pancho
	 * @deprecated
	 */
	public void cargaConstructor(String path, Class<?>[] StrArgsClass, Object[] StrArgs) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		impl.cargaConstructor(path, StrArgsClass, StrArgs);
	}

	public <T> T getNewFromClass(Class<T> clazz) {
		return impl.getNewFromClass(clazz);
	}

	public <T> T getNewFromClass(Class<T> clazz, Class<?>[] StrArgsClass, Object[] StrArgs) {
		return impl.getNewFromClass(clazz, StrArgsClass, StrArgs);
	}

	public Object getNew(String path) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return impl.getNew(path);
	}

	public Object getNew(String path, Class<?>[] StrArgsClass, Object[] StrArgs) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

		return impl.getNew(path, StrArgsClass, StrArgs);

	}

	public Object getNew(Class clase, Class<?>[] StrArgsClass, Object[] StrArgs) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return impl.getNew(clase, StrArgsClass, StrArgs);

	}

	/**
	 * No es buena para nada que el objeto en cuestion quede dentro de los metodos
	 * miembros
	 * 
	 * @author Pancho
	 * @deprecated
	 */
	public Object ejecutaMetodo(String metodo, Class[] StrArgsClass, Object[] StrArgs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return impl.ejecutaMetodo(metodo, StrArgsClass, StrArgs);
	}

	public Object ejecutaMetodo(Object objeto, String metodo, Class[] StrArgsClass, Object[] StrArgs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return impl.ejecutaMetodo(objeto, metodo, StrArgsClass, StrArgs);
	}

	@Override
	public Object ejecutaMetodo(Object objeto, Method method, Class<?>[] StrArgsClass, Object[] StrArgs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return impl.ejecutaMetodo(objeto, method, StrArgsClass, StrArgs);
	}
}