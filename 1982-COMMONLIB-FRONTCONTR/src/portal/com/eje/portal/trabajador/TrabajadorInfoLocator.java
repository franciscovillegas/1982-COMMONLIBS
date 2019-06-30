package portal.com.eje.portal.trabajador;

import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.trabajador.ifaces.ITrabajadorInfoPersonal;
import portal.com.eje.portal.trabajador.ifaces.ITrabajadorInfoPersonalHistoria;

public class TrabajadorInfoLocator {
 	
	public static ITrabajadorInfoPersonal getInstance() {
		return Weak.getInstance(TrabajadorInfoPersonal.class);
	}
	
	public static ITrabajadorInfoPersonalHistoria getInstanceh() {
		return Weak.getInstance(TrabajadorInfoPersonalHistoria.class);
	}
	
	public static ITrabajadorTool getTool() {
		return TrabajadorTool.getInstance();
	}
}
