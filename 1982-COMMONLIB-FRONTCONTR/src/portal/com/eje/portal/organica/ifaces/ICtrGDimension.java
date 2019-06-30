package portal.com.eje.portal.organica.ifaces;

import java.util.Collection;

import portal.com.eje.portal.organica.enums.DimensionCampoRef;
import portal.com.eje.portal.organica.vo.VoDimension;
import portal.com.eje.portal.organica.vo.VoDimensionValor;

public interface ICtrGDimension {

	public Collection<VoDimension> getDimensiones();
	
	/**
	 * 
	 * Deprecado porque se debe llamar por el campoRef
	 * 
	 * @deprecated
	 * @author Pancho
	 * @since 03-06-2018
	 * */
	public VoDimension getDimension(int idDimension);
	
	public VoDimension getDimension(DimensionCampoRef campoRef);

	/**
	 * 
	 * Deprecado porque se debe llamar por el campoRef
	 * 
	 * @deprecated
	 * @author Pancho
	 * @since 03-06-2018
	 * */
	public Collection<VoDimensionValor> getValores(Integer dimensionID);
	
	public Collection<VoDimensionValor> getValores(DimensionCampoRef campoRef);
	
	/**
	 * 
	 * Deprecado porque se debe llamar por el campoRef
	 * 
	 * @author Pancho
	 * @since 03-06-2018
	 * */
	public Collection<VoDimensionValor> getValores(String unidId, Integer dimensionID);
	
	public Collection<VoDimensionValor> getValores(String unidId, DimensionCampoRef campoRef);
}
