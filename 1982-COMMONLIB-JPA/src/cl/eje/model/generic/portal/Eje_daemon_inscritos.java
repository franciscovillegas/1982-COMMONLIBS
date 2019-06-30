package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = {
		@PrimaryKeyDefinition(autoIncremental = true, field = "id_daemon", isForeignKey = false, numerica = true) }, tableName = "eje_daemon_inscritos")
public class Eje_daemon_inscritos extends Vo {
	private int id_daemon;
	private int id_grupo;
	private String nombre;
	private Boolean activado;
	private Date ult_fecha_ejecucion;
	private int ult_tiempo_ejecucion;
	private String peridiosidad;
	private Date fecha_creacion;
	private Date fecha_update;
	private String icono;
	private int orden;
	private String ejecu_clase;
	private String ejecu_llamada_url;
	private String ejecu_dos;

	public int getId_daemon() {
		return id_daemon;
	}

	public void setId_daemon(int id_daemon) {
		this.id_daemon = id_daemon;
	}

	public int getId_grupo() {
		return id_grupo;
	}

	public void setId_grupo(int id_grupo) {
		this.id_grupo = id_grupo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Boolean getActivado() {
		return activado;
	}

	public void setActivado(Boolean activado) {
		this.activado = activado;
	}

	public Date getUlt_fecha_ejecucion() {
		return ult_fecha_ejecucion;
	}

	public void setUlt_fecha_ejecucion(Date ult_fecha_ejecucion) {
		this.ult_fecha_ejecucion = ult_fecha_ejecucion;
	}

	public int getUlt_tiempo_ejecucion() {
		return ult_tiempo_ejecucion;
	}

	public void setUlt_tiempo_ejecucion(int ult_tiempo_ejecucion) {
		this.ult_tiempo_ejecucion = ult_tiempo_ejecucion;
	}

	public String getPeridiosidad() {
		return peridiosidad;
	}

	public void setPeridiosidad(String peridiosidad) {
		this.peridiosidad = peridiosidad;
	}

	public Date getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(Date fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	public Date getFecha_update() {
		return fecha_update;
	}

	public void setFecha_update(Date fecha_update) {
		this.fecha_update = fecha_update;
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

	public String getEjecu_clase() {
		return ejecu_clase;
	}

	public void setEjecu_clase(String ejecu_clase) {
		this.ejecu_clase = ejecu_clase;
	}

	public String getEjecu_llamada_url() {
		return ejecu_llamada_url;
	}

	public void setEjecu_llamada_url(String ejecu_llamada_url) {
		this.ejecu_llamada_url = ejecu_llamada_url;
	}

	public String getEjecu_dos() {
		return ejecu_dos;
	}

	public void setEjecu_dos(String ejecu_dos) {
		this.ejecu_dos = ejecu_dos;
	}

}
