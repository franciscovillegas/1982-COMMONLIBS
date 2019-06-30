package portal.com.eje.genericbuttons.enums;

import portal.com.eje.portal.administradorportal.IButtonGroup;

public enum EnumGenericButtonSubGroup implements IButtonGroup {
	GRUPO1("GRUPO1", null),
	GRUPO2("GRUPO2", null),
	GRUPO3("GRUPO3", null),
	GRUPO4("GRUPO4", null);
	
	private String nombre;
	private String icon;
	private EnumGenericButtonSubGroup(String nombre, String icon) {
		this.nombre = nombre;
		this.icon = icon;
	}
	public String getNombre() {
		return nombre;
	}
	public String getIcon() {
		return icon;
	}
	@Override
	public String getTooltip() {
		// TODO Auto-generated method stub
		return null;
	}
 
	 
	@Override
	public String getCanonicalName() {
		return getClass().getCanonicalName();
	}


}
