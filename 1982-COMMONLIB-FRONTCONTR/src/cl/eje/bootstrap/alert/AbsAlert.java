package cl.eje.bootstrap.alert;

import cl.eje.bootstrap.abs.AbsTemplateSetter;

public class AbsAlert extends AbsTemplateSetter implements IAlert {

	private EnumAlertType alertType;

	public AbsAlert() {
		setAlertType(EnumAlertType.INFO);
		setHeader(true);
		setTitle("a");
		setBody("b");
		setFooter("c");
	}

	@Override
	public EnumAlertType getAlertType() {
		return alertType;
	}

	@Override
	public void setAlertType(EnumAlertType alertType) {
		this.alertType = alertType;
	}

}
