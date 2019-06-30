package cl.eje.bootstrap.v3x.modo1.paneles;

import cl.eje.bootstrap.abs.AbsPanelResource;
import portal.com.eje.portal.factory.Weak;

public class Panel extends AbsPanelResource {

	public static Panel getIntance() {
		return Weak.getInstance(Panel.class);
	}

	public Panel() {
		setPath("templates/eje/bootstrap/3x/modo1/panel/Panel.html");
		setFromTemplatePath(false);
	}

}
