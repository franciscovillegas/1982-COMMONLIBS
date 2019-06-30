package portal.com.eje.frontcontroller;

import org.apache.log4j.Logger;

import portal.com.eje.portal.parametro.ParametroLocator;
import cl.ejedigital.consultor.MyString;

public abstract class IOLog {
	
	public void logInfo(Class<?> clazz, String message) {
		Logger log = Logger.getLogger(clazz.getName());
		MyString my = new MyString();
		log.info("["+my.rellenaCadena(ParametroLocator.getInstance().getModuloContext(), ' ', 21)+"] "+message );
		System.out.println("["+ParametroLocator.getInstance().getModuloContext()+"] "+message);
	}
	
	public void logError(Class<?> clazz, String message) {
		Logger log = Logger.getLogger(clazz.getName());
		MyString my = new MyString();
		log.error("["+my.rellenaCadena(ParametroLocator.getInstance().getModuloContext(), ' ', 21)+"] "+message );
		System.out.println("["+ParametroLocator.getInstance().getModuloContext()+"] "+message);
	}

}
