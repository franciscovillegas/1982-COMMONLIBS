package repository;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import cl.ejedigital.tool.svn.SnvManipulator;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

import portal.com.eje.datos.Consulta;

public class UploaderManager {

    public UploaderManager() {
    }
    
    public SimpleList GetUpload(Connection con) {
        Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select id,uploader,universo from eje_repositorio_uploader order by uploader"));
        consul.exec(sql);
        return consul.getSimpleList();
    }
    
    public String GetIdUpload(Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select isnull(MAX(id)+1,1) idu from eje_repositorio_uploader"));
        consul.exec(sql); 
        if(consul.next()) {
        	return consul.getString("idu");
        }
        else {
        	return "1";
        }
    }
    
    public boolean deleteGrupo(String idgrupo,Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("delete from eje_repositorio_uploader where id=").append(idgrupo));
        boolean gv =consul.insert(sql);
        sql = String.valueOf(new StringBuilder("delete from eje_repositorio_clasificacionupload where id_uploaders=").append(idgrupo));
        boolean gcv =consul.insert(sql);
        return (gv && gcv);
    }

    public boolean addGrupoUpload(String id,String nombre,String universo,Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("INSERT INTO ")
        	.append("eje_repositorio_uploader(id,uploader,universo) ")
        	.append("VALUES(").append(id).append(",'").append(nombre).append("','").append(universo).append("')"));
        return consul.insert(sql);
    }

    public Map getGrupoUpload(String id, Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select id,uploader,universo from eje_repositorio_uploader where id=").append(id));
        consul.exec(sql); consul.next();
        Map<Object,String> mp = new HashMap<Object, String>();
        mp.put("uploader", consul.getString("uploader"));
        mp.put("universo", consul.getString("universo"));
        return mp;
    }
    
    public boolean updateGrupoUpload(String id,String nombre,String universo,Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("UPDATE eje_repositorio_uploader ")
        	.append("SET uploader='").append(nombre).append("',universo='").append(universo)
        	.append("' WHERE id=").append(id).append(""));
        return consul.insert(sql);
    }
        
}