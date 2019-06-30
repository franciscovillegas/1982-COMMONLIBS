package portal.com.eje.genericconf.viewide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import portal.com.eje.genericconf.ifaces.IViewIde;
import portal.com.eje.portal.EModulos;

/**
 * @author Pancho
 * Específicamente para los Senchas
 * */
public class AbsViewIde implements IViewIde {
	private String ide;
	private String descripcion;
	private Date fechaCreacion;
	private String autor;
	private double version;
	private List<EModulos> listModulosPermitidos;
	
	public AbsViewIde() {
		listModulosPermitidos=new ArrayList<>();
		
		 
	}
	
	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	@Override
	public String getIde() {
		return ide;
	}
	
	@Override
	public void setIde(String ide) {
		this.ide = ide;
	}
	
	@Override
	public String getDescripcion() {
		return descripcion;
	}
	
	@Override
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	
	@Override
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	@Override
	public String getAutor() {
		return autor;
	}
	
	@Override
	public void setAutor(String autor) {
		this.autor = autor;
	}

	public List<EModulos> getListModulosPermitidos() {
		return listModulosPermitidos;
	}

	@Override
	public void addModuloPermitidos(EModulos modulo) {
		this.listModulosPermitidos.add(modulo);
		
	}
 

}
