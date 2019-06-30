package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_escalamiento_persona", numerica = true, isForeignKey = false) }, tableName = "eje_wf_escalamientos_personas")

public class Eje_wf_escalamientos_personas extends Vo {
	private int id_escalamiento_persona;
	private int id_escalamiento_impl;
	private int rut;
	private String nombre;
	private int horas;
	private boolean vigente;
	
	
	public int getId_escalamiento_persona() {
		return id_escalamiento_persona;
	}
	public void setId_escalamiento_persona(int id_escalamiento_persona) {
		this.id_escalamiento_persona = id_escalamiento_persona;
	}
	public int getId_escalamiento_impl() {
		return id_escalamiento_impl;
	}
	public void setId_escalamiento_impl(int id_escalamiento_impl) {
		this.id_escalamiento_impl = id_escalamiento_impl;
	}
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getHoras() {
		return horas;
	}
	public void setHoras(int horas) {
		this.horas = horas;
	}
	public boolean isVigente() {
		return vigente;
	}
	public void setVigente(boolean vigente) {
		this.vigente = vigente;
	}
	
	
	
}
