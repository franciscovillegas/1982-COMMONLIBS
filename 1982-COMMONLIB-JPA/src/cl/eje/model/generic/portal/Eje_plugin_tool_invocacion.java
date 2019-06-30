package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_invocacion", isForeignKey = false, numerica = true) }, tableName = "eje_plugin_tool_invocacion")
public class Eje_plugin_tool_invocacion extends Vo {
	private int	id_invocacion;
	private int	id_plugin;
	private int	id_metodo;
	private int	id_tool;
	private int	contador;
	public int getId_invocacion() {
		return id_invocacion;
	}
	public void setId_invocacion(int id_invocacion) {
		this.id_invocacion = id_invocacion;
	}
	public int getId_plugin() {
		return id_plugin;
	}
	public void setId_plugin(int id_plugin) {
		this.id_plugin = id_plugin;
	}
	public int getId_metodo() {
		return id_metodo;
	}
	public void setId_metodo(int id_metodo) {
		this.id_metodo = id_metodo;
	}
	public int getId_tool() {
		return id_tool;
	}
	public void setId_tool(int id_tool) {
		this.id_tool = id_tool;
	}
	public int getContador() {
		return contador;
	}
	public void setContador(int contador) {
		this.contador = contador;
	}
	
	
	
}
