package cl.eje.model.generic.mac;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_registro_original", isForeignKey = false, numerica = true) }, tableName = "eje_wf_requerimientos_registro_original")
public class Eje_wf_requerimientos_registro_original extends Vo {
	private int	id_registro_original;
	private int	id_escalamiento;
	private int	id_req;
	private int	id_rol;
	private int	rut_rol;
	private Date fecha_update;
	
	public int getId_registro_original() {
		return id_registro_original;
	}
	public void setId_registro_original(int id_registro_original) {
		this.id_registro_original = id_registro_original;
	}
	public int getId_escalamiento() {
		return id_escalamiento;
	}
	public void setId_escalamiento(int id_escalamiento) {
		this.id_escalamiento = id_escalamiento;
	}
	public int getId_req() {
		return id_req;
	}
	public void setId_req(int id_req) {
		this.id_req = id_req;
	}
	public int getId_rol() {
		return id_rol;
	}
	public void setId_rol(int id_rol) {
		this.id_rol = id_rol;
	}
	public int getRut_rol() {
		return rut_rol;
	}
	public void setRut_rol(int rut_rol) {
		this.rut_rol = rut_rol;
	}
	public Date getFecha_update() {
		return fecha_update;
	}
	public void setFecha_update(Date fecha_update) {
		this.fecha_update = fecha_update;
	}
	
	
	
}
