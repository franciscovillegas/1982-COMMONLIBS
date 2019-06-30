package portal.com.eje.tools.excel.xlsx;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import portal.com.eje.tools.excel.xlsx.Xlsx2CsvWe.CellExcelBasicTypes;

public class ExcelProcessConsultaDataExact implements IXlsx2Row {
	private ConsultaData dataExcel;
	
	public ExcelProcessConsultaDataExact(Map<String, CellExcelBasicTypes> deinition) {
		iniciaData(deinition);
	}
	
	public void row(DataFields row) {
		dataExcel.add(row);
		
	}
	
	public ConsultaData getDataExcel() {
		return dataExcel;
	}
	
	private void iniciaData(Map<String, CellExcelBasicTypes> deinition) {
		List<String> lista = new LinkedList<String>();
		Set<String> set = deinition.keySet();
		for(String s: set) {
			lista.add(s);
		}
		dataExcel = new ConsultaData(lista);
	}

}
