package portal.com.eje.portal.liquidacion.vo;

import java.util.Date;

import portal.com.eje.portal.vo.vo.Vo;

public class VoLiqCabecera extends Vo {
	private int periodo;
	private String empresa;
	private String unidad;
	private int rut;
	private String causa_pago;
	private Date fec_pago;
	private String forma_pago;
	private String cuenta;
	private Double imp_tribut;
	private Double imp_no_tribut;
	private Double no_imp_tribut;
	private Double no_imp_no_tribut;
	private Double reliq_rentas;
	private Double tot_haberes;
	private Double tot_desctos;
	private Double liquido;
	private String id_forma_pago;
	private Double tope_imp;
	private Float val_uf;
	private Double dctos_varios;
	private Double dctos_legales;
	private Double dctos_impagos;
	private String banco;
	private int wp_cod_empresa;
	private int wp_cod_planta;
	private int wp_tot_imponible;
	private int wp_afecto_imponible;
	private int wp_ndias_trab;
	private Double base_tribut;
	private int n_cargas;
	private Double sobregiro;
	private String tramo;
	private int wp_ndias_lic;
	private int isapre;
	private int afp;
	private Double tot_aportaciones;
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
	public Double getImp_tribut() {
		return imp_tribut;
	}
	public void setImp_tribut(Double imp_tribut) {
		this.imp_tribut = imp_tribut;
	}
	public Double getImp_no_tribut() {
		return imp_no_tribut;
	}
	public void setImp_no_tribut(Double imp_no_tribut) {
		this.imp_no_tribut = imp_no_tribut;
	}
	public Double getNo_imp_tribut() {
		return no_imp_tribut;
	}
	public void setNo_imp_tribut(Double no_imp_tribut) {
		this.no_imp_tribut = no_imp_tribut;
	}
	public Double getNo_imp_no_tribut() {
		return no_imp_no_tribut;
	}
	public void setNo_imp_no_tribut(Double no_imp_no_tribut) {
		this.no_imp_no_tribut = no_imp_no_tribut;
	}
	public Double getReliq_rentas() {
		return reliq_rentas;
	}
	public void setReliq_rentas(Double reliq_rentas) {
		this.reliq_rentas = reliq_rentas;
	}
	public Double getTot_haberes() {
		return tot_haberes;
	}
	public void setTot_haberes(Double tot_haberes) {
		this.tot_haberes = tot_haberes;
	}
	public Double getTot_desctos() {
		return tot_desctos;
	}
	public void setTot_desctos(Double tot_desctos) {
		this.tot_desctos = tot_desctos;
	}
	public Double getLiquido() {
		return liquido;
	}
	public void setLiquido(Double liquido) {
		this.liquido = liquido;
	}
	public String getId_forma_pago() {
		return id_forma_pago;
	}
	public void setId_forma_pago(String id_forma_pago) {
		this.id_forma_pago = id_forma_pago;
	}
	public Double getTope_imp() {
		return tope_imp;
	}
	public void setTope_imp(Double tope_imp) {
		this.tope_imp = tope_imp;
	}
	public Float getVal_uf() {
		return val_uf;
	}
	public void setVal_uf(Float val_uf) {
		this.val_uf = val_uf;
	}
	public Double getDctos_varios() {
		return dctos_varios;
	}
	public void setDctos_varios(Double dctos_varios) {
		this.dctos_varios = dctos_varios;
	}
	public Double getDctos_legales() {
		return dctos_legales;
	}
	public void setDctos_legales(Double dctos_legales) {
		this.dctos_legales = dctos_legales;
	}
	public Double getDctos_impagos() {
		return dctos_impagos;
	}
	public void setDctos_impagos(Double dctos_impagos) {
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
	public int getWp_cod_planta() {
		return wp_cod_planta;
	}
	public void setWp_cod_planta(int wp_cod_planta) {
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
	public Double getBase_tribut() {
		return base_tribut;
	}
	public void setBase_tribut(Double base_tribut) {
		this.base_tribut = base_tribut;
	}
	public int getN_cargas() {
		return n_cargas;
	}
	public void setN_cargas(int n_cargas) {
		this.n_cargas = n_cargas;
	}
	public Double getSobregiro() {
		return sobregiro;
	}
	public void setSobregiro(Double sobregiro) {
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
	public Double getTot_aportaciones() {
		return tot_aportaciones;
	}
	public void setTot_aportaciones(Double tot_aportaciones) {
		this.tot_aportaciones = tot_aportaciones;
	}
	
	
}
