package portal.com.eje.tools.consola;

import java.sql.SQLException;

import portal.com.eje.tools.consola.ifaces.IConsola;


public class Consola implements IConsola {
	private static IConsola consola;
	
	private Consola() {
		
	}
	
	public static IConsola getInstance() {
		if(consola == null) {
			consola = new Consola();
		}
		
		return consola;
		
	}
	
	public void printError(Exception e) {
		System.out.println("[Exception]" + e);
	}

	public void printError(SQLException e) {
		System.out.println("[SQLException]" + e);
		
	}

	public void printWarning(String warn) {
		System.out.println("[WARNING]" + warn);
		
	}

	public void printInfo(String info) {
		System.out.println("[INFORMATION]" + info);
		
	}

}
