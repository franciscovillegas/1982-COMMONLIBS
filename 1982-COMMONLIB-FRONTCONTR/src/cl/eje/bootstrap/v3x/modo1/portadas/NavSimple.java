package cl.eje.bootstrap.v3x.modo1.portadas;

import cl.eje.bootstrap.abs.AbsNavigatorResource;
import portal.com.eje.portal.factory.Weak;

public class NavSimple extends AbsNavigatorResource {

	public static NavSimple getIntance() {
		return Weak.getInstance(NavSimple.class);
	}

	public NavSimple() {
		setPath("eje/bootstrap/3x/modo1/portadas/navsimple/index.html");
		setFromTemplatePath(true);
	}

}
