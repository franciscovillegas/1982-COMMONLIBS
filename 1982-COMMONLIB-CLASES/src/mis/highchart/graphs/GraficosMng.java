package mis.highchart.graphs;

import java.sql.SQLException;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSarrayDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.web.datos.ConsultaTool;

public class GraficosMng {

	
	public ConsultaData getPeriodos(){
		String sql = new String();
		sql = "SELECT DISTINCT periodo_accion FROM eje_cap_costos_cabecera_franquicia ORDER BY periodo_accion"; 

		ConsultaData data = null;
		
	    Object[] params = {};
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	      
		return data;
	}

	public IDataOut getSerieCostoEmpresa() {
		DataList lista = new DataList();
		DataFields d = new DataFields();
		d.put("name",new Field("Empresa"));
		d.put("data", new Field(getSerieValuesCostoE()) );
		lista.add(d);
		
		JSonDataOut javaScript = new JSonDataOut(lista);
		return javaScript;
	}

	private IDataOut getSerieValuesCostoE() {
		ConsultaData data = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT TOP 10 sum (costo_empresa) AS costo_empresa ");
		sql.append("FROM eje_cap_costos_cabecera_franquicia ");
		sql.append("GROUP BY periodo_accion ");
		Object[] params = {};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JSarrayDataOut jq = new JSarrayDataOut(data);
		return jq;
	}
	
	public IDataOut getSerieCostoSence() {
		DataList lista = new DataList();
		DataFields d = new DataFields();
		d.put("name",new Field("Sence"));
		d.put("data", new Field(getSerieValuesCostoS()) );
		lista.add(d);
		
		JSonDataOut javaScript = new JSonDataOut(lista);
		return javaScript;
	}

	private IDataOut getSerieValuesCostoS() {
		
		ConsultaData data = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT TOP 10 sum (ingreso_franquicia) AS costo_sence ");
		sql.append("FROM eje_cap_costos_cabecera_franquicia ");
		sql.append("GROUP BY periodo_accion ");
		Object[] params = {};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JSarrayDataOut jq = new JSarrayDataOut(data);
		return jq;
	}

}
