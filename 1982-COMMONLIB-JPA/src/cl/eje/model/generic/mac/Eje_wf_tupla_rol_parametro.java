package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { 
		@PrimaryKeyDefinition(autoIncremental = true, field = "id_rol_param", isForeignKey = false, numerica = true) },
		tableName = "eje_wf_tupla_rol_parametro")
public class Eje_wf_tupla_rol_parametro extends Vo {
	private int id_rol_param;
	private int id_rol;
	private String param;
	private String value;
	
	public int getId_rol_param() {
		return id_rol_param;
	}
	public void setId_rol_param(int id_rol_param) {
		this.id_rol_param = id_rol_param;
	}
	public int getId_rol() {
		return id_rol;
	}
	public void setId_rol(int id_rol) {
		this.id_rol = id_rol;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
		
	
}
