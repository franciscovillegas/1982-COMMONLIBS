package cl.eje.qsmcom.managers;

import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.fileupload.vo.EjeFileUnicoTipo;

public class ManagerUpload {
	private static ManagerUpload instance;
	
	public static ManagerUpload getInstance() {
		if(instance == null) {
			synchronized (ManagerQSM.class) {
				if(instance == null) {
					instance = new ManagerUpload();
				}
			}
		}
		
		return instance;
	}
	
	
	public ConsultaData getRegistroCargas(EjeFileUnicoTipo f) {
		String sql = " SELECT u.*, t.nombre from EJE_FILES_UNICO U LEFT OUTER JOIN eje_ges_trabajador T on u.RUT_SUBIDA = t.rut WHERE ID_TIPO = ? ";
		
		Object[] params = {f.getId()};

		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal",sql,params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
}
