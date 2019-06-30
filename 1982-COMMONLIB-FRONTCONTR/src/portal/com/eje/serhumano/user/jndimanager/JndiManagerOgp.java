package portal.com.eje.serhumano.user.jndimanager;

import java.io.Serializable;
import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;

public class JndiManagerOgp implements IJndiManager, Serializable {

	private static JndiManagerOgp jndiogp;    
	
	private JndiManagerOgp() {  
	}  
	
	public static JndiManagerOgp getInstance() {  
		if(jndiogp==null)  
			jndiogp = new JndiManagerOgp();   
		return jndiogp;  
	}  
	

	public String getJndi(int rut) {  
		String jndi = "portal";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT jndi FROM eje_ges_usuario_jndi WHERE rut_usuario = ").append(rut);
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("PortalGateWay", sql.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(data.next() ) {
			jndi = data.getString("jndi");
		}
		return jndi;
	}  
	 
}
