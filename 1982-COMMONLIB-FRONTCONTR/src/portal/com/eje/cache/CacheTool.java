package portal.com.eje.cache;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import portal.com.eje.cache.error.NotConstructorByDefaultException;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.ClaseConversor;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.tools.ClaseGenerica;

public class CacheTool {
	private String COMA = ",";
	private String BLANCO = "";
	private String GUIONBAJO = "_";
	private String ABREPARENTESIS = "(";
	private String CIERRAPARENTESIS = ")";
	private String ABREPARENTESISB = "{";
	private String CIERRAPARENTESISB = "}";
	private String ARROBA = "@";
	
	static CacheTool getInstance() {
		return Util.getInstance(CacheTool.class);
	}
	
	/**
	 * Crea el key a partir de cualquier parámetro, no es multidimensional en la clase
	 * 
	 * @author Pancho
	 * @since 28-12-2018
	 */
	public CharSequence getKeyMethodDefParams(Object object, String methodoName, Class<?>[] def, Object[] params) throws SecurityException, NoSuchMethodException {
		StringBuilder str = new StringBuilder();

		for (Object o : params) {
			if (!BLANCO.equals(str.toString())) {
				str.append(COMA);
			}

			if(o != null && List.class.isAssignableFrom(o.getClass())) {
				str.append(getListKey(o));
			}
			else if(o != null && Object[].class.isAssignableFrom(o.getClass())) {
				str.append(getObjectArray(o));
			}
			else {
				String toString = ClaseConversor.getInstance().getObject(o, String.class);
				if(toString != null && toString.contains(ARROBA)) {
					toString = toString.substring(0, toString.indexOf("@"));
				}
				
				str.append(toString);	
			}
			
		}

		StringBuilder strDef = new StringBuilder();
		for (Class<?> c : def) {
			if (!BLANCO.equals(strDef.toString())) {
				strDef.append(COMA);
			}

			strDef.append(ClaseConversor.getInstance().getObject(c, String.class));
		}
		

		return new StringBuilder().append(methodoName).append(ABREPARENTESIS).append(strDef).append(CIERRAPARENTESIS).append(ABREPARENTESIS).append(str).append(CIERRAPARENTESIS);
	}
	
	/**
	 * Crea el key a partir de cualquier parámetro, SI es multidimensional en la clase
	 * 
	 * @author Pancho
	 * @since 28-12-2018
	 */
	public CharSequence getKeyObjectMethodDefParams(Object object, String methodoName, Class<?>[] def, Object[] params) throws SecurityException, NoSuchMethodException {
		StringBuilder str = new StringBuilder();
		str.append(object.toString()).append(GUIONBAJO).append(getKeyMethodDefParams(object, methodoName, def, params));
		
		return str.toString();
	}

	private CharSequence getListKey(Object o) {
		StringBuilder str = new StringBuilder();
		
		List list = (List) o ;
		for(Object inList : list) {
			if(!BLANCO.equals(str.toString())) {
				str.append(COMA);
			}
			str.append(ClaseConversor.getInstance().getObject(inList, String.class));
		}
		
		StringBuilder retorno = new StringBuilder();
		retorno.append(ABREPARENTESISB).append(str).append(CIERRAPARENTESISB);
		
		return  retorno.toString();
	}
	
	private CharSequence getObjectArray(Object o) {
		StringBuilder str = new StringBuilder();
		
		Object[] list = (Object[]) o ;
		for(Object inList : list) {
			if(!BLANCO.equals(str.toString())) {
				str.append(COMA);
			}
			
			str.append(ClaseConversor.getInstance().getObject(inList, String.class));
		}
		
		StringBuilder retorno = new StringBuilder();
		retorno.append(ABREPARENTESISB).append(str).append(CIERRAPARENTESISB);
		
		return  retorno.toString();
	}
	
	ObjectConAntiguedad buildRetorno(Object o , String metodoName, Class<?>[] def, Object[] params, String key, Map<Object,Object> cache, boolean isSuperMethod) throws NoSuchMethodException {
		ObjectConAntiguedad contenedor = null;
		
		Object retorno = null;
		if(isSuperMethod) {
			Method metodo = VoTool.getInstance().getSuperMethod(o.getClass(), metodoName, def);
			

			if( tieneConstructorSinParametro(o.getClass().getSuperclass())) {
				Object newInstance = ClaseGenerica.getInstance().getNewFromClass(o.getClass().getSuperclass());	
				copyObject(o, newInstance);
				retorno = VoTool.getInstance().getReturnFromMethod( newInstance, metodo, params);
			}
			else {
				throw new NotConstructorByDefaultException("El objeto \""+o.getClass().getSuperclass().getCanonicalName()+"\" debe tener el constructor por defecto.");
			}
			
			
			
			
 			 
		}
		else {
			Method metodo = VoTool.getInstance().getMethodThrowError(o.getClass(), metodoName, def);
			retorno = VoTool.getInstance().getReturnFromMethod( o, metodo, params);
		}
	 
		if(retorno != null) {
			contenedor = new ObjectConAntiguedad(retorno);
			cache.put(key, contenedor);
		}
		 
		return contenedor;
	}

	private boolean tieneConstructorSinParametro(Class<?> clase) {
		for( Constructor<?> c : clase.getDeclaredConstructors()) {
			if(c.getParameterCount() == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	private void copyObject(Object original, Object toCopy) {
		
		List<Field> lFields = getAllFields(original.getClass());
		
		for(Field f : lFields) {
				f.setAccessible(true);
			
				try {
					if(!Modifier.isFinal(f.getModifiers())) {
						f.set(toCopy, f.get(original));
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	private List<Field> getAllFields(Class clazz) {
		Field[] originalFields = clazz.getDeclaredFields();
		
		List<Field> fields = new ArrayList<Field>();
		for(Field f : originalFields) {
			fields.add(f);
		}
		
		if(clazz != null && clazz.getSuperclass() != null) {
			fields.addAll(getAllFields(clazz.getSuperclass()));
		}
		
		return fields;
	}
}
