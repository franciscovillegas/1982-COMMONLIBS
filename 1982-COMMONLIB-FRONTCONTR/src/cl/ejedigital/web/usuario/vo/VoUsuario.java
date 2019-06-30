package cl.ejedigital.web.usuario.vo;

import java.io.Serializable;
import java.sql.Timestamp;


public class VoUsuario implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private final String	login_usuario;
	private final String	password_usuario;
	private final int		rut_usuario;
	private final String	emp_rel;
	private final String	uni_rel;
	private final int		error;
	private final String	passw_cambiar;
	private final Timestamp	passw_ult_cambio;
	private final String	tipo_rel;
	private final int		cant_ingresos;
	private final int		wp_cod_empresa;
	private final int		wp_cod_planta;
	private final String	md5;

	public VoUsuario() {
		login_usuario = null;
		password_usuario = null;
		rut_usuario= -1;
		emp_rel = null;
		uni_rel = null;
		error = -1;
		passw_cambiar = null;
		passw_ult_cambio = null;
		tipo_rel = null;
		cant_ingresos = -1;
		wp_cod_empresa = -1;
		wp_cod_planta = -1;
		md5 = null;

	}
	
	public VoUsuario(String login_usuario, String password_usuario, int rut_usuario, String emp_rel, String uni_rel,
						int error, String passw_cambiar, Timestamp passw_ult_cambio, String tipo_rel, int cant_ingresos,
						int wp_cod_empresa, int wp_cod_planta, String md5) {
		super();
		this.login_usuario = login_usuario;
		this.password_usuario = password_usuario;
		this.rut_usuario = rut_usuario;
		this.emp_rel = emp_rel;
		this.uni_rel = uni_rel;
		this.error = error;
		this.passw_cambiar = passw_cambiar;
		this.passw_ult_cambio = passw_ult_cambio;
		this.tipo_rel = tipo_rel;
		this.cant_ingresos = cant_ingresos;
		this.wp_cod_empresa = wp_cod_empresa;
		this.wp_cod_planta = wp_cod_planta;
		this.md5 = md5;
	}

	public String getLogin_usuario() {
		return login_usuario;
	}

	public String getPassword_usuario() {
		return password_usuario;
	}

	public int getRut_usuario() {
		return rut_usuario;
	}

	public String getEmp_rel() {
		return emp_rel;
	}

	public String getUni_rel() {
		return uni_rel;
	}

	public int getError() {
		return error;
	}

	public String getPassw_cambiar() {
		return passw_cambiar;
	}

	public Timestamp getPassw_ult_cambio() {
		return passw_ult_cambio;
	}

	public String getTipo_rel() {
		return tipo_rel;
	}

	public int getCant_ingresos() {
		return cant_ingresos;
	}

	public int getWp_cod_empresa() {
		return wp_cod_empresa;
	}

	public int getWp_cod_planta() {
		return wp_cod_planta;
	}

	public String getMd5() {
		return md5;
	}

}
