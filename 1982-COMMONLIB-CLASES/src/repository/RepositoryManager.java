package repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import portal.com.eje.datos.Consulta;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;

import com.google.gson.JsonObject;

import freemarker.template.SimpleList;

public class RepositoryManager {

    public RepositoryManager() {
    }
    
    public SimpleList GetTrabajadores(Connection con) {
        Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select rut,rtrim(ltrim(nombre)) nombre from eje_ges_trabajador order by nombre"));
        consul.exec(sql);
        return consul.getSimpleList();
    }
    
    public JsonObject GetTrabajadores(Connection con,JsonObject jsonobject) {
        Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select rut,rtrim(ltrim(nombre)) nombre from eje_ges_trabajador order by nombre"));
        consul.exec(sql);
        for(;consul.next();) { 
        	jsonobject.addProperty(consul.getString("rut"),consul.getString("nombre"));
        }
        return jsonobject;
    }
    
    public String GetNombreTrabajador(Connection con, String rut) {
        Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select rtrim(ltrim(nombre)) nombre from eje_ges_trabajador ")
        	.append("where rut=").append(rut).append(" order by nombre"));
        consul.exec(sql); consul.next();
        return consul.getString("nombre");
    }
    
    public ConsultaData GetFilesTrabajador(String rut, Connection con) {
    	
        String sql = String.valueOf(new StringBuilder("select f.id,c.clasificacion,c.abrev,f.rut,f.archivo, ")
        	.append("c.abrev+'/'+cast(f.rut as varchar)+'/'+f.archivo ruta,i.nombre icono from ")
        	.append("(eje_repositorio_archivo f inner join eje_repositorio_clasificacion c on ")
        	.append("f.id_clasificacion=c.id) inner join eje_repositorio_icono i on c.icono=i.id where f.rut=")
        	.append(rut).append(" and c.vigencia=1"));
        ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return data;
    }
    
    public ConsultaData GetNameFilesTrabajador(String rut, String strAbrev, Connection con) {
    	
    	StringBuilder strSQL = new StringBuilder();
    	
    	strSQL.append("declare @rut int, @abrev varchar(max) \n\n")

    	.append("set @rut=? \n")
    	.append("set @abrev=? \n\n")
    	
    	.append("select top 1 archivo \n")
    	.append("from ( \n")
    	.append("	select orden=1, a.archivo \n")
    	.append("	from eje_repositorio_clasificacion c \n")
    	.append("	inner join eje_repositorio_archivo a on a.id_clasificacion=c.id \n")
    	.append("	where c.abrev=@abrev and a.rut=@rut and a.vigencia=1 and c.vigencia=1 \n\n")
    	
    	.append("	union select orden=2, a.archivo \n")
    	.append("	from eje_repositorio_clasificacion c \n")
    	.append("	inner join eje_repositorio_archivo a on a.id_clasificacion=c.id \n")
    	.append("	where c.abrev=@abrev and a.rut is null and a.vigencia=1 and c.vigencia=1 \n")
    	.append(") x order by orden \n");
    	
    	Object[] params = {rut, strAbrev};
    	
    	try {
			return ConsultaTool.getInstance().getData(con, strSQL.toString(), params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    public SimpleList GetNoClasificadoTrabajador(String rut, Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select c.id,c.clasificacion,c.abrev,").append(rut).append(" rut ")
        	.append("from eje_repositorio_clasificacion c where c.id not in (select id_clasificacion ")
        	.append("from eje_repositorio_archivo where rut=").append(rut).append(")"));
        consul.exec(sql);
        return consul.getSimpleList();
    }
    
    public SimpleList GetResumenClasificacionTrabajador(String rut, Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select c.id,c.clasificacion,c.abrev, ")
        	.append("archivos=(select COUNT(id) from eje_repositorio_archivo where rut=").append(rut)
        	.append("and vigencia=1 and id_clasificacion=c.id) from eje_repositorio_clasificacion c"));
        consul.exec(sql);
        return consul.getSimpleList();
    }
    
    public String GetIdFile(Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select isnull(MAX(id)+1,1) idfile from eje_repositorio_archivo"));
        consul.exec(sql); 
        if(consul.next()) {
        	return consul.getString("idfile");
        }
        else {
        	return "1";
        }
    }
    
    public String GetAbrevClasificacion(String idclasificacion, Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select rtrim(ltrim(abrev)) abrev from ")
        	.append("eje_repositorio_clasificacion where id=").append(idclasificacion));
        consul.exec(sql); consul.next();
        return consul.getString("abrev");
    }
    
    public boolean addFileToBD(String idclasificacion,String rut, String nombrefile, Connection con) {
    	if(!existsFileBD(idclasificacion,rut,nombrefile,con)) {
    		Consulta consul = new Consulta(con);
    		String idfile = GetIdFile(con);
    		String sql = String.valueOf(new StringBuilder("insert eje_repositorio_archivo(id,id_clasificacion,rut,archivo,vigencia) ")
        		.append("values(").append(idfile).append(",").append(idclasificacion).append(",").append(rut)
        		.append(",'").append(nombrefile).append("',1)"));
    		return consul.insert(sql);
    	}
    	else {
    		return true;
    	}
    }
    
    public boolean existsFileBD(String idclasificacion,String rut, String nombrefile, Connection con) {
    	Consulta consul = new Consulta(con);
    	String idfile = GetIdFile(con);
        String sql = String.valueOf(new StringBuilder("select COUNT(*) existe from eje_repositorio_archivo ")
        	.append("where rut=").append(rut).append(" and id_clasificacion=").append(idclasificacion)
        	.append(" and archivo='").append(nombrefile).append("'"));
        consul.exec(sql); consul.next();
        return consul.getString("existe").equals("0") ? false : true;
    }
    
    public Map getfileSVN(String idfile, Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select c.abrev,a.rut,a.archivo from ")
        	.append("eje_repositorio_archivo a inner join eje_repositorio_clasificacion c ")
        	.append("on a.id_clasificacion=c.id where a.id=").append(idfile));
        consul.exec(sql); consul.next();
        Map<Object,String> mp = new HashMap<Object, String>();
        mp.put("abrev", consul.getString("abrev"));
        mp.put("rut", consul.getString("rut"));
        mp.put("archivo", consul.getString("archivo"));
        return mp;
    }
    
    public boolean deleteFileBD(String idfile,Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("delete from eje_repositorio_archivo where id=").append(idfile));
        return consul.insert(sql);
    }
    
    public Map getConfiguracionSVN(Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select url,cliente,usuario,clave from eje_repositorio_configuracion"));
        consul.exec(sql); consul.next();
        Map<Object,String> mp = new HashMap<Object, String>();
        mp.put("url", consul.getString("url"));
        mp.put("cliente", consul.getString("cliente"));
        mp.put("usuario", consul.getString("usuario"));
        mp.put("clave", consul.getString("clave"));
        return mp;
    }
    
}