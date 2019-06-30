package graficos.mng;

import java.sql.Connection;
import java.sql.SQLException;

import organica.datos.Consulta;

import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSarrayDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.web.datos.ConsultaTool;

public class GraficosMng {

			
	public ConsultaData existeTabla(String tabla){
		String sql = new String();
		sql = "SELECT * FROM sysobjects WHERE  name = ? "; 
		
	    Object[] params = {tabla};
	    ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	      
		return data;
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
	
	public ConsultaData getPerfiles(){
		String sql = new String();
		sql = "select distinct(id_perfil),perfil from eje_ggd_perfiles"; 
		
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
	public ConsultaData getCargo(String id_perfil){
		String sql = new String();
		sql = "select distinct(id_perfil), perfil from eje_ggd_perfiles where id_perfil = ?"; 
		
	    Object[] params = {id_perfil};
	    ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	      
		return data;
	}
	
	public ConsultaData getCompetenciasPerfil( String idProceso, String idPerfil ){
		StringBuilder sql = new StringBuilder();
		sql.append("select  pc.id_perfil,p.perfil, pc.id_competencia, c.competencia, c.glosa,  "); 
		sql.append("pc.nivel_esperado from eje_ggd_perfiles_competencias pc ");
		sql.append("inner join eje_ggd_perfiles p on pc.id_perfil = p.id_perfil  and pc.id_proceso = p.id_proceso ");
		sql.append("inner join eje_ggd_competencias c on pc.id_competencia = c.id_competencia  ");
		sql.append("and pc.id_proceso = c.id_proceso where pc.id_proceso = ? and pc.id_perfil = ?");
	    Object[] params = {idProceso, idPerfil};
	    ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	      
	    return data;
	}
	public ConsultaData getProcesos(){
		String sql = new String();
		sql = "select id_proceso, proceso from eje_ggd_procesos"; 
		
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
	
	public IDataOut getSerieNivelEsperado(Integer id_proceso, String proceso) {
		DataList lista = new DataList();
		DataFields d = new DataFields();
		d.put("name",new Field("N.E. ".concat(proceso)));
		d.put("data", new Field(this.getNivelEsperado(id_proceso.toString(),"7")) );
		lista.add(d);	
		
		JSonDataOut javaScript = new JSonDataOut(lista);
		return javaScript;
	}
	
	private IDataOut getNivelEsperado (String idProceso, String idPerfil){
		StringBuilder sql = new StringBuilder();
		sql.append("select pc.nivel_esperado from eje_ggd_perfiles_competencias pc ");
		sql.append("inner join eje_ggd_perfiles p on pc.id_perfil = p.id_perfil  and pc.id_proceso = p.id_proceso ");
		sql.append("inner join eje_ggd_competencias c on pc.id_competencia = c.id_competencia  ");
		sql.append("and pc.id_proceso = c.id_proceso where pc.id_proceso = ? and pc.id_perfil = ?");
	    Object[] params = {idProceso, idPerfil};
	    ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	      
		JSarrayDataOut jq = new JSarrayDataOut(data);
		return jq;
		
	}
	
	public ConsultaData getPromedio( String idCompetencia, String idProceso, String idPerfil, String empresa, String unidad, int tipo ){
		StringBuilder sql = new StringBuilder();
		sql.append("select avg(ec.ponderacion_obt)*4/100 as nivel_promedio from eje_ggd_evaluacion_competencias  ec  "); 
		sql.append("inner join eje_ges_trabajador t on t.rut = ec.rut  ");
		sql.append("inner join eje_ggd_competencias c on c.id_proceso= ec.id_proceso ");
		sql.append("and c.id_competencia = ec.id_competencia "); 
		sql.append("inner join eje_ggd_perfiles p on ec.id_perfil = p.id_perfil  and ec.id_proceso = p.id_proceso ");
		sql.append("where ec.id_competencia = ? and ec.id_proceso = ? and ec.id_perfil = ? and t.empresa = ? and t.unidad");
		if(tipo==1){
			sql.append(" = '").append(unidad).append("'");
		}
		else{
			sql.append(" in (select unidad from dbo.GetDescendientes ('").append(unidad).append("')) ");
		}
	    Object[] params = {idCompetencia, idProceso, idPerfil, empresa};
	    ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	      
		return data;
	}
	
	
	
	public IDataOut getPromedios (String idProceso, String idPerfil, String empresa, String unidad, int tipo){
		DataList lista = new DataList();
		DataFields d = null;
		ConsultaData competencias = this.getCompetenciasPerfil(idProceso, idPerfil);
		ConsultaData promedio = null;
		Integer id;
		while(competencias.next()){
			id = competencias.getInt("id_competencia");			
			d = new DataFields();			
			promedio = this.getPromedio(id.toString(), idProceso, idPerfil, empresa, unidad, tipo);				
			double promedios;
			if(promedio.next()){
				promedios = promedio.getDouble("nivel_promedio");
				d.put("name", new Field(promedios) );				
			}
			lista.add(d);
		}
		
		JSarrayDataOut javaScript = new JSarrayDataOut(lista);
		return javaScript;
	}
	
	public ConsultaData periodosCostosCursos(){
        Object[] params = {};
        ConsultaData data = null;
        
        IDBConnectionManager dbConn = DBConnectionManager.getInstance();
        Connection conn = dbConn.getConnection("portal");
        Consulta consulta = new Consulta(conn);
        
        String sql = new String();
        String existe = "0";
        
        sql = "SELECT COUNT(*) existe FROM sysobjects WHERE name='eje_cap_costos_cabecera_franquicia'";
        consulta.exec(sql);
        
        
        if (consulta.next()){
            existe = consulta.getString("existe");
        }
        
        if (existe == "1"){
            sql = "SELECT DISTINCT periodo_accion FROM eje_cap_costos_cabecera_franquicia ORDER BY periodo_accion "; 
            
            try {
                data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
            } 
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        dbConn.freeConnection("portal",conn);
        
        return data;
    }

}
