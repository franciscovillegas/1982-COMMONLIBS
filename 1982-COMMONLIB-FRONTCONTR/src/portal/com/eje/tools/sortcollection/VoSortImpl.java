package portal.com.eje.tools.sortcollection;

import java.util.Comparator;

import cl.ejedigital.web.datos.AbsOrdenCData;
import cl.ejedigital.web.datos.Order;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.portal.vo.vo.Vo;

class VoSortImpl extends AbsOrdenCData implements Comparator<Vo> {
	 
	
	VoSortImpl(String colName, Order orden) {
		super(colName, orden);
	}

	@Override
	public int compare(Vo o1, Vo o2) {
		return super.absCompare(o1, o2);
	}
	
	@Override
	protected Object getObjectToCompare(Object o1) {
		return VoTool.getInstance().getFieldValue((Vo) o1,super.colName);
	}

	 
}
