package cl.ejedigital.web.datos;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.util.Assert;

import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Util;


public class DBConnectionManagerNoContext implements IDBConnectionManager {
    private static int clients;
    private Vector drivers;
    private Hashtable pools;
    
    public static DBConnectionManagerNoContext getInstance() {
		return Util.getInstance(DBConnectionManagerNoContext.class);
	}

    public DBConnectionManagerNoContext() {
        drivers = new Vector();
        pools = new Hashtable();
        init();
    }

	public Connection getConnection(String key) throws ConnectionException {
		DBConnectionManagerNoContextPool pool = (DBConnectionManagerNoContextPool)pools.get(key);
        if(pool != null) {
            return pool.getConnection();
        }
        else {
            return null;
        }
	}

	public void freeConnection(String name, Connection conn) throws ConnectionException {
		DBConnectionManagerNoContextPool pool = (DBConnectionManagerNoContextPool)pools.get(name);
      
		Assert.isTrue(pool != null, "El jndi '"+name+"' no existe, no se puede liberar la conexión");
        
		pool.freeConnection(conn);
        
	}

	public void release() throws ConnectionException {
		// TODO Auto-generated method stub
		
	}
	
	private void createPools(Properties props) throws ConnectionException {
        Enumeration propNames = props.propertyNames();
        do {
            if(!propNames.hasMoreElements()) {
                break;
            }
            
            String name = (String)propNames.nextElement();
            if(name.endsWith(".url")) {
                String poolName = name.substring(0, name.lastIndexOf("."));
                String url = props.getProperty(String.valueOf(new StringBuilder(poolName).append(".url")));
                System.err.println(String.valueOf(new StringBuilder("---->URL: ").append(url)));
                if(url == null) {
                    //log(String.valueOf(new StringBuilder("No URL specified for ").append(poolName)));
                } 
                else {
                    String conexionbd = props.getProperty(String.valueOf(String.valueOf(poolName)).concat(".name"));
                    String host = props.getProperty(String.valueOf(String.valueOf(poolName)).concat(".host"));
                    String puerto = props.getProperty(String.valueOf(String.valueOf(poolName)).concat(".puerto"));
                    String username = props.getProperty(String.valueOf(String.valueOf(poolName)).concat(".username"));
                    String passw = props.getProperty(String.valueOf(String.valueOf(poolName)).concat(".password"));
                    String maxconn = props.getProperty(String.valueOf(String.valueOf(poolName)).concat(".maxconn"), "0");
                    String driverClasses = props.getProperty(String.valueOf(String.valueOf(poolName)).concat(".drivers"));
                    for(StringTokenizer st = new StringTokenizer(driverClasses); st.hasMoreElements();) {
                        String driverClassName = st.nextToken().trim();
                        try {
                            Driver driver = (Driver)Class.forName(driverClassName).newInstance();
                            DriverManager.registerDriver(driver);
                            drivers.addElement(driver);
                            //log(String.valueOf(new StringBuilder("--->Registered JDBC driver ").append(driverClassName)));
                        }
                        catch(Exception e) {
                            //log(String.valueOf(String.valueOf((new StringBuilder("---->Can't register JDBC driver: ")).append(driverClassName).append(", Exception: ").append(e))));
                            throw new ConnectionException(3,String.valueOf(new StringBuilder(driverClassName).append(", Exception: ").append(e)));
                        }
                    }

                    int max;
                    try {
                        max = Integer.valueOf(maxconn).intValue();
                    }
                    catch(NumberFormatException e) {
                        max = 0;
                        throw new ConnectionException(4,String.valueOf(new StringBuilder(maxconn).append(" for ").append(poolName)));
                    }
                    DBConnectionManagerNoContextPool pool = new DBConnectionManagerNoContextPool(poolName, url, username, passw, max);
                    pools.put(poolName, pool);
                    //log(String.valueOf(new StringBuilder("Initialized pool ").append(poolName)));
                }
            }
        } 
        while(true);
    }
	
	 private void init() throws ConnectionException {
	        InputStream is = getClass().getResourceAsStream("/conexion.properties");
	        Properties dbProps = new Properties();
	        GregorianCalendar Fecha = new GregorianCalendar();
	        int dia = Fecha.get(5);
	        int anio = Fecha.get(1);
	        int mes = Fecha.get(2) + 1;
	        String nombre_log = String.valueOf(new StringBuilder(String.valueOf(anio)).append("_").append(String.valueOf(mes)).append("_").append(String.valueOf(dia)));
	        try {
	            dbProps.load(is);
	        }
	        catch(Exception e) {
	        	throw new ConnectionException(1);
	        }
	        String logFile = String.valueOf((new StringBuilder(dbProps.getProperty("logfile"))).append(nombre_log).append(".log"));
	        System.err.println(String.valueOf(new StringBuilder("------>Nombre Archivo Log: ").append(logFile)));
	        /*
	        try {
	            log = new PrintWriter(new FileWriter(logFile, true), true);
	        }
	        catch(IOException e) {
	        	throw new DAOException(2,logFile);
	        }
	        */
	        createPools(dbProps);
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
		throw new NotImplementedException();
	}

	@Override
	public List<String> getJndis() {
		throw new NotImplementedException();
	}

	@Override
	public Map<String, DataSource> getPool() {
		return this.pools;
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
