package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks ={ @PrimaryKeyDefinition(autoIncremental = true, field = "idcadena", isForeignKey = false, numerica = true) }, tableName = "eje_wfgen_cadena")
public class Eje_wfgen_cadena extends Vo {
	private int	idcadena;
	private String nemo;
	private String nombre;
	
	public int getIdcadena() {
		return idcadena;
	}
	public void setIdcadena(int idcadena) {
		this.idcadena = idcadena;
	}
	public String getNemo() {
		return nemo;
	}
	public void setNemo(String nemo) {
		this.nemo = nemo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
 
	
}
