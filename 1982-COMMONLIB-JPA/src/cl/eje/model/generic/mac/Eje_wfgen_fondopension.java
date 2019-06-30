package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", tableName = "eje_wfgen_fondopension", pks={@PrimaryKeyDefinition(autoIncremental = true, field = "id_fondopension", numerica = true, isForeignKey = false)})
public class Eje_wfgen_fondopension extends Vo {
	private int id_pais;
	private int id_fondopension;
	private String nombre;
	private int vigente;
	private int id_fondopension_swinformado;
	
	public int getId_pais() {
		return id_pais;
	}
	public void setId_pais(int id_pais) {
		this.id_pais = id_pais;
	}
	public int getId_fondopension() {
		return id_fondopension;
	}
	public void setId_fondopension(int id_fondopension) {
		this.id_fondopension = id_fondopension;
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
	public int getId_fondopension_swinformado() {
		return id_fondopension_swinformado;
	}
	public void setId_fondopension_swinformado(int id_fondopension_swinformado) {
		this.id_fondopension_swinformado = id_fondopension_swinformado;
	}
	
	

}
