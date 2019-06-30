package portal.com.eje.vo;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;

public class CorreoBuilderDefault implements ICorreoBuilder, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected List<File> listaArchivos;
	protected String asunto;
	protected String body;
	protected List<IVoDestinatario> destinatarios;
	

	public CorreoBuilderDefault(List<File> listaArchivos, String asunto, String body, List<IVoDestinatario> destinatarios) {
		super();
		this.listaArchivos = listaArchivos;
		this.asunto = asunto;
		this.body = body;
		this.destinatarios = destinatarios;
	}
	
	public CorreoBuilderDefault(String asunto, String body, List<IVoDestinatario> destinatarios) {
		super();
		this.listaArchivos = new ArrayList<File>();
		this.asunto = asunto;
		this.body = body;
		this.destinatarios = destinatarios;
	}

	public List<File> getArchivosAdjuntos() {
		if( listaArchivos == null ) {
			return new ArrayList<File>();
		}
		else {
			return listaArchivos;	
		}
		
	}

	public String getAsunto() {
		return asunto;
	}

	public String getBody() {
		return body;
	}

	public List<IVoDestinatario> getDestinatarios() {
		if( destinatarios == null ) {
			return new ArrayList<IVoDestinatario>();
		}
		else {
			return destinatarios;	
		}
	}

	public void setDestinatarario(List<IVoDestinatario> lista) {
		if(lista != null) {
			this.destinatarios = lista;
		}
	}
	
}
