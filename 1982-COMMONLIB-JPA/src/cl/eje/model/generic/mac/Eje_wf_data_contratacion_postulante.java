package cl.eje.model.generic.mac;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "mac", pks = {
		@PrimaryKeyDefinition(autoIncremental = false, field = "id_req", numerica = true, isForeignKey = true),
		@PrimaryKeyDefinition(autoIncremental = false, field = "id_postulante", numerica = true, isForeignKey = true)
}, tableName = "eje_wf_data_contratacion_postulante")

public class Eje_wf_data_contratacion_postulante extends Vo {
	private int id_req;
	private int id_postulante;
	private String observacion;
	private double nota;
	private Date fecha_entrevista;
	private Boolean informado_preingreso_inicio;
	private Boolean informado_preingreso;
	private Boolean informado_setactivado;
	private Boolean informado_setdesistir;
	private Boolean entrevista_efectuada;
	private Boolean ingreso_efectuado;
	private Date fecha_real_ingreso;
	private Boolean vigente;
	
	public int getId_req() {
		return id_req;
	}
	public void setId_req(int id_req) {
		this.id_req = id_req;
	}
	public int getId_postulante() {
		return id_postulante;
	}
	public void setId_postulante(int id_postulante) {
		this.id_postulante = id_postulante;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public double getNota() {
		return nota;
	}
	public void setNota(double nota) {
		this.nota = nota;
	}
	public Date getFecha_entrevista() {
		return fecha_entrevista;
	}
	public void setFecha_entrevista(Date fecha_entrevista) {
		this.fecha_entrevista = fecha_entrevista;
	}
	public Boolean getInformado_preingreso_inicio() {
		return informado_preingreso_inicio;
	}
	public void setInformado_preingreso_inicio(Boolean informado_preingreso_inicio) {
		this.informado_preingreso_inicio = informado_preingreso_inicio;
	}
	public Boolean getInformado_preingreso() {
		return informado_preingreso;
	}
	public void setInformado_preingreso(Boolean informado_preingreso) {
		this.informado_preingreso = informado_preingreso;
	}
	public Boolean getInformado_setactivado() {
		return informado_setactivado;
	}
	public void setInformado_setactivado(Boolean informado_setactivado) {
		this.informado_setactivado = informado_setactivado;
	}
	public Boolean getInformado_setdesistir() {
		return informado_setdesistir;
	}
	public void setInformado_setdesistir(Boolean informado_setdesistir) {
		this.informado_setdesistir = informado_setdesistir;
	}
	public Boolean getEntrevista_efectuada() {
		return entrevista_efectuada;
	}
	public void setEntrevista_efectuada(Boolean entrevista_efectuada) {
		this.entrevista_efectuada = entrevista_efectuada;
	}
	public Boolean getIngreso_efectuado() {
		return ingreso_efectuado;
	}
	public void setIngreso_efectuado(Boolean ingreso_efectuado) {
		this.ingreso_efectuado = ingreso_efectuado;
	}
	public Date getFecha_real_ingreso() {
		return fecha_real_ingreso;
	}
	public void setFecha_real_ingreso(Date fecha_real_ingreso) {
		this.fecha_real_ingreso = fecha_real_ingreso;
	}
	public Boolean getVigente() {
		return vigente;
	}
	public void setVigente(Boolean vigente) {
		this.vigente = vigente;
	}
	
	
	
	
}
