package cl.eje.model.generic.portal;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal",
pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "cargo", numerica = false, isForeignKey = false),
		@PrimaryKeyDefinition(autoIncremental = false, field = "empresa", numerica = false, isForeignKey = false)}, tableName = "eje_ges_cargos")
public class Eje_ges_cargos extends Vo {
	private String cargo;
	private String descrip;
	private int indica_rrhh;
	private String cod_m4;
	private String mision;
	private String funciones;
	private String educ_id;
	private String super_id;
	private String exp_id;
	private String caracteristicas;
	private String habilidades;
	private String area_tit_id;
	private String empresa;
	private String vigente;
	private int colegas;
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	public int getIndica_rrhh() {
		return indica_rrhh;
	}
	public void setIndica_rrhh(int indica_rrhh) {
		this.indica_rrhh = indica_rrhh;
	}
	public String getCod_m4() {
		return cod_m4;
	}
	public void setCod_m4(String cod_m4) {
		this.cod_m4 = cod_m4;
	}
	public String getMision() {
		return mision;
	}
	public void setMision(String mision) {
		this.mision = mision;
	}
	public String getFunciones() {
		return funciones;
	}
	public void setFunciones(String funciones) {
		this.funciones = funciones;
	}
	public String getEduc_id() {
		return educ_id;
	}
	public void setEduc_id(String educ_id) {
		this.educ_id = educ_id;
	}
	public String getSuper_id() {
		return super_id;
	}
	public void setSuper_id(String super_id) {
		this.super_id = super_id;
	}
	public String getExp_id() {
		return exp_id;
	}
	public void setExp_id(String exp_id) {
		this.exp_id = exp_id;
	}
	public String getCaracteristicas() {
		return caracteristicas;
	}
	public void setCaracteristicas(String caracteristicas) {
		this.caracteristicas = caracteristicas;
	}
	public String getHabilidades() {
		return habilidades;
	}
	public void setHabilidades(String habilidades) {
		this.habilidades = habilidades;
	}
	public String getArea_tit_id() {
		return area_tit_id;
	}
	public void setArea_tit_id(String area_tit_id) {
		this.area_tit_id = area_tit_id;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getVigente() {
		return vigente;
	}
	public void setVigente(String vigente) {
		this.vigente = vigente;
	}
	public int getColegas() {
		return colegas;
	}
	public void setColegas(int colegas) {
		this.colegas = colegas;
	}
	
	
	
}
