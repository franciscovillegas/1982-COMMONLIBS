package cl.eje.model.generic.mac;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", tableName = "eje_wfgen_fondoprevision", pks={@PrimaryKeyDefinition(autoIncremental = true, field = "id_fondoprevision", numerica = true, isForeignKey = false)})
public class Eje_wfgen_fondoprevision extends Vo {
	private int	id_pais;
	private int	id_fondoprevision;
	private String	nombre;
	private int	vigente;
	private int	id_fondoprevision_swinformado;
	
	public int getId_pais() {
		return id_pais;
	}
	public void setId_pais(int id_pais) {
		this.id_pais = id_pais;
	}
	public int getId_fondoprevision() {
		return id_fondoprevision;
	}
	public void setId_fondoprevision(int id_fondoprevision) {
		this.id_fondoprevision = id_fondoprevision;
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
	public int getId_fondoprevision_swinformado() {
		return id_fondoprevision_swinformado;
	}
	public void setId_fondoprevision_swinformado(int id_fondoprevision_swinformado) {
		this.id_fondoprevision_swinformado = id_fondoprevision_swinformado;
	}
	
	
	

}
