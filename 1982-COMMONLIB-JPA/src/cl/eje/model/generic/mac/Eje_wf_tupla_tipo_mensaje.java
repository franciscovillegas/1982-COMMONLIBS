package cl.eje.model.generic.mac;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_corr_tipo_mensaje", isForeignKey = false, numerica = true) }, tableName = "eje_wf_tupla_tipo_mensaje")
public class Eje_wf_tupla_tipo_mensaje extends Vo {
	private int	id_corr_tipo_mensaje;
	private int	id_tipo_mensaje;
	private int	id_rol;
	private Date fecha_update;
	private boolean vigente;
	
	
	public boolean isVigente() {
		return vigente;
	}
	public void setVigente(boolean vigente) {
		this.vigente = vigente;
	}
	public int getId_corr_tipo_mensaje() {
		return id_corr_tipo_mensaje;
	}
	public void setId_corr_tipo_mensaje(int id_corr_tipo_mensaje) {
		this.id_corr_tipo_mensaje = id_corr_tipo_mensaje;
	}
	public int getId_tipo_mensaje() {
		return id_tipo_mensaje;
	}
	public void setId_tipo_mensaje(int id_tipo_mensaje) {
		this.id_tipo_mensaje = id_tipo_mensaje;
	}
	public int getId_rol() {
		return id_rol;
	}
	public void setId_rol(int id_rol) {
		this.id_rol = id_rol;
	}
	public Date getFecha_update() {
		return fecha_update;
	}
	public void setFecha_update(Date fecha_update) {
		this.fecha_update = fecha_update;
	}
	
	

}
