package cl.eje.model.generic.mis;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mis", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_corr", isForeignKey = false, numerica = true) }, tableName = "Eje_mis_metrica_liquidacion_detalle")
public class Eje_mis_metrica_liquidacion_detalle extends Vo {
	private int id_corr;
	private int id_proceso;
	private int eambiente;
	private int eindicador;
	private int rut;
	private String id_tp;
	private String glosa_haber;
	private int val_haber;
	private String glosa_descuento;
	private int val_descuento;

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

	public String getId_tp() {
		return id_tp;
	}

	public void setId_tp(String id_tp) {
		this.id_tp = id_tp;
	}

	public String getGlosa_haber() {
		return glosa_haber;
	}

	public void setGlosa_haber(String glosa_haber) {
		this.glosa_haber = glosa_haber;
	}

	public int getVal_haber() {
		return val_haber;
	}

	public void setVal_haber(int val_haber) {
		this.val_haber = val_haber;
	}

	public String getGlosa_descuento() {
		return glosa_descuento;
	}

	public void setGlosa_descuento(String glosa_descuento) {
		this.glosa_descuento = glosa_descuento;
	}

	public int getVal_descuento() {
		return val_descuento;
	}

	public void setVal_descuento(int val_descuento) {
		this.val_descuento = val_descuento;
	}

}
