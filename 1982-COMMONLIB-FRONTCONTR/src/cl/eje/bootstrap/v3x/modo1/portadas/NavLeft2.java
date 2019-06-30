package cl.eje.bootstrap.v3x.modo1.portadas;

import cl.eje.bootstrap.abs.AbsNavigatorResource;
import portal.com.eje.portal.factory.Weak;

public class NavLeft2 extends AbsNavigatorResource {

	public static NavLeft2 getIntance() {
		return Weak.getInstance(NavLeft2.class);
	}

	public NavLeft2() {
		setPath("eje/bootstrap/3x/modo1/portadas/navleft2/index.html");
		setTitle("NavLeftNavigator 2");
		setFromTemplatePath(true);
	}

}
