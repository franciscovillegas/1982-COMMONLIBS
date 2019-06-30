package cl.ejedigital.web.consola.ifaces;

import java.sql.SQLException;


public interface IConsola {
	
	public void printError(Exception e);
	
	public void printError(SQLException e);
	
	public void printWarning(String warn);
	
	public void printInfo(String info);
	
}
