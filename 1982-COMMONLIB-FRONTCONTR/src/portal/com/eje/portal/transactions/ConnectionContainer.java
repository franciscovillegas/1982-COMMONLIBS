package portal.com.eje.portal.transactions;

import java.sql.Connection;

import portal.com.eje.portal.EModulos;

public class ConnectionContainer {
	private EModulos modulo;
	private String jndi;
	private Connection conn;
	private boolean ended;
	
	ConnectionContainer(EModulos modulo, String jndi, Connection conn) {
		this.modulo = modulo;
		this.jndi = jndi;
		this.conn = conn;
	}

	public EModulos getModulo() {
		return modulo;
	}

	public void setModulo(EModulos modulo) {
		this.modulo = modulo;
	}

	public String getJndi() {
		return jndi;
	}

	public void setJndi(String jndi) {
		this.jndi = jndi;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public boolean isEnded() {
		return ended;
	}

	public void setEnded(boolean ended) {
		this.ended = ended;
	}
 
	
	
}
