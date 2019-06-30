package cl.eje.model.generic.mis;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mis", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_corr", isForeignKey = false, numerica = true) }, tableName = "eje_amis_metrica_sql")
public class Eje_amis_metrica_sql extends Vo {
	private int 	id_corr;
	private int 	id_proceso;
	private int 	eambiente;
	private int 	eindicador;
	private String 	sql;
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
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	
}
