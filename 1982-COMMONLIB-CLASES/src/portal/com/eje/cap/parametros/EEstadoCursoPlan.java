package portal.com.eje.cap.parametros;

/**
 * Se debe usar #EEstadoAcciones
 * @deprecated
 * */
public enum EEstadoCursoPlan  {
	 PENDIENTE("Pendiente");
	
	private String estadoNombre;

	private EEstadoCursoPlan(String estadoNombre) {
		this.estadoNombre = estadoNombre;
	}

	public String getEstadoNombre() {
		return estadoNombre;
	}
	
	
}
