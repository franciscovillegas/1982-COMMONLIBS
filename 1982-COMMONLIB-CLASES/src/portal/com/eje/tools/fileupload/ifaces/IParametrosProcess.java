package portal.com.eje.tools.fileupload.ifaces;

import java.util.HashMap;
import java.util.List;

/**
 * Cambiado por su homologá en el paquete cl.ejedigital.web.
 * 
 * @deprecated 
 * 
 * */

public interface IParametrosProcess {

	public void readParametros();
	public HashMap getParametros();
	public List getFiles();
	public String getRelativePath();
	
}
