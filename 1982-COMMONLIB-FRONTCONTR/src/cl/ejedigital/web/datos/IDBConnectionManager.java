package cl.ejedigital.web.datos;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import portal.com.eje.portal.EModulos;


public interface IDBConnectionManager {

	
	public Connection getConnection(String key) throws ConnectionException;
	
	public Connection getConnection(EModulos modulo, String key) throws ConnectionException;
	
	public void freeConnection(String name, Connection conn) throws ConnectionException;
	
	public void freeConnection(EModulos modulo, String name, Connection conn) throws ConnectionException;
	
	public void release() throws ConnectionException;
	
	/**
	 * @deprecated
	 * */
	public List<String> getJndis();
	
	/**
	 * Creado para conocer desde la raiz DBConnectionManager el pool y poder obtener las conexiones solicitadas, ya que se está demorando demasiado en obtener la consulta
	 * 
	 * @author Pancho
	 * @since 12-11-2018
	 * */
	public Map<String, DataSource> getPool();

	/**
	 * Retorna true si es que se puede conectar
	 * @author Pancho
	 * @since 11-01-2018
	 * */
	public boolean canConnect(String jndi);
	
	/**
	 * Retorna true si es que se puede conectar
	 * @author Pancho
	 * @since 11-01-2018
	 * */
	public boolean canConnect(EModulos modulo, String jndi);
}
