package cl.eje.bootstrap.v3x.modo1.containers;

import cl.eje.bootstrap.abs.AbsContainerResource;
import portal.com.eje.portal.factory.Weak;

public class Container extends AbsContainerResource {

	public static Container getIntance() {
		return Weak.getInstance(Container.class);
	}

	public Container() {
		setPath("templates/eje/bootstrap/3x/modo1/container/container.html");
		setFromTemplatePath(false);
	}
}
