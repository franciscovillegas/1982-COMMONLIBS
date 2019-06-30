package cl.ejedigital.tool.correo;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import cl.ejedigital.tool.correo.ifaces.ACorreoProcess;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;

public class CorreoProcessDefault extends ACorreoProcess {

	public CorreoProcessDefault() {
		
	}
	
	public CorreoProcessDefault(ICorreoBuilder cCorreo) {
		super(cCorreo);
	}
	
	public void initServidor() {
		setMailHost("192.168.1.120");

		Properties props = new Properties();
		props.put("mail.host",getMailHost());
		Session mailConnection = Session.getInstance(props,null);
		setMsg(new MimeMessage(mailConnection));
	}

	public boolean sendMsg() {
		boolean tx = true;

		Message msg = getMessage();

		try {
			Transport.send(msg);
		}
		catch (MessagingException e) {
			e.printStackTrace();
		}

		return tx;
	}

	public boolean sendMsg_catchError() throws MessagingException {
		boolean tx = true;

		Message msg = getMessage();
	 
		Transport.send(msg);

		return tx;
	}
	
}
