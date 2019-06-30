package portal.com.eje.frontcontroller.ioutils;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.portal.factory.Weak;

public class IOUtilJson extends IOUtil {

	public static IOUtilJson getIntance() {
		return Weak.getInstance(IOUtilJson.class);
	}
	
	public boolean retJson(IIOClaseWebLight io, Map<String,String> map) {
		DataList lista = new DataList();
		Set<String> set = map.keySet();
		DataFields fields = new DataFields();
		
		for(String s : set) {
			fields.put(s, new Field(map.get(s)));
			
		}
		
		lista.add(fields);
		JSonDataOut out = new JSonDataOut(lista);
		return io.retJson(out.getListData());
	}
	
	/**
	 * Retorna un json desde una consulta SQL, actúa de forma directa.
	 * @since 2015-07-17
	 * @author Francisco 
	 * */
	
	public void retJsonFromSelect(IIOClaseWebLight io, String jndi, String sql) {
		retJsonFromSelect(io, jndi, sql, null);
	}

	/**
	 * Retorna un json desde una consulta SQL, actúa de forma directa.
	 * @since 2015-07-17
	 * @author Francisco 
	 * */
	
	public void retJsonFromSelect(IIOClaseWebLight io, String jndi, String sql, Object[] params) {
		boolean ok = false;
		ConsultaData data = null;
		try {
			if(params != null) {
				data = ConsultaTool.getInstance().getData(jndi, sql, params);	
			}
			else {
				data = ConsultaTool.getInstance().getData(jndi, sql);
			}
			
			JSonDataOut out = new JSonDataOut(data);
			io.retJson("["+out.getListData()+"]");
			ok = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(!ok) {
				io.retTexto(false);
			}
		}
	}
	
	public boolean retJson(IIOClaseWebLight io, ConsultaData cData) {
		JSonDataOut out = new JSonDataOut(cData);
		return io.retJson("["+out.getListData()+"]");
	}
	
	public boolean retJson(IIOClaseWebLight io, DataList lista) {
		JSonDataOut out = new JSonDataOut(lista);
		return io.retJson("["+out.getListData()+"]");
	}
}
