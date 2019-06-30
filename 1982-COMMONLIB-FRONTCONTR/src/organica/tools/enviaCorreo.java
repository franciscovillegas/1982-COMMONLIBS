package organica.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

// Referenced classes of package tools:
//            ByteArrayDataSource, OutMessage

public class enviaCorreo
{

    public enviaCorreo(String to, String subject, String from, Template template, SimpleHash root, String NameFile)
    {
        proper = ResourceBundle.getBundle("Matico");
        String mailhost = proper.getString("mailhost.name");
        ByteArrayOutputStream uddata = new ByteArrayOutputStream(40000);
        PrintWriter ud = new PrintWriter(uddata);
        try {
			template.process(root, ud);
		}  catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        ud.close();
        String mensaje = uddata.toString();
        String cc = null;
        String bcc = null;
        String url = null;
        String mailer = "sendhtml";
        String protocol = null;
        String host = null;
        String user = null;
        String password = null;
        try
        {
            Properties props = System.getProperties();
            if(mailhost != null)
                props.put("mail.smtp.host", mailhost);
            System.err.println("------->MailHost Correo: ".concat(String.valueOf(String.valueOf(mailhost))));
            Session session = Session.getDefaultInstance(props, null);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject);
            msg.setDataHandler(new DataHandler(new ByteArrayDataSource(mensaje, "text/html")));
            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());
            msg.setFileName(NameFile);
            Transport.send(msg);
            System.out.println("subject: ".concat(String.valueOf(String.valueOf(subject))));
        }
        catch(Exception e)
        {
            OutMessage.OutMessagePrint("enviaCorreo: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
    }

    public enviaCorreo(String to, String subject, String from, Template template, SimpleHash root)
    {
        proper = ResourceBundle.getBundle("Matico");
        String mailhost = proper.getString("mailhost.name");
        ByteArrayOutputStream uddata = new ByteArrayOutputStream(20000);
        PrintWriter ud = new PrintWriter(uddata);
        try {
			template.process(root, ud);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        ud.close();
        String mensaje = uddata.toString();
        String cc = null;
        String bcc = null;
        String url = null;
        String mailer = "sendhtml";
        String protocol = null;
        String host = null;
        String user = null;
        String password = null;
        try
        {
            Properties props = System.getProperties();
            if(mailhost != null)
                props.put("mail.smtp.host", mailhost);
            Session session = Session.getDefaultInstance(props, null);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject);
            msg.setDataHandler(new DataHandler(new ByteArrayDataSource(mensaje, "text/html")));
            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());
            Transport.send(msg);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private ResourceBundle proper;
}