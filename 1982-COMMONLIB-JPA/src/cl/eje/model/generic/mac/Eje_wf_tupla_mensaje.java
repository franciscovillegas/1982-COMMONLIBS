package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_mensaje", isForeignKey = false, numerica = true) }, tableName = "eje_wf_tupla_mensaje")
public class Eje_wf_tupla_mensaje extends Vo {
	private int id_mensaje;
	private int id_tipo_mensaje;
	private int id_rol;
	private String mensaje;
	private boolean vigente;
	private boolean por_defecto;
	private int orden;
	
	public int getId_mensaje() {
		return id_mensaje;
	}
	public void setId_mensaje(int id_mensaje) {
		this.id_mensaje = id_mensaje;
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
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public boolean isVigente() {
		return vigente;
	}
	public void setVigente(boolean vigente) {
		this.vigente = vigente;
	}
	public boolean isPor_defecto() {
		return por_defecto;
	}
	public void setPor_defecto(boolean por_defecto) {
		this.por_defecto = por_defecto;
	}
	public int getOrden() {
		return orden;
	}
	public void setOrden(int orden) {
		this.orden = orden;
	}
	
	
}
