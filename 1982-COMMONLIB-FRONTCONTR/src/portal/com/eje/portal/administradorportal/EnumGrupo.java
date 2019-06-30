package portal.com.eje.portal.administradorportal;

public enum EnumGrupo {
	;
	private String nombre;
	private String icon;
		
	private EnumGrupo(String nombre, String icon) {
		this.nombre = nombre;
		this.icon = icon;
	}

	public String getNombre() {
		return nombre;
	}

	public String getIcon() {
		return icon;
	}
}
