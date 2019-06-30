package portal.com.eje.portal.perfil;

import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.organica.OrganicaLocator;

public class Perfil implements IPerfil {

	/**
	 * La persona debe tener un solo perfil, mas de uno podría acarrear problemas.
	 * */
	@Override
	public ConsultaData getAtribuciones(int rut) {		
		 
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct ");
		sql.append("	rut=").append(rut).append(", ");
		sql.append("	p.id, ");
		sql.append("	p.nombre, ");
		sql.append("	p.javascript_reporte, ");
		sql.append(" 	cast(op1 as int) op1, ");
		sql.append(" 	cast(op2 as int) op2, ");
		sql.append(" 	cast(op3 as int) op3, ");
		sql.append(" 	cast(op4 as int) op4, ");
		sql.append(" 	cast(op5 as int) op5, ");
		sql.append(" 	cast(op6 as int) op6, ");
		sql.append(" 	cast(op7 as int) op7,  ");
		sql.append(" 	cast(op8 as int) op8,  ");
		sql.append(" 	cast(op9 as int) op9,  ");
		sql.append(" 	cast(op10 as int) op10,  ");
		
		sql.append(" 	cast(miestruc_op1 as int) miestruc_op1, ");
		sql.append(" 	cast(miestruc_op2 as int) miestruc_op2, ");
		sql.append(" 	cast(miestruc_op3 as int) miestruc_op3, ");
		sql.append(" 	cast(miestruc_op4 as int) miestruc_op4, ");
		sql.append(" 	cast(miestruc_op5 as int) miestruc_op5, ");
		sql.append(" 	cast(miestruc_op6 as int) miestruc_op6, ");
		sql.append(" 	cast(miestruc_op7 as int) miestruc_op7,  ");
		sql.append(" 	cast(miestruc_op8 as int) miestruc_op8,  ");
		sql.append(" 	cast(miestruc_op9 as int) miestruc_op9,  ");
		sql.append(" 	cast(miestruc_op10 as int) miestruc_op10  ");
		
		sql.append(" from eje_generico_perfiles_webmatico p ");
		sql.append(" WHERE p.id = ?  ");
 
		ConsultaData data = null;
		
		try {
			ConsultaData dataPerfiles = getPerfiles(rut);
			if(dataPerfiles != null && dataPerfiles.next()) {
				
				Object[] params = {dataPerfiles.getInt("id")};
				data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
				
				if(data != null && data.size() > 0) {
					return data;
				}	
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return data;
	}

	@Override
	public boolean isVerTodo(int rut) {
		ConsultaData data = getAtribuciones(rut);
		
		while(data != null && data.next()) {
			if("Administrador".equals(data.getString("nombre"))  ||
			   "1.- Perfil General Gerencia Felicidad".equals(data.getString("nombre"))	||
			   "2.- Perfil Gtes Y Sgtes Gerencia Felicidad".equals(data.getString("nombre")) ||
			   "3.- Perfil Explotador".equals(data.getString("nombre")) ||
			   "4.- Perfil Administrador".equals(data.getString("nombre")) ||
			   "5.- Perfil Administrador Estructura".equals(data.getString("nombre")) ||
			   "6.- Perfil Jefe De Proyecto".equals(data.getString("nombre")) ) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public ConsultaData getWebmaticoAtribuciones(int rut) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.id_corr,a.id_perfil,a.imagen_src,a.imagen_visibilidad \n");
		sql.append(" from eje_generico_webmatico_acceso a  \n");
		sql.append(" where a.id_perfil = ? ");
		
		ConsultaData data = null;
		
		try {
			ConsultaData dataPerfiles = getPerfiles(rut);
			if(dataPerfiles != null && dataPerfiles.next()) {
				Object[] params = {dataPerfiles.getInt("id")};
				data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
				
				if(data != null && data.size() > 0) {
					data.toStart();
					return data;
				}	
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * retorna los perfiles ordenados por id
	 * 
	 * @since 16-05-2018
	 * @author Pancho 
	 * */
	public ConsultaData getPerfiles(int rut) {
		ConsultaData data = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select @top id,w.nombre,costos,dotacion,cargos,tipo_contrato,costos_empresa,ingresos,egresos,resumen_in_out,saldo_vacaciones,estudios,grupo_familiar \n");
		sql.append(" from eje_generico_perfiles_rut_webmatico r \n");
		sql.append(" 					inner join eje_generico_perfiles_webmatico w on r.perfil = w.id \n");
		
		String whereRut = (" where r.rut = "+rut+"\n");
		String whereDefault = (" where w.is_default = 1 \n");
		
		try {
			 
			data = ConsultaTool.getInstance().getData("portal", sql.toString().replaceAll("@top", "") + whereRut +" order by id desc");
		
			if(data == null || data.size() == 0) {
				data = ConsultaTool.getInstance().getData("portal", sql.toString().replaceAll("@top", "top 1") + whereDefault +" order by id desc");
			}
		
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return data;
		
	}
	
}
