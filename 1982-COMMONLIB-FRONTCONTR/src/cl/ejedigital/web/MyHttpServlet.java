package cl.ejedigital.web;

import java.sql.Connection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;


/**
 * @deprecated
 * @since 2016-agosto-03
 * 
 * No debe ocuparse esta clase
 * 
 * */
public abstract class MyHttpServlet extends HttpServlet {
	private Connection connPortal;
	
    /**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private IDBConnectionManager dbConnection;

	
	protected MyHttpServlet() {

    } 
    
    public void init(ServletConfig config)
        throws ServletException {
        super.init(config);
        dbConnection = DBConnectionManager.getInstance();
 
    }
    
    public IDBConnectionManager getDBConnectionManager() {
		return dbConnection;
	}

    public void destroy() {
    	dbConnection.notifyAll();
        super.destroy();
    }

   
}