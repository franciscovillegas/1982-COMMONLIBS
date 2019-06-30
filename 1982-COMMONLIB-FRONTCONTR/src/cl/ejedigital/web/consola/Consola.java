package cl.ejedigital.web.consola;

import java.sql.SQLException;

import cl.ejedigital.web.consola.ifaces.IConsola;


public class Consola implements IConsola {
	private static IConsola consola;
	private boolean print;
	
	private Consola() {
		print = false;
	}
	
	public static IConsola getInstance() {
		if(consola == null) {
			synchronized(Consola.class) {
				if(consola == null) {
					consola = new Consola();
				}
			}
		}
		
		return consola;
		
	}
	
	public void printError(Exception e) {
		if(print) {
			System.out.println("[Exception]" + e);
		}
	}

	public void printError(SQLException e) {
		if(print) {
			System.out.println("[SQLException]" + e);
		}
		
	}

	public void printWarning(String warn) {
		if(print) {
			System.out.println("[WARNING]" + warn);
		}
		
	}

	public void printInfo(String info) {
		if(print) {
			System.out.println("[INFORMATION]" + info);
		}
		
	}

}
