package cl.eje.model.generic.portal;

import java.util.Date;

import portal.com.eje.portal.vo.annotations.PrimaryKeyDefinition;
import portal.com.eje.portal.vo.annotations.TableDefinition;
import portal.com.eje.portal.vo.vo.Vo;

@TableDefinition(jndi = "portal", pks = { @PrimaryKeyDefinition(autoIncremental = false, field = "login_usuario", isForeignKey = true, numerica = true) }, tableName = "eje_ges_usuario")
public class Eje_ges_usuario extends Vo {
	private String login_usuario;
	private String password_usuario;
	private int rut_usuario;
	private String emp_rel;
	private String uni_rel;
	private int error;
	private String passw_cambiar;
	private Date passw_ult_cambio;
	private String tipo_rel;
	private int cant_ingresos;
	private int wp_cod_empresa;
	private int wp_cod_planta;
	private String md5;

	public String getLogin_usuario() {
		return login_usuario;
	}

	public void setLogin_usuario(String login_usuario) {
		this.login_usuario = login_usuario;
	}

	public String getPassword_usuario() {
		return password_usuario;
	}

	public void setPassword_usuario(String password_usuario) {
		this.password_usuario = password_usuario;
	}

	public int getRut_usuario() {
		return rut_usuario;
	}

	public void setRut_usuario(int rut_usuario) {
		this.rut_usuario = rut_usuario;
	}

	public String getEmp_rel() {
		return emp_rel;
	}

	public void setEmp_rel(String emp_rel) {
		this.emp_rel = emp_rel;
	}

	public String getUni_rel() {
		return uni_rel;
	}

	public void setUni_rel(String uni_rel) {
		this.uni_rel = uni_rel;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getPassw_cambiar() {
		return passw_cambiar;
	}

	public void setPassw_cambiar(String passw_cambiar) {
		this.passw_cambiar = passw_cambiar;
	}

	public Date getPassw_ult_cambio() {
		return passw_ult_cambio;
	}

	public void setPassw_ult_cambio(Date passw_ult_cambio) {
		this.passw_ult_cambio = passw_ult_cambio;
	}

	public String getTipo_rel() {
		return tipo_rel;
	}

	public void setTipo_rel(String tipo_rel) {
		this.tipo_rel = tipo_rel;
	}

	public int getCant_ingresos() {
		return cant_ingresos;
	}

	public void setCant_ingresos(int cant_ingresos) {
		this.cant_ingresos = cant_ingresos;
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

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

}
