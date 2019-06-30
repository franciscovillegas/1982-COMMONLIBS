package portal.com.eje.portal.organica.ifaces;

import cl.ejedigital.consultor.ConsultaData;

public interface IEncargadoRelativo {

	/**
	 * Retornará rut, unid_id de todas las personas que dependen del rut que se pasó como parámetro. La depencia es relativa.
	 * @author Pancho
	 * @since 22-06-2018
	 * */
	public ConsultaData getTrabajadoresDependientes(int rut);
	
	
	/**
	 * Retorna todas las unidades de la cuales una persona es JefeRelativo
	 * @author Pancho
	 * @since 22-06-2018
	 * */
	public ConsultaData getUnidadesEncargadas(int intPersona, boolean incluyeUnidadesDescendientes);
	
	/**
	 * Retorna todas las personas (rut, unidad) que son jefes relativos a partir de la unidad entregada. Dicho de otra forma
	 * busca a todos los jefes relativos debajo de la unidad indicada. <br/>
	 * 
	 * Si fromUnidad == null?, buscará en toda la organica<br/>
	 * 
	 * */
	public ConsultaData getJefaturas(String fromUnidad, boolean incluyeUnidadesDescendientes);
}
