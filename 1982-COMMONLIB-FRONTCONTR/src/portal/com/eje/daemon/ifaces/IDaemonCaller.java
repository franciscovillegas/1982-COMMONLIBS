package portal.com.eje.daemon.ifaces;

import cl.eje.model.generic.portal.Eje_daemon_inscritos;

/**
 * Tiene un �nico m�todo, use usa para llamar a la clase
 * */
public interface IDaemonCaller {

	
	public void doActions(Eje_daemon_inscritos inscritos);
	
}
