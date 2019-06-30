package portal.com.eje.tools.sortcollection;

import java.util.Comparator;

import cl.ejedigital.web.datos.AbsOrdenCData;
import cl.ejedigital.web.datos.Order;

class DoubleSortImpl extends AbsOrdenCData implements Comparator<Double>  {

	protected DoubleSortImpl(String colName, Order orden) {
		super(colName, orden);

	}

	@Override
	public int compare(Double o1, Double o2) {
		return super.absCompare(o1, o2);
	}

	@Override
	protected Object getObjectToCompare(Object o1) {
		return (Double) o1;
	}
}
