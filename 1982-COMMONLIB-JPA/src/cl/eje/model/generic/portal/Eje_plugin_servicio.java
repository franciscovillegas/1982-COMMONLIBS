package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_servicio", isForeignKey = false, numerica = true) }, tableName = "eje_plugin_servicio")
public class Eje_plugin_servicio extends Vo {
	private int id_servicio;
	private int id_paquete;
	private String nombre;
	
	
	public int getId_servicio() {
		return id_servicio;
	}
	public void setId_servicio(int id_servicio) {
		this.id_servicio = id_servicio;
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
