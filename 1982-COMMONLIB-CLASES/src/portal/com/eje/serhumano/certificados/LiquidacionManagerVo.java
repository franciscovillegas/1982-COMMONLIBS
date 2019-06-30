package portal.com.eje.serhumano.certificados;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cl.eje.model.generic.portal.Eje_ges_certif_histo_liquidacion_cabecera;
import cl.eje.model.generic.portal.Eje_ges_trabajador;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.portal.vo.CtrTGeneric;
import portal.com.eje.portal.vo.util.Wheres;
import portal.com.eje.tools.maptable.MapVoValues;

public class LiquidacionManagerVo {

	public static LiquidacionManagerVo getInstance() {
		return Util.getInstance(LiquidacionManagerVo.class);
	}
	
	
	public List<Eje_ges_certif_histo_liquidacion_cabecera> getCabeceras(TransactionConnection cons, List<Integer> ruts, List<Integer> periodos) throws NullPointerException, SQLException {
		 Collection<Eje_ges_certif_histo_liquidacion_cabecera> cabeceras = new ArrayList<>();
		
		if(cons != null && ruts != null && periodos != null) {
			cabeceras =CtrTGeneric.getInstance().getAllFromClass(cons.getPortal(), Eje_ges_certif_histo_liquidacion_cabecera.class, 
					Wheres.where("rut", "in", ruts).and("periodo", "in", periodos).build());
		}
		
		return (List<Eje_ges_certif_histo_liquidacion_cabecera>) cabeceras;
	}
	/**
	 * El key, es el rut
	 * */
	public Map<Object,List<Eje_ges_certif_histo_liquidacion_cabecera>> getCabecerasMap(TransactionConnection cons, List<Integer> ruts, List<Integer> periodos) throws NullPointerException, SQLException {
		 Map<Object, List<Eje_ges_certif_histo_liquidacion_cabecera>> retorno = MapVoValues.groupBy(getCabeceras(cons, ruts, periodos), "rut");
		 return retorno;
	}
	
	public List<Eje_ges_trabajador> getTrabajadores(TransactionConnection cons, List<Integer> ruts) throws NullPointerException, SQLException {
		Collection<Eje_ges_trabajador> coll = CtrTGeneric.getInstance().getAllFromClass(cons.getPortal(), Eje_ges_trabajador.class, Wheres.where("rut", "in", ruts).build());
		return (List<Eje_ges_trabajador>) coll;
	}
	
	public Map<Object,List<Eje_ges_trabajador>> getTrabajadoresMap(TransactionConnection cons, List<Integer> ruts) throws NullPointerException, SQLException {
		 Map<Object, List<Eje_ges_trabajador>> trabs = MapVoValues.groupBy(getTrabajadores(cons, ruts), "rut");
		 return trabs;
		
	}
}
