package portal.com.eje.portal.organica;

import java.sql.Connection;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.portal.organica.ifaces.ICtrGOrganica;
import portal.com.eje.portal.trabajador.enums.EjeGesTrabajadorField;

/**
 * @since 2015-05-28
 * @author Francisco Villegas
 * */

public interface IOrganica {
	
	/**
	 * Retorna rut, nombre, cargo y empresa del jefe de la unidad indicada, si dicha
	 * unidad no tiene un jefe asignado retornará null.
	 * @since 2015-05-28
	 * @author Francisco Villegas
	 * */
	public ConsultaData getJefeUnidad(String unidad);
	
	/**
	 * Retorna rut, nombre, cargo y empresa del jefe de la unidad indicada, si dicha
	 * unidad no tiene un jefe asignado retornará null.
	 * @since 2015-05-28
	 * @author Francisco Villegas
	 * */
	public ConsultaData getJefeUnidad(List<String> unidades);
	
	/**
	 * Retorna el rut, nombre, cargo y empresa del jefe que controla la unidad indicada, el jefe podría estar en esta unidad o en unidades ascendentes. <br/>
	 * Si unidad = null retorna nullpointer <br/>
	 * Se da por entendido que la unidad es vigente, nunca un trabajador podrá entar en una unidad no vigente. <br/>
	 * 
	 * @since 2015-05-28
	 * @author Francisco Villegas
	 * */
	public ConsultaData getJefeResponsableDeLaUnidad(String unidad);

	
	/**
	 * Retorna el rut, nombre, cargo y empresa del jefe que controla al trabajador indicado
	 * Como las personas solo pueden estar en unidades vigentes, se da por entendido que la unidad está vigente. <br/>
	 * Se da por hecho que el trabajador está en unidades vigentes, dado que es imposible que esté el unidades no vigentes
	 * 
	 * @since 2015-05-28
	 * @author Francisco Villegas
	 * */
	public ConsultaData getJefeDelTrabajador(int rut);
	
	/**
	 * Retorna el rut, nombre, cargo y empresa del jefe que controla al trabajador indicado
	 * Como las personas solo pueden estar en unidades vigentes, se da por entendido que la unidad está vigente. <br/>
	 * Se da por hecho que el trabajador está en unidades vigentes, dado que es imposible que esté el unidades no vigentes
	 * 
	 * @since 31-10-2018
	 * @author Francisco Villegas
	 * */
	public ConsultaData getJefeDelTrabajador(String rut);
	
	/**
	 * Retorna el codigo de unidad de las unidades descendientes <br/>
	 * IMPORTANTE: Solo son consideradas las unidades vigentes (MOD: 31-10-2018)<br/>
	 * unidad = null retornará NullPointerException  <br/>
	 * 
	 * @since 2015-05-28
	 * @author Francisco Villegas
	 * @return {unid_id, unidad, unid_desc}
	 * 
	 * */
	public ConsultaData getUnidadesDescendientes(String unidad);
	
	/**
	 * Retorna el codigo de unidad de las unidades descendientes <br/>
	 * Si connPortal == null entonces se considera el JNDI portal <br/>
	 * IMPORTANTE: opcionalmente se pueden mostrar los no vigentes (MOD: 31-10-2018)<br/>
	 * unidad = null retornará NullPointerException  <br/>
	 * El orden de mostrado es de arriba hacia abajo
	 * 
	 * @since 2018-10-30
	 * @author Francisco Villegas
	 * @return {unid_id, unidad, unid_desc}
	 * 
	 * */
	public ConsultaData getUnidadesDescendientes(Connection connPortal, String unidad, boolean incluyeNoVigentes);
	
	/**
	 * Retorna el codigo de unidad de las unidades descendientes <br/>
	 * IMPORTANTE: solo son consideradas las unidades vigentes  (MOD: 31-10-2018)<br/>
	 * Unidad no puede ser null
	 * unidad = null retornará NullPointerException  <br/>
	 * El orden de mostrado es de arriba hacia abajo
	 * 
	 * @since 2015-05-28
	 * @author Francisco Villegas
	 * */
	public ConsultaData getUnidadesAscendientes(String unidad);
	
