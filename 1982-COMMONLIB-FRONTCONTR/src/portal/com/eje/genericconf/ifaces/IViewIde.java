package portal.com.eje.genericconf.ifaces;

import java.util.Date;
import java.util.List;

import portal.com.eje.portal.EModulos;

public interface IViewIde {

	public List<EModulos> getListModulosPermitidos() ;

	public void addModuloPermitidos(EModulos modulo);
	
	public String getIde();
	
	public void setIde(String ide);
	
	public String getDescripcion();
	
	public void setDescripcion(String descripcion);
	
	public Date getFechaCreacion();
	
	public void setFechaCreacion(Date fechaCreacion);
	
	public String getAutor();
	
	public void setAutor(String autor);
}
