package portal.com.eje.portal.organica.ifaces;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.portal.organica.enums.DimensionCampoRef;

public interface IDimensionUtil {

	/**
	 * Agrega el valor de una determinada dimensión indicada por DimensionCampoRef a la ConsultaData entregada como parámetro.<br/>
	 * El método no hace nada si alguno de los parámetros es null o si no existe el campo en {@paramref data}<br/>
	 * El método solo agregará un valor de la dimensión, si dicha unidad tiene más de un valor se agregará el primero que se rescate.<br/>
	 * El campo de nombre (String)campoIdUnidad debe existir como parámetro en (ConsultaData) data.<br/>
	 * 
	 * 
	 * @author Pancho
	 * @since 24-08-2018
	 * @param data ConsultaData al que se le agregará la dimensión
	 * @param campoIdUnidad nombre del campo en data que tiene el id de la unidad
	 * @param ref referencia a la dimensión
	 * */
	public void addDimensionValorToConsultaData(ConsultaData retorno, String campoIdUnidad, DimensionCampoRef dimension);
	
}
