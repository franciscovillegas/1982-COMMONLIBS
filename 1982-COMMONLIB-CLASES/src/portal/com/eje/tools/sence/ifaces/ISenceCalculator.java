package portal.com.eje.tools.sence.ifaces;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import cl.eje.model.generic.portal.Eje_ges_periodo;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.tools.sence.enums.EnumSenceTramo;
import portal.com.eje.tools.sence.error.UFValueIsNotValidException;
import portal.com.eje.tools.sence.vo.SencePersonaTramoVo;

public interface ISenceCalculator {

	public EnumSenceTramo getTramo(TransactionConnection cons, Double totHaberes) throws SQLException, UFValueIsNotValidException;
	
	public Collection<SencePersonaTramoVo> getPersonas(TransactionConnection cons, List<Integer> ruts) throws NullPointerException, SQLException, UFValueIsNotValidException;
	
	public Eje_ges_periodo getLastPeriodo(TransactionConnection cons) throws SQLException;
}
