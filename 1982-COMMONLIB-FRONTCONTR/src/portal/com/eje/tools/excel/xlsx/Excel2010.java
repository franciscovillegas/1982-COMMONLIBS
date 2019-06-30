package portal.com.eje.tools.excel.xlsx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.xml.sax.SAXException;

import portal.com.eje.tools.excel.xlsx.Xlsx2CsvWe.CellExcelBasicTypes;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.tool.misc.Formatear;

public class Excel2010 implements IExcel {
	private Workbook wb = null;
	private int hojaActual;
	private Row filaActual; 
	private List<String> filas;
	private ConsultaData data;
	private boolean omitePrimeraFila;

	public Excel2010(boolean omitePrimeraFila) {
		hojaActual = 0;
		this.omitePrimeraFila = omitePrimeraFila;
	}
	
	public Excel2010() {
		this(false);
	}
	
	public ConsultaData getData() {
		return data;
	}
	
	public int getCantHojas() {
		return wb.getNumberOfSheets();
	}
	
	public void setHoja(int hoja) {
		if(hoja <= getCantHojas() && hoja > 0) {
			hojaActual = hoja-1;
			
		} else {
			System.out.println("La hoja no es valida");
		}
	}

	public boolean loadFile(File f, int minColumnsToShow) throws IOException {

		wb = getWorkBook(f);
		filas = getNombreCampos();
		data = new ConsultaData(filas);
		
		Sheet sheet = wb.getSheetAt(hojaActual);
		boolean first = true;
		for(Row row : sheet) {
			if(omitePrimeraFila && first) {
				first = false;
				continue;
			}
			first = false;
			
			
			DataFields lista = new DataFields();
			int i = 0 ;
			
			if(row != null) {
			
				for(int posCol=row.getFirstCellNum(); posCol<row.getLastCellNum();posCol++) {
					Cell cell = row.getCell(posCol);
					String celda = getValCell(cell);
					String key = "";
					
					if(filas.get(posCol) != null) {
						key= filas.get(posCol);
					}
					else {
						key=String.valueOf(i++);
					}
					
				
		  			lista.put(key.toLowerCase(), new Field(celda));
		  		}
			}
			
			data.add(lista);
		}
		
		return true;
	}
	
	
	public List<String> getNombreCampos() {
		List<String> pila = new ArrayList<String>();
    	
		try {
		
			Sheet sheet = wb.getSheetAt(hojaActual);
					
			for(Row row : sheet) {
				for(int posCol=row.getFirstCellNum();posCol<row.getLastCellNum();posCol++) {
					 Cell cell = row.getCell(posCol);
		   			 String celda = getValCell(cell);
					
		   			 if(!"".equals(celda.trim())) {
		   				 pila.add(celda);
		   			 }
				}
				
				return pila;
			}
	   		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
    	return pila;
	}
	
	
	public void getWorkBookDinamicamente(File f, String pageName, IXlsx2Row rowHandler, Map<String, CellExcelBasicTypes> definition) throws IOException {
		getWorkBookDinamicamenteGeneral(f, pageName, rowHandler, null, definition);
	}
	
	public void getWorkBookDinamicamente(File f, Integer pageNumber, IXlsx2Row rowHandler, Map<String, CellExcelBasicTypes> definition) throws IOException {
		getWorkBookDinamicamenteGeneral(f, pageNumber, rowHandler, null, definition);
	}
	
	public void getWorkBookDinamicamenteGeneral(File f, Object pageIndicator,IXlsx2Row rowHandler, IReplaceField field, Map<String, CellExcelBasicTypes> definition) throws IOException {
		
		if(!f.exists()) {
			throw new IOException("ERROR!!!!! No existe el archivo :"+f);
		}
		else if(!f.isFile()) {
			throw new IOException("ERROR!!!!! No es un archivo válido :"+f);
		}
		else if(definition == null) {
			throw new IOException("Debe existir una definición del arhivo Excel");
		}
		else {

			try {

				OPCPackage opcPackage = OPCPackage.open(f.getAbsolutePath(), PackageAccess.READ);
				
				Xlsx2CsvWe xlsx2csv = new Xlsx2CsvWe(opcPackage, definition.keySet().size(), rowHandler,this.omitePrimeraFila, field);
				
				if(pageIndicator instanceof Integer) {
					xlsx2csv.process((Integer) pageIndicator, definition);	
				}
				else if(pageIndicator instanceof String) {
					xlsx2csv.process((String) pageIndicator, definition);	
				}
					
				
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OpenXML4JException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
	
	
	public List<String> getSheets(File f) throws IOException {
		List<String> lista = new ArrayList<String>();
		
		if(!f.exists()) {
			throw new IOException("ERROR!!!!! No existe el archivo :"+f);
		}
		else if(!f.isFile()) {
			throw new IOException("ERROR!!!!! No es un archivo válido :"+f);
		}
		else {

			try {
				OPCPackage opcPackage = OPCPackage.open(f.getAbsolutePath(), PackageAccess.READ);
				
				Xlsx2CsvWe xlsx2csv = new Xlsx2CsvWe(opcPackage, -1, null,this.omitePrimeraFila, null);
				lista = xlsx2csv.getSheets();

			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OpenXML4JException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return lista;
	}	
	
	
	
	
	private Workbook getWorkBook(File f) throws IOException {
		
		if(!f.exists()) {
			throw new IOException("ERROR!!!!! No existe el archivo :"+f);
		}
		else if(!f.isFile()) {
			throw new IOException("ERROR!!!!! No es un archivo válido :"+f);
		}
		else {

			FileInputStream fi = new FileInputStream(f);

			try {
				wb = WorkbookFactory.create(fi);
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fi.close();
			
		}
		
		return wb;
	}
	

	private String getValCell(Cell cell) {
		String valor = null;
		if(cell != null) {
			switch(cell.getCellType()){
					case Cell.CELL_TYPE_BLANK: {
						valor ="";	
						break;
					}
					case Cell.CELL_TYPE_BOOLEAN: {
						valor = String.valueOf(cell.getBooleanCellValue());
					}
			 	    case Cell.CELL_TYPE_NUMERIC: {
			 	    	if (HSSFDateUtil.isCellDateFormatted(cell)) {
			 	    		valor  = Formatear.getInstance().toDate(cell.getDateCellValue(), "yyyyMMdd");
			 	    	}
			 	    	else {
			 	    		NumberFormat f = NumberFormat.getInstance();
				 	    	f.setGroupingUsed(false);
				 	    	valor = f.format(cell.getNumericCellValue());
			 	    	}
			 	    	
			     	   	break;
			        }
			        case Cell.CELL_TYPE_STRING: {
			        	valor =  cell.getRichStringCellValue().getString();
			        	break;
			        }
			        case Cell.CELL_TYPE_FORMULA: {
			        	
			        	valor =  cell.getRichStringCellValue().getString();
			        	break;
			        }
			        case Cell.CELL_TYPE_ERROR: {
			        	valor =  "#N/A";
						break;
			        }
			}
		}
		
		return valor;
	}

	public boolean loadFile(File f) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @deprecated
	 * */
	public void getWorkBookDinamicamente(File f, int pageIndex, int minColumnsToShow, IXlsx2Row rowHandler) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @deprecated
	 * */
	public void getWorkBookDinamicamente(File f, int pageIndex, int minColumnsToShow, IXlsx2Row rowHandler, IReplaceField field) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
