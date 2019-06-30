package cl.eje.qsmcom.modulos;

import cl.ejedigital.tool.correo.ifaces.ACorreoProcess;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.properties.PropertiesTools;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public class CorreoProcessFresenius
  extends ACorreoProcess
{
  protected Properties props;
  
  public CorreoProcessFresenius(ICorreoBuilder cCorreo)
  {
    super(cCorreo);
    
    ResourceBundle proper = ResourceBundle.getBundle("mail");
    
    setMailDesde(proper.getString("portal.maildesde"));
    setNombreDesde(proper.getString("portal.nombredesde"));
  }
  
  public void initServidor()
  {
    ResourceBundle proper = ResourceBundle.getBundle("mail");
    PropertiesTools tools = new PropertiesTools();
    setMailHost(proper.getString("portal.mailhost"));
    
    this.props = new Properties();
    
    this.props.put("mail.smtp.host", proper.getString("mailcliente.smtp.host"));
    this.props.put("mail.stmp.user", proper.getString("mailcliente.smtp.user"));
    this.props.put("mail.smtp.auth", proper.getString("mailcliente.smtp.auth"));
    this.props.put("mail.smtp.password", proper.getString("mailcliente.smtp.password"));
    this.props.put("mail.smtp.auth", proper.getString("mailcliente.smtp.auth"));
    this.props.put("mail.smtp.port", proper.getString("mailcliente.smtp.port"));
    
    Session mailConnection = Session.getInstance(this.props, new Authenticator()
    {
      protected PasswordAuthentication getPasswordAuthentication()
      {
        ResourceBundle proper = ResourceBundle.getBundle("mail");
        
        String username = proper.getString("mailcliente.smtp.user");
        String password = proper.getString("mailcliente.smtp.password");
        return new PasswordAuthentication(username, password);
      }
    });
    setSession(mailConnection);
    setMsg(new MimeMessage(mailConnection));
  }
  
  public boolean sendMsg()
  {
    boolean tx = true;
    
    Message msg = getMessage();
    try
    {
      Transport.send(msg);
    }
    catch (MessagingException e)
    {
      e.printStackTrace();
    }
    return tx;
  }
  
  private class SMTPAuthenticator
    extends Authenticator
  {
    private SMTPAuthenticator() {}
    
    public PasswordAuthentication getPasswordAuthentication()
    {
      PropertiesTools tools = new PropertiesTools();
      
      ResourceBundle proper = ResourceBundle.getBundle("mail");
      String username = tools.getString(proper, "portal.usuario", "");
      String password = tools.getString(proper, "portal.password", "");
      
      return new PasswordAuthentication(username, password);
    }
  }

@Override
public boolean sendMsg_catchError() throws MessagingException {
	// TODO Auto-generated method stub
	return false;
}
}
