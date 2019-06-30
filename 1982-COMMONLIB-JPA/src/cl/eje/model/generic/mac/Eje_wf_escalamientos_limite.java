package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "id_tipolimite", numerica = true, isForeignKey = false) }, tableName = "eje_wf_escalamientos_limite")
public class Eje_wf_escalamientos_limite  extends Vo  {
	private int	id_tipolimite;
	private String nombre;
	
	public int getId_tipolimite() {
		return id_tipolimite;
	}
	public void setId_tipolimite(int id_tipolimite) {
		this.id_tipolimite = id_tipolimite;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	
}
