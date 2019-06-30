package cl.ejedigital.web.datos.ifaces;

public interface IDriverTool {

	/**
	 * Retorna el driver en forma cannonicalName
	 * */
	public String getClassConnectionDriver();
	
	/**
	 * Retorna la url de conexion
	 * */
	public String getUrl(String host, String puerto, String conexionbd);

	public String getDataBaseFromUrl(String url);
}
