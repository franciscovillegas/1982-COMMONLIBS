package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", tableName = "eje_wfgen_estadocivil", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_estadocivil", numerica = true, isForeignKey = false) })
public class Eje_wfgen_estadocivil extends Vo {
	private int	id_estadocivil;
	private String nombre;
	private int vigente;
	
	public int getId_estadocivil() {
		return id_estadocivil;
	}
	public void setId_estadocivil(int id_estadocivil) {
		this.id_estadocivil = id_estadocivil;
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
