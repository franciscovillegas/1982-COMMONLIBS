package portal.com.eje.portal.transactions;

import java.sql.Connection;
import java.sql.SQLException;

import cl.ejedigital.web.datos.DBConnectionManager;
import portal.com.eje.portal.EModulos;

public class JndiTool {

	protected JndiTool() {

	}

	public String getUrlConnection(EModulos modulo, String jndi) {
		String catalog = null;
		Connection connSimula = null;
		try {
			connSimula = DBConnectionManager.getInstance().getConnection(modulo, jndi);
			catalog = connSimula.getCatalog();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnectionManager.getInstance().freeConnection(modulo, jndi, connSimula);
		}

		return catalog;
	}

}
