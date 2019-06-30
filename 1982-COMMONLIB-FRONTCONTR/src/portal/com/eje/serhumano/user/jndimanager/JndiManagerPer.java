package portal.com.eje.serhumano.user.jndimanager;

import java.io.Serializable;

public class JndiManagerPer implements IJndiManager, Serializable {


	public String getJndi(int rut) {
		return "portal";
	}
	
}
