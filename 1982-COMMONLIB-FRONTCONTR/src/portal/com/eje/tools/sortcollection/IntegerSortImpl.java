package portal.com.eje.tools.sortcollection;

import java.util.Comparator;

import cl.ejedigital.web.datos.AbsOrdenCData;
import cl.ejedigital.web.datos.Order;

class IntegerSortImpl extends AbsOrdenCData implements Comparator<Integer>  {

	protected IntegerSortImpl(String colName, Order orden) {
		super(colName, orden);

	}

	@Override
	public int compare(Integer o1, Integer o2) {
		return super.absCompare(o1, o2);
	}

	@Override
	protected Object getObjectToCompare(Object o1) {
		return (Integer) o1;
	}
}
