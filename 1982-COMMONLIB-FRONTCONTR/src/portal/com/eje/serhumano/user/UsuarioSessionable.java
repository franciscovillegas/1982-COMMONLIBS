package portal.com.eje.serhumano.user;

import java.io.Serializable;
import java.util.Calendar;

import javax.servlet.http.HttpSession;

import cl.ejedigital.tool.misc.Formatear;

public class UsuarioSessionable implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected final String sessionId;
	protected final String sessionInitDate;

	public UsuarioSessionable(HttpSession sessionId) {
		super();
		this.sessionId = sessionId != null ? sessionId.getId() : null;
		this.sessionInitDate = Formatear.getInstance().toDate(Calendar.getInstance().getTime(),"ddMMyyyy HH:mm:ss.SSS");
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getSessionInitDate() {
		return sessionInitDate;
	}

	 
	
	
}