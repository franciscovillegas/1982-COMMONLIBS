package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", tableName = "eje_wfgen_comuna", pks = {@PrimaryKeyDefinition(autoIncremental = true, field = "id_comuna", numerica = true, isForeignKey = false)})

public class Eje_wfgen_comuna extends Vo {
	private int	id_comuna;
	private int	id_ciudad;
	private String nombre;
	private int	vigente;
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
	public int getVigente() {
		return vigente;
	}
	public void setVigente(int vigente) {
		this.vigente = vigente;
	}
	
	
}
