package portal.com.eje.cap.menuadmin.optionsgroup;

import portal.com.eje.portal.administradorportal.IButtonGroup;

public enum EnumCapGroupAdminMenu implements IButtonGroup {
	PLANIFICACION("Planificación", null),
	CURSOS_Y_ACCIONES("Cursos y acciones", null),
	RELATORES("Cursos y acciones", null),
	PRESUPUESTO("Cursos y acciones", null),
	INFORMES("Cursos y acciones", null),
	TABLAS_PARAMETROS("Cursos y acciones", null),
	FORMULARIOS("Cursos y acciones", null);
	
	private String nombre;
	private String icon;
	
	private EnumCapGroupAdminMenu(String nombre, String icon) {
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
