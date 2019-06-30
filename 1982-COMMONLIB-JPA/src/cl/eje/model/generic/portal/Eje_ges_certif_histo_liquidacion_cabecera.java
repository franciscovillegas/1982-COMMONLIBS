package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { 
		@PrimaryKeyDefinition(autoIncremental = false, field = "id_tabliq", isForeignKey = true, numerica = true) }, tableName = "eje_ges_certif_histo_liquidacion_cabecera")
public class Eje_ges_certif_histo_liquidacion_cabecera extends Vo {
	private int id_tabliq;
	private int periodo;
	private String empresa;
	private String unidad;
	private int rut;
	private String causa_pago;
	private Date fec_pago;
	private String forma_pago;
	private String cuenta;
	private double imp_tribut;
	private double imp_no_tribut;
	private double no_imp_tribut;
	private double no_imp_no_tribut;
	private double reliq_rentas;
	private double tot_haberes;
	private double tot_desctos;
	private double liquido;
	private String id_forma_pago;
	private double tope_imp;
	private double val_uf;
	private double dctos_varios;
	private double dctos_legales;
	private double dctos_impagos;
	private String banco;
	private int wp_cod_empresa;
	private double wp_cod_planta;
	private int wp_tot_imponible;
	private int wp_afecto_imponible;
	private int wp_ndias_trab;
	private double base_tribut;
	private int n_cargas;
	private double sobregiro;
	private String tramo;
	private int wp_ndias_lic;
	private int isapre;
	private int afp;
	private double tot_aportaciones;
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
	public String getCausa_pago() {
		return causa_pago;
	}
	public void setCausa_pago(String causa_pago) {
		this.causa_pago = causa_pago;
	}
	public Date getFec_pago() {
		return fec_pago;
	}
	public void setFec_pago(Date fec_pago) {
		this.fec_pago = fec_pago;
	}
	public String getForma_pago() {
		return forma_pago;
	}
	public void setForma_pago(String forma_pago) {
		this.forma_pago = forma_pago;
	}
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public double getImp_tribut() {
		return imp_tribut;
	}
	public void setImp_tribut(double imp_tribut) {
		this.imp_tribut = imp_tribut;
	}
	public double getImp_no_tribut() {
		return imp_no_tribut;
	}
	public void setImp_no_tribut(double imp_no_tribut) {
		this.imp_no_tribut = imp_no_tribut;
	}
	public double getNo_imp_tribut() {
		return no_imp_tribut;
	}
	public void setNo_imp_tribut(double no_imp_tribut) {
		this.no_imp_tribut = no_imp_tribut;
	}
	public double getNo_imp_no_tribut() {
		return no_imp_no_tribut;
	}
	public void setNo_imp_no_tribut(double no_imp_no_tribut) {
		this.no_imp_no_tribut = no_imp_no_tribut;
	}
	public double getReliq_rentas() {
		return reliq_rentas;
	}
	public void setReliq_rentas(double reliq_rentas) {
		this.reliq_rentas = reliq_rentas;
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
	public double getLiquido() {
		return liquido;
	}
	public void setLiquido(double liquido) {
		this.liquido = liquido;
	}
	public String getId_forma_pago() {
		return id_forma_pago;
	}
	public void setId_forma_pago(String id_forma_pago) {
		this.id_forma_pago = id_forma_pago;
	}
	public double getTope_imp() {
		return tope_imp;
	}
	public void setTope_imp(double tope_imp) {
		this.tope_imp = tope_imp;
	}
	public double getVal_uf() {
		return val_uf;
	}
	public void setVal_uf(double val_uf) {
		this.val_uf = val_uf;
	}
	public double getDctos_varios() {
		return dctos_varios;
	}
	public void setDctos_varios(double dctos_varios) {
		this.dctos_varios = dctos_varios;
	}
	public double getDctos_legales() {
		return dctos_legales;
	}
	public void setDctos_legales(double dctos_legales) {
		this.dctos_legales = dctos_legales;
	}
	public double getDctos_impagos() {
		return dctos_impagos;
	}
	public void setDctos_impagos(double dctos_impagos) {
		this.dctos_impagos = dctos_impagos;
	}
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
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
	public int getWp_tot_imponible() {
		return wp_tot_imponible;
	}
	public void setWp_tot_imponible(int wp_tot_imponible) {
		this.wp_tot_imponible = wp_tot_imponible;
	}
	public int getWp_afecto_imponible() {
		return wp_afecto_imponible;
	}
	public void setWp_afecto_imponible(int wp_afecto_imponible) {
		this.wp_afecto_imponible = wp_afecto_imponible;
	}
	public int getWp_ndias_trab() {
		return wp_ndias_trab;
	}
	public void setWp_ndias_trab(int wp_ndias_trab) {
		this.wp_ndias_trab = wp_ndias_trab;
	}
	public double getBase_tribut() {
		return base_tribut;
	}
	public void setBase_tribut(double base_tribut) {
		this.base_tribut = base_tribut;
	}
	public int getN_cargas() {
		return n_cargas;
	}
	public void setN_cargas(int n_cargas) {
		this.n_cargas = n_cargas;
	}
	public double getSobregiro() {
		return sobregiro;
	}
	public void setSobregiro(double sobregiro) {
		this.sobregiro = sobregiro;
	}
	public String getTramo() {
		return tramo;
	}
	public void setTramo(String tramo) {
		this.tramo = tramo;
	}
	public int getWp_ndias_lic() {
		return wp_ndias_lic;
	}
	public void setWp_ndias_lic(int wp_ndias_lic) {
		this.wp_ndias_lic = wp_ndias_lic;
	}
	public int getIsapre() {
		return isapre;
	}
	public void setIsapre(int isapre) {
		this.isapre = isapre;
	}
	public int getAfp() {
		return afp;
	}
	public void setAfp(int afp) {
		this.afp = afp;
	}
	public double getTot_aportaciones() {
		return tot_aportaciones;
	}
	public void setTot_aportaciones(double tot_aportaciones) {
		this.tot_aportaciones = tot_aportaciones;
	}
	
	
	
	
}
