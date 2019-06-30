package portal.com.eje.cap.parametros;

public enum EEstadoWorkflow {
	INVITADO("Invitado"),
	POSTULANDO("Pendiente aprobación Jefatura"),
	APROBADO("Aprobado por la Jefatura"),
	RECHAZADO("Rechazado por la Jefatura"),
	INSCRITO("Inscrito en el Curso"),
	DESVINCULADO("Desvinculado del curso");
	
	private String desc;

	private EEstadoWorkflow(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
	
	
}
