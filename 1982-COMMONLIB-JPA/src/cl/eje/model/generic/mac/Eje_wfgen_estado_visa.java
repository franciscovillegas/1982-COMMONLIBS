package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_estado_visa", numerica = true, isForeignKey = false) }, tableName = "eje_wfgen_estado_visa")
public class Eje_wfgen_estado_visa extends Vo {
	private int	id_estado_visa;
	private String nombre;
	private boolean	vigente;
	
	public int getId_estado_visa() {
		return id_estado_visa;
	}
	public void setId_estado_visa(int id_estado_visa) {
		this.id_estado_visa = id_estado_visa;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean isVigente() {
		return vigente;
	}
	public void setVigente(boolean vigente) {
		this.vigente = vigente;
	}

	
}
