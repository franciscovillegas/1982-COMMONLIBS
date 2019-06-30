package portal.com.eje.movil;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.tallaje.mgr.ManagerTallajeTrabajador;
import cl.ejedigital.tool.correo.CorreoDispatcher;
import cl.ejedigital.tool.correo.CorreoProcessPropertie;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.ifaces.ICorreoProcess;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;
import freemarker.template.SimpleHash;

public class Movil extends AbsClaseWeb {
	
	public Movil(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doPost() throws Exception {
		doGet();
	}

	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		String htm	  = super.getIoClaseWeb().getParamString("htm","Movil/showMovil.htm");
		String rut = super.getIoClaseWeb().getParamString("rut",super.getIoClaseWeb().getUsuario().getRutId());
		 
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		
		if("show".equals(accion)) {
			if("movil".equals(thing)) {
				super.getIoClaseWeb().retTemplate("Movil/Movil.html",modelRoot);
			} 
		}
		else if("send".equals(accion)) {
			
			ResourceBundle proper = ResourceBundle.getBundle("movil");
			
			String resultado = "FAIL";
			String movil = super.getIoClaseWeb().getParamString("movil","android");
			String mail  = super.getIoClaseWeb().getParamString("mail","soporte@ejedigital.cl");
			
			String emailDestinatario = mail;
			String dedicatoriaDestinatario = proper.getString("movil.dedicatoria");
			String nombre = super.getIoClaseWeb().getUsuario().getName() ;
			String asunto = proper.getString("movil.asunto");
			String ruta = super.getIoClaseWeb().getServletContext().getRealPath("/");
			String directorio = proper.getString("movil.directorio");
			String archivo = proper.getString("movil." + movil);
			String url = proper.getString("movil.url");
			
			ICorreoBuilder cb = new CorreoEnvioMovil(asunto,mail,dedicatoriaDestinatario,nombre,ruta,directorio,archivo,url);
			ICorreoProcess cp = new CorreoProcessPropertie(cb);
			boolean ok;
			try {
				ok = CorreoDispatcher.getInstance().sendMail(cp);
			} 
			catch (MessagingException e) {
				e.printStackTrace();
				ok = false;
			}
			
			if(ok) {
				resultado = "OK";
			}
			super.getIoClaseWeb().retTexto("[ { \"sending\" : \"" + resultado + "\"} ]");
		}
	}
		
}


class CorreoEnvioMovil implements ICorreoBuilder {
	private String emailDestinatario;
	private String dedicatoriaDestinatario;
	private String nombreAmigo;
	private String asunto;
	private String ruta;
	private String directorio;
	private String archivo;
	private String url;
	private VoDestinatario vo;
	
	public CorreoEnvioMovil(String asunto, String email, String mensaje, String amigo, String ruta, String directorio, String archivo, String url) {
		this.emailDestinatario = email;
		this.dedicatoriaDestinatario = mensaje;
		this.nombreAmigo = amigo;
		this.vo = new VoDestinatario("Portal Movil", email, RecipientType.TO);
		this.asunto = asunto;
		this.ruta = ruta;
		this.directorio = directorio;
		this.archivo = archivo;
		this.url = url;
	}
	
	public String getAsunto() {
		return asunto;
	}

	public String getBody() {
		String msg = "Estimado(a) " + this.nombreAmigo + ": <br><br>" +
					"</b> Se adjuntan los datos necesarios para poder instalar la versión movil de Portal.<br><br>" + 
					"Peoplemanager.";
		if(this.archivo.equals("none")) {
			msg = "Estimado(a) " + this.nombreAmigo + ": <br><br>" +
					"</b> Para su dispositivo aún no está activo el instalador, por lo cual por favor ingrese " +
					"en su navegador de smartphone la siguiente URL para la versión movil de Portal:<br><br>" +
					"<a href='" + this.url + "'>" + this.url + "</a><br><br>Peoplemanager.";
		}
		return msg;
	}

	public List<File> getArchivosAdjuntos() {
		List<File> adjuntos = new ArrayList<File>();
		try {
			if(!this.archivo.equals("none")) {
				String ruta1 = this.ruta + this.directorio + this.archivo;
				File file = new File(ruta1);  
				adjuntos.add(file);
			}
			else {
				adjuntos = null;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		return adjuntos;
	}

	public List<IVoDestinatario> getDestinatarios() {
		List<IVoDestinatario> lista = new ArrayList<IVoDestinatario>();
		lista.add(vo);
		return lista;
	}
	
}

