package portal.com.eje.portal.organica;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.portal.organica.enums.DimensionCampoRef;
import portal.com.eje.portal.organica.ifaces.ICtrGDimension;
import portal.com.eje.portal.organica.ifaces.IDimensionUtil;

/**
 * @since 2016-11-04
 * @author Francisco
 * */

public interface IDimension {
	
	/**
	 * Retornará un ConsultaData con la información de la dimensión de una unidad.
	 * @return
	 * 	<ul>
	 * 		<li>dimension_id : id de la dimensión</li>
	 * 		<li>dimension_desc</li>
	 * 		<li>ditem_id : id del valor </li>
	 * 		<li>ditem_desc</li>
	 *  </ul>
	 *  
	 *  
	 * */
	public ConsultaData getDimensionFromUnidad(String unidad);

	/**
	 * Retornará un ConsultaData con la información de la dimensión a la que pertenece la unidad en la cual reside la persona en cuestión
	 * Retorna: <br/>
	 * 	<ul>
	 * 		<li>dimension_id</li>
	 * 		<li>dimension_desc</li>
	 * 		<li>ditem_id</li>
	 * 		<li>ditem_desc</li>
	 *  </ul>
	 * */
	public ConsultaData getDimensionFromRut(int rut);
	
	/**
	 * Retornará un ConsultaData con la información de todas las dimensiones
	 * Retorna: <br/>
	 * 	<ul>
	 * 		<li>dimension_id</li>
	 * 		<li>dimension_desc</li>
	 * 		<li>ditem_id</li>
	 * 		<li>ditem_desc</li>
	 *  </ul>
	 * */
	public ConsultaData getDimensiones();
	
	
	/**
	 * Retornará un ConsultaData con la información de una dimensión en particular.
	 * Retorna: <br/>
	 * 	<ul>
	 * 		<li>dimension_id</li>
	 * 		<li>dimension_desc</li>
	 * 		<li>dimension_tipo</li>
	 * 		<li>dimension_cardinalidad</li>
	 * 		<li>camporef</li>
	 *  </ul>
	 *  
	 *  deprectado porque se debe llamar por el camporef y no por el id
	 *  
	 * @author Pancho
	 * @since 03-07-2018
	 * @deprecated 
	 * */
	public ConsultaData getDimension(Integer dimensionID);
	
	/**
	 * Retornará un ConsultaData con la información de una dimensión en particular.
	 * Retorna: <br/>
	 * 	<ul>
	 * 		<li>dimension_id</li>
	 * 		<li>dimension_desc</li>
	 * 		<li>dimension_tipo</li>
	 * 		<li>dimension_cardinalidad</li>
	 * 		<li>camporef</li>
	 *  </ul>
	 *  
	 *  
	 * @author Pancho
	 * @since 03-07-2018
	 * */
	public ConsultaData getDimension(DimensionCampoRef campoRef);
	
	/**
	 * Retornará un ConsultaData con la información de la dimensión a la pertenece el valor indicado
	 * Retorna: <br/>
	 * 	<ul>
	 * 		<li>dimension_id</li>
	 * 		<li>dimension_desc</li>
	 * 		<li>ditem_id</li>
	 * 		<li>ditem_desc</li>
	 *  </ul>
	 * */
	public ConsultaData getDimensionFromValor(String ditemID);
	
	/**
	 * Retornará un ConsultaData con la información de todos los valores de una dimensión
	 * Retorna: <br/>
	 * 	<ul>
	 * 		<li>dimension_id</li>
	 * 		<li>dimension_desc</li>
	 * 		<li>dimension_tipo</li>
	 * 		<li>dimension_cardinalidad</li>
	 * 		<li>campo_ref</li>
	 *  </ul>
	 *  deprecado por que se debe llamar por el camporef de la dimensión<br/><br/>
	 *  
	 * @author Pancho
	 * @since 03-07-2018
	 * @deprecated
	 * */
	public ConsultaData getValores(Integer dimensionID);
	
	/**
	 * Retornará un ConsultaData con la información de todos los valores de una dimensión
	 * Retorna: <br/>
	 * 	<ul>
	 * 		<li>dimension_id</li>
	 * 		<li>dimension_desc</li>
	 * 		<li>dimension_tipo</li>
	 * 		<li>dimension_cardinalidad</li>
	 * 		<li>campo_ref</li>
	 *  </ul>
	 *  
	 * @author Pancho
	 * @since 03-07-2018
	 * */
	public ConsultaData getValores(DimensionCampoRef campoRef);
	
	
	/**
	 * Retornará un ConsultaData con la información de todos los valores de una dimensión
	 * Retorna: <br/>
	 * 	<ul>
	 * 		<li>dimension_id</li>
	 * 		<li>dimension_desc</li>
	 * 		<li>dimension_tipo</li>
	 * 		<li>dimension_cardinalidad</li>
	 * 		<li>campo_ref</li>
	 *  </ul>
	 *  deprecado por que se debe llamar por el camporef de la dimensión<br/><br/>
	 *  
	 * @author Pancho
	 * @since 03-07-2018
	 * @deprecated
	 * */
	public ConsultaData getValores(String unidId,Integer dimensionID);
	
	
	/**
	 * Retornará un ConsultaData con la información de todos los valores de una dimensión
	 * Retorna: <br/>
	 * 	<ul>
	 * 		<li>dimension_id</li>
	 * 		<li>dimension_desc</li>
	 * 		<li>dimension_tipo</li>
	 * 		<li>dimension_cardinalidad</li>
	 * 		<li>campo_ref</li>
	 *  </ul>
	 *  
	 * @author Pancho
	 * @since 03-07-2018
	 * */
	public ConsultaData getValores(String unidId,DimensionCampoRef campoRef);
	
	/**
	 * Retornará un ConsultaData con la información de todas las personas que pertenecen a una unidad
	 * Retorna: <br/>
	 * 	<ul>
	 * 		<li>rut</li>
	 * 		<li>digito_ver</li>
	 * 		<li>nombres</li>
	 * 		<li>ape_paterno</li>
	 * 		<li>ape_materno</li>
	 * 		<li>unid_id</li>
	 * 		<li>unid_desc</li>
	 *  </ul>
	 * */
	public ConsultaData getTrabajadoresInDimension(String dimensionID);
	
	/**
	 * Retornará un ConsultaData con la información de todas las personas que pertenecen a un valor
	 * Retorna: <br/>
	 * 	<ul>
	 * 		<li>rut</li>
	 * 		<li>digito_ver</li>
	 * 		<li>nombres</li>
	 * 		<li>ape_paterno</li>
	 * 		<li>ape_materno</li>
	 * 		<li>unid_id</li>
	 * 		<li>unid_desc</li>
	 *  </ul>
	 * */
	
	public ConsultaData getTrabajadoresInValor(String ditemID);

	public ICtrGDimension getCtrG();

	public IDimensionUtil getUtil();
}


