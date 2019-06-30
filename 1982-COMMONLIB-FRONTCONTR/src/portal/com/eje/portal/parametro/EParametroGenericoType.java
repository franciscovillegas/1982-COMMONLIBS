package portal.com.eje.portal.parametro;

public enum EParametroGenericoType {
	String(1), ListString(2), Image(3);
	
	private int id;
	EParametroGenericoType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
