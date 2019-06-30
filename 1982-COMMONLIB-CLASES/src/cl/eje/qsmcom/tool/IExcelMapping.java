package cl.eje.qsmcom.tool;

public interface IExcelMapping {

	public void addMap(int pos, String colName, String dbColName);
	
	public ExcelMap getMapById(int pos);
	
	public ExcelMap getMapByColName(String colName);
	
	public ExcelMap getMapByColNameBD(String colNameBD);
}
