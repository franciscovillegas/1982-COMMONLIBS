package portal.com.eje.tools.sence.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cl.eje.model.generic.portal.Eje_ges_certif_histo_liquidacion_cabecera;
import cl.eje.model.generic.portal.Eje_ges_trabajador;
import cl.ejedigital.tool.strings.ArrayFactory;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.portal.vo.VoMathTool;
import portal.com.eje.serhumano.certificados.LiquidacionManagerVo;
import portal.com.eje.tools.sence.error.UFValueIsNotValidException;
import portal.com.eje.tools.sence.vo.SencePersonaTramoVo;

class SenceTramoCalculatorImplDefault extends AbsSenceTramoCalculator {

	public static SenceTramoCalculatorImplDefault getInstance() {
		return Util.getInstance(SenceTramoCalculatorImplDefault.class);
	}
	
	/**
	 * Para el último periodo
	 * @throws UFValueIsNotValidException 
	 * */
	public Collection<SencePersonaTramoVo> getPersonas(TransactionConnection cons, List<Integer> ruts) throws NullPointerException, SQLException, UFValueIsNotValidException {
		Collection<SencePersonaTramoVo> personas = new ArrayList<>();
		
		List<Integer> periodos = new ArrayList<>();
		periodos.add(getLastPeriodo(cons).getPeri_id());
		
		Map<Object, List<Eje_ges_certif_histo_liquidacion_cabecera>> coll =LiquidacionManagerVo.getInstance().getCabecerasMap(cons, ruts, periodos);
		Map<Object,List<Eje_ges_trabajador>> mapTrab = LiquidacionManagerVo.getInstance().getTrabajadoresMap(cons, ruts);
		
		for(Entry<Object, List<Eje_ges_certif_histo_liquidacion_cabecera>> c : coll.entrySet()) {
			SencePersonaTramoVo vo = new SencePersonaTramoVo();
			double total_haberes = VoMathTool.getInstance().getSuma("tot_haberes", c.getValue());
			
			Eje_ges_trabajador t = mapTrab.get(c.getKey()).get(0);
			vo.setTotal_haberes(total_haberes);
			vo.setRut((int) c.getKey());
			vo.setNombre(t.getNombre());
			vo.setApe_paterno(t.getApe_paterno());
			vo.setApe_paterno(t.getApe_materno());
			vo.setWp_cod_empresa(t.getWp_cod_empresa());
			vo.setWp_cod_planta(t.getWp_cod_planta());
			
			vo.setTramo(getTramo(cons, total_haberes));
			
			personas.add(vo);
		}
		
		return personas;
	}

 
}
