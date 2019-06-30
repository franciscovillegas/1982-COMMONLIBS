package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "peri_id", isForeignKey = false, numerica = true) }, tableName = "eje_ges_periodo")
public class Eje_ges_periodo {
	private Integer peri_id;
	private Integer peri_ano;
	private Integer peri_mes;
	private Double peri_utm;
	private Double peri_uf;
	
	public Integer getPeri_id() {
		return peri_id;
	}
	public void setPeri_id(Integer peri_id) {
		this.peri_id = peri_id;
	}
	public Integer getPeri_ano() {
		return peri_ano;
	}
	public void setPeri_ano(Integer peri_ano) {
		this.peri_ano = peri_ano;
	}
	public Integer getPeri_mes() {
		return peri_mes;
	}
	public void setPeri_mes(Integer peri_mes) {
		this.peri_mes = peri_mes;
	}
	public Double getPeri_utm() {
		return peri_utm;
	}
	public void setPeri_utm(Double peri_utm) {
		this.peri_utm = peri_utm;
	}
	public Double getPeri_uf() {
		return peri_uf;
	}
	public void setPeri_uf(Double peri_uf) {
		this.peri_uf = peri_uf;
	}
	
	
	
}
