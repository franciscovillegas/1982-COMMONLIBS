package portal.com.eje.daemon;

import java.sql.SQLException;
import java.util.Collection;

import cl.eje.model.generic.portal.Eje_daemon_grupos;
import cl.eje.model.generic.portal.Eje_daemon_inscritos;
import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.portal.factory.Ctr;
import portal.com.eje.portal.vo.VoTool;

public class DaemonCtr {

	public static DaemonCtr getInstance() {
		return Ctr.getInstance(DaemonCtr.class);
	}

	public Collection<Eje_daemon_grupos> getGrupos() throws NullPointerException, SQLException {
		return privateGetGrupos(null);
	}
	
	public Eje_daemon_grupos getGrupo(Integer idGrupo) throws NullPointerException, SQLException {
		Collection<Eje_daemon_grupos> vos =  privateGetGrupos(idGrupo);
		if(vos != null && vos.size() > 0) {
			 return vos.iterator().next();
		}
		else {
			return null;
		}
	}
	
	public Collection<Eje_daemon_grupos> privateGetGrupos(Integer idGrupo) throws NullPointerException, SQLException {
		ConsultaData data = DaemonManager.getInstance().getGrupos(idGrupo);

		return VoTool.getInstance().buildVo(data, Eje_daemon_grupos.class);

	}
	
	public Collection<Eje_daemon_inscritos> getDaemons() throws NullPointerException, SQLException {
		return privateGetDaemons(null);
	}
	
	public Eje_daemon_inscritos getDaemon(Integer idDaemon) throws NullPointerException, SQLException {
		Collection<Eje_daemon_inscritos> vos =  privateGetDaemons(idDaemon);
		if(vos != null && vos.size() > 0) {
			 return vos.iterator().next();
		}
		else {
			return null;
		}
	}
	
	public Collection<Eje_daemon_inscritos> privateGetDaemons(Integer idDaemon) throws NullPointerException, SQLException {
		ConsultaData data = DaemonManager.getInstance().getDaemon(idDaemon);

		return VoTool.getInstance().buildVo(data, Eje_daemon_inscritos.class);

	}
	
}
