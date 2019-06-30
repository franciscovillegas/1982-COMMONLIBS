package portal.com.eje.portal.organica;

public enum EstadoJefaUnidad {
	vigente(1);
	
	int value;
	EstadoJefaUnidad(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
}
