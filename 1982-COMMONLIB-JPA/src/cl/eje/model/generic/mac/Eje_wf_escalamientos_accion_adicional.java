package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_escalamiento_accion_adicional", numerica = true, isForeignKey = false) }, tableName = "eje_wf_escalamientos_accion_adicional")
public class Eje_wf_escalamientos_accion_adicional extends Vo {
	private int	id_escalamiento_accion_adicional;
	private String nombre;
	private String clase;
	private boolean vigente;
	
	
	public int getId_escalamiento_accion_adicional() {
		return id_escalamiento_accion_adicional;
	}
	public void setId_escalamiento_accion_adicional(int id_escalamiento_accion_adicional) {
		this.id_escalamiento_accion_adicional = id_escalamiento_accion_adicional;
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
