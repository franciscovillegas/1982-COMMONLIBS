package cl.eje.qsmcom.service;


public enum GatewayJndi {
	portal ("jndiPortal"),
	mac ("jndiMac"),
	winper ("jndiWinper");
	private String jndi;
	
	GatewayJndi(String jndi) {
		this.jndi = jndi;
	}
	
	public String toString() {
		return jndi;
	}

}
