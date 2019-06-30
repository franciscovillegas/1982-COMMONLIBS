package portal.com.eje.portal.organica;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.organica.ifaces.IEncargadoRelativo;

/**
 * Resulve todo lo relacionado con las unidades y la obtención de valores
 * */
public class OrganicaLocator {

	/**
	 * Obtiene un resolutor de dependencias, jefaturas y relaciones entre unidades
	 * @since 30-10-2018
	 * */
	public static IOrganica getInstance() {		 
		return Util.getInstance(Organica.class);
	}
	
	/**
	 * Instancia rápida de Organica, solo retornará los datos justos, sin adicionales.
	 * Por favor mantener así
	 * @author Pancho
	 * @since 21-06-2018
	 * 
	 * */
	public static IOrganica getFastestInstance() {
		return Util.getInstance(FastestOrganica.class);
	}
 
	/**
	 * Resuelve las dimensiones por unidades
	 * 
	 * @since 30-10-2018
	 * */
	public static IDimension getInstanceDimension() {
		return Util.getInstance(OrganicaPorDimension.class);
	}
 	
	/**
	 * Permite resolver todo lo relacionado con dependencias en encargados relativos
	 * @since 30-10-2018
	 * */
	public static IEncargadoRelativo getEncargadoRelativoInstance() {
		return EncargadoRelativaLocator.getInstance();
	}
}
 