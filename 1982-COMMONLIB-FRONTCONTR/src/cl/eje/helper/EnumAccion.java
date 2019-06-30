package cl.eje.helper;

import java.lang.reflect.Field;

public enum EnumAccion {
	get,
	add,
	del,
	upd;
	
	public static EnumAccion fromString(String nombre) {
		Field[] fields = EnumAccion.class.getDeclaredFields();
		for(Field f : fields) {
			if(f.getClass().isAssignableFrom(EnumAccion.class)) {
				EnumAccion e = Enum.valueOf(EnumAccion.class, f.getName());
				if(e.toString().equals(nombre)) {
					return e;	
				}
			}
		}
		
		return null;
	}
}
