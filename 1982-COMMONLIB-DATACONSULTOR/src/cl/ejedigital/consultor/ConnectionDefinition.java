package cl.ejedigital.consultor;

public class ConnectionDefinition {
	private String nombre;
	private String ip;
	private int puerto;
	private String bd;
	private String usuario;
	private String password;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPuerto() {
		return puerto;
	}
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	public String getBd() {
		return bd;
	}
	public void setBd(String bd) {
		this.bd = bd;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String toString() {
		StringBuilder sql = new StringBuilder();
		sql.append(nombre).append("_");
		sql.append(ip).append("_");
		sql.append(puerto).append("_");
		sql.append(bd).append("_");
		sql.append("usuario").append("_");
		sql.append(password).append("_");
		
		return sql.toString();
		
	}
}
