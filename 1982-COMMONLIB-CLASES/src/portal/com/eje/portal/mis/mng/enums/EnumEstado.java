package portal.com.eje.portal.mis.mng.enums;

import java.lang.reflect.Field;

public enum EnumEstado {
	
	Ninguno(0, ""),
	Pendiente(1, "Pendiente"),
	Iniciado(2, "Iniciado"),
	Error(6, "Error"),
	Finalizado(7, "Finalizado");
	
	private int intId;
	private String strNombre;
	private String strIcono;
	
	EnumEstado(int intId, String strNombre) {
		this.intId = intId;
		this.strNombre = strNombre;
	}
	
	public int getId() {
		return intId;
	}
	public String getNombre() {
		return strNombre;
	}
	
	public String getIcono() {
		return strIcono;
	}

	public void setNombre(String strNombre) {
		this.strNombre = strNombre;
	}

	public void setIcono(String strIcono) {
		this.strIcono = strIcono;
	}
	
	public static EnumEstado fromInteger(int id) {
		Field[] fields = EnumEstado.class.getDeclaredFields();
		for(Field f : fields) {
			if( !f.getType().isAssignableFrom(EnumEstado.class) ) {
				continue;
			}
			
			EnumEstado enm = Enum.valueOf(EnumEstado.class, f.getName());
			if(enm.getId() == id) {
				return enm;
			}
			
		}
		
		throw new RuntimeException("No fue posible determinar el estado ["+EnumEstado.class.getName()+"]");
	}
	
	
}
