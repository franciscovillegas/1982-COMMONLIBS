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
	 * Retornar� un ConsultaData con la informaci�n de la dimensi�n de una unidad.
	 * @return
	 * 	<ul>
	 * 		<li>dimension_id : id de la dimensi�n</li>
	 * 		<li>dimension_desc</li>
	 * 		<li>ditem_id : id del valor </li>
	 * 		<li>ditem_desc</li>
	 *  </ul>
	 *  
	 *  
	 * */
	public ConsultaData getDimensionFromUnidad(String unidad);

	/**
	 * Retornar� un ConsultaData con la informaci�n de la dimensi�n a la que pertenece la unidad en la cual reside la persona en cuesti�n
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
	 * Retornar� un ConsultaData con la informaci�n de todas las dimensiones
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
	 * Retornar� un ConsultaData con la informaci�n de una dimensi�n en particular.
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
	 * Retornar� un ConsultaData con la informaci�n de una dimensi�n en particular.
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
	 * Retornar� un ConsultaData con la informaci�n de la dimensi�n a la pertenece el valor indicado
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
	 * Retornar� un ConsultaData con la informaci�n de todos los valores de una dimensi�n
	 * Retorna: <br/>
	 * 	<ul>
	 * 		<li>dimension_id</li>
	 * 		<li>dimension_desc</li>
	 * 		<li>dimension_tipo</li>
	 * 		<li>dimension_cardinalidad</li>
	 * 		<li>campo_ref</li>
	 *  </ul>
	 *  deprecado por que se debe llamar por el camporef de la dimensi�n<br/><br/>
	 *  
	 * @author Pancho
	 * @since 03-07-2018
	 * @deprecated
	 * */
	public ConsultaData getValores(Integer dimensionID);
	
	/**
	 * Retornar� un ConsultaData con la informaci�n de todos los valores de una dimensi�n
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
	 * Retornar� un ConsultaData con la informaci�n de todos los valores de una dimensi�n
	 * Retorna: <br/>
	 * 	<ul>
	 * 		<li>dimension_id</li>
	 * 		<li>dimension_desc</li>
	 * 		<li>dimension_tipo</li>
	 * 		<li>dimension_cardinalidad</li>
	 * 		<li>campo_ref</li>
	 *  </ul>
	 *  deprecado por que se debe llamar por el camporef de la dimensi�n<br/><br/>
	 *  
	 * @author Pancho
	 * @since 03-07-2018
	 * @deprecated
	 * */
	public ConsultaData getValores(String unidId,Integer dimensionID);
	
	
	/**
	 * Retornar� un ConsultaData con la informaci�n de todos los valores de una dimensi�n
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
	 * Retornar� un ConsultaData con la informaci�n de todas las personas que pertenecen a una unidad
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
	 * Retornar� un ConsultaData con la informaci�n de todas las personas que pertenecen a un valor
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


