package portal.com.eje.portal.trabajador;

import portal.com.eje.tools.ArrayFactory;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataPaged;

public interface ITrabajadorData {

	/**
	 * PRECAUCI�N, solo se debe ocupar este m�todo si se va a llamar hasta 10 personas en un loop o secuencia de c�digo, m�s que esto se se pondr� lento. 
	 * 
	 * @since 2015-08-21
	 * @see getDataFiltrada
	 * @author Francisco
	 * */
	public ConsultaData getDataPersonal(int rut);
	
	/**
	 * Retorna la data de una trabajador seg�n filtro
	 * 
	 * */
	public ConsultaDataPaged getDataFiltrada(TrabajadorDataFiltro filtro);
	
	/**
	 * Retorna la data de una trabajador seg�n filtro
	 * */
	public ConsultaData getDataFiltrada(TrabajadorDataFiltro filtro, int rut);
	
	/**
	 * Retorna la data de una trabajador seg�n filtro
	 * */
	public ConsultaData getDataFiltrada(TrabajadorDataFiltro filtro, ArrayFactory ruts);
	
	public ConsultaData getDataFiltrada(TrabajadorDataFiltro filtro, cl.ejedigital.tool.strings.ArrayFactory ruts);

	/**
	 * @since 2016-nov-10
	 * */
	public boolean createOrUpdatePassword(int rut, String clave);


	
	
}
