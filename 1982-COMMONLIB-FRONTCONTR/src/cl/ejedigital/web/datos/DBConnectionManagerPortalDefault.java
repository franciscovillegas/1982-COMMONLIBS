package cl.ejedigital.web.datos;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Util;

public class DBConnectionManagerPortalDefault implements IDBConnectionManager {
	
	private Context envContext;
	private final String ERRORBD = "Error de conexion a BD ";
	private int clients;
	private Map<String, DataSource> pool;
	private Logger logger = Logger.getLogger(getClass());
	public static DBConnectionManagerPortalDefault getInstance() {
		return Util.getInstance(DBConnectionManagerPortalDefault.class);
	}
	
	
	DBConnectionManagerPortalDefault() {
		pool = new HashMap<String, DataSource>();
		try {
			Context initContext = new InitialContext();
			envContext = (Context)initContext.lookup("java:/comp/env"); 
		} 
		catch (NamingException e) {
			e.printStackTrace();
		}

		
	}
	
	public Connection getConnection(String key) {
		Connection conn = null;
		try {
			DataSource ds = getPool(key);
			
			if(ds != null) {
				conn = ds.getConnection();
				if (conn == null  || conn.isClosed()) {
					System.out.println("clients reconected : " + clients);
					conn = getPool(key).getConnection();
				}
			}
			else {
				logger.error("No existe DataSource para el jndi '"+key+"'");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("SQLException", e);
		}
		

		
		return conn;
	}
	
	private DataSource getPool(String key) {		
		DataSource ds = pool.get(key);
		
		if(ds == null) {
			synchronized (DBConnectionManagerPortalDefault.class) {
				if(ds == null) {
					pool.put(key, createPool(key));
					ds = pool.get(key);
				}
			}
		}
		
		return ds;
	}
	
	private DataSource createPool(String key) {
		DBLoggerPrinter.printINIT_DEF(logger, key,"Desde Contexto");
		
		try {
			String bd = "jdbc/"+key;
			DataSource datasource = (DataSource)envContext.lookup(bd);

			return datasource;
		} 
		catch (Exception e) {
			System.out.println(ERRORBD + e.toString());
		 
		}
		return null; 
	}
	
	public void freeConnection(String name, Connection conn) {
		freeConnection(conn);
	}
	
	private void freeConnection(Connection conn) {
		release(conn);
	}

	public void release(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close(); 
			}

			conn = null;
		} 
		catch(SQLException e) { }
	}

	public void release() {
	}

	@Override
	public Connection getConnection(EModulos modulo, String key) throws ConnectionException {
		throw new NotImplementedException();
	}

	@Override
	public void freeConnection(EModulos modulo, String name, Connection conn) throws ConnectionException {
		throw new NotImplementedException();
		
	}

	private Connection privateGetConnection(EModulos modulo, String key) throws SQLException {
		Connection conn = null;
		try {
			DataSource ds = getPool(key);
			
			if(ds != null) {
				conn = ds.getConnection();
				 
			}
			 
		} catch (SQLException e) {
 
		}
		return conn;
	}

	@Override
	public List<String> getJndis() {
		List<String> jndis = new ArrayList<String>(pool.keySet());
		return jndis;
	}


	@Override
	public Map<String, DataSource> getPool() {
		// TODO Auto-generated method stub
		return pool;
	}


	@Override
	public boolean canConnect(String jndi) {
		return canConnect(EModulos.getThisModulo(), jndi);
	}

	@Override
	public boolean canConnect(EModulos modulo, String jndi) {
		boolean ok = false;
		Connection conn = null;
		try {
			conn = privateGetConnection(modulo, jndi);
			
			ok = !conn.isClosed();
		} catch (SQLException e) {
			 
		}
		finally {
			freeConnection(jndi, conn);
		}
		
		return ok;
	}

 
}
