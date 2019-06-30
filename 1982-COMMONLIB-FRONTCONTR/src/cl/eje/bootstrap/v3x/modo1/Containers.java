package cl.eje.bootstrap.v3x.modo1;

import cl.eje.bootstrap.ifaces.IContainerResource;
import cl.eje.bootstrap.ifaces.IContainers;
import cl.eje.bootstrap.v3x.modo1.containers.Container;
import portal.com.eje.portal.factory.Weak;

public class Containers implements IContainers {

	public static Containers getIntance() {
		return Weak.getInstance(Containers.class);
	}

	@Override
	public IContainerResource getDefault() {
		return Container.getIntance();
	}

}
