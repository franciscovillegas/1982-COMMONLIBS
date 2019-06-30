package cl.ejedigital.web.datos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.tool.misc.Cronometro;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.parametro.ParametroLocator;

/**
 * Es un singleton que lo único que hace es entregar una Interfaz de IDBConnectionManager con una implementación actualizada y funcional de conección a BD.
 * 
 * @see cl.ejedigital.web.datos.IDBConnectionManager
 * 
 * */
public class DBConnectionManager implements IDBConnectionManager {
	private Logger logger = Logger.getLogger(getClass());
	private static IDBConnectionManager pivot;
	private IDBConnectionManager realInstance;
	private Map<String,Integer> map;
	private Map<String,String> mapJndi;
	
	protected DBConnectionManager(IDBConnectionManager instance) {
		this.realInstance = instance;
		map= new HashMap<String, Integer>();
		mapJndi = new HashMap<String, String>();
	 
	}

	public static IDBConnectionManager getInstance() {
		if (pivot == null) {
			synchronized(DBConnectionManager.class) {
				if (pivot == null) {
					
					if(ExistContext()) {
						if(usePropertyConnection()) {
							System.out.println("[conexión by Properties]");
							pivot = new DBConnectionManager( DBConnectionManagerNoContext.getInstance() );
						}
						else { 
							System.out.println("[conexión by Context]");
							pivot = new DBConnectionManager( DBConnectionManagerPortalDefault.getInstance() );	
						}
					}
					else {
						System.out.println("[conexión by Properties]");
						pivot = new DBConnectionManager( DBConnectionManagerNoContext.getInstance() );
					}
				}
			}
		}
		
		return pivot;
	}	

	private static boolean ExistContext() {
		Context initContext;
		boolean ok = true;
		
		try {
			initContext = new InitialContext();
			Context c = (Context)initContext;
			c.lookup("java:/comp/env"); 
		}
		catch (NoInitialContextException e) {
			ok = false;
		}
		catch (NamingException e) {
			ok = false;
		}
				
		return ok;
	}
	
	private static boolean usePropertyConnection() {
		boolean ok = true;
		
		try {
			ResourceBundle proper = ResourceBundle.getBundle("conexion");
			ok = new Boolean(proper.getString("connection.property"));
		}
		catch (NullPointerException e) {
			ok = false;
		}
		catch(MissingResourceException e) {
			ok = false;
		}
		finally {
			
		}
		
		return ok;
	}
	
	private static boolean useContextConnection() {
		boolean ok = true;
		
		try {
			ResourceBundle proper = ResourceBundle.getBundle("conexion");
			ok = new Boolean(proper.getString("connection.onlycontext"));
		}
		catch (NullPointerException e) {
			ok = false;
		}
		catch(MissingResourceException e) {
			ok = false;
		}
		finally {
			
		}
		
		return ok;
	}

	@Override
	public Connection getConnection(String key) throws ConnectionException {
		return getConnection(EModulos.getThisModulo(), key);
	}
	
	@Override
	public Connection getConnection(EModulos modulo, String key) throws ConnectionException {
		Connection conn = null;
		try {
			conn = privateGetConnection(modulo, key);
		} catch (SQLException e) {
			System.out.println("@@@@@@ ERROR [Conexión no definida;en mod:"+modulo.toString()+", jndi:"+key+"]  error ocurrió en CLIENTE:   "+ParametroLocator.getInstance().getClienteContext()+"  ;  MODULO:   "+ParametroLocator.getInstance().getModuloContext()+"]");
			//e.printStackTrace();
		}
		return conn;
	}
	
