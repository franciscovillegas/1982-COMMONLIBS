package mis;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Tools;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class MisMng {
    public MisMng(Connection conex) {
        con = conex;
    }

    public Consulta getReportes(String empresa) {
    	Consulta consul = new Consulta(con);
    	
        String sql = "select m.id,m.Titulo_portal as nombre from eje_web_modelos m,eje_web_modelos_permiso p where m.tipo=2 and m.id = p.id_modelo and p.id_cliente ="+empresa+" and p.activo = 1";
        consul.exec(sql);
        
        System.out.println("Query Links : " + sql);
        return consul;
    }
    
    public Consulta JPGgetGrafosVisibles(String IDcliente) {
    	Consulta consul = new Consulta(con);
    	String sql = "select M.id as grafo_id, M.titulo_portal as grafo_name from eje_web_modelos_permiso P left outer join eje_web_modelos M on P.id_modelo = M.id where P.activo = 1 and M.tipo=1 and P.id_cliente =" + IDcliente;
        consul.exec(sql);
        return consul;
    }
    
    public Consulta JPGperiodosDisponibles(String IDcliente) {
    	Consulta consul = new Consulta(con);
    	String sql = "select periodo as peri_id, substring(periodo,1,4) as peri_ano, substring(periodo,5,2) as peri_mes from eje_web_modelos_disponible WHERE ID_CLIENTE =" + IDcliente + " order by periodo desc";
        consul.exec(sql);
        return consul;
    }
    
    public Consulta getPeriodosReporte(String IDcliente) {
    	Consulta consul = new Consulta(con);
       	String sql = "select substring(periodo,1,4) as periodo from eje_web_modelos_disponible where id_cliente ="+IDcliente+" group by substring(periodo,1,4) order by substring(periodo,1,4) desc";
        consul.exec(sql);
        return consul;
    }
    
    public SimpleList getTipoGrafosFormateados(Consulta p_periodosConsulta) {
        SimpleList graficos = new SimpleList();
        boolean primero = true;
        while (p_periodosConsulta.next()) {
        	SimpleHash grafo = new SimpleHash();
        	grafo.put("id", p_periodosConsulta.getString("grafo_id"));
        	grafo.put("desc", p_periodosConsulta.getString("grafo_name"));
            
            if (primero) {
            	grafo.put("primero", "selected");
                primero = false;
            }
            graficos.add(grafo);
         }
         return graficos;
    }  
    
    public SimpleList getPeriodosFormateados(Consulta p_periodosConsulta) {
        SimpleList periodos = new SimpleList();
        boolean primero = true;
        while (p_periodosConsulta.next()) {
        	SimpleHash periodo = new SimpleHash();
            periodo.put("id", p_periodosConsulta.getString("peri_id"));
            String periodoAnio = p_periodosConsulta.getString("peri_ano");
            int periodoMes = p_periodosConsulta.getInt("peri_mes");
            periodo.put("desc", Tools.RescataMes(periodoMes) + " de " + periodoAnio);
            
            if (primero) {
            	periodo.put("primero", "selected");
                primero = false;
            }
            periodos.add(periodo);
         }
         return periodos;
    }  
    
    public SimpleList GetPeriodosFormateadosAAA(Consulta consulta){
        SimpleList periodos = new SimpleList();
        boolean primero = true;
        while (consulta.next()) {
        	SimpleHash periodo = new SimpleHash();
            periodo.put("id", consulta.getString("periodo"));
            periodo.put("desc", consulta.getString("periodo"));
            
            if (primero) {
            	periodo.put("primero", "selected");
                primero = false;
            }
            periodos.add(periodo);
         }
         return periodos;
    }
    
    
    public SimpleList getTipoReportes(Consulta consulta) {  
        SimpleList tipos = new SimpleList();
        while (consulta.next()) {
        	SimpleHash reporte = new SimpleHash();
        	reporte.put("id",   consulta.getString("id"));
        	reporte.put("desc", consulta.getString("nombre"));
            
        	tipos.add(reporte);
         }
         return tipos;
    }

    public SimpleHash getSimpleHash(Consulta rs) {  
    	SimpleHash sh = new SimpleHash();
    	if(rs != null) {  
    		sh = rs.getSimpleHash(); 
    		rs.close(); 
    	}
    	return sh;
    }    

    private Connection con;
}
