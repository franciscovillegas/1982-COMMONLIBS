package intranet.com.eje.qsmcom.estructuras;

import java.util.ResourceBundle;

public class Debug {
	
	public Debug() {
			
	}

	public boolean isDebug() {
		return  "true".equals(ParametrosObligatorios.ISDEBUG.toString()) ? true : false;
	}

	public String getMail() {
		return ParametrosObligatorios.MAIL.toString();
	}
	
	
	
}
