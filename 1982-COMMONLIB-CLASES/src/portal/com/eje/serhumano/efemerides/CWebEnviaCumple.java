package portal.com.eje.serhumano.efemerides;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import cl.ejedigital.tool.correo.CorreoDispatcher;
import cl.ejedigital.tool.correo.CorreoProcessPropertie;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.ifaces.ICorreoProcess;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;


public class CWebEnviaCumple extends AbsClaseWeb {

	public CWebEnviaCumple(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doPost() throws Exception {

				String asunto 		= super.getIoClaseWeb().getParamString("asunto", "");
				String email  		= super.getIoClaseWeb().getParamString("email","");
				String dedicatoria 	= super.getIoClaseWeb().getParamString("comments","");
				boolean ok = false;
				ICorreoBuilder cb = new CorreoDedicatoriaCumple("Feliz Cumpleaños te desea...",email,dedicatoria,super.getIoClaseWeb().getUsuario().getName());
				ICorreoProcess cp = new CorreoProcessPropertie(cb); 
				try {
					ok = CorreoDispatcher.getInstance().sendMail(cp);
				} 
				catch (MessagingException e) {
					e.printStackTrace();
					ok = false;
				}
				
				String msgResultado = ok ? "La Dedicatoria fue enviada con éxito..." : "La Dedicatoria no pudo ser enviada, inténtelo nuevamente...";
			
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("mensaje",msgResultado);
				super.getIoClaseWeb().retJson(map);
		
	}

	@Override
	public void doGet() throws Exception {
		// TODO Auto-generated method stub
		
	}
}

class CorreoDedicatoriaCumple implements ICorreoBuilder {
	private String emailDestinatario;
	private String dedicatoriaDestinatario;
	private String nombreAmigo;
	private String asunto;
	private List<IVoDestinatario> listaDestino;
	
	public CorreoDedicatoriaCumple(String asunto, String email, String mensaje, String amigo) {
		this.emailDestinatario = email;
		this.dedicatoriaDestinatario = mensaje;
		this.nombreAmigo = amigo;
		this.asunto = asunto;
		
		listaDestino = new ArrayList<IVoDestinatario>();
		listaDestino.add(new VoDestinatario("Festejado"	   , email, RecipientType.TO));
		listaDestino.add(new VoDestinatario("Administrador", "ejedigital.cl@gmail.com", RecipientType.BCC));
	}
	
	public String getAsunto() {
		return asunto;
	}

	public String getBody() {
		String msg = "Estimado(a):<br><br>Tu compañero(a) <b>" + this.nombreAmigo + 
					"</b> te a enviado una dedicatoria:<br><br><b><i><ul>\"" +this.dedicatoriaDestinatario + 
					"\"</ul></i></b><br><br> Felicidades.";
		return msg;
	}

	public List<File> getArchivosAdjuntos() {
		return null;
	}

	public List<IVoDestinatario> getDestinatarios() {
		
		return listaDestino;
	}
	
}