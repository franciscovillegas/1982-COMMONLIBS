package cl.ejedigital.consultor;

import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.web.datos.Order;
import cl.ejedigital.web.datos.Sort;

public class ConsultaDataPage implements ISenchaPage {
 
	private Integer limit;
	private Integer start;
	private Integer page;
	private Sort sorts;

	public ConsultaDataPage(Sort sorts, Integer limit, Integer start, Integer page) {
		super();
		this.limit = limit;
		this.start = start == null ? 0 : start;
		this.page = page == null ? 1 : page;
		this.sorts = sorts;
	}

	@Override
	public Integer getLimit() {
		return limit;
	}

	@Override
	public Integer getStart() {
		return start;
	}

	@Override
	public Integer getPage() {
		if(page > 0) {
			return page;
		}
		else {
			return 1;
		}
	}

	@Override
	public Sort getSorts() {
		return sorts;
	}

	@Override
	public void clearSort() {
		this.sorts = null;
		
	}
	
	 
	@Override
	public void addSort(String property, Order direction) {
		addSort(property, direction == Order.Ascendente ? "asc" : "desc");
	}
	
	
	@Override
	public void addSort(String property, String direction) {
		if( property != null && direction != null) {
			if(sorts == null) {
				List<String> columnas = new ArrayList<String>();
				columnas.add("property");
				columnas.add("direction");	
				sorts = new Sort(columnas);
			}
			DataFields df = new DataFields();
			df.put("property", property );
			df.put("direction", direction );
			sorts.add(df);
		}

	}

 

}

