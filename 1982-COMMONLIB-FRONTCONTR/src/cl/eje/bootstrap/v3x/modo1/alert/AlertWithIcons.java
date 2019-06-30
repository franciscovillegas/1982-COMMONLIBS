package cl.eje.bootstrap.v3x.modo1.alert;

import cl.eje.bootstrap.abs.AbsResource;
import cl.eje.bootstrap.alert.IAlertResource;
import portal.com.eje.portal.factory.Weak;

public class AlertWithIcons extends AbsResource implements IAlertResource {
	
	public static AlertWithIcons getIntance() {
		return Weak.getInstance(AlertWithIcons.class);
	}

	public AlertWithIcons() {
		setPath("templates/eje/bootstrap/3x/modo1/alert/alertWithIcons.html");
		setFromTemplatePath(false);
	}  

}
