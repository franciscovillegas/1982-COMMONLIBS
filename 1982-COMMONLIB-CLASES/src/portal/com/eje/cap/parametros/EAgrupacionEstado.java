package portal.com.eje.cap.parametros;

public enum EAgrupacionEstado {
	Agrupado(0),
	EnPlanificacion(5),
	EnEjecucion(10);
	
	private int id;
	private EAgrupacionEstado(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
}
