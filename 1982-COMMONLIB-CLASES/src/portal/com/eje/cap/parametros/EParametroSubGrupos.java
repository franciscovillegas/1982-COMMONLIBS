package portal.com.eje.cap.parametros;

public enum EParametroSubGrupos {
	Modificables(1),
	Sistema(2);
	
	int identificador;
	
	EParametroSubGrupos(int i) {
		this.identificador = i;
	}

	public int getId() {
		return this.identificador;
	}
}
