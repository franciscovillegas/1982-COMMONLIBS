package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_movimiento", isForeignKey = false, numerica = true) }, tableName = "eje_ges_unidades_log")
public class Eje_ges_unidades_log extends Vo {
	private int id_movimiento;
	private String glosa;
	private Date fecha;
	private int rut_responsable;
	private String unid_id;
	private Boolean vigente;
	private String nombre;
	private String unid_id_parent;
	
	public int getId_movimiento() {
		return id_movimiento;
	}
	public void setId_movimiento(int id_movimiento) {
		this.id_movimiento = id_movimiento;
	}
	public String getGlosa() {
		return glosa;
	}
	public void setGlosa(String glosa) {
		this.glosa = glosa;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public int getRut_responsable() {
		return rut_responsable;
	}
	public void setRut_responsable(int rut_responsable) {
		this.rut_responsable = rut_responsable;
	}
	public String getUnid_id() {
		return unid_id;
	}
	public void setUnid_id(String unid_id) {
		this.unid_id = unid_id;
	}
	public Boolean getVigente() {
		return vigente;
	}
	public void setVigente(Boolean vigente) {
		this.vigente = vigente;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUnid_id_parent() {
		return unid_id_parent;
	}
	public void setUnid_id_parent(String unid_id_parent) {
		this.unid_id_parent = unid_id_parent;
	}
 
	

}
