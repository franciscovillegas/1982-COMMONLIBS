package repository;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

import portal.com.eje.datos.Consulta;

public class ConceptManager {

    public ConceptManager() {
    }
    
    public SimpleList GetVisibilidad(Connection con) {
        Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select id,nombre from eje_repositorio_visibilidad order by nombre"));
        consul.exec(sql);
        return consul.getSimpleList();
    }
    
    public SimpleList GetVisibilidadByClasificacion(String clasificacion,Connection con) {
        Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select v.id,v.nombre,existe=(select COUNT(*) ")
        	.append("from eje_repositorio_clasificacionvisibilidad where id_clasificacion=").append(clasificacion).append(" and ")
        	.append("id_visibilidad=v.id ) from eje_repositorio_visibilidad v order by v.nombre"));
        consul.exec(sql);
        return consul.getSimpleList();
    }
    
    public SimpleList GetUploader(Connection con) {
        Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select id,uploader from eje_repositorio_uploader order by uploader"));
        consul.exec(sql);
        return consul.getSimpleList();
    }
    
    public SimpleList GetUploaderByClasificacion(String clasificacion,Connection con) {
        Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select u.id,u.uploader,existe=(select COUNT(*) ")
        	.append("from eje_repositorio_clasificacionupload where id_clasificacion=").append(clasificacion).append(" and ")
        	.append("id_uploaders=u.id ) from eje_repositorio_uploader u order by u.uploader"));
        consul.exec(sql);
        return consul.getSimpleList();
    }
    
    public SimpleList GetIcono(Connection con) {
        Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select id,nombre from eje_repositorio_icono"));
        consul.exec(sql);
        return consul.getSimpleList();
    }
    
    public SimpleList GetIconoByClasificacion(String clasificacion,Connection con) {
        Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select i.id,i.nombre,existe=(select COUNT(*) ")
        	.append("from eje_repositorio_clasificacion where id=").append(clasificacion).append(" and icono=i.id ) ")
        	.append("from eje_repositorio_icono i order by i.id"));
        consul.exec(sql);
        return consul.getSimpleList();
    }
    
    public SimpleList GetClasificaciones(Connection con) {
        Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select id,clasificacion from eje_repositorio_clasificacion order by clasificacion"));
        consul.exec(sql);
        return consul.getSimpleList();
    }
    
    public Map getClasificacion(String id, Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select id,clasificacion,abrev,vigencia,icono,descripcion ")
        	.append("from eje_repositorio_clasificacion where id=").append(id));
        consul.exec(sql); consul.next();
        Map<Object,String> mp = new HashMap<Object, String>();
        mp.put("clasificacion", consul.getString("clasificacion"));
        mp.put("abrev", consul.getString("abrev"));
        mp.put("vigencia", consul.getString("vigencia"));
        mp.put("icono", consul.getString("icono"));
        mp.put("descripcion", consul.getString("descripcion"));
        return mp;
    }
    
    public String GetIdClasificacion(Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select isnull(MAX(id)+1,1) idclasificacion from eje_repositorio_clasificacion"));
        consul.exec(sql); 
        if(consul.next()) {
        	return consul.getString("idclasificacion");
        }
        else {
        	return "1";
        }
    }
    
    public String GetIdVisibilidadByClasificacion(Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select isnull(MAX(id)+1,1) idcv from eje_repositorio_clasificacionvisibilidad"));
        consul.exec(sql); 
        if(consul.next()) {
        	return consul.getString("idcv");
        }
        else {
        	return "1";
        }
    }
    
    public String GetIdUploaderByClasificacion(Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select isnull(MAX(id)+1,1) idcu from eje_repositorio_clasificacionupload"));
        consul.exec(sql); 
        if(consul.next()) {
        	return consul.getString("idcu");
        }
        else {
        	return "1";
        }
    }
    
    public boolean addClasificacion(String id,String clasificacion,String abrev,String vigencia,String icono,String descripcion,Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("INSERT INTO ")
        	.append("eje_repositorio_clasificacion(id,clasificacion,abrev,vigencia,icono,descripcion) ")
        	.append("VALUES(").append(id).append(",'").append(clasificacion).append("','").append(abrev)
        	.append("',").append(vigencia).append(",").append(icono).append(",'").append(descripcion).append("')"));
        return consul.insert(sql);
    }
    
    public boolean addClasificacionVisibilidad(String id,String idclasificacion,String idvisibilidad,Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("INSERT INTO ")
        	.append("eje_repositorio_clasificacionvisibilidad(id,id_clasificacion,id_visibilidad) ")
        	.append("VALUES (").append(id).append(",").append(idclasificacion).append(",").append(idvisibilidad).append(")"));
        return consul.insert(sql);
    }
    
    public boolean addClasificacionUploader(String id,String idclasificacion,String iduploader,Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("INSERT INTO ")
        	.append("eje_repositorio_clasificacionupload(id,id_clasificacion,id_uploaders) ")
        	.append("VALUES (").append(id).append(",").append(idclasificacion).append(",").append(iduploader).append(")"));
        return consul.insert(sql);
    }
    
    public boolean updateClasificacion(String id,String clasificacion,String abrev,String vigencia,String icono,String descripcion,Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("UPDATE eje_repositorio_clasificacion ")
        	.append("SET clasificacion='").append(clasificacion).append("',abrev='").append(abrev)
        	.append("',vigencia=").append(vigencia).append(",icono=").append(icono)
        	.append(",descripcion='").append(descripcion).append("' WHERE id=").append(id).append(""));
        return consul.insert(sql);
    }
    
    public boolean deleteVisibilidadByClasificacion(String idclasificacion,Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("delete from eje_repositorio_clasificacionvisibilidad where id_clasificacion=")
        	.append(idclasificacion));
        return consul.insert(sql);
    }
    
    public boolean deleteUploaderByClasificacion(String idclasificacion,Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("delete from eje_repositorio_clasificacionupload where id_clasificacion=")
        	.append(idclasificacion));
        return consul.insert(sql);
    }
    
}