package portal.com.eje.portal.trabajador.util;

import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.Order;
import portal.com.eje.portal.trabajador.enums.EjeGesTrabajadorField;

/**
 * 
 * Indica el orden de la eje_ges_trabajador
 * @author Pancho
 * @since 26-06-2018
 * */
public class EjeGesTrabajadorOrderByFields {

	private ConsultaData getSort() {
		List<String> fields = new ArrayList<String>();
		fields.add("property");
		fields.add("direction");
		ConsultaData data = new ConsultaData(fields);
		return data;
	}

	private ConsultaData addSort(ConsultaData sort,EjeGesTrabajadorField field, Order order) {
		if(sort != null) {
			DataFields dataFields = new DataFields();
			dataFields.put("property", field.toString() );
			dataFields.put("direction", order == Order.Ascendente ? "asc" : "desc"  );
			
			sort.add(dataFields);
		}
		
		return sort;
	}
	
	public ConsultaData orderByRut() {

		ConsultaData sort = getSort();
		addSort(sort, EjeGesTrabajadorField.rut , Order.Ascendente);
		
		return sort;
	}
	

}
