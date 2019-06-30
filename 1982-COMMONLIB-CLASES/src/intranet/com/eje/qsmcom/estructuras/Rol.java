package intranet.com.eje.qsmcom.estructuras;

public class Rol {
	private int idRol;
	private String rolNombre;
	private String rolDesc;
	
	
	public Rol(int idRol, String rolNombre , String rolDesc ) {
		super(); 
		this.idRol = idRol;
		this.rolDesc = rolDesc;
		this.rolNombre = rolNombre;
	}


	public int getIdRol() {
		return idRol;
	}


	public String getRolDesc() {
		return rolDesc;
	}


	public String getRolNombre() {
		return rolNombre;
	}
	
	
	
	
}
