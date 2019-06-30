package portal.com.eje.genericbuttons.enums;

import portal.com.eje.portal.administradorportal.IButtonGroup;

public enum EnumGenericButtonGroup implements IButtonGroup {
	BASEDEDATOS("Base de datos", "database.ico","Todo lo referente a la manipulación de la Base de Datos"),
	AYUDA("Scripts", "sql.ico" , "Sobre el módulo");
	
	private String nombre;
	private String icon;
	private String tooltip;
	
	private EnumGenericButtonGroup(String nombre, String icon, String tooltip) {
		this.nombre = nombre;
		this.icon = icon;
		this.tooltip = tooltip;
	}
	public String getNombre() {
		return nombre;
	}
	public String getIcon() {
		return icon;
	}
	public String getTooltip() {
		return tooltip;
	}
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	@Override
	public String getCanonicalName() {
		return getClass().getCanonicalName();
	}
	
	
	
}
