package cl.eje.mail.managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;

public class ManagerAcceso {
	private static ManagerAcceso instance;
	
	private ManagerAcceso() {
	}
	
	public static ManagerAcceso getInstance() {
		if(instance == null) {
			synchronized (ManagerAcceso.class) {
				if(instance == null) {
					instance = new ManagerAcceso();
				}
			}
		}
		
		return instance;
	}

	public boolean insertPerfil(String newPassword, int rut) {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" UPDATE eje_ges_usuario SET password_usuario = ?  WHERE login_usuario = ?  ");
		
		Object[] params = {newPassword,rut};
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("portal",strConsulta.toString(),params) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public ConsultaData getUltimoAcceso(int rut) {
		String sql = "select rut,fecha_ult_acceso,clave,clave_enc from eje_ges_usuario_ult_acceso where rut = ?";

		ConsultaData data = null;
		Object[] params = {rut};
		try {
			data = ConsultaTool.getInstance().getData("portal", sql,params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public ConsultaData getEjeGesUsuario(int rut) {
		String sql = "select login_usuario,password_usuario,rut_usuario,emp_rel,uni_rel,error,passw_cambiar,passw_ult_cambio,tipo_rel,cant_ingresos,wp_cod_empresa,wp_cod_planta,md5 from eje_ges_usuario where rut_usuario = ?";

		ConsultaData data = null;
		Object[] params = {rut};
		try {
			data = ConsultaTool.getInstance().getData("portal", sql,params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	public List<String> getPrivilegios(int rut) {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" SELECT app_id,rut_usuario, vigente, wp_cod_empresa, wp_cod_planta FROM eje_ges_user_app WHERE rut_usuario = ? ");
		ArrayList<String> lista = new ArrayList<String>();
		Object[] params = {rut};

		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal",strConsulta.toString(),params);
			
			while(data.next()) {
				lista.add(data.getString("app_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return lista;
	}
	
	public boolean addPrivilegios(int rut, String privilegio) {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" INSERT INTO  eje_ges_user_app ");
		strConsulta.append(" 	(app_id,rut_usuario, vigente, wp_cod_empresa, wp_cod_planta) ");
		strConsulta.append(" VALUES (?,?,NULL,-1,-1)");
		
		Object[] params = {privilegio,rut};

		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().insert("portal",strConsulta.toString(),params);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
}
