package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_esclamiento_clase", numerica = true, isForeignKey = false) }, tableName = "eje_wf_escalamientos_clases")
public class Eje_wf_escalamientos_clases extends Vo {
	private int	id_esclamiento_clase;
	private String nombre;
	private String clase;
	private boolean vigente;
	
	
	public int getId_esclamiento_clase() {
		return id_esclamiento_clase;
	}
	public void setId_esclamiento_clase(int id_esclamiento_clase) {
		this.id_esclamiento_clase = id_esclamiento_clase;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	public boolean isVigente() {
		return vigente;
	}
	public void setVigente(boolean vigente) {
		this.vigente = vigente;
	}
	
	
}
