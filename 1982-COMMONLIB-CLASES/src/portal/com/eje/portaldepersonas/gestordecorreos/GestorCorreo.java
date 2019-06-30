package portal.com.eje.portaldepersonas.gestordecorreos;

public class GestorCorreo {

	public static GestorCorreoManager getManager() {
		return GestorCorreoManager.getInstance();
	}
	
	public static GestorManagerEvents getEvents() {
		return GestorManagerEvents.getInstance();
	}
 
	public static GestorCorreoVista getVistas() {
		return GestorCorreoVista.getInstance();
	}
}
