package portal.com.eje.portal.mis.mng.enums;

import java.lang.reflect.Field;

public enum EnumTipoConexion {
	PPMWService(1, "PPM-WService"),
	MSSQLServer(2, "MS-SQL Server");

	private int intId;
	private String strNombre;
	EnumTipoConexion(int intId, String strNombre) {
		this.intId = intId;
		this.strNombre = strNombre;
	}
	public int getId() {
		return intId;
	}
	public String getNombre() {
		return strNombre;
	}
	
	public static EnumTipoConexion fromInteger(int id) {
		Field[] fields = EnumTipoConexion.class.getDeclaredFields();
		for(Field f : fields) {
			if( !f.getType().isAssignableFrom(EnumTipoConexion.class) ) {
				continue;
			}
			
			EnumTipoConexion enm = Enum.valueOf(EnumTipoConexion.class, f.getName());
			if(enm.getId() == id) {
				return enm;
			}
			
		}
		
		return null;
	}
	
	
}
