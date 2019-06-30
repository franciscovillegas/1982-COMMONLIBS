package portal.com.eje.cap.parametros;

public enum EEstadoPlanificacion {
	Pendiente(65),
	Organizada(66),
	Configurada(67),
	AsignadaAAjecutar(68),
	Enejecucion(69),
	Ejecutada(70),
	NoAplica(71);
	
	private int id;
	
	EEstadoPlanificacion(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
}

