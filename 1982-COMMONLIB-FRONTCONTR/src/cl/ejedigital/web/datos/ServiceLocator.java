package cl.ejedigital.web.datos;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import portal.com.eje.portal.factory.Util;

/**
 * Muy Antigua, se debe usar DataSource
 * @deprecated
 * 
 * */
public class ServiceLocator {
	private Map<String,DataSource> cache;

	private ServiceLocator()  {
		this.cache = Collections.synchronizedMap(new HashMap<String,DataSource>());
	}

	public static ServiceLocator getInstance() {
		return Util.getInstance(ServiceLocator.class);
	}

	public DataSource getDataSource(String dataSourceName) throws NamingException {
		DataSource datasource = null;  
		if (this.cache.containsKey(dataSourceName)) {
			datasource = (DataSource) this.cache.get(dataSourceName);
		}
		else {  
			Context ctx = new InitialContext();
			datasource = (DataSource)ctx.lookup(dataSourceName);
			this.cache.put(dataSourceName, datasource);
		}
		
		return datasource;
	}
}