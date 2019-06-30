package portal.com.eje.portal.parametro;

import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;

public class ParametroHelper {

	public boolean existeContexto(String contexto) {
		ConsultaData data = null;
		
		if(contexto != null) {
			String sql = "SELECT * FROM eje_generico_cliente where ltrim(rtrim(contexto)) = ? and isnull(vigente,1) = 1 ";
			try {
				Object[] params = {contexto};
				data = ConsultaTool.getInstance().getData("portal", sql, params);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return data!= null && data.next();
	}
	
	public String normalizaContexto(String contexto) {
		StringBuilder url = new StringBuilder("/");
		
		if(contexto != null) {
			String[] params = contexto.split("/");
		
			for(String p: params) {
				if(p != null && !"".equals(p.trim())) {
					url.append(p);
				}
			}
		}
		
		return url.toString();
	}
}
