package cl.eje.model.generic.mis;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mis", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_corr", isForeignKey = false, numerica = true) }, tableName = "eje_mis_metrica_licencia")
public class Eje_mis_metrica_licencia extends Vo {
	private int id_corr;
	private int id_proceso;
	private int eambiente;
	private int eindicador;
	private int rut;
	private Date fecha_inicio;
	private Date fecha_termino;
	private int dias;

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

	public Date getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public Date getFecha_termino() {
		return fecha_termino;
	}

	public void setFecha_termino(Date fecha_termino) {
		this.fecha_termino = fecha_termino;
	}

	public int getDias() {
		return dias;
	}

	public void setDias(int dias) {
		this.dias = dias;
	}

}
