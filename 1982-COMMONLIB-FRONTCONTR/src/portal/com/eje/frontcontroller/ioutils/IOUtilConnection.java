package portal.com.eje.frontcontroller.ioutils;

import java.sql.Connection;

import cl.ejedigital.web.datos.DBConnectionManager;
import portal.com.eje.frontcontroller.IIOClaseWebLight;

/**
 * Ha de usarse Transaction Connection
 * @deprecated
 * */
public class IOUtilConnection extends IOUtil {

	/**
	 * Ha de usarse Transaction Connection
	 * @deprecated
	 * */
	public Connection getConnection(IIOClaseWebLight io, String key) {
		return DBConnectionManager.getInstance().getConnection(key);
	}
	
	/**
	 * Ha de usarse Transaction Connection
	 * @deprecated
	 * */
	public void freeConnection(IIOClaseWebLight io, String key, Connection conn) {
		DBConnectionManager.getInstance().freeConnection(key, conn);
	}
}
