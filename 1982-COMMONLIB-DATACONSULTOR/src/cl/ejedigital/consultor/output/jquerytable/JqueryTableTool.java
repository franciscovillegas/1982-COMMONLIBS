package cl.ejedigital.consultor.output.jquerytable;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSarrayDataOut;
import cl.ejedigital.consultor.output.JSarrayTableDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;

public class JqueryTableTool {
	
	public JSonDataOut getServerResponde(IDataOut out, int sEcho) {
		DataList lista = new DataList();
		DataFields fields = new DataFields();

		fields.put("sEcho", new Field(sEcho));
		fields.put("aaData", new Field(out));
		lista.add(fields);

		JSonDataOut ret = new JSonDataOut(lista);
		return ret;
	}
	
	public JSonDataOut getServerResponde(IDataOut out, int sEcho, int totalRecords, int totalAfterFilter) {
		DataList lista = new DataList();
		DataFields fields = new DataFields();

		fields.put("sEcho", new Field(sEcho));
		fields.put("aaData", new Field(out));
		fields.put("iTotalRecords", new Field( totalRecords ));
		fields.put("iTotalDisplayRecords", new Field(totalAfterFilter));
		lista.add(fields);

		JSonDataOut ret = new JSonDataOut(lista);
		return ret;
	}
	
	
	public JSonDataOut getServerResponde(ConsultaData data, int sEcho) {
		JSarrayDataOut out     = null;
		JSonDataOut ret = null;
		
		{
			JSonDataOut prepara = new JSonDataOut(data);
			DataList lista = new DataList();
			DataFields fields = new DataFields();
			fields.put("value", new Field(prepara));
			lista.add(fields);
			out = new JSarrayDataOut(lista);
		}
		
		
		{
			DataList lista = new DataList();
			DataFields fields = new DataFields();
			fields.put("sEcho", new Field(sEcho));
			fields.put("aaData", new Field(out));
			lista.add(fields);
			ret = new JSonDataOut(lista);
		}
		
		return ret;
	}
	
}
