package portal.com.eje.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import cl.ejedigital.tool.strings.MyString;

public class Excel extends File implements Iterator {
	/*VERSION 2.0*/
	private int hojaActual =0;
	private boolean estaOK= true;
	private HSSFWorkbook wb = null;
	private HSSFRow filaActual; 
	
	public Excel(String ruta) throws IOException {
		super(ruta);
		
		if(!exists()) {
			System.out.println("ERROR!!!!! No existe el archivo");
			estaOK = false;
		}
		if(!isFile()) {
			System.out.println("ERROR!!!!! No es un archivo");
			estaOK = false;
		}
		
		if(estaOK) {

			FileInputStream fi = new FileInputStream(this);
			POIFSFileSystem fs = new POIFSFileSystem(fi);
			fi.close();
			
			wb = new HSSFWorkbook(fs);
			setHoja(1);
			
		}
	}
	
	public void close() {
		
	}
	
	public void setHoja(int hoja) {
		if(hoja <= getCantHojas() && hoja > 0) {
			hojaActual = hoja-1;
			filaActual = getFirstRow(wb.getSheetAt(hojaActual));
			
		} else {
			System.out.println("La hoja no es valida");
		}
	}
	
	public Iterator getRowIterator() {
		return this;
	}
	
	public ArrayList getNombreCampos() {
		ArrayList pila = new ArrayList();
		
		try {
		
			HSSFSheet sheet = wb.getSheetAt(hojaActual);
			
			HSSFRow row = getFirstRow(sheet);
			
	   		for(int posCol=row.getFirstCellNum();posCol<row.getLastCellNum();posCol++) {
	   			 HSSFCell cell = row.getCell((short)posCol);
	   			 String celda = getValCell(cell);
				
	   			 if(!"".equals(celda.trim())) {
	   				 pila.add(celda);
	   			 }
	   		}
	   		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
    	return pila;
	}
	
	private String getValCell(HSSFCell cell) {
		String valor = null;
		if(cell != null) {
			switch(cell.getCellType()){
					case HSSFCell.CELL_TYPE_BLANK: {
						valor ="";	
						break;
					}
					case HSSFCell.CELL_TYPE_BOOLEAN: {
						valor = String.valueOf(cell.getBooleanCellValue());
					}
			 	    case HSSFCell.CELL_TYPE_NUMERIC: {
			 	    	NumberFormat f = NumberFormat.getInstance();
			 	    	f.setGroupingUsed(false);
			 	    	valor = f.format(cell.getNumericCellValue());
			     	   	break;
			        }
			        case HSSFCell.CELL_TYPE_STRING: {
			        	valor =  cell.getRichStringCellValue().getString();
			        	break;
			        }
			        case HSSFCell.CELL_TYPE_FORMULA: {
			        	valor =  String.valueOf(cell.getNumericCellValue());
				          break;
			        }
			        case HSSFCell.CELL_TYPE_ERROR: {
			        	valor =  "#N/A";
						break;
			        }
			}
		}
		
		return valor;
	}
	
	public HSSFRow getFirstRow(HSSFSheet sheet) {
		int firstRow = sheet.getFirstRowNum();
		boolean encontroOrigen = false;
		HSSFRow row = null;
		
		try {
			row = sheet.getRow(firstRow);
			
			while(!encontroOrigen) {
				while(row == null) {
					row = sheet.getRow(++firstRow);
				}
				
				for(int posCol=row.getFirstCellNum();posCol<row.getLastCellNum();posCol++) {
					String valor = getValCell(row.getCell((short)posCol));
					
					if(valor != null && !"".equals(valor) ) {
						encontroOrigen = true;
						break;
					}
					
				}
				
				if(!encontroOrigen) {
					row = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return row;
	}
	
	public int getCantHojas() {
		return wb.getNumberOfSheets();
	}
	
    public ArrayList getNombreHojas()  {
    	ArrayList hojas = new ArrayList();
    	
    	try {
	    	
	    	for(int i=0;i<getCantHojas();i++) {
	    		hojas.add(wb.getSheetName(i));
	    	}
    	} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return hojas;
    }

    
    public void gotoStart() {
    	filaActual = getFirstRow(wb.getSheetAt(hojaActual));
    }
    
	public boolean hasNext() {
		return filaActual != null && filaActual.getRowNum() < wb.getSheetAt(hojaActual).getLastRowNum(); 
	}

	public Object next() {
		HashMap lista = new HashMap();
		filaActual = wb.getSheetAt(hojaActual).getRow(filaActual.getRowNum()+1);
		int i = 0 ;
		
		if(filaActual != null) {
			for(int posCol=filaActual.getFirstCellNum();posCol<filaActual.getLastCellNum();posCol++) {
				HSSFCell cell = filaActual.getCell((short)posCol);
				String celda = getValCell(cell);
				String key = String.valueOf(i++);
	  			lista.put(key, celda);
	  		}
		}
		
		return lista;
	}

	public void remove() {
		System.out.println("No se permite borrar");	
	}

	
}
