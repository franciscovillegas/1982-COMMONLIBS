package portal.com.eje.tools.sortcollection;

import java.util.Comparator;

import cl.ejedigital.web.datos.AbsOrdenCData;
import cl.ejedigital.web.datos.Order;
import portal.com.eje.portal.vo.VoTool;

class VoSortImplByMethodValue extends AbsOrdenCData implements Comparator<Object> {
	 
	
	VoSortImplByMethodValue(String colName, Order orden) {
		super(colName, orden);
	}
 
	
	@Override
	protected Object getObjectToCompare(Object o1) {
		if(o1 != null && !VoTool.getInstance().existeField(o1.getClass(), super.colName)) {
			throw new NoSuchMethodError("VoSortImplByMethodValue dice:la clase \""+o1.getClass().getCanonicalName()+"\" no tiene el field \""+super.colName+"\"");
		}
		else if(o1 != null){
			return VoTool.getInstance().getMethodValue_byField( o1,super.colName);	
		} else {
			return null;
		}
		
		
	}


	@Override
	public int compare(Object o1, Object o2) {
		return super.absCompare(o1, o2);
	}

	 
}
