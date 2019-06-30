package cl.ejedigital.consultor;

import cl.ejedigital.web.datos.Order;
import cl.ejedigital.web.datos.Sort;

public interface ISenchaPage {

	Integer getLimit();

	Integer getStart();

	Integer getPage();

	Sort getSorts();

	void clearSort();

	void addSort(String property, Order direction);

	void addSort(String property, String direction);

}