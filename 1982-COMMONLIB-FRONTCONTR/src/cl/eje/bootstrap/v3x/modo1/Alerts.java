package cl.eje.bootstrap.v3x.modo1;

import cl.eje.bootstrap.alert.IAlertResource;
import cl.eje.bootstrap.alert.IAlerts;
import cl.eje.bootstrap.v3x.modo1.alert.Alert;
import cl.eje.bootstrap.v3x.modo1.alert.AlertWithIcons;
import portal.com.eje.portal.factory.Weak;

public class Alerts implements IAlerts {

	public static Alerts getIntance() {
		return Weak.getInstance(Alerts.class);
	}

	@Override
	public IAlertResource getDefault() {
		return Alert.getIntance();
	}

	@Override
	public IAlertResource getAlertWithIcons() {
		return AlertWithIcons.getIntance();
	}
}
