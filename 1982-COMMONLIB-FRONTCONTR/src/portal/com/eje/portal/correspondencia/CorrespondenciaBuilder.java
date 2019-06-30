package portal.com.eje.portal.correspondencia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import portal.com.eje.vo.CorreoBuilderDefault;

public class CorrespondenciaBuilder extends CorreoBuilderDefault {
	private List<CorrespondenciaProgramacion> listaProgramacion;
	private int cantAgregados;
	
	public CorrespondenciaBuilder(ICorreoBuilder ic) {
		this(ic.getArchivosAdjuntos(), ic.getAsunto(), ic.getBody(), ic.getDestinatarios());
	}
			
	public CorrespondenciaBuilder(List<File> listaArchivos, String asunto,String body, List<IVoDestinatario> destinatarios) {
		super(listaArchivos, asunto, body, destinatarios);
		
		//super.destinatarios = CorrespondenciaLocator.getInstance().getDestinatariosParsed(destinatarios);
 
		initProgramacion();
	}
	
	public void initProgramacion() {
		//CorrespondenciaProgramacion programacion = new CorrespondenciaProgramacion(ECorrespondenciaProgramacionTipo.ejecucion_fechas, new Date());
		listaProgramacion = new ArrayList<CorrespondenciaProgramacion>();
		//listaProgramacion.add(programacion);
		cantAgregados = 0;
		
	}
	
	public CorrespondenciaBuilder(String asunto,String body, List<IVoDestinatario> destinatarios) {
		this(new ArrayList<File>(), asunto, body, destinatarios);
	}
	
	public List<CorrespondenciaProgramacion> addProgramacion(CorrespondenciaProgramacion cp) {
		if(cp != null) {
			if(cantAgregados == 0) {
				this.listaProgramacion.clear();
			}
			this.listaProgramacion.add(cp);
			
			cantAgregados++;
		}
		
		return this.listaProgramacion;
	}

	public List<CorrespondenciaProgramacion> getProgramacion() {
		return listaProgramacion;
	}
 
	
}
