package portal.com.eje.tools.excel.xlsx;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;

/**
 * @deprecated
 * @autor Francisco
 * */
public class ExcelProcessConsultaData implements IXlsx2Row {
	private ConsultaData dataExcel;
	
	public ExcelProcessConsultaData(int totalColumnas) {
		iniciaData(totalColumnas);
	}
	
	public void row(DataFields row) {
		dataExcel.add(row);
		
	}
	
	public ConsultaData getDataExcel() {
		return dataExcel;
	}
	
	private void iniciaData(int totalColumnas) {
		List<String> lista = new ArrayList<String>();
		for(int i=1; i <= totalColumnas; i++) {
			lista.add(String.valueOf(i));
		}
		dataExcel = new ConsultaData(lista);
	}

}
