package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "id_ciudad", isForeignKey = true, numerica = true) }, tableName = "eje_geo_ciudad")
public class Eje_geo_ciudad extends Vo {
	private int id_ciudad;
	private int id_region;
	private String nombre;
	private int cod_telefono;
	public int getId_ciudad() {
		return id_ciudad;
	}
	public void setId_ciudad(int id_ciudad) {
		this.id_ciudad = id_ciudad;
	}
	public int getId_region() {
		return id_region;
	}
	public void setId_region(int id_region) {
		this.id_region = id_region;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getCod_telefono() {
		return cod_telefono;
	}
	public void setCod_telefono(int cod_telefono) {
		this.cod_telefono = cod_telefono;
	}
	
	
}
