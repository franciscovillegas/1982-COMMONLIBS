package cl.ejedigital.tool.correo;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Address;
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

public class CorreoProcessPropertie extends ACorreoProcess {

	protected Properties props;
	
	public CorreoProcessPropertie() {
		super(null);
		
		//System.out.println("@@@ CorreoProcessPropertie()");
	}
	
	public CorreoProcessPropertie(ICorreoBuilder cCorreo) {
		super(cCorreo);
		
		//System.out.println("@@@ CorreoProcessPropertie(ICorreoBuilder)");
		ResourceBundle proper = ResourceBundle.getBundle("mail");
		
		setMailDesde(proper.getString("portal.maildesde"));
		setNombreDesde(proper.getString("portal.nombredesde"));
		//System.out.println("@@@ CorreoProcessPropertie(ICorreoBuilder) END");
	}
	
	public void initServidor() {
		ResourceBundle proper = ResourceBundle.getBundle("mail");
		PropertiesTools tools = new PropertiesTools(); 
		setMailHost(proper.getString("portal.mailhost"));		

		props = new Properties();
		
		props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", getMailHost());
        props.put("mail.smtp.port",tools.getString(proper, "portal.port", "25"));
        props.put("mail.smtp.auth", tools.getString(proper, "portal.auth", "false"));
                
        if(tools.getString(proper, "portal.localhost", null) != null ) {
        	props.put("mail.smtp.localhost", tools.getString(proper, "portal.localhost", null));
       }
       

		Session mailConnection = Session.getInstance(props,null);
		MimeMessage mime = new MimeMessage(mailConnection);
		
		setMsg(mime);
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
	
	public boolean sendMsg_catchError() throws MessagingException {
		boolean tx = true;

		Message msg = getMessage();

	 
		Transport.send(msg);
 

		return tx;
	}
	

	private boolean sendMsgValidado() {
		Authenticator auth = new SMTPAuthenticator();
		Message msg = getMessage();
		Session mailSession = Session.getDefaultInstance(props, auth);
		boolean ok = true;
		
		try {
			Transport transport = mailSession.getTransport();
			transport.connect();
			
			Address[] to  = msg.getRecipients(Message.RecipientType.TO);
			Address[] cc  = msg.getRecipients(Message.RecipientType.CC);
			Address[] bcc = msg.getRecipients(Message.RecipientType.BCC);
			List<Address> all = new ArrayList<Address>();
			
			addIntoList(to,all);
			addIntoList(cc,all);
			addIntoList(bcc,all);
			
			transport.sendMessage(msg, getAddresses(all) );
			transport.close();           
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ok;
	}
	
	private void addIntoList(Address[] way,List<Address> lista) {
		if(way != null) {
			for(int i = 0 ; i<way.length; i++) {
				lista.add(way[i]);
			}
		}
	}
	
	private Address[] getAddresses(List<Address> lista) {
		Address[] arrayAll = new Address[lista.size()];
		lista.toArray(arrayAll);
		
		return arrayAll;
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
