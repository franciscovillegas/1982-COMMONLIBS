package cl.ejedigital.tool.correo.ifaces;

import javax.mail.Message;
import javax.mail.MessagingException;



public interface ICorreoProcess {

	public void initServidor();
	public void initDesde();
	public void initDestino(ICorreoBuilder cBuilder);
	public void initAsunto(ICorreoBuilder cBuilder);
	public void initBody(ICorreoBuilder cBuilder);
	public void initFilesAdjuntos(ICorreoBuilder cBuilder);
	public void setMailDesde(String mailDesde);
	public void setNombreDesde(String mailNombreDesde);
	public String getMailDesde();
	public String getMailNombreDesde();
	
	abstract public Message getMessage();
	
	abstract public boolean sendMsg();
	
	abstract public boolean sendMsg_catchError() throws MessagingException;
}
