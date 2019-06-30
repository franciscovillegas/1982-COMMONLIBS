package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_persona_excepcion", numerica = true, isForeignKey = false) }, tableName = "eje_wf_escalamientos_persona_excepcion")

public class Eje_wf_escalamientos_persona_excepcion extends Vo {

	private int 	id_persona_excepcion;
	private int 	id_escalamiento_impl;
	private int 	rut;
	private String nombre;
	
	public int getId_persona_excepcion() {
		return id_persona_excepcion;
	}
	public void setId_persona_excepcion(int id_persona_excepcion) {
		this.id_persona_excepcion = id_persona_excepcion;
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
	
	
}
