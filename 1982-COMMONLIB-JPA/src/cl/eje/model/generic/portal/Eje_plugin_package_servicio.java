package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_paquete", isForeignKey = false, numerica = true) }, tableName = "Eje_plugin_package_servicio")
public class Eje_plugin_package_servicio extends Vo {
	private int	id_paquete;
	private String paquete;
	public int getId_paquete() {
		return id_paquete;
	}
	public void setId_paquete(int id_paquete) {
		this.id_paquete = id_paquete;
	}
	public String getPaquete() {
		return paquete;
	}
	public void setPaquete(String paquete) {
		this.paquete = paquete;
	}
	
	
	
}
