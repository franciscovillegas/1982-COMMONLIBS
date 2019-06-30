package cl.eje.model.generic.mis;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mis", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_corr", isForeignKey = false, numerica = true) }, tableName = "eje_mis_metrica_vacacion")
public class Eje_mis_metrica_vacacion extends Vo {
	private int 	id_corr;
	private int 	id_proceso;
	private int 	eambiente;
	private int 	eindicador;
	private int 	rut;
	private Date 	desde;
	private Date 	hasta;
	private int 	dias_normales;
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
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}
	public Date getDesde() {
		return desde;
	}
	public void setDesde(Date desde) {
		this.desde = desde;
	}
	public Date getHasta() {
		return hasta;
	}
	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}
	public int getDias_normales() {
		return dias_normales;
	}
	public void setDias_normales(int dias_normales) {
		this.dias_normales = dias_normales;
	}
	
	
	
}
