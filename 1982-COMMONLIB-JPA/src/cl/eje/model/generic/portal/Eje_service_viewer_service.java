package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_servicio", isForeignKey = false, numerica = true) }, tableName = "eje_service_viewer_service")
public class Eje_service_viewer_service extends Vo {
	private int id_servicio;
	private String nombre;
	private String contexto;
	private String url;
	private int intervalo_segundos;
	private Boolean activa;
	private int id_contenedor;

	public int getId_servicio() {
		return id_servicio;
	}

	public void setId_servicio(int id_servicio) {
		this.id_servicio = id_servicio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getContexto() {
		return contexto;
	}

	public void setContexto(String contexto) {
		this.contexto = contexto;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIntervalo_segundos() {
		return intervalo_segundos;
	}

	public void setIntervalo_segundos(int intervalo_segundos) {
		this.intervalo_segundos = intervalo_segundos;
	}

	public Boolean getActiva() {
		return activa;
	}

	public void setActiva(Boolean activa) {
		this.activa = activa;
	}

	public int getId_contenedor() {
		return id_contenedor;
	}

	public void setId_contenedor(int id_contenedor) {
		this.id_contenedor = id_contenedor;
	}

}