	private Connection privateGetConnection(EModulos modulo, String key) throws SQLException {
		Cronometro cro = new Cronometro();
		cro.start();
		DBLoggerPrinter.printGettingConnection(logger, key, modulo.toString()+" (1)");
		
		Connection conn = null;
		if("portal".equals(key) || usePropertyConnection() || useContextConnection()) {
			DBLoggerPrinter.printGettingConnection(logger, key, modulo.toString()+" (2)");
			conn = this.realInstance.getConnection(key);
		}
		else {
			DBLoggerPrinter.printGettingConnection(logger, key, modulo.toString()+" (3)");
				conn = ParametroLocator.getInstance().getConnection(modulo, key);
				if(conn != null) { 

				}
				else {
					throw new SQLException("Error al crear la conexión para el módulo:"+modulo+" jndi:"+key);
				}
			
		}
		
		 
		/*
		 * Agregado en Oct de 2017
		 * Francisco
		 * */
		String connKey = getKeyFromConnection(conn);
		if(connKey != null && mapJndi.get(connKey) == null) {
			mapJndi.put(connKey, key);
		}
		
		 
		return conn;
	}
	
	public String getJndiFromConnection(Connection conn) {
		return mapJndi.get(getKeyFromConnection(conn));
	}

	private String getKeyFromConnection(Connection conn) {
		try {
			if(conn != null && conn.getMetaData() != null) {
				StringBuilder strKey = new StringBuilder();
				strKey.append(conn.getMetaData().getURL()).append(";");
				strKey.append(conn.getCatalog()).append(";");
				strKey.append(conn.getMetaData().getUserName()).append(";");
				
				return strKey.toString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	


	@Override
	public void freeConnection( String name, Connection conn) throws ConnectionException {
		freeConnection(EModulos.getThisModulo(), name, conn);
		
	}
	
	@Override
	public void freeConnection(EModulos modulo, String key, Connection conn) throws ConnectionException {
		Cronometro cro = new Cronometro();
		cro.start();
		
		if(conn != null) {
			if("portal".equals(key) || usePropertyConnection() || useContextConnection()) {
				this.realInstance.freeConnection(key, conn);
			}
			else {
				try {
					ParametroLocator.getInstance().freeConnection(modulo, key, conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}	
			
	 	}
		
		
		
	}

	@Override
	public void release() throws ConnectionException {
		this.realInstance.release();
	}

	private void count(String jndi, Connection conn, boolean seSuma) {
		if(conn != null) {
			Integer real = this.map.get(jndi);
			if(real== null) {
				this.map.put(jndi, 0);
				real = this.map.get(jndi);
			}
			
			real = seSuma ? real + 1 : real  -1;
			this.map.put(jndi, real );
			
			//System.out.println(jndi + " " + real);
			if("mac".equals(jndi)) {
				//System.out.println();
			}
		}

	}
	
	public ConsultaData getCount() {
		List<String> cols = new ArrayList<String>();
		cols.add("jndi");
		cols.add("count");
		ConsultaData data = new ConsultaData(cols);
		
		Set<String> sets = this.map.keySet();
		for(String jndi : sets) {
			DataFields df =new DataFields();
			df.put("jndi", jndi);
			df.put("count",this.map.get(jndi));
			
			data.add(df);
		}
		
		return data;
	}
	
	public void clearCount() {
		this.map.clear();
	}

	@Override
	public List<String> getJndis() {
		return ((DBConnectionManager)pivot).realInstance.getJndis();
	}

	@Override
	public Map<String, DataSource> getPool() {
		return ((DBConnectionManager)pivot).realInstance.getPool();
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
			
			if("portal".equals(jndi) || usePropertyConnection() || useContextConnection()) {
				if(this.realInstance.canConnect(modulo, jndi)) {
					conn = this.realInstance.getConnection(jndi);	
				}
			}
			else {
				if(ParametroLocator.getInstance().cannConnect(modulo, jndi)) {
					conn = ParametroLocator.getInstance().getConnection(modulo, jndi);
				}
			}
			
			ok = (conn != null && !conn.isClosed());
		} catch (SQLException e) {
		}
		catch(Exception e){
			
		}
		finally {
			freeConnection(jndi, conn);
		}
		
		return ok;
	}

}