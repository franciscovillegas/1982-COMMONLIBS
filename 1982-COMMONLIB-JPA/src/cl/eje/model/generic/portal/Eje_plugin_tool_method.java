package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_metodo", isForeignKey = false, numerica = true) }, tableName = "Eje_plugin_tool_method")
public class Eje_plugin_tool_method extends Vo {
	private int	id_metodo;
	private String nombre;
    private String def;
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
	public String getDef() {
		return def;
	}
	public void setDef(String def) {
		this.def = def;
	}
    
    
    
}
