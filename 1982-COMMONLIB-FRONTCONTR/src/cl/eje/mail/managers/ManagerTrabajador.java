package cl.eje.mail.managers;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;

public class ManagerTrabajador {
	private static ManagerTrabajador instance;
	private List<String> columnName;
	
	private ManagerTrabajador() {
		columnName = new ArrayList<String>();
		columnName.add("rut");
		columnName.add("digito_ver");
		columnName.add("nombres");
		columnName.add("ape_paterno");
		columnName.add("ape_materno");
		columnName.add("fecha_ingreso");

	}
	
	public static ManagerTrabajador getInstance() {
		if(instance == null) {
			synchronized (ManagerTrabajador.class) {
				if(instance == null) {
					instance = new ManagerTrabajador();
				}
			}
		}
		
		return instance;
	}
	
	public ConsultaData getConfirmacionCorreo(int rut) {
		String sql = " select id_corr, rut, fecha, correo from eje_ges_correo_confirmacion where rut = ?  ";
		
		Object[] params = {rut};

		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal",sql,params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public ConsultaData getConfirmacionCorreo(int rut, String correo) {
		String sql = " select id_corr, rut, fecha, correo from eje_ges_correo_confirmacion where rut = ? and correo = ? ";
		
		Object[] params = {rut, correo};

		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal",sql,params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public boolean updateTrabajador(int rut, int campoPos, String value)  {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" update eje_ges_trabajador SET ".concat(getTrabajadorColumnName(campoPos)).concat(" = ? where rut = ?"));
		
		Object[] params = {value,rut};

		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("portal",strConsulta.toString(),params) > 0 ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public boolean updateTrabajador(int rut, String fieldName, String value)  {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" update eje_ges_trabajador SET ".concat(fieldName).concat(" = ? where rut = ?"));
		
		Object[] params = {value,rut};

		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("portal",strConsulta.toString(),params) > 0 ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public String getTrabajadorColumnName(int id) {
		return columnName.get(id);
	}
	
	public boolean insertTrabajador(int rut, String digitoVer, String nombre,String nombres, String apePaterno, String apeMaterno,
								String cargo, String unidad, String dependencia,String fecIngreso, String centroCosto,String division,
								String mail) {
		
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" INSERT INTO eje_ges_trabajador (rut,digito_ver,nombre,nombres,ape_Paterno,ape_Materno, ");
		strConsulta.append(" cargo,unidad,dependencia,fecha_ingreso,ccosto,division, mail, periodo, ");
		strConsulta.append(" wp_cod_empresa, wp_cod_planta,wp_cod_sucursal)");
		strConsulta.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		
		String fecha = Formatear.getInstance().toAnotherDate(fecIngreso, "yyyyMMdd", "yyyyMMdd");
		Object[] params = {rut,digitoVer,
					Validar.getInstance().cutString(nombre, 50),
					Validar.getInstance().cutString(nombres, 20),
					Validar.getInstance().cutString(apePaterno, 20),
					Validar.getInstance().cutString(apeMaterno, 20),cargo,unidad,dependencia, fecha, centroCosto, division, mail,0,0,0,0};

		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().insert("portal",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public boolean existeTrabajador(int rut) {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" SELECT 1 FROM eje_ges_trabajador ");
		strConsulta.append(" WHERE rut = ? ");
		
		Object[] params = {rut};

		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data.next();
	}
	
	

	public boolean updateTrabajador(int rut, String digitoVer, String nombre,String nombres, String apePaterno, String apeMaterno,
									String cargo, String unidad, String dependencia,String fecIngreso, String centroCosto,String division,
									String mail)  {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" UPDATE eje_ges_trabajador ");
		strConsulta.append(" SET digito_ver = ?,nombre=?,nombres=?,ape_Paterno=?,ape_Materno=?, ");
		strConsulta.append(" 	cargo=?,unidad=?,dependencia=?,fecha_ingreso=?,ccosto=?,division=?, mail=? ");
		strConsulta.append(" WHERE  rut = ? ");
		
		String fecha = Formatear.getInstance().toAnotherDate(fecIngreso, "yyyyMMdd", "yyyyMMdd");
		Object[] params = {digitoVer, nombre, nombres, apePaterno, apeMaterno, cargo,
							unidad, dependencia, fecha, centroCosto, division, mail, rut};
		
		SimpleDateFormat format = new SimpleDateFormat();
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("portal",strConsulta.toString(),params) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public ConsultaData getTrabajadoresInUnidad(String unidad) {
		return getTrabajadores(null, unidad);
	}
	
	public ConsultaData getTrabajadores() {
		return getTrabajadores(null,null);
	}
	
	public ConsultaData getTrabajador(String rut) {
		return getTrabajadores(Validar.getInstance().validarInt(rut, -1),null);
	}
	
	public ConsultaData getTrabajador(int rut) {
		return getTrabajadores(rut,null);
	}
	
	public ConsultaData getTrabajadores(Integer rut, String unidad) {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" select t.rut,t.digito_ver,nombres=rtrim(ltrim(t.nombres)),ape_paterno=rtrim(ltrim(t.ape_paterno)),ape_materno=rtrim(ltrim(t.ape_materno)), \n");
		strConsulta.append(" 	convert(varchar,t.fecha_ingreso,106) as fecha_ingreso, \n");
		strConsulta.append(" 	isnull(u.estado,0) as es_encargado, \n");
		strConsulta.append(" 	'' as options, mail,e_mail, nombre=rtrim(ltrim(t.nombre)) \n");
		strConsulta.append(" from eje_ges_trabajador t \n");
		strConsulta.append(" 	left outer join eje_ges_unidad_encargado u on \n");
		strConsulta.append(" 					u.unid_id = t.unidad and t.rut = u.rut_encargado and u.estado = 1  \n");
		strConsulta.append(" WHERE 1 = 1 and t.rut <> 99999999  \n");
		
		ArrayList<Object> lista = new ArrayList<Object>();
		if(rut != null) {
			strConsulta.append(" and t.rut = ?");
			lista.add(rut);
		}
		
		if(unidad != null) {
			strConsulta.append(" and t.unidad = ?");
			lista.add(unidad);
		}
		
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal",strConsulta.toString(),lista.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	

	public boolean delTrabajador(int rut) {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" DELETE FROM eje_ges_trabajador WHERE rut = ?  ");
		
		Object[] params = {rut};
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("portal",strConsulta.toString(),params) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public boolean updateTrabajadorUnidad(int rut, String id) {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" UPDATE eje_ges_trabajador SET unidad = ? ");
		strConsulta.append(" WHERE rut = ?  ");
		
		Object[] params = {id,rut};
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("portal",strConsulta.toString(),params) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}

	public boolean updateContrasena(String newPassword, int rut) {
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
	

	public boolean insertContrasena(String newPassword, int rut) {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" INSERT INTO eje_ges_usuario ");
		strConsulta.append(" (login_usuario, password_usuario, rut_usuario, emp_rel, uni_rel, error,passw_cambiar,passw_ult_cambio, ");
		strConsulta.append(" tipo_rel,cant_ingresos,wp_cod_empresa,wp_cod_planta,md5)  ");
		strConsulta.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
		Object[] params = {rut,newPassword,rut,null,null,null,null,null,null,0,-1,-1,"N"};
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().insert("portal",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public boolean existeContrasena(int rut) {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" SELECT 1 FROM eje_ges_usuario WHERE login_usuario = ? ");
		
		Object[] params = {rut};

		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data != null && data.next();
	}
	


	public boolean insertConfirmacion(int rut, String mail) {
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append(" insert into eje_ges_correo_confirmacion (rut, fecha, correo) values (?,GETDATE(),?)");
		
		Object[] params = {rut,mail};
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().insert("portal",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}


}
