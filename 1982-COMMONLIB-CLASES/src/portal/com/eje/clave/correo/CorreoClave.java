package portal.com.eje.clave.correo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;


public class CorreoClave implements ICorreoBuilder {
	private String clave;
	private VoDestinatario vo;
	
	public CorreoClave(String clave, VoDestinatario vo) {
		this.clave = clave;
		this.vo = vo;
	}
	
	public List<File> getArchivosAdjuntos() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAsunto() {		
		return "[EJEDIGITAL] recuperación de clave";
	}

	public String getBody() {
		return "Su clave es:".concat(clave);
	}

	public List<IVoDestinatario> getDestinatarios() {
		
		List<IVoDestinatario> lista = new ArrayList<IVoDestinatario>();
		lista.add(vo);
		return lista;
	}

}
