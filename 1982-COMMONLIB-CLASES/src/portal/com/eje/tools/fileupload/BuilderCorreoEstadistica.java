package portal.com.eje.tools.fileupload;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.mail.internet.MimeMessage.RecipientType;

import portal.com.eje.tools.fileupload.vo.EstadisticaReplicacion;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.vo.VoDestinatario;
import freemarker.template.SimpleHash;

/**
 * Cambiado por su homologá en el paquete cl.ejedigital.web.
 * 
 * @deprecated 
 * 
 * */
public class BuilderCorreoEstadistica implements ICorreoBuilder {

	private EstadisticaReplicacion	est;

	public BuilderCorreoEstadistica(EstadisticaReplicacion est) {

		this.est = est;
	}

	public List getDestinatarios() {
		List lista = new ArrayList();
		ResourceBundle properCorreo = ResourceBundle.getBundle("replication");

		String mails = properCorreo.getString("replication.email");
		String[] aMails = mails.split("\\,");
		for(int i = 0; i < aMails.length; i++) {
			lista.add(new VoDestinatario("",aMails[i],RecipientType.TO));
		}
		return lista;
	}

	public List getArchivosAdjuntos() {
		return null;
	}

	public String getAsunto() {
		String machineName = null;
		try {
			machineName = InetAddress.getLocalHost().getHostName();
		}
		catch (UnknownHostException e) {
			System.out.println("No se pudo reconocer el nombre de la maquina ");
		}
		
		return "[origen:"+machineName+"]   Correo de replicacion";
	}

	public String getPathTemplate() {
		return "/correo/avisoReplicacion.html";
	}

	public SimpleHash getModelRoot() {
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("lineas",est.getSimpleList());

		return modelRoot;
	}

	public String getBody() {
		// TODO Auto-generated method stub
		return null;
	}

}