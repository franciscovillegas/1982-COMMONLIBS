package portal.com.eje.tools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class EnumsTools  {

	protected EnumsTools() {

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T getEnumByNameIngoreCase(Class<? extends Enum<?>> clazz, String name, Object defaultValue) {
		if(clazz != null && name != null) {
			List<T> enums = getArrayList(clazz);
			for(T t : enums) {
				if(((Enum) t).name().toLowerCase().equals(name.toLowerCase()) ) {
					defaultValue = t;
					break;
				}
			}
		}
		
		return (T) defaultValue;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T getEnumByName(Class<? extends Enum<?>> clazz, String name, Object defaultValue) {
		if(clazz != null && name != null) {
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields) {
				if(clazz.isAssignableFrom(f.getType())) {
					T e = (T)Enum.valueOf((Class)clazz, f.getName());
					if(name != null && name.equals(((Enum)e).name())) {
						defaultValue = e;
						break;
					}
				}
			}
		}
		
		
		
		return (T) defaultValue;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T getEnumByToString(Class<? extends Enum<?>> clazz, String name, Object defaultValue) {
		if(clazz != null && name != null) {
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields) {
				if(clazz.isAssignableFrom(f.getType())) {
					T e = (T)Enum.valueOf((Class)clazz, f.getName());
					if(name != null && name.equals(((Enum)e).toString())) {
						defaultValue = e;
						break;
					}
				}
			}
		}
		
		return (T) defaultValue;
	}
	
	@SuppressWarnings("unchecked")
	public <T>List<T> getArrayList(Class<? extends Enum<?>> clazz) {
		List<T> lista = null;
		
		if(clazz != null) {
			lista = new ArrayList<T>();
			
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields) {
				if(clazz.isAssignableFrom(f.getType())) {
					Object e = Enum.valueOf((Class)clazz, f.getName());
					
					lista.add((T)e);
				}
			}
		}
		
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getEnum(Class<? extends Enum<?>> clazz, String toString, Object defaultValue) {
		if(clazz != null && toString != null) {
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields) {
				if(clazz.isAssignableFrom(f.getType())) {
					T e = (T)Enum.valueOf((Class)clazz, f.getName());
					if(toString != null && toString.equals(e.toString()) || ((Enum<?>)e).name().equals(toString)) {
						defaultValue = e;
						break;
					}
				}
			}
		}
		
		return (T) defaultValue;
	}
	
	public <T>Enum<?> getEnumByToStringIngoreCase(Class<? extends Enum<?>> clase, String valor) {
		Enum<?> retorno = null;
		
		if(clase != null && valor != null) {
			List<Enum<?>> enums = getArrayList(clase);
			if(enums != null) {
				for(Enum<?> e : enums) {
					if(e.toString().equalsIgnoreCase(valor)) {
						retorno = e;
						break;
					}
				}
			}
		}
		
		return retorno;
	}
	
}
