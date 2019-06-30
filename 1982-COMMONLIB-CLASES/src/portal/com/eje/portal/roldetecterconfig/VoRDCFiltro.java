package portal.com.eje.portal.roldetecterconfig;

public class VoRDCFiltro  {
	
	private VoRDCParametro parametro;
	private String strCodigo;
	private String strDescripcion;
	private String strJndi;
	private String strTabla;
	private String strCampo;
	private String strCondicion;
	private String strOperador;
	private String strSQLOperador;
	private String strValor;
	private int intContador;
	
	public VoRDCFiltro(VoRDCParametro parametro, String strJndi, String strTabla, String strCampo, String strCondicion, String strCodigo, String strDescripcion, String strOperador, String strValor) {
		super();
		this.parametro = parametro;
		this.strJndi = strJndi;
		this.strTabla = strTabla;
		this.strCampo = strCampo;
		this.strCodigo = strCodigo;
		if (!"".equals(strCondicion)) {
			this.strCondicion = strCondicion;
		}
		this.strDescripcion = strDescripcion;
		this.strOperador = strOperador;
		this.strSQLOperador = null;
		this.strValor = strValor;
		this.intContador = 1;
	}

	public void setOperador(String strOperador) {
		this.strOperador = strOperador;
	}
	public void setSQLOperador(String strSQLOperador) {
		this.strSQLOperador = strSQLOperador;
	}
	public void setValor(String strValor) {
		this.strValor = strValor;
	}
	public void setContador(int intContador) {
		this.intContador = intContador;
	}
	
	public VoRDCParametro getParametro() {
		return parametro;
	}
	public String getJndi() {
		return strJndi;
	}
	public String getTabla() {
		return strTabla;
	}
	public String getCampo() {
//		if(getParametro().getParametro() == VoRDCParametro.Parametro.Ticket) {
//			return getDescripcion();
//		}
//		else {
			return strCampo;	
//		}
		
	}
	public String getCondicion() {
		return strCondicion;
	}
	public String getCodigo() {
		return strCodigo;
	}
	public String getDescripcion() {
		return strDescripcion;
	}
	public String getOperador() {
		return strOperador;
	}
	public String getValor() {
		return strValor;
	}
	public String getSQLOperador() {
		return strSQLOperador;
	}
	public int getContador() {
		return intContador;
	}
}
