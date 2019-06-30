package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;

@TableDefinition(jndi = "portal", pks = { 
		@PrimaryKeyDefinition(autoIncremental = false, field = "id_tabliq", isForeignKey = true, numerica = true) 
}, tableName = "eje_ges_certif_histo_liquidacion_detalle")
public class Eje_ges_certif_histo_liquidacion_detalle {
	private int id_tabliq;
	private int periodo;
	private String empresa;
	private String unidad;
	private int rut;
	private double tip_unidad;
	private String id_tp;
	private String glosa_haber;
	private double val_haber;
	private String glosa_descuento;
	private double val_descuento;
	private int orden;
	private int wp_cod_empresa;
	private double wp_cod_planta;
	private String wp_indic_papeleta;
	private String haber_variable;
	public int getId_tabliq() {
		return id_tabliq;
	}
	public void setId_tabliq(int id_tabliq) {
		this.id_tabliq = id_tabliq;
	}
	public int getPeriodo() {
		return periodo;
	}
	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getUnidad() {
		return unidad;
	}
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}
	public double getTip_unidad() {
		return tip_unidad;
	}
	public void setTip_unidad(double tip_unidad) {
		this.tip_unidad = tip_unidad;
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
	public double getVal_haber() {
		return val_haber;
	}
	public void setVal_haber(double val_haber) {
		this.val_haber = val_haber;
	}
	public String getGlosa_descuento() {
		return glosa_descuento;
	}
	public void setGlosa_descuento(String glosa_descuento) {
		this.glosa_descuento = glosa_descuento;
	}
	public double getVal_descuento() {
		return val_descuento;
	}
	public void setVal_descuento(double val_descuento) {
		this.val_descuento = val_descuento;
	}
	public int getOrden() {
		return orden;
	}
	public void setOrden(int orden) {
		this.orden = orden;
	}
	public int getWp_cod_empresa() {
		return wp_cod_empresa;
	}
	public void setWp_cod_empresa(int wp_cod_empresa) {
		this.wp_cod_empresa = wp_cod_empresa;
	}
	public double getWp_cod_planta() {
		return wp_cod_planta;
	}
	public void setWp_cod_planta(double wp_cod_planta) {
		this.wp_cod_planta = wp_cod_planta;
	}
	public String getWp_indic_papeleta() {
		return wp_indic_papeleta;
	}
	public void setWp_indic_papeleta(String wp_indic_papeleta) {
		this.wp_indic_papeleta = wp_indic_papeleta;
	}
	public String getHaber_variable() {
		return haber_variable;
	}
	public void setHaber_variable(String haber_variable) {
		this.haber_variable = haber_variable;
	}
	
	
	
}
