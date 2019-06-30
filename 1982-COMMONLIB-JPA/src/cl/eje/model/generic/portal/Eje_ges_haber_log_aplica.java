package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_corr_aplica", isForeignKey = false, numerica = true) }, tableName = "eje_ges_haber_log_aplica")
public class Eje_ges_haber_log_aplica extends Vo {
	private int id_corr_aplica;
	private int periodo;
	private int rut_usuario;
	private Date fecha_aplica;
	private int origen_aplica;
	private String accion;
	
	public int getId_corr_aplica() {
		return id_corr_aplica;
	}

	public void setId_corr_aplica(int id_corr_aplica) {
		this.id_corr_aplica = id_corr_aplica;
	}

	public int getPeriodo() {
		return periodo;
	}

	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}

	public int getRut_usuario() {
		return rut_usuario;
	}

	public void setRut_usuario(int rut_usuario) {
		this.rut_usuario = rut_usuario;
	}

	public Date getFecha_aplica() {
		return fecha_aplica;
	}

	public void setFecha_aplica(Date fecha_aplica) {
		this.fecha_aplica = fecha_aplica;
	}

	public int getOrigen_aplica() {
		return origen_aplica;
	}

	public void setOrigen_aplica(int origen_aplica) {
		this.origen_aplica = origen_aplica;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	
	
}
