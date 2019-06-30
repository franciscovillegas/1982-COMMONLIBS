package cl.ejedigital.web.datos.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.web.datos.ifaces.IDriverTool;
import portal.com.eje.portal.factory.Util;

public class DriverConnectionTool {
	private final List<IDriverTool> listaDeDriversTools;

	private DriverConnectionTool() {
		this.listaDeDriversTools = new ArrayList<>();
		listaDeDriversTools.add(DriverToolJtds.getInstance());
		listaDeDriversTools.add(DriverToolMSSqlServer.getInstance());
	}

	public static IDriverTool getInstance() {
		//return DriverToolMSSqlServer.getInstance();
		return DriverToolJtds.getInstance();
	}

	public static DriverConnectionTool getTool() {
		return Util.getInstance(DriverConnectionTool.class);
	}

	public static IDriverTool getInstance(Connection conn) {
		String originalURL;
		try {
			originalURL = conn.getMetaData().getURL();
			Driver drv = DriverManager.getDriver(originalURL);
			String driverClass = drv.getClass().getName();
			return getInstance(driverClass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static IDriverTool getInstance(String value) {
		IDriverTool es = null;

		if (value != null) {
			for (IDriverTool posible : getTool().listaDeDriversTools) {
				if (value.equals(posible.getClassConnectionDriver())) {
					es = posible;
					break;
				}
			}
		}

		return es;
	}

}
