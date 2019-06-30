package cl.ejedigital.consultor.def;


import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSarrayTableDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;


public class DataTableDef  {
	private DataColumnsDef defincionColumnas;
	private ConsultaData data;
	
	public DataTableDef(ConsultaData data) {
		this.data = data;
		defincionColumnas = new DataColumnsDef(data.getNombreColumnas());
	}
	
	public ConsultaData getConsultaData() {
		return this.data;
	}
	
	public DataColumnsDef getColumnsDef() {
		return this.defincionColumnas;
	}

	public IDataOut getIDataOutAaData() {
		JSarrayTableDataOut out3 = new JSarrayTableDataOut(this.data);
		return out3;
	}
	
	public IDataOut getIDataOutAoColumns() {
		return defincionColumnas.toJarray();
	}
	
	public String getStringTableDefinition() {
		
		DataFields fields = new DataFields();
		fields.put("bProcessing"	, "true");
		fields.put("aaData"			, getIDataOutAaData());
		fields.put("aoColumns"		, getIDataOutAoColumns());
		fields.put("sScrollX"		, "100%");
		fields.put("bScrollCollapse", "true");
		
		DataList lista = new DataList();
		lista.add(fields);
		
		JSonDataOut out = new JSonDataOut(lista);
		return out.getListData();
	}
}
