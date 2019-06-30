package portal.com.eje.tools.sortcollection;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cl.ejedigital.web.datos.Order;
import portal.com.eje.portal.factory.Util;

/**
 * @author Pancho
 * @since 17-04-2019
 * */
public class NumericSort {

	public static NumericSort getInstance() {
		return Util.getInstance(NumericSort.class);
	}
	
	public void sortDouble(Collection<Double> cols, Order orden) {
		Collections.sort((List<Double>)cols, new DoubleSortImpl(null, orden));
	}
	
	public void sortInteger(Collection<Integer> cols, Order orden) {
		Collections.sort((List<Integer>)cols, new IntegerSortImpl(null, orden));
	}
}
