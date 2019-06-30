package portal.com.eje.tools.javabeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.Row;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.ClaseConversor;

public class JavaBeanColumnTool  {
	private final String SUFIJO_TYPE = "_type";
	
	public static JavaBeanColumnTool getInstance() {
		return Util.getInstance(JavaBeanColumnTool.class);
	}
	
	public void addColumn(ConsultaData data, Row row, String field, Object value, boolean addTypeOfField) {
		if( ! data.getNombreColumnas().contains(field) ) {
			data.getNombreColumnas().add(field);
		}
		
		if(field != null && row != null) {
			
			Class<?> clase = getClassColumn(value);
			
			row.add(field, getValueColumn(value, clase));
			
			String type = getTypeColumn(clase);
			String dtype = getDeclaringType(clase, value);
			if( dtype != null && dtype.length() > 0) {
				type = type + "<"+dtype+">";
			}
			
			if(addTypeOfField) {
				row.add(field+SUFIJO_TYPE, type );	
			}
		}
	}

	private Object getValueColumn(Object value, Class<?> claseFinal) {
		Object retorno = null;
		if(claseFinal == List.class) {
			retorno =  new Gson().toJson(toListPrimitive((List<?>) value), (new TypeToken<List<?>>(){}).getType());
		}
		else if(claseFinal == Map.class) {
			throw new NotImplementedException();
			//retorno = new Gson().toJson(value, (new TypeToken<List<String>>(){}).getType());
		}
		else {
			retorno = value;
		}
		
		return retorno;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<?> toListPrimitive(List<?> values) {
		
		List  retorno = new ArrayList<>();
		
		if(values != null) {
			for(Object o : values) {
				if(o != null) {
					boolean isNumeric = ClaseConversor.getInstance().getMappingObjectToPrimitive().containsKey(o.getClass());
					boolean isString = CharSequence.class.isAssignableFrom(o.getClass());
					
					if(isNumeric || isString) {
						retorno.add(o);
					}	
					else {
						retorno.add(ClaseConversor.getInstance().getObject(o, String.class));
					}
				}
				
			}
		}
		
		return retorno;
	}
	
	private Class<?> getClassColumn(Object value){
		Class<?> clase = null;
		
		if(value != null  ) {
			boolean esLista = List.class.isAssignableFrom(value.getClass());
			boolean esMap = List.class.isAssignableFrom(value.getClass());
 
			if(esLista) {
				clase = List.class;
			}
			else if(esMap) {
				clase =Map.class;
			}
			else {
				clase = value.getClass();	
			}
		}
		else {
			clase = String.class;
			
		}
		
		return clase;
	}
	
	private String getTypeColumn(Class<?> clase) {
 
		String cn = clase.getCanonicalName();
		cn = cn.substring(cn.lastIndexOf(".")+1, cn.length());	
		
		
		
		return cn;
	}
	
	private String getDeclaringType(Class<?> clase ,Object value) {
		String declaringType = "";
		
		if(clase == List.class && value != null && ((List)value).size()> 0) {
			List<Class<?>> clases = new ArrayList<>();
			
			for(Object v : (List)value) {
				if(v != null) {
					clases.add(v.getClass());
				}
			}
			
			if(isSameObject(clases)) {
				declaringType = clases.iterator().next().getCanonicalName();
			}
		}
		else if(clase == Map.class && value != null && ((Map)value).size()> 0) {
			List<Class<?>> clases = new ArrayList<>();
			
			Map map = ((Map)value);
			Set sets = map.keySet();
			
			for(Object key : sets) {
				Object valueMap = map.get(key);
				
				if(valueMap != null) {
					clases.add(valueMap.getClass());
				}
			}
			
			if(isSameObject(clases)) {
				declaringType = clases.iterator().next().getCanonicalName();
			}
		}
		
		return declaringType;
	}
	
	private boolean isSameObject(List<Class<?>> clases) {
		boolean is = true;
		if(clases != null && clases.size() > 0) {
			for(Class<?> c : clases) {
				for(Class<?> c2 : clases) {
					if(c != c2) {
						is = false;
						break;
					}
				}
			}
		}
		
		return is;
	}
	
}
