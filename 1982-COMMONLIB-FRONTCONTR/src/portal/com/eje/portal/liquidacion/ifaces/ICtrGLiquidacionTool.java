package portal.com.eje.portal.liquidacion.ifaces;

import java.util.Collection;
import java.util.List;

import cl.eje.model.generic.portal.Eje_ges_certif_histo_liquidacion_cabecera;
import cl.ejedigital.tool.strings.ArrayFactory;
import portal.com.eje.portal.liquidacion.VoPeriodoMensual;
import portal.com.eje.portal.liquidacion.enums.EjeGesCertifHistoLiquidacionCabeceraField;

public interface ICtrGLiquidacionTool {

	public Collection<VoPeriodoMensual> getPeriodos();
	
	/**
	 * Retorna todas las cabeceras para el grupo de personas para un periodo particular<br/>
	 * Desde abril 2019 que una persona puede tener multiples cabeceras por cada empresa<br/>
	 * 
	 * @author Pancho
	 * @since 26-04-2019
	 * */
	public Collection<Eje_ges_certif_histo_liquidacion_cabecera> getCabecera(int peri_id, ArrayFactory rutTrabajador , List<EjeGesCertifHistoLiquidacionCabeceraField> fields);
	
}
