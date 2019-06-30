package cl.ejedigital.tool.correo;

import javax.mail.MessagingException;

import cl.ejedigital.tool.correo.ifaces.ICorreoProcess;

public class CorreoDispatcher {
	private static CorreoDispatcher instance; 

	private CorreoDispatcher() {}
	
	public static CorreoDispatcher getInstance() {
		if(instance == null) {
			synchronized(CorreoDispatcher.class) {
				if(instance == null) {
					instance = new CorreoDispatcher();
				}				
			}
		}
		
		return instance;		
	}
	

	public boolean sendMail(ICorreoProcess correoProcess) throws MessagingException {
		boolean tx = true;

		
		tx = correoProcess.sendMsg();

		return tx;
	}

	public boolean sendMail_catchError(ICorreoProcess correoProcess) throws MessagingException {
		boolean tx = true;

		
		tx = correoProcess.sendMsg_catchError();

		return tx;
	}
	
}
