package portal.com.eje.portal.parametro;

public enum ParametroType {
	tipo_String(1),
	tipo_Imagen(2);
	private int id;
	
	private ParametroType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
}
