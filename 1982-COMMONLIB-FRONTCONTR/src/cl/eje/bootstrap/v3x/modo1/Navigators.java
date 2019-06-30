package cl.eje.bootstrap.v3x.modo1;

import cl.eje.bootstrap.ifaces.INavigatorResource;
import cl.eje.bootstrap.ifaces.INavigators;
import cl.eje.bootstrap.v3x.modo1.portadas.NavLeft;
import cl.eje.bootstrap.v3x.modo1.portadas.NavLeft2;
import cl.eje.bootstrap.v3x.modo1.portadas.NavSimple;
import portal.com.eje.portal.factory.Weak;

public class Navigators implements INavigators {
	
	public static Navigators getIntance() {
		return Weak.getInstance(Navigators.class);
	}
	
	@Override
	public INavigatorResource getDefault() {
		return NavSimple.getIntance();
	}

	@Override
	public INavigatorResource getLeftNavigator() {
		return NavLeft.getIntance();
	}

	@Override
	public INavigatorResource getLeftNavigator2() {
		return NavLeft2.getIntance();
	}
}
