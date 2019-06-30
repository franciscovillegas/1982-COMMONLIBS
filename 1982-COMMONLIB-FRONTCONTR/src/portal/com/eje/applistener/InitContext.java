package portal.com.eje.applistener;

import java.net.MalformedURLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cl.ejedigital.tool.validar.Validar;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet_onInitServer;


public class InitContext implements ServletContextListener {

    public void contextDestroyed(final ServletContextEvent event) {
    	
       System.out.println("[event:contextDestroyed] "+getServletContext(event.getServletContext())+" ["+ ParametroLocator.getInstance().getContext()+"]");
       
       MyHttpServlet_onInitServer.destroyInstance(event.getServletContext());
    }

    public void contextInitialized(ServletContextEvent event) {

    	MyHttpServlet_onInitServer.getInstance(event.getServletContext());
    	
    	System.out.println("[contextInitialized] "+getServletContext(event.getServletContext())+" ["+ ParametroLocator.getInstance().getContext()+"]" + (EModulos.getThisModulo() != EModulos.nodefinido ? " !!!OK" : " !!!NO DEFINIDO"));
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