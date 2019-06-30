package repository;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import cl.ejedigital.tool.svn.SnvManipulator;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

import portal.com.eje.datos.Consulta;

public class VisibilityManager {

    public VisibilityManager() {
    }
    
    public SimpleList GetVisibilidad(Connection con) {
        Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select id,nombre,universo from eje_repositorio_visibilidad order by nombre"));
        consul.exec(sql);
        return consul.getSimpleList();
    }
    
    public String GetIdVisibilidad(Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select isnull(MAX(id)+1,1) idv from eje_repositorio_visibilidad"));
        consul.exec(sql); 
        if(consul.next()) {
        	return consul.getString("idv");
        }
        else {
        	return "1";
        }
    }
    
    public boolean deleteGrupo(String idgrupo,Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("delete from eje_repositorio_visibilidad where id=").append(idgrupo));
        boolean gv =consul.insert(sql);
        sql = String.valueOf(new StringBuilder("delete from eje_repositorio_clasificacionvisibilidad where id_visibilidad=").append(idgrupo));
        boolean gcv =consul.insert(sql);
        return (gv && gcv);
    }

    public boolean addGrupoVisibilidad(String id,String nombre,String universo,Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("INSERT INTO ")
        	.append("eje_repositorio_visibilidad(id,nombre,universo) ")
        	.append("VALUES(").append(id).append(",'").append(nombre).append("','").append(universo).append("')"));
        return consul.insert(sql);
    }

    public Map getGrupoVisibilidad(String id, Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select id,nombre,universo from eje_repositorio_visibilidad where id=").append(id));
        consul.exec(sql); consul.next();
        Map<Object,String> mp = new HashMap<Object, String>();
        mp.put("nombre", consul.getString("nombre"));
        mp.put("universo", consul.getString("universo"));
        return mp;
    }
    
    public boolean updateGrupoVisibilidad(String id,String nombre,String universo,Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("UPDATE eje_repositorio_visibilidad ")
        	.append("SET nombre='").append(nombre).append("',universo='").append(universo)
        	.append("' WHERE id=").append(id).append(""));
        return consul.insert(sql);
    }
        
}