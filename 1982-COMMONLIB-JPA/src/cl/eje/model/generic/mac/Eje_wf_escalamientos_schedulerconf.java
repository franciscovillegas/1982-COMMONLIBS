package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_escalamientos_configurationwf", numerica = true, isForeignKey = false) }, tableName = "eje_wf_escalamientos_schedulerconf")
public class Eje_wf_escalamientos_schedulerconf extends Vo {
	private int id_escalamientos_configurationwf;
	private int id_modulo;
	private String modulo;
	private boolean	activado;
	
	public int getId_escalamientos_configurationwf() {
		return id_escalamientos_configurationwf;
	}
	public void setId_escalamientos_configurationwf(int id_escalamientos_configurationwf) {
		this.id_escalamientos_configurationwf = id_escalamientos_configurationwf;
	}
	public int getId_modulo() {
		return id_modulo;
	}
	public void setId_modulo(int id_modulo) {
		this.id_modulo = id_modulo;
	}
	public String getModulo() {
		return modulo;
	}
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	public boolean isActivado() {
		return activado;
	}
	public void setActivado(boolean activado) {
		this.activado = activado;
	}
	
	
}