	/**
	 * Retorna el codigo de unidad de las unidades Ascendientes <br/>
	 * Si connPortal == null entonces se considera el JNDI portal <br/>
	 * IMPORTANTE: opcionalmente se pueden mostrar los no vigentes  (MOD: 31-10-2018)<br/>
	 * unidad = null retornará NullPointerException  <br/>
	 * Los retornará ordenados, desde la unidad soleccionada hasta el tope de la unidad (unidad=0) <br/> 
	 * 
	 * @author Pancho
	 * @since 31-10-2018
	 * */
	public ConsultaData getUnidadesAscendientes(Connection connPortal, String unidad, boolean incluyeNoVigentes);
	
	/**
	 * Retorna todos los ruts que están en una unidad 
	 * @since 2015-05-28
	 * @author Francisco Villegas
	 * */
	public ConsultaData getTrabajadoresInUnidad(String unidad);
	
	
	/**
	 * Retorna los ruts que están en una unidad, con al posibilidad de omitir a las jefaturas 
	 * @since 2015-05-28
	 * @update 2015-08-14
	 * @author Francisco Villegas
	 * */
	public ConsultaData getTrabajadoresInUnidad(String unidad, boolean omiteJefe);
	
	/**
	 * 
	 * @author Marcelo
	 * */
	public ConsultaData getTrabajadoresInUnidad(String strUnidad, List<EjeGesTrabajadorField> fields);
	/**
	 * Retorna la información especificada en fields 
	 * @since 2018-10-24
	 * @update 2018-10-24
	 * @author Marcelo Madrid
	 * */
//	public ConsultaData getTrabajadoresInUnidad(String strUnidad, List<EjeGesTrabajadorField> fields);
	
	/**
	 * Retorna los ruts de todas personas que estan en las unidades dependientes.
	 * 
	 * @since 2015-05-28
	 * @author Francisco Villegas
	 * @throws UnidadNotFoundException 
	 * */
	public ConsultaData getTrabajadoresDependientes(String unidad, boolean omiteEstaUnidad);
	
	
	/**
	 * Retorna los ruts de todas personas que dependen del rut, solo si este es jefe
	 * 
	 * @since 2015-05-28
	 * @author Francisco Villegas
	 * @throws UnidadNotFoundException 
	 * */
	public ConsultaData getTrabajadoresDependientes(int rut);
	
	/**
	 * Retorna los ruts de todas personas que dependen del rut, solo si este es jefe
	 * 
	 * @since 2015-05-28
	 * @author Francisco Villegas
	 * @throws UnidadNotFoundException 
	 * */
	public ConsultaData getTrabajadoresDependientes(String unidad);
	
	/**
	 * Retorna los ruts de todos los jefes que estan en las unidades dependientes
	 * @since 2015-05-28
	 * @author Francisco Villegas
	 * */
	public ConsultaData getJefesDependientes(String unidad, boolean omiteEstaUnidad);
	
	/**
	 * Retorna los ruts de todas las personas que reportan a una determinada unidad, considera tres grupos<br/>
	 * 
	 * <ul>
	 * 		<li>Toda las personas dentro de la misma unidad que no son jefes.</>
	 *  	<li>Todas los jefes que dependen de esta unidad.</li>
	 *  	<li>Todos las personas en unidades dependendientes que no tienen jefe en la relación intermedia.</li>
	 * </ul>
	 * @since 2015-05-28
	 * @author Francisco Villegas
	 * */
	public ConsultaData getTrabajadoresReporteadores(String unidad); 
	
	/**
	 * Retorna los ruts de todas las personas que reportan a la unidad de un determinado jefe<br/>
	 * 
	 * <ul>
	 * 		<li>Toda las personas dentro de la misma unidad que no son jefes.</>
	 *  	<li>Todas los jefes que dependen de esta unidad.</li>
	 *  	<li>Todos las personas en unidades dependendientes que no tienen jefe en la relación intermedia.</li>
	 * </ul>
	 * @since 2015-05-28
	 * @author Francisco Villegas
	 * */
	public ConsultaData getTrabajadoresReporteadores(int rut); 
	
