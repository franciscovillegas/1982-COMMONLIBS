package portal.com.eje.portal.roldetecterconfig;

import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.portal.roldetecterconfig.VoRDCParametro.TipoMiembro;
import portal.com.eje.portal.transactions.TransactionConnection;

public interface IRolDetecterConfig {

	public List<VoRDCParametro> getParametros(TransactionConnection cons, int intIdParametro);
		
	public List<VoRDCParametro> getParametros(TransactionConnection cons, int intIdParametro, TipoMiembro tipo_miembro);
	
	public List<VoRDCParametro> getParametros(TransactionConnection cons, int intIdParametro, String strOmitidos);
	
	public List<VoRDCParametro> getParametros(TransactionConnection cons, int intIdParametro, TipoMiembro tipo_miembro, String strOmitidos);
	
	public List<VoRolDetecterConfig> getRolDetecterConfig(TransactionConnection cons);
	
	public List<VoRDCMiembro> getMiembros(TransactionConnection cons, int intIdRolDetecter);
	
	public boolean bolUpdateRDC(TransactionConnection cons, VoRolDetecterConfig voRDC, ConsultaData data);
	
	public boolean bolDeleteRDC(TransactionConnection cons, VoRolDetecterConfig voRDC);
	
	public ConsultaData getFiltros(TransactionConnection cons, VoRolDetecterConfig voRDC);
	
	public ConsultaData getResponsables(TransactionConnection cons, VoRolDetecterConfig voRDC);
	
}
