package portal.com.eje.tools;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cl.ejedigital.tool.correo.CorreoDispatcher;
import cl.ejedigital.tool.correo.CorreoProcessPropertie;
import cl.ejedigital.tool.correo.CorreoProcessPropertieSSL;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.ifaces.ICorreoProcess;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;

public class SendMail {

	public SendMail() 
	{ }
	
	public void send(String Mensaje) {
		 try {
			  proper = ResourceBundle.getBundle("db");
		      Properties props = new Properties();
		      props.put("mail.host", proper.getString("traspaso.mailhost"));
		       
		      Session mailConnection = Session.getInstance(props, null);
		      Message msg = new MimeMessage(mailConnection);

		      Address bill = new InternetAddress(proper.getString("traspaso.origenmail"),proper.getString("traspaso.origenalias"));
		      Address elliotte = new InternetAddress(proper.getString("traspaso.destinomail"),proper.getString("traspaso.destinoalias"));
		      Address ccopia = new InternetAddress(proper.getString("traspaso.origenmail"),proper.getString("traspaso.origenalias"));
		    
		      msg.setContent("","text/plain");
		      msg.setFrom(bill);
		      msg.setRecipient(Message.RecipientType.TO, elliotte);
		      msg.setRecipient(Message.RecipientType.CC, ccopia);
		      msg.setSubject("Informacion Traspaso Portales");

		      msg.setText(proper.getString("traspaso.nombreportal") + " : " + Mensaje);
		      Transport.send(msg);
		    }
		    catch (Exception ex) {
		      ex.printStackTrace(); 
		    }	
	}

	public void sendAvisoBloqueo(String rut, Connection con, String tipo) {
	
			  proper = ResourceBundle.getBundle("db");

		      List<IVoDestinatario> list = new ArrayList<IVoDestinatario>();
		      list.add(new VoDestinatario("Administrador Copia Oculta","ejedigital.cl@gmail.com",RecipientType.TO));
		      list.add(new VoDestinatario(proper.getString("clave.destinoalias"),proper.getString("clave.destinomail"),RecipientType.TO));
		      list.add(new VoDestinatario(proper.getString("clave.destino2alias"),proper.getString("clave.destino2mail"),RecipientType.BCC));
		      
		      String mensaje ="Estimado(a) :\n\n\r" +
		                      "Se informa que el usuario\n\n\r" +
		                      "Rut: " + rut + "-" + ExtrasOrganica.DigVerEncargadoUnidad(rut, con) + ", " +
		                      "Nombre : " + ExtrasOrganica.NombreTrabajador(rut, con) + "\n\n\r" +
		                      "ha sido bloqueado para acceder a Portal de Autoservicio por " + tipo + ".\n\n\r" +
		                      "Por favor ingresar a Portal y regularizar situación del usuario.\n\n\r"+
		                      "Administrador Portales Ejedigital"; 
		      
		  
		      ICorreoBuilder cb = new AvisoBloqueo("Aviso Bloqueo Acceso a Portal ",
		    	  								mensaje, list);
		  		
			  ICorreoProcess cp = new CorreoProcessPropertie(cb);
			  boolean ok = false;
			  try {
				ok = CorreoDispatcher.getInstance().sendMail(cp);
			  } catch (MessagingException e) {
				ok = false;
			  }

	}

	public void resetMessage() 
	{ }
	
	public void addMessage(String text) 
	{ }
	
	public String readMessage() {
		return "";
	}
	
	private ResourceBundle proper;
}

class AvisoBloqueo implements ICorreoBuilder {
	private String asunto;
	private String body;
	private List<IVoDestinatario> destinos;
	
	public AvisoBloqueo(String asunto, String body, List<IVoDestinatario> destinos ) {
		this.asunto = asunto;
		this.body = body;
		this.destinos = destinos;
	}
	
	
	public List<IVoDestinatario> getDestinatarios() {
		return destinos;
	}

	public String getAsunto() {
		return asunto;
	}

	public List<File> getArchivosAdjuntos() {
		return new ArrayList<File>();
	}

	public String getBody() {
		return this.body;
	}
	
}
