package cl.eje.bootstrap.v3x.modo1.portadas;

import cl.eje.bootstrap.abs.AbsNavigatorResource;
import portal.com.eje.portal.factory.Weak;

public class NavLeft extends AbsNavigatorResource {

	public static NavLeft getIntance() {
		return Weak.getInstance(NavLeft.class);
	}

	public NavLeft() {
		setPath("eje/bootstrap/3x/modo1/portadas/navleft/index.html");
		setTitle("NavLeftNavigator");
		setFromTemplatePath(true);
	}

}
