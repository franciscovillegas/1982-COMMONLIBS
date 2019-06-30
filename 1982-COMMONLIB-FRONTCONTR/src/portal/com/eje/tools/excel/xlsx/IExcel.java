package portal.com.eje.tools.excel.xlsx;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import portal.com.eje.tools.excel.xlsx.Xlsx2CsvWe.CellExcelBasicTypes;
import cl.ejedigital.consultor.ConsultaData;

public interface IExcel {

	public ConsultaData getData();
	
	public boolean loadFile(File f) throws IOException;
	
	public boolean loadFile(File f, int minColumnsToShow) throws IOException;
	
	/**
	 * @deprecated
	 * */
	public void getWorkBookDinamicamente(File f, int pageIndex, int minColumnsToShow, IXlsx2Row rowHandler) throws IOException;
	
	/**
	 * @deprecated
	 * */
	public void getWorkBookDinamicamente(File f, int pageIndex, int minColumnsToShow, IXlsx2Row rowHandler, IReplaceField field) throws IOException;
	
	public void getWorkBookDinamicamente(File f, String pageName, IXlsx2Row rowHandler, Map<String, CellExcelBasicTypes> definition) throws IOException;
	
	public int getCantHojas();
	
	public void setHoja(int hoja);
	
}
