package portal.com.eje.portal.organica.ifaces;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.portal.organica.enums.DimensionCampoRef;

public interface IDimensionUtil {

	/**
	 * Agrega el valor de una determinada dimensi�n indicada por DimensionCampoRef a la ConsultaData entregada como par�metro.<br/>
	 * El m�todo no hace nada si alguno de los par�metros es null o si no existe el campo en {@paramref data}<br/>
	 * El m�todo solo agregar� un valor de la dimensi�n, si dicha unidad tiene m�s de un valor se agregar� el primero que se rescate.<br/>
	 * El campo de nombre (String)campoIdUnidad debe existir como par�metro en (ConsultaData) data.<br/>
	 * 
	 * 
	 * @author Pancho
	 * @since 24-08-2018
	 * @param data ConsultaData al que se le agregar� la dimensi�n
	 * @param campoIdUnidad nombre del campo en data que tiene el id de la unidad
	 * @param ref referencia a la dimensi�n
	 * */
	public void addDimensionValorToConsultaData(ConsultaData retorno, String campoIdUnidad, DimensionCampoRef dimension);
	
}
