package portal.com.eje.frontcontroller.ioutils.sencha.toolsonrenderjson;

import java.util.HashMap;
import java.util.Map;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataPageRenderer;
import cl.ejedigital.consultor.ConsultaDataPaged;
import cl.ejedigital.consultor.IMetable;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.Order;
import cl.ejedigital.web.datos.Sort;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.factory.Util;

public class TruncaYPagina  {

	public static TruncaYPagina getInstance() {
		return Util.getInstance(TruncaYPagina.class);
	}
	 
	public Map<String, Object> truncaYPagina(ConsultaData data, IOClaseWeb io, ConsultaDataPageRenderer dataPageRenderer) {
		
		Map<String,Object> retorno = new HashMap<>();
		int countSize = -1; 
		/*ORDEN INSTANCEOF POR JERARQUIA INVERSA*/
		if(data instanceof ConsultaDataPaged) {
			countSize = ((ConsultaDataPaged) data).getTotalSize();
		}
		if( IMetable.class.isAssignableFrom(data.getClass()) && ((IMetable)data).getMetaData() != null) {
			countSize = data.getMetaData().getPaggedTotalCount();
		}
		else if(data instanceof ConsultaData) {
			countSize = data.size();
			data = privatePaginaData(io, data, dataPageRenderer);
			
		} 
		
		retorno.put("Integer", countSize);
		retorno.put("ConsultaData", data);
		
		return retorno;
	}

private ConsultaData privatePaginaData(IIOClaseWebLight  io, ConsultaData data, ConsultaDataPageRenderer dataPageRenderer) {
		
		String limit  = io.getParamString("limit", "999999");
		String page   = io.getParamString("page", "1");
		int start  = Validar.getInstance().validarInt(io.getParamString("start", "0"),0);
		if(start < 0) start = 0;
 
		int rows	  = 0 ;
		
		if(  data != null) {
			rows = data.getData().size();
		}
		
		if(data != null && limit != null) {
		
			Sort dataSort = ((IOClaseWeb)io).getSenchaPage().getSorts();
			
			while(dataSort != null && dataSort.next()) {
			 
				Order order = Order.fromString(dataSort.getString("direction"));
				
				ConsultaTool.getInstance().sort(data, dataSort.getString("property") , order);
			}				
			 
			
			ConsultaData subData = new ConsultaData(data.getNombreColumnas());
			int limiteSuperior = Integer.parseInt(limit ) * Integer.parseInt(page);
			if(limiteSuperior > rows) {
				limiteSuperior = rows;
			}
			
			if(start> limiteSuperior) {
				start = 1; //Manejo de error.
			}
			
			try {
				subData.getData().addAll(data.getData().subList(start, limiteSuperior ));
				data = subData;
			}
			catch(IllegalArgumentException e) {
			
			}
			
			
			if(dataPageRenderer != null && data != null) {
				data.toArray();
				dataPageRenderer.pageRenderer(data);
				data.toStart();
			}
		}
		
		return data;
	}
}
