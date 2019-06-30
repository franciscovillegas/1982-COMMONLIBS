package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "id_comuna", isForeignKey = true, numerica = true) }, tableName = "eje_geo_comuna")
public class Eje_geo_comuna extends Vo {
	private int id_comuna;
	private int id_ciudad;
	private String nombre;
	public int getId_comuna() {
		return id_comuna;
	}
	public void setId_comuna(int id_comuna) {
		this.id_comuna = id_comuna;
	}
	public int getId_ciudad() {
		return id_ciudad;
	}
	public void setId_ciudad(int id_ciudad) {
		this.id_ciudad = id_ciudad;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	
}
