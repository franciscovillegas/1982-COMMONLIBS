package cl.ejedigital.web.datos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory;

import cl.ejedigital.web.datos.util.DriverConnectionTool;
import portal.com.eje.portal.parametro.ParametroValue;

/**
 * Se elimino el syncronized
 * 
 * @author Pancho
 * 
 */
public class DBConnectionManagerNoContextPool {

	private BasicDataSource dataSource;
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * @deprecated
	 */
	public DBConnectionManagerNoContextPool(String name, String url, String user, String password, int maxConn) {	
		DBLoggerPrinter.printINIT_DEF(logger, name, "Usuario y clase solamente");
		
		Properties proper = new Properties();
		proper.put("drivers", DriverConnectionTool.getInstance().getClassConnectionDriver());
		proper.put("url", url);
		proper.put("username", user);
		proper.put("password", password);
		proper.put("maxconn", maxConn);

		inicioManager(proper);
	}

	public DBConnectionManagerNoContextPool(String name, List<ParametroValue> lista) {
		DBLoggerPrinter.printINIT_DEF(logger, name, "Desde una lista de parámetros");
		
		if (name != null && lista != null && lista.size() > 0) {
			Properties proper = new Properties();
			for (ParametroValue pv : lista) {
				logger.debug(pv.getKey() + ":" + pv.getValue());
				proper.put(pv.getKey(), pv.getValue());
			}

			inicioManager(proper);
		}
	}

	private void inicioManager(Properties proper) {
		try {
			dataSource = BasicDataSourceFactory.createDataSource(proper);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void freeConnection(String name, Connection conn) {
		freeConnection(conn);
	}

	public void freeConnection(Connection conn) {
		release(conn);
	}

	public void release(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}

			conn = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws ConnectionException {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}

	public boolean cannConnect() throws ConnectionException {
		boolean ok = false;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			ok = (conn != null && !conn.isClosed());
		} catch (SQLException e1) {

		} catch (Exception e) {

		} finally {
			freeConnection(conn);
		}

		return ok;
	}

	public Connection getConnection(long timeout) throws ConnectionException {
		throw new NotImplementedException();
	}

	/**
	 * @deprecated
	 */
	public Map getDefinition() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("name", name);
		map.put("url", URL);
		map.put("user", user);
		map.put("password", password);
		map.put("maxConn", String.valueOf(maxConn));

		return map;
	}

	private int maxConn;
	private String name;
	private String password;
	private String URL;
	private String user;

}
