package cl.eje.qsmcom.tool;

import java.util.HashMap;

public class ExcelMapping implements IExcelMapping {
	private HashMap<Integer, ExcelMap> colPos;
	private HashMap<String, ExcelMap> colNameExcel;
	private HashMap<String, ExcelMap> colNameDB;
	
	protected ExcelMapping() {
		colPos = new HashMap<Integer, ExcelMap>();
		colNameExcel = new HashMap<String, ExcelMap>();
		colNameDB = new HashMap<String, ExcelMap>(); 
	}
	
	public void addMap(int pos, String colName, String dbColName) {
		ExcelMap map = new ExcelMap(pos, colName, dbColName);
		
		colPos.put(new Integer(pos), map );
		colNameExcel.put(colName, map );
		colNameDB.put(dbColName, map );
	}
	
	public ExcelMap getMapById(int pos) {
		return colPos.get(new Integer(pos));
	}
	
	public ExcelMap getMapByColName(String colName) {
		return colPos.get(colName);
	}
	
	public ExcelMap getMapByColNameBD(String colNameBD) {
		return colPos.get(colNameBD);
	}
}