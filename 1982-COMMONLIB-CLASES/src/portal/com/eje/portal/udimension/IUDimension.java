package portal.com.eje.portal.udimension;

import java.util.List;

import cl.ejedigital.consultor.ConsultaData;

public interface IUDimension {
	
	public List<VoUTipoDimension> getTipoDimension();
	
	public List<VoUTipoDimension> getTipoDimension(int intIdTipoDimension);
	
	public List<VoUTipoDimension> getTipoDimension(int intIdTipoDimension, String strFiltro);
	
	public VoUTipoDimension getTipoDimension(VoUDimension dimension);
	
	public List<VoUDimension> getDimensiones(int intIdDimension, String strUnidad);
	
	public List<VoUDimension> getDimensiones(int intIdDimension, String strUnidad, String strFiltro);
	
	public List<VoUDimension> getDimensiones();
	
	public List<VoUDimension> getDimensiones(int intIdDimension);
	
	public List<VoUDimension> getDimensiones(String strUnidad);
	
	public boolean updDimension(VoUDimension dimension);
	
	public boolean delDimension(VoUDimension dimension);
	
	public ConsultaData getValores(int intIdDimension);
	
	public ConsultaData getValores(int intIdDimension, String strUnidad);
	
	public ConsultaData getValores(int intIdDimension, String strUnidad, String strFiltro);
	
	public boolean updValores(VoUDimension dimension);

	public boolean updValores(int intIdDimension, List<VoUDValor> valores);
	
	public boolean updDimensionUnidad(String strIdUnidad, List<VoUDimension> dimensiones);
	
	public boolean updDimensionUnidad(String strIdUnidad, List<VoUDimension> dimensiones, boolean bolSubUnidades);

	/**
	 * 
	 * Crea un nuevo valor y retorna su VoUDvalor generado
	 * @since 23-08-2018
	 * @author Pancho
	 * 
	 */
	public VoUDValor newItem(int intIdDimension, String valor, boolean activo);
	
}
