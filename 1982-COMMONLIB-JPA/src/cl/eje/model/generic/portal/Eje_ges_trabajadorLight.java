package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "rut", numerica = true, isForeignKey = false) }, tableName = "eje_ges_trabajador")
public class Eje_ges_trabajadorLight extends Vo {
	private int rut;
	private String digito_ver;
	private String nombre;
	
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}
	public String getDigito_ver() {
		return digito_ver;
	}
	public void setDigito_ver(String digito_ver) {
		this.digito_ver = digito_ver;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	 
	
}
