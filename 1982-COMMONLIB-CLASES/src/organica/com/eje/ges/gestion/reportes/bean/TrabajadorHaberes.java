package organica.com.eje.ges.gestion.reportes.bean;

public class TrabajadorHaberes {
	
	private String rut;
	private String dv;
	private String nombre;
	private String glosa;
	private String tipoHaber;
	private String cantidad;
	private String sumaHaber;
	
	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public String getDv() {
		return dv;
	}

	public void setDv(String dv) {
		this.dv = dv;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getGlosa() {
		return glosa;
	}
		
	public void setGlosa(String glosa) {
		this.glosa = glosa;
	}
		
	public String getTipoHaber() {
		return tipoHaber;
	}
		
	public void setTipoHaber(String tipoHaber) {
		this.tipoHaber = tipoHaber;
	}
		
	public String getCantidad() {
		return cantidad;
	}
		
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
		
	public String getSumaHaber() {
		return sumaHaber;
	}
	
	public void setSumaHaber(String sumaHaber) {
		this.sumaHaber = sumaHaber;
	}
}