package cl.ejedigital.tool.correo.vo;

import java.sql.Date;

import javax.mail.Message;

 
public interface IVoDestinatario {

	public Integer getRut();
	
	public String getNombre();
	
	public String getEmail();
	
	public Message.RecipientType getTipo();
	
	public boolean isValidMail();
	
	public Date getFechaActualizacion();
	
}
