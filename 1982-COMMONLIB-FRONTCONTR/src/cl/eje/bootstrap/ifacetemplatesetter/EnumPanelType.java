package cl.eje.bootstrap.ifacetemplatesetter;

public enum EnumPanelType {
	DEFAULT("panel-default"),
	PRIMARY("panel-primary"),
	SUCCESS("panel-success"),
	INFO("panel-info"),
	WARNING("panel-warning"),
	DANGER("panel-danger");
	
	private String styleClass;

	private EnumPanelType(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getStyleClass() {
		return styleClass;
	}

	 
	
	
}
