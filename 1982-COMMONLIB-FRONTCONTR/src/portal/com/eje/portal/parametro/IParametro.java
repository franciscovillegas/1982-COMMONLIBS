package portal.com.eje.portal.parametro;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import cl.eje.model.generic.portal.Eje_generico_modulo;
import cl.ejedigital.web.datos.error.DuplicateKeyException;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.PPMException;

public interface IParametro {

	
	
	/**
	 * @deprecated
	 * @since 03-agosto-2016
	 * 
	 * Retorna todos los valores de un parámetro
	 * A partir del 
	 * */
	
	public List<ParametroValue> getValues(EModulos modulo, String param);
	
	/**
	 * @deprecated
	 * @since 03-agosto-2016
	 * 
	 * */
	public File getFile(EModulos modulo, String param);
	
	
	/**
	 * se cambió por getValor
	 * @deprecated
	 * @since 03-agosto-2016
	 * 
	 * */
	public ParametroValue getValue(EModulos modulo, String param);
	
	/**
	 * @deprecated
	 * @since 03-agosto-2016
	 * 	
	 * */
	public ParametroValue getValue(EModulos modulo, String param, String valor);
	
	
	/**
	 * Retornará los valores del parámetro indicado para otro módulo diferente a this, el cliente se obtiene de manera interna.
	 * */
	public List<ParametroValue> getValores(EModulos mods, String nemoParam);
	
	/**
	 * Retornará los valores del parámetro indicado para otro módulo diferente a this, el cliente se obtiene de manera interna.
	 * */
	public List<ParametroValue> getValores(EModulos mods, int idCliente, String nemoParam);
	/**
	 * Retornará los valores del parámetro indicado, el cliente se obtiene de manera interna.
	 * */
	public List<ParametroValue> getValores(String nemoParam);
	
	/**
	 * Retornará los valores del parámetro indicado, el cliente se obtiene de manera interna.
	 * */
	public ParametroValue getValor(String nemoParam, String key);
	
	/**
	 * Retornará los valores del parámetro indicado, el cliente se obtiene de manera interna.<br/>
	 * Puede retornar null si el valor no existe
	 * */
	public ParametroValue getValor(EModulos mods, String nemoParam, String key);
	
		/**
	 * Retornará la conexión del parámetro. Para que funcione correctamente deben estar todos los valores bien definidos.<br/>
	 * El parámetro nemoParam, tendrá siempre el prefijo <i>conexion.</i>. Ej. si buscamos la conexión mac, entonces el parámetro deberá llamarse <i>conexion.mac</i></br>
	 * Los valores que deben estar y su definición por defecto son: <br/>
	 * <ul>
	 * 		<li>driver: net.sourceforge.jtds.jdbc.Driver</li>
	 * 		<li>maxconn: 15</li>
	 * 		<li>password: sa</li>
	 * 		<li>username: josefina</li>
	 * 		<li>url: jdbc:jtds:sqlserver://192.168.5.18:1433/mac_corona_wfs_contratacion_test</li>
	 * </ul>
	 *  
	 * */
	
	public ParametroValue getKey(EModulos mods, String nemoParam, String valor);
	
	
	public Connection getConnection(String nemoParam) throws SQLException;
	
	public Connection getConnection(EModulos mods, String nemoParam) throws SQLException;
	
	public List<EModulos> getModulos(boolean vigente);
	
	public List<EModulos> getModulosWorkflows(boolean vigente);
	
	/**
	 * Luego de obtener una conexión, es obligatorio liberar la conexione, para eso debe ejecutarse este método. 
	 * */
	public void freeConnection(String nemoParam, Connection conn) throws SQLException;
	
	public void freeConnection(EModulos modulos, String nemoParam, Connection conn) throws SQLException;
	/**
	 * Método que ha se ejecutarse solo una vez, o cuando se modifiquen los parámetros ya que eventualmente podría crearse un contexto o aplicación
	 * */
	public void recognizeIDCliente(ServletConfig sc);
	
	/**
	 * Método que ha se ejecutarse solo una vez, o cuando se modifiquen los parámetros ya que eventualmente podría crearse un contexto o aplicación
	 * */
	public void recognizeIDCliente(ServletContext sc);
	
	/**
	 * Indica true cuando ya se ha reconocido el cliente y el módulo, si es que no, retornará false
	 * */
	public boolean isRecognized();
	
	public Integer getIDCliente();
	
	public String getClienteContext();
	
	public Integer getIDModulo();
	
	public String getModuloContext();
	
	public String getContext();
	
