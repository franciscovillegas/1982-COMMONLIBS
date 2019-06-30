package portal.com.eje.portal.roldetecterconfig;

import java.util.List;

public class VoRDCMiembro  {
	
	private int idMiembro;
	private List<VoRDCFiltro> filtros;
	private List<VoRDCResponsable> responsables;

	public VoRDCMiembro(int idMiembro, List<VoRDCFiltro> filtros, List<VoRDCResponsable> responsables) {
		super();
		this.idMiembro = idMiembro;
		this.filtros = filtros;
		this.responsables = responsables;
	}

	public List<VoRDCFiltro> getFiltros(){
		return filtros;
	}
	
	public List<VoRDCResponsable> getResponsables(){
		return responsables;
	}
}
