package cl.ejedigital.web.fileupload.ifaces;

import java.io.File;
import java.util.List;

import cl.ejedigital.web.fileupload.vo.EstadisticaReplicacion;

public interface IReplication {

	public EstadisticaReplicacion replica(File fileOrigen);
	
	public int getCantArchivosCopiados();

	public int getCantArchivosConError();

	public List getRutas();
	
}
