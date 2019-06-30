package cl.ejedigital.tool.correo;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import cl.ejedigital.tool.correo.ifaces.ACorreoProcess;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.properties.PropertiesTools;

public class CorreoProcessPropertieSSL extends ACorreoProcess {

	protected Properties props;

	public CorreoProcessPropertieSSL() {
		
	}
	
	public CorreoProcessPropertieSSL(ICorreoBuilder cCorreo) {
		super(cCorreo);
		
		ResourceBundle proper = ResourceBundle.getBundle("mail");
		
		setMailDesde(proper.getString("portal.maildesde"));
		setNombreDesde(proper.getString("portal.nombredesde"));
		
	}
	
	public void initServidor() {
		ResourceBundle proper = ResourceBundle.getBundle("mail");
		PropertiesTools tools = new PropertiesTools(); 
		setMailHost(proper.getString("portal.mailhost"));		
		
		props = new Properties();
		props.put("mail.smtp.host", proper.getString("mail.smtp.host"));
        props.put("mail.stmp.user", proper.getString("mail.stmp.user"));
        props.put("mail.smtp.auth", proper.getString("mail.smtp.auth"));

        props.put("mail.smtp.starttls.enable", proper.getString("mail.smtp.starttls.enable"));
        props.put("mail.smtp.password", proper.getString("mail.smtp.password"));
        props.put("mail.smtp.socketFactory.port", proper.getString("mail.smtp.socketFactory.port"));
        props.put("mail.smtp.socketFactory.class", proper.getString("mail.smtp.socketFactory.class"));
        props.put("mail.smtp.auth", proper.getString("mail.smtp.auth"));
        props.put("mail.smtp.port", proper.getString("mail.smtp.port"));

        setSession(Session.getInstance(props, new SMTPAuthenticator()));
		setMsg(new MimeMessage(getSession()));
	}

	public boolean sendMsg() {
		ResourceBundle proper = ResourceBundle.getBundle("mail");
		boolean ok = false;
		PropertiesTools tools = new PropertiesTools(); 
		
		if ("true".equals(tools.getString(proper, "portal.auth", "false"))) {
			ok = sendMsgValidado();
		}
		else {
			ok = sendMsgNormal();
		}
	
		return ok;
	}
	
	private boolean sendMsgNormal() {
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

	private boolean sendMsgValidado() {
		Authenticator auth = new SMTPAuthenticator();
		Message msg = getMessage();
		boolean ok = true;
		
		try {
			
			Transport transport = getSession().getTransport("smtp");			 
            transport.send(msg);
          
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ok;
	}
	
	public boolean sendMsg_catchError() throws MessagingException {
		Authenticator auth = new SMTPAuthenticator();
		Message msg = getMessage();
		boolean ok = true;
		
		Transport transport = getSession().getTransport("smtp");			 
        transport.send(msg);
        
		return ok;
	}
	
	private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
        	ResourceBundle proper = ResourceBundle.getBundle("mail");
        	
            String username = proper.getString("mail.stmp.user");
            String password = proper.getString("mail.smtp.password");
            return new PasswordAuthentication(username,password);
        }
    }


	
}
