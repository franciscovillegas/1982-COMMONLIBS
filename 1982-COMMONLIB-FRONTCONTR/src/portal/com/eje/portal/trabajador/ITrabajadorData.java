package portal.com.eje.portal.trabajador;

import portal.com.eje.tools.ArrayFactory;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataPaged;

public interface ITrabajadorData {

	/**
	 * PRECAUCIÓN, solo se debe ocupar este método si se va a llamar hasta 10 personas en un loop o secuencia de código, más que esto se se pondrá lento. 
	 * 
	 * @since 2015-08-21
	 * @see getDataFiltrada
	 * @author Francisco
	 * */
	public ConsultaData getDataPersonal(int rut);
	
	/**
	 * Retorna la data de una trabajador según filtro
	 * 
	 * */
	public ConsultaDataPaged getDataFiltrada(TrabajadorDataFiltro filtro);
	
	/**
	 * Retorna la data de una trabajador según filtro
	 * */
	public ConsultaData getDataFiltrada(TrabajadorDataFiltro filtro, int rut);
	
	/**
	 * Retorna la data de una trabajador según filtro
	 * */
	public ConsultaData getDataFiltrada(TrabajadorDataFiltro filtro, ArrayFactory ruts);
	
	public ConsultaData getDataFiltrada(TrabajadorDataFiltro filtro, cl.ejedigital.tool.strings.ArrayFactory ruts);

	/**
	 * @since 2016-nov-10
	 * */
	public boolean createOrUpdatePassword(int rut, String clave);


	
	
}
