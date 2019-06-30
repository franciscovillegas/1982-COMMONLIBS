package cl.eje.qsmcom.tool;

import cl.ejedigital.consultor.DataFields;

public interface IExcelMappingAction {

	public boolean doInsert(DataFields campos);
	
	public boolean doUpdate(DataFields campos);
	
	public boolean doDelete(DataFields campos);
	
	public boolean doExist(DataFields campos);
		
	
}