	/**
	 * Retorna información de los trabajadores
	 * @since 2017-12-06
	 * @author Marcelo Madrid
	 * */	
	public ConsultaData getTrabajadores(); 
	
	/**
	 * Retorna información del trabajador
	 * 
	 * @since 2017-12-06
	 * @author Marcelo Madrid

	 * */	
	public ConsultaData getTrabajadores(int intIdPersona); 
	
	
	/**
	 * Retorna información de los trabajadores filtrados <br/>
	 * 
	 * @since 2017-12-06
	 * @author Marcelo Madrid
	 * */	
	public ConsultaData getTrabajadores(String strFiltro); 
	
	/**
	 * Retorna las unidades directamente dependientes, solo entregará la lista de unidades del siguiente nivel
	 * @since 2015-05-28
	 * @author Francisco Villegas
	 * */
	
	public ConsultaData getUnidadesHijas(String unidad);
	
	/**
	 * Retorna las unidades directamente dependientes, solo entregará la lista de unidades del siguiente nivel.<br/>
	 * puede incluir las vigentes así como omitirlas, usar parámetro incluyeNoVigentes<br/>
	 * 
	 * @since 2018-11-14
	 * @author Francisco Villegas
	 * */
	
	public ConsultaData getUnidadesHijas(String unidad, boolean incluyeNoVigentes);

	/**
	 * Retorna todas las unidades vigentes de la orgánica
	 * @since 2016-08-29
	 * @author Francisco Villegas
	 * */
	public ConsultaData getUnidades();
	
	/**
	 * Retorna todas las unidades vigentes de la orgánica según filtro indicado
	 * @since 2016-08-29
	 * @author Francisco Villegas
	 * */
	public ConsultaData getUnidades(String strFiltro);
	
	/**
	 * Retorna todas las unidades padres, en el caso de no encontrar ninguna unidad padre retornará un null
	 * */
	public ConsultaData getUnidadesPadres(String unidad);
	
	/**
	 * Retorna la unidad raíz
	 * */
	public ConsultaData getUnidadRaiz();
	
	/**
	 * Retorna todos los nodos raices, que son aquellos cuyos nodo_padre es null o 0
	 * */
	public List<String> getUnidadesRaices();
	
	
	/**
	 * Retorna esta unidad <br/>
	 * Unidad no puede ser null <br/>
	 * Da lo mismo si es o no vigente, de igual forma retorna la INFO de la unidad <br/>
	 * */
	public ConsultaData getUnidad(String unidad);
	
	/**
	 * Retorna información de la lista de unidades <br/>
	 * List<String> listaDeUnidId no pueder null o tener 0 valores <br/>
	 * Si connPortal == null entonces se considera el JNDI portal <br/>
	 * Da lo mismo si la unidad es o no vigente, siempre retornará la información de la unidad si esta existe. <br/>
	 * 
	 * @author Pancho
	 * @since 30-10-2018
	 * */
	public ConsultaData getUnidad(Connection connPortal, List<String> listaDeUnidId);
	
	/**
	 * Retorna esta unidad
	 * */
	public ConsultaData getUnidadFromRut(int rut);
	
	/**
	 * Retorna todas las uniaddes de las cuales la persona es encargada
	 * */
	public ConsultaData getUnidadesEncargadas(int intPersona);
	
	/**
	 * Retorna las uniaddes de las cuales la persona es encargada filtrando por vigencia
	 * */
	public ConsultaData getUnidadesEncargadas(int intPersona, Integer intVigente);
	
	public boolean isJefe(int rut);

	/**
	 * Entrega el controlador de la orgánica.<br/>
	 * No sirve mucho debido a que como la implementación de la orgánica varia,<br/>
	 *  muchas veces se ocupa un VO que tiene más campos de los que debiera tener, asi que se pierde espacio
	 * 
	 * @since 25-06-2018
	 * */
	public ICtrGOrganica getCtrG();
}


