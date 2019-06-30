package portal.com.eje.daemon;

import java.sql.SQLException;
import java.util.List;

import cl.eje.model.generic.portal.Eje_daemon_grupos;
import cl.eje.model.generic.portal.Eje_daemon_inscritos;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Mng;
import portal.com.eje.portal.vo.CtrGeneric;
import portal.com.eje.portal.vo.util.Where;
import portal.com.eje.portal.vo.util.Wheres;

public class DaemonManager {

	public static DaemonManager getInstance() {
		return Mng.getInstance(DaemonManager.class);
	}
	
	public static DaemonCtr getCtr() {
		return DaemonCtr.getInstance();
	}
	
	public ConsultaData getGrupos(Integer idGrupo) throws NullPointerException, SQLException {
		
		List<Where> wheres = null;
		
		if(idGrupo != null) {
			wheres = (Wheres.where("id_grupo", "=", idGrupo).build());
		}
		
		return CtrGeneric.getInstance().getDataFromClass(Eje_daemon_grupos.class, wheres, null);
	}
	
	public ConsultaData getDaemon(Integer idDaemon) throws NullPointerException, SQLException {
		
		List<Where> wheres = null;
		
		if(idDaemon != null) {
			wheres = (Wheres.where("id_daemon", "=", idDaemon).build());
		}
		
		return CtrGeneric.getInstance().getDataFromClass(Eje_daemon_inscritos.class, wheres, null);
	}

	public boolean updateLastDateExecution(int id_daemon) throws SQLException {
		String sql = "UPDATE eje_daemon_inscritos SET ult_fecha_ejecucion = getdate() WHERE id_daemon = ? ";
		
		Object[] params = {id_daemon};
		ConsultaTool.getInstance().update("portal", sql, params);
		return true;
	}
	
	
	public boolean updateLastTimeExecution(int id_daemon, double timeInMilliseconds) throws SQLException {
		String sql = "UPDATE eje_daemon_inscritos SET ult_tiempo_ejecucion = ? WHERE id_daemon = ? ";
		
		Object[] params = {timeInMilliseconds, id_daemon};
		ConsultaTool.getInstance().update("portal", sql, params);
		return true;
	}

	public boolean delDaemon(int idDaemon) throws SQLException {
		String sql = "DELETE FROM eje_daemon_inscritos WHERE id_daemon = ? ";
		
		Object[] params = {idDaemon};
		ConsultaTool.getInstance().update("portal", sql, params);
		return true;
	}
}
