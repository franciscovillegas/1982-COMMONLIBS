package portal.com.eje.cap.parametros;

public enum ECapOrigen {
	DESEMPENO("Desempeño"),
	MALLA("Malla"),
	SEC("Sec"),
	EPT("Ept"),
	HISTORIA("Historia");
	
	private String nombre;

	private ECapOrigen(String nombre) {
		this.nombre = nombre;
	}
	
	@Override
	public String toString() {
		return this.nombre;
	}
}
