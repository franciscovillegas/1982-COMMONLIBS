package portal.com.eje.portal.organica;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataMode;
import portal.com.eje.portal.organica.enums.DimensionCampoRef;
import portal.com.eje.portal.organica.ifaces.IDimensionUtil;
import portal.com.eje.portal.organica.vo.VoDimensionValor;

public class DimensionUtil implements IDimensionUtil {

	@Override
	public void addDimensionValorToConsultaData(ConsultaData retorno, String campoIdUnidad, DimensionCampoRef dimension) {
		if(retorno != null && campoIdUnidad != null && dimension != null) {
			retorno.setMode(ConsultaDataMode.CONVERSION);
			
			Map<String, VoDimensionValor> valoresUniDim = new HashMap<String, VoDimensionValor>();
			
			int pos = retorno.getPosition();
			retorno.toStart();
			
			while( retorno.next() ) {
				if(retorno.existField(campoIdUnidad)) {
					String idUnidad = retorno.getString(campoIdUnidad);
					VoDimensionValor valor = valoresUniDim.get(idUnidad);
					
					if(valor == null) {
						Collection<VoDimensionValor> valoresDim = OrganicaLocator.getInstanceDimension().getCtrG().getValores(idUnidad, dimension);
						
						if(valoresDim != null && valoresDim.size() > 0) {
							valor = valoresDim.iterator().next();
						}
					}
				
					if(valor != null) {
						retorno.getActualData().put(dimension.toString()+"_id", valor.getDitem_id());
						retorno.getActualData().put(dimension.toString(), valor.getDitem_desc());
					}
				}
			}	
			
			retorno.getNombreColumnas().add(dimension.toString()+"_id");
			retorno.getNombreColumnas().add(dimension.toString());
			
			
			retorno.setPosition(pos);
		}
	}
	
	

}
