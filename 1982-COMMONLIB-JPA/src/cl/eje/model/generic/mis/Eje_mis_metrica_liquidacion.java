package cl.eje.model.generic.mis;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mis", pks = {
		@PrimaryKeyDefinition(autoIncremental = true, field = "id_corr", isForeignKey = false, numerica = true) }, tableName = "eje_mis_metrica_liquidacion")
public class Eje_mis_metrica_liquidacion extends Vo {
	private int id_corr;
	private int id_proceso;
	private int eambiente;
	private int eindicador;
	private int rut;
	private double tot_haberes;
	private double tot_desctos;
	private double tot_liquido;

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

	public double getTot_haberes() {
		return tot_haberes;
	}

	public void setTot_haberes(double tot_haberes) {
		this.tot_haberes = tot_haberes;
	}

	public double getTot_desctos() {
		return tot_desctos;
	}

	public void setTot_desctos(double tot_desctos) {
		this.tot_desctos = tot_desctos;
	}

	public double getTot_liquido() {
		return tot_liquido;
	}

	public void setTot_liquido(double tot_liquido) {
		this.tot_liquido = tot_liquido;
	}

}
