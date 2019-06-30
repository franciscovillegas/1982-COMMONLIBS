package intranet.com.eje.qsmcom.estructuras;

public class PerfilQsmCom {
	public final static PerfilQsmCom PERFIL_ADM			= new PerfilQsmCom(ParametrosObligatorios.IDPERFILGESTOR.toString());
	public final static PerfilQsmCom PERFIL_INGRESADOR  = new PerfilQsmCom(ParametrosObligatorios.IDPERFILINGRESADOR.toString());
	public final static PerfilQsmCom PERFIL_COORDINADOR = new PerfilQsmCom(ParametrosObligatorios.IDPERFILGESTOR.toString());  
	public final static PerfilQsmCom PERFIL_EJECUTOR	= new PerfilQsmCom(ParametrosObligatorios.IDPERFILATENDEDOR.toString());
	public final static PerfilQsmCom PERFIL_CONTROLER	= new PerfilQsmCom(ParametrosObligatorios.IDPERFILGESTOR.toString());
	
	private String idPerfil;
	
	public PerfilQsmCom(String idPerfil) {
		this.idPerfil = idPerfil;
	}
	
	public String toString() {
		return idPerfil;
	}
	
}