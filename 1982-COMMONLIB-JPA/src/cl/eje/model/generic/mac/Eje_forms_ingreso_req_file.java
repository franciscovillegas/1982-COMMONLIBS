package cl.eje.model.generic.mac;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { 
		@PrimaryKeyDefinition(autoIncremental = true, field = "id_corr_file", isForeignKey = false, numerica = true) 
		}, tableName = "eje_forms_ingreso_req_file")
public class Eje_forms_ingreso_req_file extends Vo {
	private int id_corr_file;
	private int id_corr;
	private int id_file;
	private int rut_update;
	private Date fecha_update;
	private int id_corr_comentario;
	
	public int getId_corr_file() {
		return id_corr_file;
	}
	public void setId_corr_file(int id_corr_file) {
		this.id_corr_file = id_corr_file;
	}
	public int getId_corr() {
		return id_corr;
	}
	public void setId_corr(int id_corr) {
		this.id_corr = id_corr;
	}
	public int getId_file() {
		return id_file;
	}
	public void setId_file(int id_file) {
		this.id_file = id_file;
	}
	public int getRut_update() {
		return rut_update;
	}
	public void setRut_update(int rut_update) {
		this.rut_update = rut_update;
	}
	public Date getFecha_update() {
		return fecha_update;
	}
	public void setFecha_update(Date fecha_update) {
		this.fecha_update = fecha_update;
	}
	public int getId_corr_comentario() {
		return id_corr_comentario;
	}
	public void setId_corr_comentario(int id_corr_comentario) {
		this.id_corr_comentario = id_corr_comentario;
	}
	
	
	
	
	
}
