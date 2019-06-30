package portal.com.eje.tools.instalable;

import java.util.Date;

public interface IModuloInstalable {

	public IDesinstalable getDesinstalador();
	
	public IInstalable getInstalador();
	
	public String getDescripcion();
	
	public Date getFecCreacion();
	
	public String getAutor();
}
