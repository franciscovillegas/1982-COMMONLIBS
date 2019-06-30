package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_metodo", isForeignKey = false, numerica = true) }, tableName = "eje_plugin_servicio_metodo")
public class Eje_plugin_servicio_metodo extends Vo {
	private int id_metodo;
	private String nombre;
	private boolean is_transaccional;
	
	
	public int getId_metodo() {
		return id_metodo;
	}
	public void setId_metodo(int id_metodo) {
		this.id_metodo = id_metodo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean isIs_transaccional() {
		return is_transaccional;
	}
	public void setIs_transaccional(boolean is_transaccional) {
		this.is_transaccional = is_transaccional;
	}
	
	
	
	
}
