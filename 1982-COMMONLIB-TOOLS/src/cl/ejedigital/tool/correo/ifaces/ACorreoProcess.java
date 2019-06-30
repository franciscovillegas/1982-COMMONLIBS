package cl.ejedigital.tool.correo.ifaces;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import cl.ejedigital.tool.correo.vo.IVoDestinatario;

public abstract class ACorreoProcess implements ICorreoProcess {

	private String	mailHost;
	protected Message	msg;
	private String	mailDesde;
	private String	mailNombreDesde;
	private MimeMultipart multipart;
	private ICorreoBuilder cCorreo;
	private Session session;
	
	public ACorreoProcess() {
		//System.out.println("@@@ ACorreoProcess");
	}
	
	public ACorreoProcess(ICorreoBuilder cCorreo) {
		this();
		//System.out.println("@@@ ACorreoProcess(ICorreoBuilder)");
		if( cCorreo != null) {
			this.cCorreo = cCorreo;
			
			setMailDesde("mail@noreply.cl");
			setNombreDesde("CorreoProcessDefault");
			
			multipart = new MimeMultipart("mixed");
		}
		//System.out.println("@@@ ACorreoProcess(ICorreoBuilder) END");
	}

	public Message getMessage() {
		//System.out.println("@@@ getMessage");
		
		initServidor();
		initDesde();
		initDestino(cCorreo);
		initBody(cCorreo);
		initAsunto(cCorreo);
		initFilesAdjuntos(cCorreo);
		finishMultiPart();
		
		return msg;
	}

	public void setMailDesde(String mailDesde) {
		this.mailDesde = mailDesde;
	}

	public void setNombreDesde(String mailNombreDesde) {
		this.mailNombreDesde = mailNombreDesde;
	}

	public String getMailDesde() {
		return mailDesde;
	}

	public String getMailNombreDesde() {
		return mailNombreDesde;
	}

	protected String getMailHost() {
		return mailHost;
	}

	protected void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	protected Message getMsg() {
		return msg;
	}

	protected void setMsg(Message msg) {
		this.msg = msg;
	}

	protected MimeMultipart getMultipart() {
		return multipart;
	}

	protected void setMultipart(MimeMultipart multipart) {
		this.multipart = multipart;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void initDesde() {
		try {
			msg.setSentDate(Calendar.getInstance().getTime());
			
			msg.setFrom(new InternetAddress(getMailDesde(),getMailNombreDesde()));
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void initDestino(ICorreoBuilder cBuilder) {
		List<IVoDestinatario> lista = cBuilder.getDestinatarios();

		for(int i = 0; i < lista.size(); i++) {
			IVoDestinatario d = lista.get(i);

			try {
				if(d != null && d.getEmail() != null && d.getEmail().indexOf("@") != -1
					&& d.getEmail().indexOf("@") == d.getEmail().lastIndexOf("@")) {

					Address a = new InternetAddress(d.getEmail());
					msg.addRecipient(d.getTipo(),a);
				}
			}
			catch (AddressException e) {
				e.printStackTrace();
			}
			catch (MessagingException e) {
				e.printStackTrace();
			}
		}

	}

	public void initFilesAdjuntos(ICorreoBuilder cBuilder)  {

		try {
			if(cBuilder.getArchivosAdjuntos() != null && cBuilder.getArchivosAdjuntos().size() > 0) {			
				for(File file :  cBuilder.getArchivosAdjuntos()) {
					if(file.exists()) {
				    	try {
				    		MimeBodyPart attachmentPart = new MimeBodyPart();			    	  
					    	FileDataSource fileDataSource = new FileDataSource(file.getAbsolutePath() );
							attachmentPart.setDataHandler(new DataHandler(fileDataSource));
							attachmentPart.setFileName(file.getName());
					        multipart.addBodyPart(attachmentPart);
						}
						catch (MessagingException e) {
							e.printStackTrace();
						}
				    }
				}
				
				msg.setContent(multipart);
			}
		}
		catch (MessagingException e1) {
			e1.printStackTrace();
		}
	}

	public void initAsunto(ICorreoBuilder cBuilder) {
		try {
			msg.setSubject( MimeUtility.encodeText( cBuilder.getAsunto(), "utf-8", "B"));
			
		}
		catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void initBody(ICorreoBuilder cBuilder) {
		

		try {
			//msg.setText(cBuilder.getBody());
			//msg.setContent(cBuilder.getBody(),"text/html");
			
			MimeBodyPart textPart = new MimeBodyPart();
	        textPart.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
	        textPart.setContent(cBuilder.getBody(), "text/html; charset=utf-8");

	        multipart.addBodyPart(textPart);
		}
		catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	private void finishMultiPart()  {
		try {
			msg.setContent(multipart);
		}
		catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
