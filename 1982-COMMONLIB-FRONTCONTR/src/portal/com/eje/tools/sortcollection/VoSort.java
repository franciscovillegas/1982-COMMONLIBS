package portal.com.eje.tools.sortcollection;

import java.util.Collections;
import java.util.List;

import cl.ejedigital.web.datos.Order;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.vo.Vo;

public class VoSort {

	public static VoSort getInstance() {
		return Util.getInstance(VoSort.class);
	}

 
	public void sort( List<Vo> collection, String campo, Class<?> tipoDelCampo, Order order) {
		if (collection != null && collection.size() > 0 && campo != null && order != null) {
			Collections.sort(collection, new VoSortImpl(campo, order));
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void sortByMethodValue( List collection, String campo, Class<?> tipoDelCampo, Order order) {
		if (collection != null && collection.size() > 0 && campo != null && order != null) {
			Collections.sort(collection, new VoSortImplByMethodValue(campo, order));
		}

	}
}
