package organica.com.eje.datos;

import java.util.ResourceBundle;

public class ConexionOrganica {
	static ConexionOrganica co = new ConexionOrganica();
	String propertyDB;
	
	private ConexionOrganica() {
		ResourceBundle proper;
		try {
			proper = ResourceBundle.getBundle("db");
			propertyDB = proper.getString("database.organica");
		}
		catch(Exception e) {
			System.out.println("No existe la clave de la BD organica");
		}
	}
	
	public ConexionOrganica getConexion() {
		return co;
	}
}
