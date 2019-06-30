package cl.eje.qsmcom.tool;

import portal.com.eje.tools.excel.xlsx.IExcel;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;

public class ExcelMappingTool {

	
	public ExcelMappingTool() {

	}
	
	public void fullUpdateToBd(IExcel excel, IExcelMapping mapping, IExcelMappingAction actions, boolean withDelete) {
		ConsultaData data = excel.getData();
		
		while(data.next()) {
			DataFields fields = data.getActualData();
			
			if(actions.doExist(fields)) {
				if(withDelete) {
					actions.doDelete(fields);
					actions.doInsert(fields);
				}
				else {
					actions.doUpdate(fields);
				}
				
			}
			else {
				actions.doInsert(fields);
			}
		}
	}
	
}