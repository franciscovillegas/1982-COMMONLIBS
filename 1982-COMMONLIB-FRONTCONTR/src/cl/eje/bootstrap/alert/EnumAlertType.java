package cl.eje.bootstrap.alert;

public enum EnumAlertType {
	PRIMARY("alert-primary"),
//	/**bootstrap 4*/
//	SECONDARY("alert-secondary"),
	SUCCESS("alert-success"),
	DANGER("alert-danger"),
	WARNING("alert-warning"),
	INFO("alert-info"),
	LIGHT("alert-light");
//	/**bootstrap 4*/
//	DARK("alert-dark");
	
	private String styleClass;

	private EnumAlertType(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getStyleClass() {
		return styleClass;
	}
}
