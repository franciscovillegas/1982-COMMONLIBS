package cl.eje.mail.modulos;

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

public class CorreoProcessBase extends ACorreoProcess {

	protected Properties props;
	private Exception e;
	
	public CorreoProcessBase(ICorreoBuilder cCorreo) {
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

        props.put("mail.smtp.host", proper.getString("mailcliente.smtp.host"));
        props.put("mail.stmp.user", proper.getString("mailcliente.smtp.user"));
        props.put("mail.smtp.auth", proper.getString("mailcliente.smtp.auth"));
        props.put("mail.smtp.password", proper.getString("mailcliente.smtp.password"));
 
        Session mailConnection = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
            	ResourceBundle proper = ResourceBundle.getBundle("mail");
            	
                String username = proper.getString("mailcliente.smtp.user");
                String password = proper.getString("mailcliente.smtp.password");
                return new PasswordAuthentication(username,password);
            }
        });
        
        
        setSession(mailConnection);
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
			this.e = e;
		}

		return tx;
	}
	

	@Override
	public boolean sendMsg_catchError() throws MessagingException {
		boolean tx = true;

		Message msg = getMessage();
 	 
		Transport.send(msg);
		 

		return tx;
	}
	
	
	public Exception getLastException() {
		return this.e;
	}
	
	private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
        	PropertiesTools tools = new PropertiesTools(); 
        	
        	ResourceBundle proper = ResourceBundle.getBundle("mail");
    		String username = tools.getString(proper, "portal.usuario", "");
    		String password = tools.getString(proper, "portal.password", "");
    		
           return new PasswordAuthentication(username, password);
        }
    }

	
}