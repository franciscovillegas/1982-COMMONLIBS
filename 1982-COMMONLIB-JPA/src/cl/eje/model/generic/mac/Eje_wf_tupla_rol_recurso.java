package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { 
			@PrimaryKeyDefinition(autoIncremental = false, field = "id_rol", isForeignKey = true, numerica = true),
			@PrimaryKeyDefinition(autoIncremental = false, field = "cod_recurso", isForeignKey = true, numerica = true)
		}, tableName = "eje_wf_tupla_rol_recurso")

public class Eje_wf_tupla_rol_recurso extends Vo {
	private int	id_rol;
	private String cod_recurso;
	private String params;
	
	public int getId_rol() {
		return id_rol;
	}
	public void setId_rol(int id_rol) {
		this.id_rol = id_rol;
	}
	public String getCod_recurso() {
		return cod_recurso;
	}
	public void setCod_recurso(String cod_recurso) {
		this.cod_recurso = cod_recurso;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	
	
}
