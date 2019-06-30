package portal.com.eje.serhumano.httpservlet;

import java.net.MalformedURLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.servlet.ServletContext;

import cl.ejedigital.tool.properties.PropertiesTools;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;
import portal.com.eje.applistener.ContextInfo;
import portal.com.eje.daemon.DaemonGuard;
import portal.com.eje.daemon.PPMDaemon;
import portal.com.eje.portal.factory.Static;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.tools.QuartzScheduler;

public class MyHttpServlet_onInitServer {
	private static MyHttpServlet_onInitServer instance;
	protected PropertiesTools properTools;
    protected IDBConnectionManager connMgr;
    
	private MyHttpServlet_onInitServer() {
		
	}
	
	private void init(ServletContext sc) {
		try {
		 connMgr = DBConnectionManager.getInstance();
         properTools = new PropertiesTools();
         
         if(!properTools.existsBundle("analytics")) {
         	System.err.println("No existe \"analytics.properties\"");
         }
         
         ParametroLocator.getInstance().recognizeIDCliente(sc);
         
         if(ParametroLocator.getInstance().getClienteContext() != null && QuartzScheduler.getInstance() != null) {
        	 QuartzScheduler.getInstance().init(null);
             DaemonGuard.getInstance().startIfThisContextIsActive();	 
         }
         
         
     	 ContextInfo.getInstance().setServletContext(sc);
     	
     	 
     	
 		 System.out.println("[MyHttpServlet_onInitServer] Creando singleton para InitContext "+ParametroLocator.getInstance().getContext());
         
		}
		catch(Exception e) {
			System.out.println(e.getCause()+" "+getServletContext(sc));
			e.printStackTrace();
		}
	}

	public static void destroyInstance(ServletContext context) {
		if(instance != null) {
			synchronized (MyHttpServlet_onInitServer.class) {
				if(instance != null) { 
					 instance.connMgr.release();
					 instance.connMgr = null;
					 instance.properTools= null;
			         instance = null;
			         
					 QuartzScheduler.getInstance().destroy(null);			
				}
			}
		}
		
	}
	
	public static MyHttpServlet_onInitServer getInstance(ServletContext context) {
		if(instance == null) {
			synchronized (MyHttpServlet_onInitServer.class) {
				if(instance == null) {
					instance = Static.getInstance(MyHttpServlet_onInitServer.class);
					instance.init(context);	
				}
			}

		}
		
		 
		return instance;
	}
	

	public PropertiesTools getProperTools() {
		return properTools;
	}
 

	public IDBConnectionManager getConnMgr() {
		return connMgr;
	}

	public boolean existContext() {
		Context initContext;
		boolean ok = true;
		
		try {
			initContext = new InitialContext();
			Context c = (Context)initContext;
			return c.lookup("java:/comp/env") != null; 
		}
		catch (NoInitialContextException e) {
			ok = false;
		}
		catch (NamingException e) {
			ok = false;
		}
				
		return ok;
	}
	
	public String getRealPath(String path) {
		return ContextInfo.getInstance().getRealPath(path);
	}
	
	public String getServletContext(ServletContext sContext) {
		 try {
			String fullPath = Validar.getInstance().validarDato(sContext.getResource("/").getPath(),"");
			String[] partes = fullPath.split("/");
			if(partes.length >= 4) {
				return "/"+partes[2]+"/"+partes[3];
			}
		 } catch (MalformedURLException e) {
			e.printStackTrace();
		 }
		
		 return "";
	}
}
