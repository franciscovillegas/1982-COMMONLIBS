package portal.com.eje.portal.roldetecterconfig;

public class VoRDCResponsable  {
	
	private VoRDCParametro parametro;
	private VoRDCParametro.Ticket dtaTicket;
	private String strCodigo;
	private String strDescripcion;
	private boolean bolPrincipal;
	
	public VoRDCResponsable(VoRDCParametro parametro, VoRDCParametro.Ticket dtaTicket, String strCodigo, String strDescripcion, boolean bolPrincipal) {
		super();
		this.parametro = parametro;
		this.dtaTicket = dtaTicket;
		this.strCodigo = strCodigo;
		this.strDescripcion = strDescripcion;
		this.bolPrincipal = bolPrincipal;
	}

	public VoRDCResponsable(VoRDCParametro parametro, VoRDCParametro.Ticket dtaTicket, String strCodigo, String strDescripcion) {
		this(parametro, dtaTicket, strCodigo, strDescripcion, false);
	}
	
	public VoRDCParametro getParametro() {
		return parametro;
	}

	public VoRDCParametro.Ticket getDtaTicket(){
		return dtaTicket;
	}
	
	public String getCodigo() {
		return strCodigo;
	}

	public String getDescripcion() {
		return strDescripcion;
	}

	public boolean getPrincipal() {
		return bolPrincipal;
	}
}
