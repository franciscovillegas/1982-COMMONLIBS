package portal.com.eje.tools.sence.util;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import cl.eje.model.generic.portal.Eje_ges_periodo;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.tools.sence.enums.EnumSenceTramo;
import portal.com.eje.tools.sence.error.UFValueIsNotValidException;
import portal.com.eje.tools.sence.ifaces.ISenceCalculator;
import portal.com.eje.tools.sence.vo.SencePersonaTramoVo;

abstract class AbsSenceTramoCalculator implements ISenceCalculator {
	private final int UF_MIN_VALUE = 25000;
	
	abstract public Collection<SencePersonaTramoVo> getPersonas(TransactionConnection cons, List<Integer> ruts) throws NullPointerException, SQLException, UFValueIsNotValidException;
	
	public EnumSenceTramo getTramo(TransactionConnection cons, Double totHaberes) throws SQLException, UFValueIsNotValidException {
		EnumSenceTramo tramo = EnumSenceTramo.DESCONOCIDO;
		
		Eje_ges_periodo lastPeriodo = getLastPeriodo(cons);
		
		if(lastPeriodo == null || lastPeriodo.getPeri_uf() < UF_MIN_VALUE) {
			throw new UFValueIsNotValidException("En valor de la UF no puede ser menor a "+UF_MIN_VALUE);
		}
		if(totHaberes != null && lastPeriodo != null && lastPeriodo.getPeri_uf() != null) {
			
			double n = (totHaberes / lastPeriodo.getPeri_uf());
			if(  n < 26) {
				tramo = EnumSenceTramo.TRAMO1;
			}
			else if(  n >= 26 && n < 51) {
				tramo = EnumSenceTramo.TRAMO2;
			}
			else if(  n >= 51 ) {
				tramo = EnumSenceTramo.TRAMO3;
			}
		}
		
		return tramo;
	}

	
	public Eje_ges_periodo getLastPeriodo(TransactionConnection cons) throws SQLException {
		String sql = "select * from eje_ges_periodo where peri_id in (select MAX(periodo) from eje_ges_certif_histo_liquidacion_cabecera)";
		ConsultaData data = ConsultaTool.getInstance().getData(cons.getPortal(), sql);
		return VoTool.getInstance().buildVoSimple(data, Eje_ges_periodo.class);
	}
	
	
}
