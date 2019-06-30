package portal.com.eje.frontcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.validar.Validar;
import portal.com.eje.portal.factory.Util;

/**
 * Pensado para ayudar a IOClaseWEB, al ser esta  una clase dios ocurre que no es necesario crear otra clase para tener herramientas, sin embargo,
 * esta clase podrá ser llamada solo para convertir la respuesta texto en un vo voOUTClaseWeb, no le veo otro fin.
 * 
 * @since 2016-10-20
 * @autor Francisco
 * 
 * */

public class IOClaseWebTool  {
	
 
	public IOClaseWebTool() {
		
	}
	
	public static IOClaseWebTool getInstance() {
		return Util.getInstance(IOClaseWebTool.class);
		
	}
	
	public IOClaseWebResponse getSimulaTrue() {
		Cronometro cro = new Cronometro();
		cro.start();
		
		IOClaseWebResponse io = new IOClaseWebResponse();
		io.setSuccess(true);
		io.setMessage("Simulación de un true");
		io.setTime_process(cro.getTimeHHMMSS_milli());
		io.setTotal( 0 );
		io.setContext_path( null );
		io.setData( null );
		io.setUsuario( null );
		io.setUsuario_is_session( false);
		
		return io;
	}
	
	public IOClaseWebResponse fromText(String xml) {
		IOClaseWebResponse io = null;
		
		try {
			io =  new IOClaseWebResponse();
			io.setOriginalReponse(xml);
			Map params = new Gson().fromJson(xml, Map.class);
			
			if(params != null) {
				io.setSuccess( (Boolean) params.get("success"));
				io.setMessage( (String) params.get("message") );
				io.setTime_process( (String) params.get("time_process")	);
				io.setTotal( (Double) params.get("total") );
				io.setContext_path( (String) params.get("context_path"));
				io.setData( consultaDatafromText( (ArrayList) params.get("data") ));
				io.setUsuario( consultaDatafromText( (ArrayList) params.get("usuario") ));
				io.setUsuario_is_session( Validar.getInstance().validarBoolean( String.valueOf(params.get("usuario_is_session_valid")) , false));
				
				if(io.total > 0) {
					ConsultaData data = io.getData();
					if(data != null && data.next()) {
						
						if(data.existField("msg")) {
							io.setMessage(data.getString("msg"));	
						}
						
						if(data.existField("message")) {
							io.setMessage(data.getString("message"));
						}
					}
				}
			}
			
		}
		catch(Exception e) {
			//System.out.println( new portal.com.eje.frontcontroller.error.PPMBadResponse( e ));
		}
		
		return io;
	}
	
	private ConsultaData consultaDatafromText(ArrayList<Map> datos) {
			ConsultaData data = null;
		
		try {
			List<String>	 columnas 	= new ArrayList<String>();
			for(Map map : datos) {
				DataFields fields = new DataFields();
				Set<String> sets = map.keySet();
				for(String key: sets) {
					if(columnas.indexOf(key) == -1 ){
						columnas.add(key);	
					}
				}
			}
			
			if(columnas.size() == 0) {
				columnas.add("vacia");
			}
			
			data = new ConsultaData(columnas);
			for(Map map : datos) {
				DataFields fields = new DataFields();
				Set<String> sets = map.keySet();
				for(String key: sets) {
					fields.put(key, new Field(map.get(key)));
				}
				data.add(fields);
			}
		}
		catch(Exception e) {
			//System.out.println(e.getMessage());
		}
		
		return data;
	}


}

