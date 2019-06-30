package portal.com.eje.tools.ifaces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface IClaseGenerica {

		public Object getObject();
	
		public void cargaConstructor(String path,Class<?>[] StrArgsClass,Object[] StrArgs) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
		
		public <T> T getNewFromClass(Class<T> clazz);
		
		public <T> T getNewFromClass(Class<T> clazz,Class<?>[] StrArgsClass,Object[] StrArgs);
	 
		public Object getNew(String path) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
		
		public Object getNew(String path,Class<?>[] StrArgsClass,Object[] StrArgs) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
		
		public Object getNew(Class clase,Class<?>[] StrArgsClass,Object[] StrArgs) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
		
		public Object ejecutaMetodo(String metodo,Class[] StrArgsClass,Object[] StrArgs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
		
		public Object ejecutaMetodo(Object objeto, String metodo, Class[] StrArgsClass, Object[] StrArgs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
		
		public Object ejecutaMetodo(Object objeto, Method method, Class<?>[] StrArgsClass, Object[] StrArgs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}
