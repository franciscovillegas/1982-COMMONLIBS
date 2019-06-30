package cl.eje.qsmcom.managers;

import java.sql.SQLException;

import cl.eje.qsmcom.service.GatewayJndi;
import cl.eje.qsmcom.service.GatewayManager;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;

public class ManagerCargo {
	private static ManagerCargo instance;
	
	private ManagerCargo() {
	}
	
	public static ManagerCargo getInstance() {
		if(instance == null) {
			synchronized (ManagerCargo.class) {
				if(instance == null) {
					instance = new ManagerCargo();
				}
			}
		}
		
		return instance;
	}
	
	public ConsultaData getCargos() {
		
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" SELECT c.cargo, c.descrip, c.caracteristicas, '' as opciones, p.id_plantilla, p.nombre, ");
		strConsulta.append(" 	p.fecha_creacion, p.path_excel, p.path_htm, p.path_img ");
		strConsulta.append(" FROM eje_ges_cargos c left outer join ").append(GatewayManager.getInstance().jndi(GatewayJndi.mac)).append("..eje_qsmcom_plantilla p on c.caracteristicas = p.id_plantilla "); 
		strConsulta.append(" WHERE c.vigente = 'S' ");
		
		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("portal",strConsulta.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	public boolean delCargo(String idCargo) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" DELETE eje_ges_cargos WHERE cargo = ? ");
		
		Object[] params = {idCargo};
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("portal",strConsulta.toString(),params) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public boolean updateCargo(String idCargo, String campo, Object value) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" UPDATE eje_ges_cargos SET ").append(campo).append("= ? WHERE cargo = ? ");
		
		Object[] params = {value,idCargo};
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().update("portal",strConsulta.toString(),params) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}

	public boolean insertCargo(String id, String nombre, String idPlantilla) {
		StringBuffer strConsulta = new StringBuffer();
		strConsulta.append(" INSERT INTO eje_ges_cargos ");
		strConsulta.append(" (empresa,cargo, descrip, caracteristicas,vigente) ");
		strConsulta.append(" VALUES (?,?,?,?,?) ");
		
		Object[] params = {0,id,nombre,idPlantilla,"S"};
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().insert("portal",strConsulta.toString(),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
}
