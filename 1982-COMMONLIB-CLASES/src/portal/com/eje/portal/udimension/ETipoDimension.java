package portal.com.eje.portal.udimension;

import java.lang.reflect.Field;

public enum ETipoDimension {
	texto(1),
	numero(2), 
	porcentaje(3),
	seleccionsimple(4),
	seleccionmultiple(5);
	
	private int id;
	
	ETipoDimension(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public static ETipoDimension getEstado(int intIdEstado) {
		if(intIdEstado != 0) {
			Field[] fields = ETipoDimension.class.getDeclaredFields();
			for(Field f : fields) {
				ETipoDimension e = Enum.valueOf(ETipoDimension.class, f.getName());
				if(intIdEstado== e.getId()) {
					return e; 
				}
			}	
		}
		
		throw new RuntimeException("No fue posible determinar el estado ["+ETipoDimension.class.getName()+"]");
	}
	
}
