package cl.ejedigital.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.gson.Gson;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.IEscape;
import cl.ejedigital.consultor.output.JSarrayDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.consultor.output.ReservedWord;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.Order;
import cl.ejedigital.web.frontcontroller.IOClaseWeb;

/**
 * Ya no se ocupa, 
 * @deprecated
 * @author Pancho
 * @see IOClaseWeb
 * */
public class SenchaTool {

	public String getSenchaJsonConsulta(ConsultaData data, boolean estado, String limit, String page, String start, String sort) {
		
		try {
			int rows	  = 0 ;
			
			if(  data != null) {
				rows = data.getData().size();
			}
			
			if(data != null && start != null && limit != null) {
			
				if(  sort != null ) {
					List array = new Gson().fromJson(sort, List.class);
					if(array != null && array.size() > 0) {
						if(array != null && array.size() > 0){
							Order order = Order.Ascendente;
							if("DESC".endsWith((String)((Map)array.get(0)).get("direction"))) {
								order =Order.Descendente;
							}
							
							ConsultaTool.getInstance().sort(data, (String)((Map)array.get(0)).get("property"), order);
						}
					}				
				}
				
				ConsultaData subData = new ConsultaData(data.getNombreColumnas());
				int limiteSuperior = Integer.parseInt(limit ) * Integer.parseInt(page);
				if(limiteSuperior > rows) {
					limiteSuperior = rows;
				}
				subData.getData().addAll(data.getData().subList(Integer.parseInt(start), limiteSuperior ));
				data = subData;
			}
				
			
			JSonDataOut dataJs    = new JSonDataOut(data);
			dataJs.setEscape(new EscapeSencha());
			
			DataList result2 = new DataList();
			DataFields fields2 = new DataFields();
			fields2.put("value", new Field(dataJs));
			result2.add(fields2);
			JSarrayDataOut dataArray = new JSarrayDataOut(result2);
	
			
			DataList result = new DataList();
			DataFields fields = new DataFields();
			result.add(fields);
			fields.put("success", new Field(estado));
			fields.put("data"  , new Field(dataArray));
			fields.put("total"    , new ReservedWord(String.valueOf(rows)));	
	
			JSonDataOut dataOutResponse = new JSonDataOut(result);
			dataOutResponse.setEscape(new EscapeSencha());
	
			return dataOutResponse.getListData();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}

	public String getSenchaJsonConsulta(ConsultaData data ) {
		return getSenchaJsonConsulta(data, true, null, null, null, null);
	}

	public String getSenchaJsonConsultas(String mensaje, ConsultaData[] adicional, boolean valor) {
		return getSenchaJsonConsultas(mensaje, adicional, valor, null, null, null, null);
	}
	public String getSenchaJsonConsultas(String mensaje, ConsultaData[] adicional, boolean valor , String limit, String page, String start, String sort ) {
		List<String> columnas = new ArrayList<String>();
		columnas.add("msg");
		ConsultaData data = new ConsultaData(columnas);
		DataFields fields = new DataFields();
		fields.put("msg", mensaje);
		int pos = 0 ;
		
		if( adicional != null) {
			for(ConsultaData dataAdicional : adicional) {
				JSonDataOut dataJs    = new JSonDataOut(dataAdicional);
				
				DataList result2 = new DataList();
				DataFields fields2 = new DataFields();
				fields2.put("value", new Field(dataJs));
				result2.add(fields2);
				JSarrayDataOut dataArray = new JSarrayDataOut(result2);
				
				
				DataList result = new DataList();
				DataFields fieldsAdicional = new DataFields();
				result.add(fieldsAdicional);
				fields.put("success"				, new Field(true));
				fields.put("dataAdicional"+(pos++)	, new Field(dataArray));
				fields.put("total"    				, new ReservedWord(String.valueOf(dataAdicional.size())));	
			}
		}
		
		data.add(fields);
		
		return getSenchaJsonConsulta(data, valor, limit, page, start, sort);
	}

	public String getSenchaJsonList(DataList list, boolean estado) {
		List<String> cabecera = new ArrayList<String>();
		
		if(list != null && list.size() > 0) {
			Set<String> set = list.get(0).keySet();
			for(String s : set) {
				cabecera.add(s);
			}
		}
		else {
			cabecera.add("vacio");
		}
		
		ConsultaData data = new ConsultaData(cabecera);
		if(list != null && list.size() > 0) {
			for(DataFields d : list) {
				data.add(d);
			}
		}
		
		return getSenchaJsonConsulta(data, estado, null, null, null, null);
	}

	public String getSenchaJson(boolean valor) {
		
		List<String> lista = new ArrayList<String>();
		ConsultaData data = new ConsultaData(lista);
		return getSenchaJsonConsulta(data, valor, null, null, null, null);
	}
	
	public String getSenchaJson(String mensaje, boolean valor) {
		return getSenchaJsonConsultas(mensaje, null ,valor, null, null, null, null);
	}
	
	public String getSenchaJson(Map row, boolean valor) {
		List<String> columnas = new ArrayList<String>();
		Set<String> set = row.keySet();
		DataFields fields = new DataFields();
		
		for(String s : set ) {
			columnas.add(s);
			fields.put(s, row.get(s).toString());
		}
		
		ConsultaData data = new ConsultaData(columnas);
		data.add(fields);
		
		return getSenchaJsonConsulta(data, valor, null, null, null, null);
	}	
	
	
	class EscapeSencha implements IEscape {

		public String escape(String normal) {
			return StringEscapeUtils.escapeJava(normal);
		}
		
	}
}
