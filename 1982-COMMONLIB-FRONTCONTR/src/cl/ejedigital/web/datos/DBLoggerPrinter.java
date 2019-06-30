package cl.ejedigital.web.datos;

import org.apache.log4j.Logger;

class DBLoggerPrinter {
	private static final String DEF = "Creando definici�n para : ";
	private static final String GETCONNECTION = "getConnection : ";
	
	/**
	 * Es la impresi�n que ocurre al iniciar la definici�n del pool de una conexi�n
	 * @author Pancho
	 * */
	static void printINIT_DEF(Logger logger, String jndi, String msg) {
		if(logger != null && jndi != null) {
			logger.debug(DEF+jndi+"   "+msg);
		}
	}
	
	static void printGettingConnection(Logger logger, String jndi, String msg) {
		if(logger != null && jndi != null) {
			logger.debug(GETCONNECTION+jndi+"   "+msg);
		}
	}
	
}
