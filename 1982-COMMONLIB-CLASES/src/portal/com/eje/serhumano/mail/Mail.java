package portal.com.eje.serhumano.mail;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;
import javax.servlet.ServletException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.correo.CorreoDispatcher;
import cl.ejedigital.tool.correo.CorreoProcessPropertie;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.ifaces.ICorreoProcess;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import portal.com.eje.serhumano.saludos.admin.MgrTemplate;
import portal.com.eje.usuario.UsuarioImagen;


public class Mail extends AbsClaseWeb {

	public Mail(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		sendCorreo();
		
	}

	@Override
	public void doGet() throws Exception {
		sendCorreo();
		
	}

	
	private void sendCorreo() {
		String nombre = super.getIoClaseWeb().getParamString("nombre","");
		String mail  = super.getIoClaseWeb().getParamString("mail","");
		String fono	   = super.getIoClaseWeb().getParamString("fono","");
		String texto   = super.getIoClaseWeb().getParamString("texto","");
		
		FreemakerTool tool = new FreemakerTool();
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("nombre",nombre);
		
		StringBuilder str = new StringBuilder();
		str.append(" Nombre:").append( nombre ).append("<br/>");
		str.append(" Mail:").append( mail ).append("<br/>");
		str.append(" Fono:").append( fono ).append("<br/>");
		str.append(" Texto:").append( texto ).append("<br/>");
		
		modelRoot.put("mensaje", str.toString());
		
		UsuarioImagen ui = new UsuarioImagen(getIoClaseWeb());
		int id_imagen = ui.getIdFileImagen(super.getIoClaseWeb().getUsuario().getRutIdInt());
		modelRoot.put("id_imagen", String.valueOf(id_imagen));
		modelRoot.put("servlet",	String.valueOf(super.getIoClaseWeb().getReq().getRequestURL()).replaceAll("/EjeCore","/SLoadFile?idfile="+ id_imagen).trim() );
		
		
		Template template = null;
		ResourceHtml rsHtml = new ResourceHtml();
		try {
			
			template =rsHtml.getTemplate("mail/plantillaCorreo.html");
			String html = tool.templateProcess(template , modelRoot);
			
			List<IVoDestinatario> lista = new ArrayList<IVoDestinatario>();
			lista.add(new VoDestinatario("Ejedigital EBCO", "ejedigital.cl@gmail.com" , RecipientType.BCC));
			lista.add(new VoDestinatario("Victoria Alvarez" , "victoria.alvarez@ebco.cl", RecipientType.TO));
			lista.add(new VoDestinatario("NICOLE RUSTOM CARDENAS", "nicole.rustom@ebco.cl" , RecipientType.TO));


			
			CorreoMail correo = new CorreoMail(html, lista, nombre);
			ICorreoProcess correoProcess = new CorreoProcessPropertie(correo);
			CorreoDispatcher.getInstance().sendMail(correoProcess);
			
		} catch (IOException e1) {
			System.out.println(e1);
		} catch (MessagingException e) {
			System.out.println(e);
		} catch (ServletException e) {
			System.out.println(e);
		} catch (SQLException e) {
			System.out.println(e);
		}
	}


	class CorreoMail implements ICorreoBuilder {
		private String html;
		private String colaboradorName;
		private List<IVoDestinatario> lista;
		
		CorreoMail( String html, List<IVoDestinatario> lista , String colaboradorName ) {
			this.html = html;
			this.colaboradorName = colaboradorName;
		
			IVoDestinatario vo2 = new VoDestinatario("Copia de Respaldo", "ejedigital.cl@gmail.com", RecipientType.BCC);
			lista.add(vo2);
			
			this.lista = lista;
		}
		
		public List<File> getArchivosAdjuntos() {
			// TODO Auto-generated method stub
			return new ArrayList<File>();
		}
	
		public String getAsunto() {
			return "[" + colaboradorName + "] ha comentado sobre la intranet";
		}
	
		public String getBody() {
			// TODO Auto-generated method stub
			return html;
		}
	
		public List<IVoDestinatario> getDestinatarios() {
			// TODO Auto-generated method stub
			return lista;
		}
		
	}
}
