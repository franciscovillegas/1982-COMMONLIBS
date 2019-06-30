package cl.eje.model.generic.mis;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mis", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_corr", isForeignKey = false, numerica = true) }, tableName = "eje_mis_metrica_centro_costo")
public class Eje_mis_metrica_centro_costo extends Vo {
	private int id_corr;
	private int id_proceso;
	private int eambiente;
	private int eindicador;
	private String ccosto_id;
	private String ccosto_desc;
	
	public int getId_corr() {
		return id_corr;
	}
	public void setId_corr(int id_corr) {
		this.id_corr = id_corr;
	}
	public int getId_proceso() {
		return id_proceso;
	}
	public void setId_proceso(int id_proceso) {
		this.id_proceso = id_proceso;
	}
	public int getEambiente() {
		return eambiente;
	}
	public void setEambiente(int eambiente) {
		this.eambiente = eambiente;
	}
	public int getEindicador() {
		return eindicador;
	}
	public void setEindicador(int eindicador) {
		this.eindicador = eindicador;
	}
	public String getCcosto_id() {
		return ccosto_id;
	}
	public void setCcosto_id(String ccosto_id) {
		this.ccosto_id = ccosto_id;
	}
	public String getCcosto_desc() {
		return ccosto_desc;
	}
	public void setCcosto_desc(String ccosto_desc) {
		this.ccosto_desc = ccosto_desc;
	}
	
	
	
}
