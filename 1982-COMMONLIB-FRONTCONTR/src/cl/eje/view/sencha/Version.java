package cl.eje.view.sencha;

public enum Version {
	PORDEFECTO("cl.eje.view.sencha"),
	EJEB("cl.eje.view.senchab");
	String version;
	
	Version(String version) {
		this.version = version;
	}
 
	public String getPaquete() {
		// TODO Auto-generated method stub
		return this.version;
	}
}
