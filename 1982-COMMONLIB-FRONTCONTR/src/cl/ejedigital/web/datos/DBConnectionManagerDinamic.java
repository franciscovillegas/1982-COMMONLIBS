package cl.ejedigital.web.datos;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import cl.ejedigital.consultor.ConnectionDefinition;
import cl.ejedigital.web.datos.util.DriverConnectionTool;
import portal.com.eje.portal.factory.Util;


public class DBConnectionManagerDinamic {
    
    public static DBConnectionManagerDinamic getInstance() {
    	return Util.getInstance(DBConnectionManagerDinamic.class);
    }
	
	
	
	private static int clients;
    private Vector drivers;
    private Hashtable pools;
    

    public DBConnectionManagerDinamic() {
        drivers = new Vector();
        pools = new Hashtable();
       
    }
    
	public Connection getConnection(ConnectionDefinition key) throws ConnectionException {
		createPools(key);
		
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
        if(pool != null) {
            pool.freeConnection(conn);
        }
	}

	public void release() throws ConnectionException {
		// TODO Auto-generated method stub
		
	}
	
	private void createPools(ConnectionDefinition conn) throws ConnectionException {
       

	
	        String conexionbd = conn.getBd();
	        String host = conn.getIp();
	        String puerto = String.valueOf(conn.getPuerto());
	        String username = conn.getUsuario();
	        String passw = conn.getPassword();
	        int    max = 1;
	        String url = DriverConnectionTool.getInstance().getUrl(host, String.valueOf(puerto), conexionbd);
	        
	        String driverClasses = DriverConnectionTool.getInstance().getClassConnectionDriver();
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
	
	         
	        DBConnectionManagerNoContextPool pool = new DBConnectionManagerNoContextPool(conn.toString(), url.toString(), username, passw, max);
	        pools.put(conn, pool);
	        //log(String.valueOf(new StringBuilder("Initialized pool ").append(poolName)));
	 
    }
	

 
    
}
