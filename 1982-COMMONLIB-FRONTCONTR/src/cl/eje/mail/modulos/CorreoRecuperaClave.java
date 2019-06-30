package cl.eje.mail.modulos;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.mail.Message.RecipientType;
import javax.servlet.ServletException;

import org.apache.commons.lang.WordUtils;

import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;

public class CorreoRecuperaClave implements ICorreoBuilder {
	protected IOClaseWeb cw;
	protected VoDestinatario vo;
	protected String pass;
	protected String rutDestinatario;
	protected String nombre;
	
	public CorreoRecuperaClave(IOClaseWeb cw, String rutDestinatario, String nombre, String email, String password ) {
		this.pass = password;
		this.cw = cw;
		int rut = 0;
		if(rutDestinatario != null && rutDestinatario.indexOf("-") != -1 ) {
			rut = Validar.getInstance().validarInt(rutDestinatario.substring(0, rutDestinatario.indexOf("-")),-1);
		}

		
		this.vo = new VoDestinatario(rut, nombre, email, RecipientType.TO);
		this.rutDestinatario = rutDestinatario;
		this.nombre = nombre;
	}
	
	public List<File> getArchivosAdjuntos() {
		List<File> f = new ArrayList<File>();
		return  f;
	}

	public String getAsunto() {
		ResourceBundle rb = ResourceBundle.getBundle("mail");
		return rb.getString("asunto.autorecuperacion");
	}

	public String getBody() {
		FreemakerTool free = new FreemakerTool();
		ResourceHtml html = new ResourceHtml();

		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("usuario",rutDestinatario );
		modelRoot.put("clave", pass ); 
		modelRoot.put("nombre", WordUtils.capitalizeFully(nombre)); 
		
		ResourceBundle rb = ResourceBundle.getBundle("mail");
		String logo = rb.getString("logo.dominio");
		modelRoot.put("logo", logo ); 
		modelRoot.put("auto", "true" );
 
		
		modelRoot.put("servidor", cw.getUrlContext());
		modelRoot.put("url", cw.getUrlContext());
		modelRoot.put("logo", cw.getUrlContext() + "/Tool?images/new/logos/logo_ppm.png");

		
		try {
			return free.templateProcess(html.getTemplate("mail/correoRecuperaClave.html"), modelRoot);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public List<IVoDestinatario> getDestinatarios() {
		List<IVoDestinatario> lista = new ArrayList<IVoDestinatario>();
		lista.add(vo);
		return lista;
	}
	
}
