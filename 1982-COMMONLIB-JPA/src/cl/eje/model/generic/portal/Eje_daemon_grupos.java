package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = {
		@PrimaryKeyDefinition(autoIncremental = true, field = "id_grupo", isForeignKey = false, numerica = true) }, tableName = "eje_daemon_grupos")
public class Eje_daemon_grupos extends Vo {
	private int id_grupo;
	private String nombre;
	private Date ult_fecha_ejecucion;
	private int cant_errores;
	private Date fecha_creacion;
	private Date fecha_update;
	private String icono;
	private int orden;

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

	public Date getUlt_fecha_ejecucion() {
		return ult_fecha_ejecucion;
	}

	public void setUlt_fecha_ejecucion(Date ult_fecha_ejecucion) {
		this.ult_fecha_ejecucion = ult_fecha_ejecucion;
	}

	public int getCant_errores() {
		return cant_errores;
	}

	public void setCant_errores(int cant_errores) {
		this.cant_errores = cant_errores;
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

}
