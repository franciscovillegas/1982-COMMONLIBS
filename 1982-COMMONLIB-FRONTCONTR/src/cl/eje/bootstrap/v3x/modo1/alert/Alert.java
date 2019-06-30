package cl.eje.bootstrap.v3x.modo1.alert;

import cl.eje.bootstrap.abs.AbsResource;
import cl.eje.bootstrap.alert.IAlertResource;
import portal.com.eje.portal.factory.Weak;

public class Alert extends AbsResource implements IAlertResource {
	
	public static Alert getIntance() {
		return Weak.getInstance(Alert.class);
	}

	public Alert() {
		setPath("templates/eje/bootstrap/3x/modo1/alert/alert.html");
		setFromTemplatePath(false);
	}  

}
