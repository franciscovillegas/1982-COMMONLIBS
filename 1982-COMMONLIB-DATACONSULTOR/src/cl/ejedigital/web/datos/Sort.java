package cl.ejedigital.web.datos;



import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;

public class Sort extends ConsultaData {

	public Sort(List<String> nombreColumnas) {
		super(nombreColumnas);
		// TODO Auto-generated constructor stub
	}

	public Sort add(String property, Order order) {
		DataFields fields = new DataFields();
		fields.put("property", property);
		fields.put("direction", order == Order.Descendente ? "desc" : "asc");
		
		super.add(fields);
		
		return this;
	}
	
	public static Sort sort(String property, Order order) {
		List<String> columns = new ArrayList<String>();
		columns.add("property");
		columns.add("direction");
		
		Sort sort = new Sort(columns);
		sort.add(property, order);
		
		return sort;
	}
	
	public static Sort fromConsultaData(ConsultaData data) {
		Sort sort = null;
		if(data != null) {
			sort = new Sort(data.getNombreColumnas());
			sort.addAll(data);
		}
		else {
			List<String> cols = new ArrayList<String>();
			cols.add("vacia");
			sort = new Sort(cols);
		}
		
		
		return sort;
	}

	
	public SortTupla getFirstOrder() {
		String property = null;
		String direction = null;

		int pos = getPosition();
		try {
			if (next()) {
				property = getString("property");
				direction = getString("direction");
			}
		} finally {
			setPosition(pos);
		}

		return new SortTupla(property, direction);
	}
	
	public class SortTupla {
		private String property;
		private Order direction;

		public SortTupla(String property, String direction) {
			super();
			this.property = property;

			if (direction != null && Order.Descendente.name().toLowerCase().indexOf(direction) != -1) {
				this.direction = Order.Descendente;
			} else if (direction != null && Order.Ascendente.name().toLowerCase().indexOf(direction) != -1) {
				this.direction = Order.Ascendente;
			}
			else {
				this.direction = Order.Descendente;
			}
		}

		public SortTupla(String property, Order direction) {
			super();
			this.property = property;
			this.direction = direction;
		}

		public String getProperty() {
			return property;
		}

		public Order getDirection() {
			return direction;
		}

	}
}


