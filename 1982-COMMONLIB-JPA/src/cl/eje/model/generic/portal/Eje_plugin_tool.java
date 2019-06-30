package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_tool", isForeignKey = false, numerica = true) }, tableName = "eje_plugin_tool")
public class Eje_plugin_tool extends Vo {
	private int id_tool;
	private int id_paquete;
	private String nombre;

	public int getId_tool() {
		return id_tool;
	}

	public void setId_tool(int id_tool) {
		this.id_tool = id_tool;
	}

	public int getId_paquete() {
		return id_paquete;
	}

	public void setId_paquete(int id_paquete) {
		this.id_paquete = id_paquete;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
