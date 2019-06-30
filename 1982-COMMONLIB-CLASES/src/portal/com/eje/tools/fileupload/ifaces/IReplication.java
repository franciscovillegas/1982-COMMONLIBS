package portal.com.eje.tools.fileupload.ifaces;

import java.io.File;
import java.util.List;

import portal.com.eje.tools.fileupload.vo.EstadisticaReplicacion;

/**
 * Cambiado por su homologá en el paquete cl.ejedigital.web.
 * 
 * @deprecated 
 * 
 * */
public interface IReplication {

	public EstadisticaReplicacion replica(File fileOrigen);
	
	public int getCantArchivosCopiados();

	public int getCantArchivosConError();

	public List getRutas();
	
}
