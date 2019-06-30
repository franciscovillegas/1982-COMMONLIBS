package portal.com.eje.cap.parametros;

public enum EParametroGrupos {
	Areas(6),
	Tematicas(7),
	Competencias(8),
	AreaCurso(11),
	Calculos(12),
	Costos(13),
	Sence(14),
	Vigencia(16),
	Estado(17),
	EstadoPlanificacion(18),
	Acciones(19),
	Cursos(9),
	Origenes(10),
	Regiones(20),
	Comuna(21),
	Ciudad(22),
	TipoJor(23),
	TipoAlcance(24),
	EstadoAcciones(25),
	Meses(26),
	ConfiguracionEnvioCorreos(27),
	Postulaciones(28);
	
	int identificador;
	
	EParametroGrupos(int i) {
		this.identificador = i;
	}

	public int getId() {
		return this.identificador;
	}
}
