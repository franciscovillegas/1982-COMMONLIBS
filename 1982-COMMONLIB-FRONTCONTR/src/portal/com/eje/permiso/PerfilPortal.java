package portal.com.eje.permiso;


public enum PerfilPortal {
	
	JefeUnidad (-1),
	Root  (0);

	private int id;
	
	PerfilPortal(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public String toString() {
		return String.valueOf(id);
	}
}
