package portal.com.eje.daemon;

public class DaemonDefinition {
	private String nombre;
	private String cronExpresion;

	private int idGrupo;
	private int idDaemon;
	private boolean activado;
	
	private String icono;
	private int orden;
	
	private String clase;
	private String llamadaUrl;
	private String dos;

	public DaemonDefinition(Integer idDaemon, String nombre, String cronExpresion) {
		this(nombre, cronExpresion);

		this.idDaemon = idDaemon;
	}

	public DaemonDefinition(String nombre, String cronExpresion) {
		super();
		this.nombre = nombre;
		this.cronExpresion = cronExpresion;
	}

	public String getNombre() {
		return nombre;
	}

	public String getCronExpresion() {
		return cronExpresion;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public String getLlamadaUrl() {
		return llamadaUrl;
	}

	public void setLlamadaUrl(String llamadaUrl) {
		this.llamadaUrl = llamadaUrl;
	}

	public String getDos() {
		return dos;
	}

	public void setDos(String dos) {
		this.dos = dos;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setCronExpresion(String cronExpresion) {
		this.cronExpresion = cronExpresion;
	}

	public Integer getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(Integer idGrupo) {
		this.idGrupo = idGrupo;
	}

	public boolean isActivado() {
		return activado;
	}

	public void setActivado(boolean activado) {
		this.activado = activado;
	}

	public int getIdDaemon() {
		return idDaemon;
	}

	public void setIdDaemon(int idDaemon) {
		this.idDaemon = idDaemon;
	}

	public String getIcono() {
		return icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	
	
}
