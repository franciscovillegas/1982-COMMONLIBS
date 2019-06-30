package cl.eje.bootstrap.v3x.modo1;

import cl.eje.bootstrap.ifaces.IPanelResource;
import cl.eje.bootstrap.ifaces.IPanelsResources;
import cl.eje.bootstrap.v3x.modo1.paneles.Panel;
import portal.com.eje.portal.factory.Weak;

public class Paneles implements IPanelsResources {

	public static Paneles getIntance() {
		return Weak.getInstance(Paneles.class);
	}

	@Override
	public IPanelResource getPanel() {
		return Panel.getIntance();
	}
}
