package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = true, field = "id_corr", isForeignKey = false, numerica = true) }, tableName = "eje_ges_haber_log_save")
public class Eje_ges_haber_log_save extends Vo {
	private int id_corr;
	private String haber;
	private String descrip;
	private int tipo;
	private int afec_imp;
	private int imponible;
	private String id_tp;
	private int orden;
	private int wp_cod_empresa;
	private int wp_cod_planta;
	private String campo1;
	private String campo2;
	private String campo3;
	private String campo4;
	private String campo5;
	private String campo6;
	private String campo7;
	private String campo8;
	private int id_corr_aplica;
	public int getId_corr() {
		return id_corr;
	}
	public void setId_corr(int id_corr) {
		this.id_corr = id_corr;
	}
	public String getHaber() {
		return haber;
	}
	public void setHaber(String haber) {
		this.haber = haber;
	}
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public int getAfec_imp() {
		return afec_imp;
	}
	public void setAfec_imp(int afec_imp) {
		this.afec_imp = afec_imp;
	}
	public int getImponible() {
		return imponible;
	}
	public void setImponible(int imponible) {
		this.imponible = imponible;
	}
	public String getId_tp() {
		return id_tp;
	}
	public void setId_tp(String id_tp) {
		this.id_tp = id_tp;
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
	public int getWp_cod_planta() {
		return wp_cod_planta;
	}
	public void setWp_cod_planta(int wp_cod_planta) {
		this.wp_cod_planta = wp_cod_planta;
	}
	public String getCampo1() {
		return campo1;
	}
	public void setCampo1(String campo1) {
		this.campo1 = campo1;
	}
	public String getCampo2() {
		return campo2;
	}
	public void setCampo2(String campo2) {
		this.campo2 = campo2;
	}
	public String getCampo3() {
		return campo3;
	}
	public void setCampo3(String campo3) {
		this.campo3 = campo3;
	}
	public String getCampo4() {
		return campo4;
	}
	public void setCampo4(String campo4) {
		this.campo4 = campo4;
	}
	public String getCampo5() {
		return campo5;
	}
	public void setCampo5(String campo5) {
		this.campo5 = campo5;
	}
	public String getCampo6() {
		return campo6;
	}
	public void setCampo6(String campo6) {
		this.campo6 = campo6;
	}
	public String getCampo7() {
		return campo7;
	}
	public void setCampo7(String campo7) {
		this.campo7 = campo7;
	}
	public String getCampo8() {
		return campo8;
	}
	public void setCampo8(String campo8) {
		this.campo8 = campo8;
	}
	public int getId_corr_aplica() {
		return id_corr_aplica;
	}
	public void setId_corr_aplica(int id_corr_aplica) {
		this.id_corr_aplica = id_corr_aplica;
	}
	 
	
	
	
	
}
