package portal.com.eje.frontcontroller;

public enum EMessageHtmlLevel {
	error("error","exclamation.ico"),
	info("info","information.ico"),
	warning("warning","warning.ico");
	private String icon;
	private String type;
	
	EMessageHtmlLevel(String type, String iconName) {
		this.icon = iconName;
		this.type = type;
	}
	
	public String toString() {
		return  this.icon;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getIcon() {
		return this.icon;
	}
	
}
