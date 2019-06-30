package cl.eje.qsmcom.modulos;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import portal.com.eje.frontcontroller.resobjects.ResourceHtml;

import cl.eje.qsmcom.managers.ManagerQSM;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class PlantillaRegProcess {
	private Map <String,Double> totales;
	
	public PlantillaRegProcess() {
		totales = new HashMap<String,Double>();
	}
	
	public String procesaRegistros(SimpleHash modelRoot ,  ConsultaData dataRegistros, ConsultaData dataPlantilla ) {
		return procesaRegistros(modelRoot, null, dataRegistros, dataPlantilla);
	}
	
	public String procesaRegistros(SimpleHash modelRoot , ConsultaData dataRegistrosDetalle, ConsultaData dataRegistros, ConsultaData dataPlantilla ) {
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		
		
		if(dataPlantilla != null) {
			
			dataPlantilla.toStart();
			if(dataPlantilla.next()) { 
				String htm = dataPlantilla.getString("path_htm");
				boolean conDetalle = dataPlantilla.getBoolean("con_detalle");
				
				/* REGISTROS */
				String value = null;
				while(dataRegistros.next()) {
					List<String> lista = dataRegistros.getNombreColumnas();
					
					for(int i = 2 ; i<lista.size() ; i++) {
						value = dataRegistros.getString(lista.get(i));
						
						if(lista.get(i) != null) {
							modelRoot.put("columna_".concat(lista.get(i)), value);
						}
						
					}
	
				}
				
				if(conDetalle && dataRegistrosDetalle != null) {
					modelRoot.put("detalle", getDetalles(dataRegistrosDetalle));
				}
				
				/* REGISTROS EN FORMA DE LISTA */
				dataRegistros.toStart();
				modelRoot.put("regs", getListData(dFormat,dataRegistros));
				
				/* TOTALES */
				Set<String> set = totales.keySet();
				for(String s : set) {
					modelRoot.put("total_".concat(s), String.valueOf(totales.get(s)));
				}
				
				/* RETORNA LA PLANTILLA ADECUADA */
				htm = htm.substring(10, htm.length());
				FreemakerTool tool = new FreemakerTool();
				ResourceHtml html = new ResourceHtml();
				
				System.out.println("htm="+htm);
				
				try {
					return tool.templateProcess(html.getTemplate(htm), modelRoot);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("plantilla: ".concat(htm));
			}
		}
		
		return "Error";
	}
	
	private SimpleList getDetalles(ConsultaData dataRegistrosDetalle) {
		SimpleList lista = new SimpleList();
		SimpleHash hash = new SimpleHash();
		
		if(dataRegistrosDetalle != null) {
			hash.put("detalles", getListData(new SimpleDateFormat("dd-MM-yyyy"), dataRegistrosDetalle));
		}
		lista.add(hash);
		
		
		return lista;	
		
	}

	private SimpleList getListData(SimpleDateFormat dFormat , ConsultaData data) {
		SimpleList simple = new SimpleList();
		
		data.toStart();
		while(data.next()) {
			simple.add(getData(dFormat,data));			
		}
		
		return simple;
	}
	
	private SimpleHash getData(SimpleDateFormat dFormat , ConsultaData data) {
		SimpleHash hash = new SimpleHash();
		
		if(data != null && data.existData()) {
			Set<String> set = data.getActualData().keySet();
			
			for(String key : set) {
				String val = manipulateString(dFormat , data.getActualData().get(key));
				hash.put("columna_".concat(key), val);
				
				addTotal(key, val);
			}
		}

		
		return hash;
	}
	
	private void addTotal(String key, String val) {
		double valDouble = Validar.getInstance().validarDouble(val, 0);
		double valDoubleAnt = totales.get(key) == null ? 0 :  totales.get(key);
		totales.put(key, valDouble + valDoubleAnt );
	}
	
	private String manipulateString(SimpleDateFormat dFormat , Field f) {
		
		if(f != null) {
			try {
				if(f.getObject() instanceof Boolean) {
					return  String.valueOf((Boolean)f.getObject()) ;
				}
				else if(f.getObject() instanceof Byte) {
					return  String.valueOf((Byte)f.getObject()) ;
				}
				else if(f.getObject() instanceof Short) {
					return  String.valueOf((Short)f.getObject()) ;
				}
				else if(f.getObject() instanceof Integer) {
					return  String.valueOf((Integer)f.getObject()) ;
				}
				else if(f.getObject() instanceof Long) {
					return  String.valueOf((Long)f.getObject()) ;
				}
				else if(f.getObject() instanceof Float) {
					return  String.valueOf((Float)f.getObject()) ;
				}
				else if(f.getObject() instanceof Double) {
					return  String.valueOf((Double)f.getObject()) ;
				}
				else if(f.getObject() instanceof java.util.Date) {
					
					return  String.valueOf( dFormat.format((java.util.Date)f.getObject()) ) ;
				}
				else if(f.getObject() instanceof java.sql.Date) {
					return  String.valueOf(dFormat.format((java.sql.Date)f.getObject())) ;
				}
				else if(f.getObject() instanceof java.sql.Time) {
					return  String.valueOf(dFormat.format((java.sql.Time)f.getObject())) ;
				}
				else if(f.getObject() instanceof java.sql.Timestamp) {
					return  String.valueOf(dFormat.format((java.sql.Time)f.getObject())) ;
				}
				else if(f.getObject() instanceof String) {
					return  String.valueOf((String)f.getObject()) ;
				}
				else if(f.getObject() instanceof Blob) {
					return  String.valueOf((Blob)f.getObject()) ;
				}
				else if(f.getObject() instanceof BigDecimal) {
					return  String.valueOf((BigDecimal)f.getObject()) ;
				}
				else if(f.getObject() == null) {
					return null;
				}
				else {
					return "parseError";
				}
			}
			catch (Exception e) {
				return "parseError (".concat(e.toString()).concat(")");
			}
		}
		
		return null;
	}
	
}
