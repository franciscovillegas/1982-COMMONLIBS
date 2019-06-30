package portal.com.eje.frontcontroller.ioutils;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IFieldManipulator;
import cl.ejedigital.consultor.ManipulatorSqlServer2000;
import cl.ejedigital.consultor.output.ReservedWord;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.misc.Formatear;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.tools.excel.xlsx.Excel2010;
import portal.com.eje.tools.excel.xlsx.ExcelProcessConsultaDataExact;
import portal.com.eje.tools.excel.xlsx.IXlsx2Row;
import portal.com.eje.tools.excel.xlsx.Xlsx2CsvWe.CellExcelBasicTypes;

public class IOUtilExcel extends IOUtil {

	public static IOUtilExcel getIntance() {
		return Weak.getInstance(IOUtilExcel.class);
	}

	public void getParamExcel(IIOClaseWebLight  io, String nameParam, boolean saltaPrimeraFila, String hojaName, int cantColumnas, IXlsx2Row manipulador, Map<String, CellExcelBasicTypes> definition ) {
		Excel2010 excel = new Excel2010(true);
		try {
			File f = io.getFile(nameParam);
			
			if(f.exists()) {
				excel.loadFile(f);
				excel.getWorkBookDinamicamente(f, hojaName, manipulador, definition);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Entrega un objeto ConsultaData para la hoja indicada del archivo excel enviado a servidor.<br/>
	 * Siempre tomará el nombre de columna como indicador del campo, por norma del Objeto ConsultaData los nombre se transformarán a minúsculas.<br/>
	 * 
	 * @author SUPER-PANCHO
	 * @since 2016-03-04
	 * 
	 * */
	public ConsultaData getParamExcel_ColRenamed(IIOClaseWebLight  io, String nameParam, Integer hojaPosition, Map<String, CellExcelBasicTypes> definition, boolean useColumnNames ) throws Exception {
		ConsultaData data = getParamExcelPrivate(io, nameParam, false, hojaPosition, definition);
		ConsultaData newData = null;
		boolean primeraFila = true;
		List<String> listaColumnas = null;
		Map<String,String> relacionColumnas = new HashMap<String,String>();
		
		while( data != null && data.next() ) {
			if(primeraFila == true && useColumnNames) {
				listaColumnas = data.getNombreColumnas();
				List<String> newNombre = new ArrayList<String>();
				
				for(String n : listaColumnas) {
					if(n != null) {
						String newCol = data.getForcedString(n.trim());
						
						if(newCol != null) {
							newCol = newCol.trim();
						}
						
						int adic = 1;
						if(newCol.length() > 0) {
							newCol = newCol.toLowerCase();
							
							while(newNombre.indexOf(newCol) > -1) {
								newCol = newCol + "_" + adic;
								adic++;
							}
							
							newNombre.add(newCol);
							relacionColumnas.put(n.trim(), newCol);
						}
						
					}
				}
				
 	
				newData = new ConsultaData(newNombre);
				primeraFila=false;
			}
			else if(!primeraFila) {
				DataFields fields = new DataFields();
				
				for(String n : listaColumnas) {
					if(n != null && relacionColumnas.get(n.trim()) != null ) {
						fields.put(relacionColumnas.get(n.trim()).toLowerCase(), data.getForcedString(n.trim()).trim().toLowerCase());
					}
				}
				
				newData.add(fields);	
			}
		}
		
		return newData;
	}
	
	public ConsultaData getParamExcelPrivate(IIOClaseWebLight  io, String nameParam, boolean saltaPrimeraFila, Object hojaIndicator, Map<String, CellExcelBasicTypes> definition ) throws Exception {
		if(definition == null) {
			/*si no tiene definición, se toman los primeros 50 únicas columnas como  String*/
			definition = new HashMap<String, CellExcelBasicTypes>();
			for(int i = 1; i<=50;i++) {
				definition.put(String.valueOf(i), CellExcelBasicTypes.String);
			}
		}
		
		Excel2010 excel = new Excel2010(saltaPrimeraFila);
		try {
			File f = io.getFile(nameParam);
			
			if(f != null && f.exists()) {
				excel.loadFile(f);
				
				ExcelProcessConsultaDataExact process = new ExcelProcessConsultaDataExact(definition);
				if(hojaIndicator instanceof Integer) {
					excel.getWorkBookDinamicamente(f, (Integer)hojaIndicator, process, definition);
				}
				else if(hojaIndicator instanceof String) {
					excel.getWorkBookDinamicamente(f, (String)hojaIndicator, process, definition);
				}
				
				ConsultaData data = process.getDataExcel();
				 
				return data;	
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<String> getParamExcelSheetsName(IIOClaseWebLight  io, String nameParam ) throws Exception {
		Excel2010 excel = new Excel2010(false);
		try {
			File f = io.getFile(nameParam);
			if(f != null && f.exists()) {
				excel.loadFile(f);
				return excel.getSheets(f);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return new ArrayList<String>();
	}

	
	public boolean retExcel(IIOClaseWebLight  io,ConsultaData data,  String fileName) {
		Cronometro cro = new Cronometro();
		cro.Start();
		return retExcel(io, data,fileName,cro, null, new HashMap<String,String>());
	}
	
	public boolean retExcel(IIOClaseWebLight  io,ConsultaData data,  String fileName , Cronometro cro) {

		return retExcel(io, data,fileName,cro, null, new HashMap<String,String>());
	}
	
	public boolean retExcel(IIOClaseWebLight  io, ConsultaData data, String fileName,Cronometro cro, Integer rowHeight) {
		return retExcel(io, data, fileName, cro, rowHeight, null);
	}
	
	public boolean retExcel(IIOClaseWebLight  io,ConsultaData data, String fileName,Cronometro cro, Integer rowHeight, Map<String,String> rowMapping) {
		return retExcel(io, data, fileName, cro, rowHeight, rowMapping, null);
	}

	public boolean retExcel(IIOClaseWebLight  io, ConsultaData data, String fileName, Cronometro cro, Integer rowHeight, Map<String,String> rowMapping, String strTemplate) {
		
		SimpleHash modelRoot = new SimpleHash();
		IFieldManipulator fManipulator = new ManipulatorSqlServer2000();
		
		Map<String,String> rowVibles = new HashMap<String, String>();
		
		Locale.setDefault(Locale.GERMANY);
		
//		System.out.println("java.version = " + System.getProperty("java.version"));
//		System.out.println("Locale.getDefault() = " + Locale.getDefault());

		//Cabecera
		SimpleList cabecera = new SimpleList();
		SimpleList campos = new SimpleList();
		
		List<String> nombres = data.getNombreColumnas();
		for(String nombre : nombres) {
			
			String strNombre = nombre;
			boolean bolVisible = true;
			
			SimpleHash hash = new SimpleHash();
			
			int i = 0;
			if (bolVisible) { 
				rowVibles.put(strNombre, "1");
				
				hash.put("valor", nombre);
				campos.add(hash);
				if (rowMapping.size()==0) {
					cabecera.add(hash);
				}
				
				for(String n: nombres) {
					if(nombre != null && nombre.equals(n)) {
						i++;
					}
				}
			}else {
				rowVibles.put(strNombre, "0");
			}
		
			try {
				if(i > 1) {
					throw new Exception("El campo \""+nombre+"\" está repetido, esto no puede ser así.");	
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (rowMapping.size()>0) {
			for (Map.Entry<String, String> campo : rowMapping.entrySet()) {
				SimpleHash hash = new SimpleHash();
				hash.put("valor", campo.getValue());
				cabecera.add(hash);
			}
		}
		
		//Carga de Datos
		DataList lista = data.getData();
		SimpleList filas = new SimpleList();
		for( DataFields f : lista ) {
			
			SimpleHash hashColumnas = new SimpleHash();
			SimpleList columnas = new SimpleList();
			
			LinkedHashMap valores = new LinkedHashMap();
			
			if (rowMapping.size()>0) {
				for (Map.Entry<String, String> campo : rowMapping.entrySet()) {
					boolean bolVisible = false;
					if (rowVibles.get(campo.getKey())!=null){
						bolVisible = "1".equals(rowVibles.get(campo.getKey()).toString());
					}
					if (bolVisible) {

						SimpleHash hash = new SimpleHash();
						String val = null;
						
						if (f.get(campo.getKey()).getObject() instanceof ReservedWord) {
							val = ((ReservedWord)f.get(campo.getKey()).getObject()).toString();
						}else{
							val = toFormatLocaleOut(fManipulator, f.get(campo.getKey()));
//							val = StringEscapeUtils.escapeHtml(fManipulator.getForcedString(f.get(campo.getKey())));
						}
						
						if(val == null || val.trim().length() == 0) {
							val = "";
						}
						 
						val = val.trim().replaceAll(" ", "&nbsp;");
						 
						valores.put(campo.getValue(), val);
						hash.put("valor", val );
						columnas.add(hash);
					}
				}
			}else{
			
				Set<String> set = f.keySet();
				for(String key : set) {
					
					boolean bolVisible = false;
					if (rowVibles.get(key.toString())!=null){
						bolVisible = "1".equals(rowVibles.get(key.toString()).toString());
					}
					
					if (bolVisible) {
						
						SimpleHash hash = new SimpleHash();
						String val = null;
						
						if( f.get(key).getObject() instanceof ReservedWord) {
							val = ((ReservedWord)f.get(key).getObject()).toString();
						}
						else {
							val = toFormatLocaleOut(fManipulator, f.get(key));
//							val = StringEscapeUtils.escapeHtml(fManipulator.getForcedString(f.get(key)));
						}
						
						if(val == null || val.trim().length() == 0) {
							val = "";
						}
						 
						val = val.trim().replaceAll(" ", "&nbsp;");
						//val = setSpan(val.trim());
						
						 
						valores.put(key, val);
						hash.put("valor", val );
						columnas.add(hash);
					}
	
				}
			}

			hashColumnas.put("columnas", columnas);
			filas.add(hashColumnas);
			
		}
		
//		modelRoot.put("cabecera_size" , String.valueOf(nombres.size())); 
		modelRoot.put("tiempo"	, cro.getTimeHHMMSS_milli()); 
		modelRoot.put("hora"	, Formatear.getInstance().toDate(Calendar.getInstance().getTime(),"dd-MM-yyyy HH:mm:ss")); 
		modelRoot.put("filas"	,filas);
		modelRoot.put("cabecera",cabecera);
		if( rowHeight != null) {
			modelRoot.put("rowHeight", String.valueOf(rowHeight));	
		}
		
		if (strTemplate==null){
			strTemplate = "excel/reporteGenerico.html";
		}
		
		return retExcel(io, strTemplate, modelRoot, fileName);
	}
	
	public boolean retExcel(IIOClaseWebLight  io,ConsultaData data,  String fileName , Integer rowHeight) {
		Cronometro cro = new Cronometro();
		cro.Start();
		return retExcel(io,data,fileName,cro, rowHeight, new HashMap<String,String>());
	}
	
	public boolean retExcel(IIOClaseWebLight  io,ConsultaData data,  String fileName, Map<String,String> rowMapping) {
		Cronometro cro = new Cronometro();
		cro.Start();
		return retExcel(io, data, fileName, cro, null, rowMapping);
	}
	
	public boolean retExcel(IIOClaseWebLight  io,ConsultaData data,  String fileName, String strTemplate) {
		Cronometro cro = new Cronometro();
		cro.Start();
		return retExcel(io,data,fileName,cro, null, new HashMap<String,String>(), strTemplate);
	}
	
	
	public boolean retExcel(IIOClaseWebLight  io, String templatePath, SimpleHash modelRoot, String fileName) {
		io.getResp().setContentType("application/vnd.ms-excel");
		io.getResp().setHeader("Content-Disposition", "attachment; filename="+fileName);
    	
    	return retTemplatePriv(io, templatePath, modelRoot);
	}
			
	
	public boolean retExcel(IIOClaseWebLight  io, String templatePath, String fileName) {
		 return retExcel(io, templatePath, new SimpleHash(), fileName);
	}

	public boolean retExcel(IIOClaseWebLight  io, String templatePath, String fileName, SimpleHash modelRoot) {
		 return retExcel(io, templatePath, modelRoot , fileName);
	}
	
	public boolean retExcelConError(IIOClaseWebLight  io,String fileName) {
		Cronometro cro = new Cronometro();
		cro.Start();
		
		List<String> nombreColumnas = new ArrayList<String>();
		nombreColumnas.add("resultado");
		
		ConsultaData data = new ConsultaData(nombreColumnas);
		
		DataFields fields = new DataFields();
		fields.put("resultado", "Error desconocido");
		
		return retExcel(io,data,fileName,cro, null, new HashMap<String,String>());
	}
	
	private boolean retTemplatePriv(IIOClaseWebLight  io, String templatePath, SimpleHash modelRoot) {
    	boolean ok = false;
    	PrintWriter out = null;
    	
    	try {  
    		String flow = io.getUtil(IOUtilFreemarker.class).getTemplateProcces(io, templatePath, modelRoot);
	    	out = io.getResp().getWriter();
	    	out.write(flow);
	    	ok = true;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		ok = false;
    	}
    	finally {
    		if(out != null) {
    			out.close();
    			out.flush();
    		}
    	}
    	
    	return ok ;
    }
	
	private String toFormatLocaleOut(IFieldManipulator fManipulator, Field fld) {
		String strOut = null;
		
		if(fld != null) {
			Object obj = fld.getObject();
			try {
				if (obj instanceof Double || obj instanceof BigDecimal) {
					strOut = String.format(Locale.getDefault(), "%f%n", Double.valueOf(fld.toString()));			
				}else if (obj instanceof Float || obj instanceof BigDecimal) {
					strOut = String.format(Locale.getDefault(), "%f%n", Float.valueOf(fld.toString()));
				}else{
					strOut = StringEscapeUtils.escapeHtml(fManipulator.getForcedString(fld));
				}
			}catch (Exception e) {
				strOut = StringEscapeUtils.escapeHtml(fManipulator.getForcedString(fld));
			}
		}

		return strOut;
	}
	
}
