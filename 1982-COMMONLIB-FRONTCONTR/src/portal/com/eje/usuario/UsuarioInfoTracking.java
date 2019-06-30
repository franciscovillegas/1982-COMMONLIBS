package portal.com.eje.usuario;

import java.io.Serializable;
import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.web.datos.ConsultaTool;

public class UsuarioInfoTracking implements Serializable {
	private static UsuarioInfoTracking instance;
	private Cronometro cro;
	private ConsultaData data;
	
	private StringBuilder sql;
	
	private UsuarioInfoTracking() {
		cro = new Cronometro();
		cro.start();
		
		sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" 	(SELECT COUNT(DISTINCT RUT) FROM EJE_GES_TRACKING ) AS INGRESOS_SIEMPRE, ");
		sql.append(" 	(SELECT COUNT(DISTINCT RUT) FROM EJE_GES_TRACKING   WHERE CONVERT(VARCHAR(8), FECHA, 112) = CONVERT(VARCHAR(8), GETDATE(), 112) ) AS INGRESOS_HOY, ");
		sql.append(" 	(SELECT COUNT(DISTINCT RUT) FROM EJE_GES_TRABAJADOR WHERE SUBSTRING( CONVERT(VARCHAR(8), FECHA_NACIM, 112), 5, 6) =SUBSTRING( CONVERT(VARCHAR(8), GETDATE(), 112), 5, 6) ) AS CUMPL_HOY "); 
		
	}
	
	public static UsuarioInfoTracking getInstance() {
		if(instance == null) {
			synchronized (UsuarioInfoTracking.class) {
				if(instance == null) {
					instance = new UsuarioInfoTracking();
				}
			}	
		}
		
		return instance;
	}
		
	public DataFields getDataTracking() {
		if( data == null || cro.GetMilliseconds() >= 20000) {
			synchronized (UsuarioInfoTracking.class) {
				if( data == null || cro.GetMilliseconds() >= 20000) {
					try {
						data = ConsultaTool.getInstance().getData("portal", sql.toString());
						if( data != null ) {
							data.next();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if(data != null) {
			return data.getActualData(); 
		}
		else {
			return null;
		}
	}
}
