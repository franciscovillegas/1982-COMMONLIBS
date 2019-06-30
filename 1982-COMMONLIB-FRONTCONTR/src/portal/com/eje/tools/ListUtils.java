package portal.com.eje.tools;
 
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.ClaseConversor;
import portal.com.eje.portal.vo.VoTool;

public class ListUtils {

	@SuppressWarnings("unchecked")
	public static <T> List<T> getList(T[] valores) {
		return (List<T>) ListUtils.getInstance().getList(valores, ArrayList.class);
	}
	
	public static <T> List<T> getListAndConvert(Object[] valores ,Class<T> toConvert) {
		return ListUtils.getInstance().getList(valores, ArrayList.class, toConvert);
	}
	
	public static ListUtils getInstance() {
		return Util.getInstance(ListUtils.class);
	}
	
	/**
	 * 
	 * Retona la concatenación de los values de una lista<br/>
	 * Ejemplo: value1,value2,value3...,valuen
	 * 
	 * 
	 @author Pancho
	 @since 03-07-2018
	 * */
	public String concatenateValues(List lista) {
		StringBuilder str = new StringBuilder();
		
		if(lista != null && lista.size() > 0) {
			for(Object o : lista) {
				if(!"".equals(str.toString())){
					str.append(",");
				}
				str.append(String.valueOf(o));
			}
		}
		
		return str.toString();
	}

	/**
	 * 
	 * Retona la concatenación de los caracteres replace por cada item de la lista<br/>
	 * Ejemplo: <replace1>,<replace2>,<replace3>...,<replacen>
	 * 
	 @author Pancho
	 @since 03-07-2018
	 * */
	public Object concatenateReplace(List<String> lista, String replace) {
		StringBuilder str = new StringBuilder();
		
		if(lista != null && lista.size() > 0) {
			for(Object o : lista) {
				if(!"".equals(str.toString())){
					str.append(",");
				}
				str.append(replace);
			}
		}
		
		return str.toString();
	}

	/**
	 * 
	 * Retona la concatenación de los campos más un sufijo por cada uno de ellos<br/>
	 * Ejemplo: value1<sufijo1>,value2<sufijo2>,value3<sufijo3>...,valuen<sufijon>
	 * 
	 @author Pancho
	 @since 03-07-2018
	 * */
	public Object concatenateSufijo(List<String> lista,String sufijo) {
		StringBuilder str = new StringBuilder();
		
		if(lista != null && lista.size() > 0) {
			for(Object o : lista) {
				if(!"".equals(str.toString())){
					str.append(",");
				}
				str.append(String.valueOf(o)).append(sufijo);
			}
		}
		
		return str.toString();
	}

	public <T> T changeType(List lista, Class<? extends List> class1, Class class2) {
		ClaseGenerica cg = Util.getInstance(ClaseGenerica.class);
		List t = (List) cg.getNewFromClass(class1);
		
		if(lista != null) {
			for(Object o : lista) {
				t.add(ClaseConversor.getInstance().getObject(o, class2));
			}
		}
		
		return (T)t;
	}

	/**
	 * Retorna los objetos de una lista, separados por "," con los retornos del metodo que hace referencia al field
	 * */
	public <T>String concatenateValues(List<T> vos, String field) {
		StringBuilder retorno = new StringBuilder();
		
		if(vos != null && vos.size() > 0) {
			Method mGet = VoTool.getInstance().getGetMethod(vos.iterator().next().getClass(), field);
			
			if(mGet != null) {
				for(Object o : vos ) {
					Object objRetorno = VoTool.getInstance().getReturnFromMethod(o, mGet);
					
					if(objRetorno != null) {
						if(!"".equals(retorno.toString())) {
							retorno.append(",");
						}
						retorno.append(String.valueOf(objRetorno));
					}
				}
			}
		}
		
		return retorno.toString();
	}

	@SuppressWarnings("unchecked")
	/**
	 * Retorna una lista con los valores de un arreglo String[] Object[] Integer[] o cualquier tipo de arreglo
	 * 
	 * @author Pancho
	 * @since 29-08-2018
	 * */
	public List<Object> getList(Object[] valores, @SuppressWarnings("rawtypes") Class<? extends List> clazz) {
		return getList(valores, clazz, null);
	}
	
	public <T> List<T> getList(Object[] valores, @SuppressWarnings("rawtypes") Class<? extends List> clazz, Class<T> toConvertObject) {
		List<T> lista = ClaseGenerica.getInstance().getNewFromClass(clazz);
		
		if(valores != null) {
			for(Object v : valores) {
				if(toConvertObject != null) {
					lista.add(ClaseConversor.getInstance().getObject(v, toConvertObject));
				}
				else {
					lista.add((T) v);
				}
			}
		}
		
		return lista;
	}

	/**
	 * Agrega en la lista todos los objetos de la lista2 que no están contenidos en lista
	 * 
	 * @author Pancho
	 * @since
	 * */
	@SuppressWarnings("rawtypes")
	public boolean addIfNotExists(List<Class> lista, List<Class> listaRecursiva) {
		if(lista != null && listaRecursiva != null) {
			for(Class o : listaRecursiva) {
				if( !lista.contains(o)) {
					lista.add(o);
				}
			}
		}
		
		return true;
	}
}
