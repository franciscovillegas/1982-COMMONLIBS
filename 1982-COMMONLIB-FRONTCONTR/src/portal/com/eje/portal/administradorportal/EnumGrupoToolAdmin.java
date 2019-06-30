package portal.com.eje.portal.administradorportal;

public enum EnumGrupoToolAdmin implements IButtonGroup {
	REMUNERACIONES("Remuneraciones", "view/images/btns/participation_rate.png");
	
	private String nombre;
	private String icon;

	EnumGrupoToolAdmin(String nombre, String icon) {
		this.nombre = nombre;
		this.icon = icon;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String getNombre() {
		return this.nombre;
	}

	@Override
	public String getIcon() {
		return this.icon;
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
