package cl.ejedigital.web.datos;

import java.util.Comparator;

import cl.ejedigital.consultor.DataFields;

public class OrdenCData extends AbsOrdenCData implements Comparator<DataFields> {

	protected OrdenCData(String colName, Order orden) {
		super(colName, orden);
	}
	
	@Override
	public int compare(DataFields o1, DataFields o2) {
		return super.absCompare(o1, o2);
	}
	
	@Override
	protected Object getObjectToCompare(Object o1) {
		if(o1 != null && ((DataFields)o1).get(colName) != null) {
			return ((DataFields)o1).get(colName).getObject();
		}
		
		return null;
	}

}
