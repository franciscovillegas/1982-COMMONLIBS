package cl.eje.qsmcom.tool;



public class ExcelMap {
	private int pos;
	private String excelColName;
	private String dbColName;
	
	public ExcelMap(int pos, String excelColName, String dbColName) {
		super();
		this.pos = pos;
		this.excelColName = excelColName;
		this.dbColName = dbColName;
	}
	
	public int getPos() {
		return pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public String getExcelColName() {
		return excelColName;
	}
	
	public void setExcelColName(String excelColName) {
		this.excelColName = excelColName;
	}
	
	public String getDbColName() {
		return dbColName;
	}
	public void setDbColName(String dbColName) {
		this.dbColName = dbColName;
	}

}
