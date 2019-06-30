package portal.com.eje.portal.tool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.util.DriverConnectionTool;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroValue;

public class AplicationsTool {
	public static final String JNDI_PORTAL = "portal";
	public static final String JNDI_MAC = "mac";
	
	public static AplicationsTool getInstance() {
		return Util.getInstance(AplicationsToolCached.class);
	}
	
	/**
	 * Erroneamente, se estaba obteniendo las conexiones de otras aplicaciones,
	 *  pero el módulo de aplicaciones solo se reconoce a si mismo, 
	 *  así que se decidió por deprecar los métodos
	 * @deprecated
	 * @since 17-oct-2016
	 * */
	public String getDatabase(Aplications a) {
		//return getDatabaseFromContext(a);
		return getDatabaseFromParametroBD(a);
	}
	
	/**
	 * Erroneamente, se estaba obteniendo las conexiones de otras aplicaciones,
	 *  pero el módulo de aplicaciones solo se reconoce a si mismo, 
	 *  así que se decidió por deprecar los métodos
	 * @deprecated
	 * @since 17-oct-2016
	 * */
	private String getDatabaseFromParametroBD(Aplications a) {
		return getDatabase(a.getConnectionName());
	}
	
	public String getDatabaseJndiPortal() {
		return getDatabase(JNDI_PORTAL);
	}
	
	/**
	 * No funciona cuando se está simulando, la mejora opción es obtener el nombre desde la conexión
	 * 
	 * @deprecated
	 * @author Pancho
	 * @since 27-05-2019
	 * */
	public String getDatabaseJndiMac() {
		return getDatabase(JNDI_MAC);
	}
	
	public String getDatabase(String conectionJNDI) {
		String retorno = null;
		
		if(conectionJNDI != null) {
			Connection conn = null; 
					
			try {
				conn = DBConnectionManager.getInstance().getConnection(conectionJNDI);
				
				 
				retorno = getDatabaseNameFromUrl(conn);
				 
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				DBConnectionManager.getInstance().freeConnection(conectionJNDI, conn);
			}

		}
		
		return retorno;
	}
	
//	public String getDatabase(EModulos modulo, String conectionJNDI) {
//		IOSimulator simulator = new IOSimulator(modulo);
//		simulator.simuleTransaction(new ITransactionSimulator() {
//			
//			@Override
//			public Object simuleTransaction(IOClaseWebSimulator io) throws Exception {
//				return getDatabaseNameFromUrl(io.getTransactionConnection().getConnection(conectionJNDI));
//			}
//		});
//		
//		return (String) simulator.getObject();
//	}
	
	public String getDatabase(EModulos modulos, String conectionJNDI) {
		
		if("portal".equals(conectionJNDI)) {
			return getDatabase("portal");
		}
		else {
			ParametroValue pv = ParametroLocator.getInstance().getValor(modulos, "conexion."+conectionJNDI, "url");
			ParametroValue driver = ParametroLocator.getInstance().getValor(modulos, "conexion."+conectionJNDI, "drivers");
			String ret = null;
			
			if(pv.getValue() != null && driver.getValue() != null) {
				ret = DriverConnectionTool.getInstance(driver.getValue()).getDataBaseFromUrl(pv.getValue());
			}
			else {
				ret = pv.getValue();
			}
			
			return ret;
		}
		
	}
	
	public String getDatabaseNameFromUrl(DataSource source) {
		String databaseName = null;
		Connection conn = null;
		try {
			conn = source.getConnection();
			databaseName = getDatabaseNameFromUrl(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		conn = null;
		return databaseName;
	}
	public String getDatabaseNameFromUrl(Connection conn) {
		String retorno =null;
		try {
			if(conn != null && conn.getMetaData() != null) {
				 retorno = DriverConnectionTool.getInstance(conn).getDataBaseFromUrl(conn.getMetaData().getURL());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	public String getDatabaseNameFromUrl(String url) {
		return DriverConnectionTool.getInstance().getDataBaseFromUrl(url);
	}
	
	/**
	 * En jdk 11, no existe BasicDataSource
	 * */
	public Map<String,DataSource> getDataSourcesFromContext() {
		Map<String,DataSource> datas = new HashMap<String, DataSource>();
		
		Context initContext = null;
		Context envContext = null;
		
		try {
			
			initContext = new InitialContext();
			envContext = (Context)initContext.lookup("java:/comp/env"); 
			
			NamingEnumeration<Binding> bind = initContext.listBindings("java:/comp/env");
			while(bind.hasMoreElements()) {
				Binding b = bind.next();
				
				b.getObject();
				Context c = (Context)b.getObject();
				NamingEnumeration<Binding> bindings = c.listBindings("");
				
				while(bindings.hasMoreElements()) {
					String jndiName = "jdbc/"+ ((Binding)bindings.next()).getName();
 
					DataSource datasource = (DataSource)envContext.lookup(jndiName);
					datas.put(jndiName, datasource);
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
			 
			
		}
		
		return datas;
	}
}
