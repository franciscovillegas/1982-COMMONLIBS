package portal.com.eje.portal.organica.ifaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import error.BadParameterException;
import portal.com.eje.portal.organica.errors.AlreadyExistUnidIdExcepcion;
import portal.com.eje.portal.organica.errors.NotExistUnidIdException;

public interface IOrganicaManipulatorManager {

	/**
	 * Determina si existe la unidad. <br/>
	 * Si conn = null se considera JNDI = portal <br/>
	 * 
	 * @author Pancho
	 * @since 31-10-2018
	 */
	public boolean existUnidId(Connection conn, String unid_id) throws SQLException;
	
	/**
	 * 
	 * Si conn = null se considera JNDI = portal <br/>
	 * 
	 * @author Pancho
	 * @since 31-10-2018
	 * */
	public boolean addUnidad(Connection conn, String unid_id, String unid_id_parent, String unid_desc) throws SQLException, AlreadyExistUnidIdExcepcion, NotExistUnidIdException;
	
	/**
	 * Si conn = null se considera JNDI = portal <br/>
	 * 
	 * @author Pancho
	 * @since 31-10-2018
	 * */
	public boolean setVigente(Connection conn, String unid_id, boolean vigente) throws NullPointerException, SQLException;
	
	/**
	 * Si conn = null se considera JNDI = portal <br/>
	 * 
	 * @author Pancho
	 * @since 31-10-2018
	 * */
	public boolean setVigente(Connection conn, List<String> listaUnidsIds, boolean vigente) throws NullPointerException, SQLException;
	
	/**
	 * Si conn = null se considera JNDI = portal <br/>
	 * String unid_id != null && debe existir si no error  <br/>
	 * 
	 * @author Pancho
	 * @since 31-10-2018
	 * */
	public boolean setNombre(Connection conn, String unid_id, String unid_desc) throws NullPointerException, SQLException;
	
	/**
	 * Si conn = null se considera JNDI = portal<br/>
	 * String unid_id != null && debe existir si no error  <br/>
	 * String unid_id_parent != null && debe existir si no error  <br/>
	 * 
	 * @author Pancho
	 * @since 31-10-2018
	 * */
	public boolean setParent(Connection conn, String unid_id, String unid_id_parent) throws NullPointerException, SQLException;
	
	/**
	 * Si conn = null se considera JNDI = portal
	 * 
	 * @author Pancho
	 * @since 31-10-2018
	 * */
	public boolean addLog(Connection conn, String glosa, Integer rutUsuario, String unid_id, Boolean vigente, String nombre, String unid_id_parent) throws NotExistUnidIdException, SQLException, BadParameterException;
	
	/**
	 * Si conn = null se considera JNDI = portal <br/>
	 * List<String> != null && List<String>.size() > 0 si no error <br/>
	 * Cuenta la cantidad total de personas que están en las unidades indicadas <br/>
	 * 
	 * @author Pancho
	 * @since 31-10-2018
	 * */
	public int countPersonas(Connection conn, List<String> listaUnidsIds) throws SQLException;
	
	/**
	 * Actualiza referencias de eje_ges_unidades y eje_ges_jerarquia, como nodo_nivel, codo_corr, nodo_hijos
	 * 
	 * @author Pancho
	 * @since 05-11-2018
	 * */
	public void updateReferences(Connection conn, String unid_id) throws NullPointerException, SQLException, NotExistUnidIdException;
	
	/**
	 * Crea un backup de la organica a partir de lo que ahora existe, guarda todo, vigentes y no vigentes
	 * 
	 * @author Pancho
	 * @since 05-11-2018
	 * */	
	public void backupOrganica(Connection conn, int id_movimiento) throws NullPointerException, SQLException;
 
}
