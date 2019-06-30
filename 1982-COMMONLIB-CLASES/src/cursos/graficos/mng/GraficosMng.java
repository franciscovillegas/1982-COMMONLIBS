package cursos.graficos.mng;

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
		
	    Object[] params = {};
	    ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	      
		return data;
	}
	
	public ConsultaData getMeses(String periodo){
		String sql = new String();
		sql = "SELECT distinct MONTH(fecha_cierre) AS meses FROM eje_cap_costos_cabecera_franquicia  " +
			  "WHERE periodo_accion = " + periodo +
			  " ORDER BY MONTH(fecha_cierre)"; 
		
	    Object[] params = {};
	    ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	      
		return data;
	}

	public IDataOut getSerieCostoEmpresa(String empresa, String planta, String esarea, String periodo) {
		DataList lista = new DataList();
		DataFields d = new DataFields();
		d.put("name",new Field("Empresa"));
		
		if (!periodo.equals("0")){
			if (esarea.equals("0")){
				d.put("data", new Field(getSerieValuesCostoEmpUnidadP(empresa, planta, periodo)) );
			}
			else
			{
				d.put("data", new Field(getSerieValuesCostoEmpAreaP(empresa, planta, periodo)) );
			}
		}else{
			if (esarea.equals("0")){
				d.put("data", new Field(getSerieValuesCostoEmpUnidad(empresa, planta)) );
			}
			else
			{
				d.put("data", new Field(getSerieValuesCostoEmpArea(empresa, planta)) );
			}
		}
		
		
		lista.add(d);
		
		JSonDataOut javaScript = new JSonDataOut(lista);
		return javaScript;
	}

	private IDataOut getSerieValuesCostoEmpUnidad(String empresa, String unidad) {
		ConsultaData data = null;
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT TOP 10 sum (cf.costo_empresa) AS costo_empresa ");
		sql.append(" FROM eje_cap_costos_cabecera_franquicia cf ");
		sql.append(" INNER JOIN eje_cap_students_history sh ON sh.course_id = cf.course_id and sh.accion_id = cf.accion_id ");
		sql.append(" INNER JOIN eje_ges_trabajador t ON t.rut = sh.student_rut ");
		sql.append(" WHERE t.empresa = ").append(empresa);
		sql.append(" AND t.unidad = '").append(unidad).append("'");
		sql.append(" GROUP BY periodo_accion ");
		Object[] params = {};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JSarrayDataOut jq = new JSarrayDataOut(data);
		return jq;
	}
	
	private IDataOut getSerieValuesCostoEmpUnidadP(String empresa, String unidad, String periodo) {
		ConsultaData data = null;
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT TOP 12 sum (cf.costo_empresa) AS costo_empresa ");
		sql.append(" FROM eje_cap_costos_cabecera_franquicia cf ");
		sql.append(" INNER JOIN eje_cap_students_history sh ON sh.course_id = cf.course_id and sh.accion_id = cf.accion_id ");
		sql.append(" INNER JOIN eje_ges_trabajador t ON t.rut = sh.student_rut ");
		sql.append(" INNER JOIN eje_cap_accion ca ON ca.course_id = cf.course_id and ca.accion_id = cf.accion_id  ");
		sql.append(" WHERE t.empresa = ").append(empresa);
		sql.append(" AND t.unidad = '").append(unidad).append("'");
		sql.append(" AND cf.periodo_accion = ").append(periodo);
		sql.append(" GROUP BY MONTH(ca.accion_inicio) ");
		Object[] params = {};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JSarrayDataOut jq = new JSarrayDataOut(data);
		return jq;
	}
	
	private IDataOut getSerieValuesCostoEmpArea(String empresa, String unidad) {
		ConsultaData data = null;
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT TOP 10 sum (cf.costo_empresa) AS costo_empresa ");
		sql.append(" FROM eje_cap_costos_cabecera_franquicia cf ");
		sql.append(" INNER JOIN eje_cap_students_history sh ON sh.course_id = cf.course_id and sh.accion_id = cf.accion_id ");
		sql.append(" INNER JOIN eje_ges_trabajador t ON t.rut = sh.student_rut ");
		sql.append(" WHERE t.empresa = ").append(empresa);
		sql.append(" AND t.unidad in (SELECT unidad FROM GetDescendientes('").append(unidad).append("'))");
		sql.append(" GROUP BY periodo_accion ");
		Object[] params = {};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JSarrayDataOut jq = new JSarrayDataOut(data);
		return jq;
	}
	
	private IDataOut getSerieValuesCostoEmpAreaP(String empresa, String unidad, String periodo) {
		ConsultaData data = null;
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT TOP 12 sum (cf.costo_empresa) AS costo_empresa ");
		sql.append(" FROM eje_cap_costos_cabecera_franquicia cf ");
		sql.append(" INNER JOIN eje_cap_students_history sh ON sh.course_id = cf.course_id and sh.accion_id = cf.accion_id ");
		sql.append(" INNER JOIN eje_ges_trabajador t ON t.rut = sh.student_rut ");
		sql.append(" INNER JOIN eje_cap_accion ca ON ca.course_id = cf.course_id and ca.accion_id = cf.accion_id  ");
		sql.append(" WHERE t.empresa = ").append(empresa);
		sql.append(" AND t.unidad in (SELECT unidad FROM GetDescendientes('").append(unidad).append("'))");
		sql.append(" AND cf.periodo_accion = ").append(periodo);
		sql.append(" GROUP BY MONTH(ca.accion_inicio) ");
		Object[] params = {};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JSarrayDataOut jq = new JSarrayDataOut(data);
		return jq;
	}
	
	public IDataOut getSerieCostoSence(String empresa, String unidad, String esarea, String periodo) {
		DataList lista = new DataList();
		DataFields d = new DataFields();
		d.put("name",new Field("Sence"));
		
		if (!periodo.equals("0")){
			if (esarea.equals("0")){
				d.put("data", new Field(getSerieValuesCostoSenceUnidadP(empresa, unidad, periodo)) );
			}
			else
			{
				d.put("data", new Field(getSerieValuesCostoSenceAreaP(empresa, unidad, periodo)) );
			}
		}else{
			if (esarea.equals("0")){
				d.put("data", new Field(getSerieValuesCostoSenceUnidad(empresa, unidad)) );
			}
			else
			{
				d.put("data", new Field(getSerieValuesCostoSenceArea(empresa, unidad)) );
			}
		}
		
		
		lista.add(d);
		
		JSonDataOut javaScript = new JSonDataOut(lista);
		return javaScript;
	}

	private IDataOut getSerieValuesCostoSenceUnidad(String empresa, String unidad) {
		
		ConsultaData data = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT TOP 10 sum (cf.ingreso_franquicia) AS costo_sence ");
		sql.append(" FROM eje_cap_costos_cabecera_franquicia cf ");
		sql.append(" INNER JOIN eje_cap_students_history sh ON sh.course_id = cf.course_id and sh.accion_id = cf.accion_id ");
		sql.append(" INNER JOIN eje_ges_trabajador t ON t.rut = sh.student_rut ");
		sql.append(" WHERE t.empresa = ").append(empresa);
		sql.append(" AND t.unidad = '").append(unidad).append("'");
		sql.append(" GROUP BY periodo_accion ");
		Object[] params = {};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JSarrayDataOut jq = new JSarrayDataOut(data);
		return jq;
	}
	
	private IDataOut getSerieValuesCostoSenceUnidadP(String empresa, String unidad, String periodo) {
		
		ConsultaData data = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT TOP 12 sum (cf.ingreso_franquicia) AS costo_sence ");
		sql.append(" FROM eje_cap_costos_cabecera_franquicia cf ");
		sql.append(" INNER JOIN eje_cap_students_history sh ON sh.course_id = cf.course_id and sh.accion_id = cf.accion_id ");
		sql.append(" INNER JOIN eje_ges_trabajador t ON t.rut = sh.student_rut ");
		sql.append(" INNER JOIN eje_cap_accion ca ON ca.course_id = cf.course_id and ca.accion_id = cf.accion_id  ");
		sql.append(" WHERE t.empresa = ").append(empresa);
		sql.append(" AND t.unidad = '").append(unidad).append("'");
		sql.append(" AND cf.periodo_accion = ").append(periodo);
		sql.append(" GROUP BY MONTH(ca.accion_inicio) ");
		Object[] params = {};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JSarrayDataOut jq = new JSarrayDataOut(data);
		return jq;
	}
	
	private IDataOut getSerieValuesCostoSenceArea(String empresa, String unidad) {
		
		ConsultaData data = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT TOP 10 sum (cf.ingreso_franquicia) AS costo_sence ");
		sql.append(" FROM eje_cap_costos_cabecera_franquicia cf ");
		sql.append(" INNER JOIN eje_cap_students_history sh ON sh.course_id = cf.course_id and sh.accion_id = cf.accion_id ");
		sql.append(" INNER JOIN eje_ges_trabajador t ON t.rut = sh.student_rut ");
		sql.append(" WHERE t.empresa = ").append(empresa);
		sql.append(" AND t.unidad in (SELECT unidad FROM GetDescendientes('").append(unidad).append("'))");
		sql.append(" GROUP BY periodo_accion ");
		Object[] params = {};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JSarrayDataOut jq = new JSarrayDataOut(data);
		return jq;
	}
	
	private IDataOut getSerieValuesCostoSenceAreaP(String empresa, String unidad, String periodo) {
		
		ConsultaData data = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT TOP 12 sum (cf.ingreso_franquicia) AS costo_sence ");
		sql.append(" FROM eje_cap_costos_cabecera_franquicia cf ");
		sql.append(" INNER JOIN eje_cap_students_history sh ON sh.course_id = cf.course_id and sh.accion_id = cf.accion_id ");
		sql.append(" INNER JOIN eje_ges_trabajador t ON t.rut = sh.student_rut ");
		sql.append(" INNER JOIN eje_cap_accion ca ON ca.course_id = cf.course_id and ca.accion_id = cf.accion_id  ");
		sql.append(" WHERE t.empresa = ").append(empresa);
		sql.append(" AND t.unidad in (SELECT unidad FROM GetDescendientes('").append(unidad).append("'))");
		sql.append(" AND cf.periodo_accion = ").append(periodo);
		sql.append(" GROUP BY MONTH(ca.accion_inicio) ");
		Object[] params = {};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JSarrayDataOut jq = new JSarrayDataOut(data);
		return jq;
	}
	
	public IDataOut getSerieCostoTotal(String empresa, String unidad, String esarea, String periodo) {
		DataList lista = new DataList();
		DataFields d = new DataFields();
		d.put("name",new Field("Total"));
		
		if (!periodo.equals("0")){
			if (esarea.equals("0")){
				d.put("data", new Field(getSerieValuesCostoTotalUnidadP(empresa, unidad, periodo)) );
			}
			else
			{
				d.put("data", new Field(getSerieValuesCostoTotalAreaP(empresa, unidad, periodo)) );
			}
		}else{
			if (esarea.equals("0")){
				d.put("data", new Field(getSerieValuesCostoTotalUnidad(empresa, unidad)) );
			}
			else
			{
				d.put("data", new Field(getSerieValuesCostoTotalArea(empresa, unidad)) );
			}
		}
		
		
		lista.add(d);
		
		JSonDataOut javaScript = new JSonDataOut(lista);
		return javaScript;
	}
	
	private IDataOut getSerieValuesCostoTotalUnidad(String empresa, String unidad) {
		
		ConsultaData data = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT TOP 10 sum (cf.total_costos_curso_accion) AS costo_total ");
		sql.append(" FROM eje_cap_costos_cabecera_franquicia cf ");
		sql.append(" INNER JOIN eje_cap_students_history sh ON sh.course_id = cf.course_id and sh.accion_id = cf.accion_id ");
		sql.append(" INNER JOIN eje_ges_trabajador t ON t.rut = sh.student_rut ");
		sql.append(" WHERE t.empresa = ").append(empresa);
		sql.append(" AND t.unidad = '").append(unidad).append("'");
		sql.append(" GROUP BY periodo_accion ");
		Object[] params = {};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JSarrayDataOut jq = new JSarrayDataOut(data);
		return jq;
	}
	
	private IDataOut getSerieValuesCostoTotalUnidadP(String empresa, String unidad, String periodo) {
		
		ConsultaData data = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT TOP 12 sum (cf.total_costos_curso_accion) AS costo_total ");
		sql.append(" FROM eje_cap_costos_cabecera_franquicia cf ");
		sql.append(" INNER JOIN eje_cap_students_history sh ON sh.course_id = cf.course_id and sh.accion_id = cf.accion_id ");
		sql.append(" INNER JOIN eje_ges_trabajador t ON t.rut = sh.student_rut ");
		sql.append(" INNER JOIN eje_cap_accion ca ON ca.course_id = cf.course_id and ca.accion_id = cf.accion_id  ");
		sql.append(" WHERE t.empresa = ").append(empresa);
		sql.append(" AND t.unidad = '").append(unidad).append("'");
		sql.append(" AND cf.periodo_accion = ").append(periodo);
		sql.append(" GROUP BY MONTH(ca.accion_inicio) ");
		Object[] params = {};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JSarrayDataOut jq = new JSarrayDataOut(data);
		return jq;
	}
	
	private IDataOut getSerieValuesCostoTotalArea(String empresa, String unidad) {
		
		ConsultaData data = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT TOP 10 sum (cf.total_costos_curso_accion) AS costo_total ");
		sql.append(" FROM eje_cap_costos_cabecera_franquicia cf ");
		sql.append(" INNER JOIN eje_cap_students_history sh ON sh.course_id = cf.course_id and sh.accion_id = cf.accion_id ");
		sql.append(" INNER JOIN eje_ges_trabajador t ON t.rut = sh.student_rut ");
		sql.append(" WHERE t.empresa = ").append(empresa);
		sql.append(" AND t.unidad in (SELECT unidad FROM GetDescendientes('").append(unidad).append("'))");
		sql.append(" GROUP BY periodo_accion ");
		Object[] params = {};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JSarrayDataOut jq = new JSarrayDataOut(data);
		return jq;
	}
	
	private IDataOut getSerieValuesCostoTotalAreaP(String empresa, String unidad, String periodo) {
		
		ConsultaData data = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT TOP 12 sum (cf.total_costos_curso_accion) AS costo_total ");
		sql.append(" FROM eje_cap_costos_cabecera_franquicia cf ");
		sql.append(" INNER JOIN eje_cap_students_history sh ON sh.course_id = cf.course_id and sh.accion_id = cf.accion_id ");
		sql.append(" INNER JOIN eje_ges_trabajador t ON t.rut = sh.student_rut ");
		sql.append(" INNER JOIN eje_cap_accion ca ON ca.course_id = cf.course_id and ca.accion_id = cf.accion_id  ");
		sql.append(" WHERE t.empresa = ").append(empresa);
		sql.append(" AND t.unidad in (SELECT unidad FROM GetDescendientes('").append(unidad).append("'))");
		sql.append(" AND cf.periodo_accion = ").append(periodo);
		sql.append(" GROUP BY MONTH(ca.accion_inicio) ");
		Object[] params = {};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JSarrayDataOut jq = new JSarrayDataOut(data);
		return jq;
	}
	
	public ConsultaData getUnidad(String unidad){
		String sql = new String();
		sql = "select unid_desc from eje_ges_unidades where unid_id = ?"; 
		
	    Object[] params = {unidad};
	    ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	      
		return data;
	}
}