	public String getCoreValue(EParametroCore p);

	/**
	 * Objetiene el parametro key, si no existe lo crea.
	 * Siempre ha de retornar un objeto.
	 * */
	public ParametroKey getParamKey(EModulos modulo, String key);
	
	/**
	 * Agrega un parámetro a la BD
	 * */
	public boolean addParam(int idCliente, ParametroKey key, ParametroValue value);
	
	/**
	 * Agrega un parámetro a la BD
	 * */
	public boolean addParam(ParametroKey key, ParametroValue value);
	
	/**
	 * Reinicia cache
	 * */
	public boolean clear();
	
	/**
	 * Creará el parámetro <b>si no está creado con anterioridad.</b></br>
	 *
	 * @since 04-05-2018
	 * @author Pancho
	 * */
	
	public void ifNotExistThenBuildParametro(EModulos modulo, String paramNemo) throws PPMException;
	
	/**
	 * Creará el parámetro con sus valores por defecto, esto solo <b>si no está creado con anterioridad.</b></br>
	 * Si nos ponemos en el caso de ya estar creado, entonces no se hará nada, el método revisará la existencia y no se hará nada más. Es por esto mismo que cualquier persona puede modificar los parámetros y no se verán afectados los valores
	 *
	 * @since 2017-04-24
	 * */
	
	public void ifNotExistThenBuildParametro(EModulos modulo, String paramNemo, String valueKey, String valueDefault) throws PPMException;
	
	/**
	 * Creará el parámetro con sus valores por defecto, esto solo <b>si no está creado con anterioridad.</b></br>
	 * Si nos ponemos en el caso de ya estar creado, entonces no se hará nada, el método revisará la existencia y no se hará nada más. Es por esto mismo que cualquier persona puede modificar los parámetros y no se verán afectados los valores <br/>
	 *
	 * <b>IMPORTANTE</b> <br/>
	 * El parámetro EModulos recibe los contextos frente a los cuales se permitirá agregar los parámetros, es un bloqueo por contexto, de esa forma solo se agregarán los parámetros cuando le petición HTTP_Request venga desde los indicados.
	 * @since 2017-04-24
	 * */
	
	public void ifNotExistThenBuildParametro(List<EModulos> modulo, String paramNemo, String valueKey, String valueDefault) throws PPMException;
	
	/**
	 * Solo se creo para lesear
	 * @deprecated
	 * @author Pancho
	 * @since 04-05-2018
	 * */
	public void setParametroListener(IParametroListener param);

	/**
	 * Elimina una key
	 * @author Pancho
	 * @since 04-05-2018
	 * */
	public boolean deleteKey(EModulos portaldepersonas, String string, String forcedString);
	
	public Integer getIDModuloFromBD(EModulos modulo);
	
	public Integer getIDParamFromBD(Integer idMoldulo, String nemo);
	
	/**
	 * cambia un param y agraga otro
	 * */
	public void setParam(EModulos modulo, String paramNemo, String key, String value);

	/**
	 * Retorna los JNDIS existentes en el modulos, por defecto omite PORTAL
	 * @since 07-11-2018
	 * */
	public List<String> getJndis(EModulos mmodulo);
	
	/**
	 * Retorna una lista de los nemoParam del modulo indicado <br/>
	 * El módulo debe estar configurado
	 * @since 07-11-2018 
	 * **/
	public List<String> getNemoParams(EModulos modulo);
	
	/**
	 * Get database name 
	 * */
	public String getDatabaseName(EModulos modulo, String jndi);

	boolean cannConnect(EModulos mods, String nemoParam);
	
	/**
	 * Retorna la definición (valores en tabla) del modulo, solo si está definido, si no entregará null
	 * 
	 * @since 29-01-2019
	 * @author Pancho
	 * */
	public Eje_generico_modulo getModuloDef(EModulos modulo);
	
	
	/**
	 * retorna un map con todos los valores de un param, key = key del map = algun valor del vo del parametro
	 * 
	 * @since 28-02-2019
	 * @author Pancho
	 * @throws DuplicateKeyException 
	 * */
	public Map<String, ParametroValue> getMapParamValues(EModulos modulo, String paramNemo) throws DuplicateKeyException;
	
	/**
	 * retorna un map con todos los valores de un param, key = key del map = algun valor del vo del parametro
	 * 
	 * @since 28-02-2019
	 * @author Pancho
	 * @throws DuplicateKeyException 
	 * */
	public Map<String, ParametroValue> getMapParamValues(String paramNemo) throws DuplicateKeyException;
}
