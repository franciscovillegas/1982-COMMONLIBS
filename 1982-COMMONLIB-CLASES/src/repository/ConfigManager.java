package repository;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import cl.ejedigital.tool.svn.SnvManipulator;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

import portal.com.eje.datos.Consulta;

public class ConfigManager {

    public ConfigManager() {
    }
    
    public Map getConfiguracion(Connection con) {
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
    
    public boolean updateConfiguracion(String url,String cliente,String usuario,String clave,Connection con) {
    	Consulta consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("UPDATE eje_repositorio_configuracion ")
        	.append("SET url='").append(url).append("',cliente='").append(cliente).append("',")
        	.append("usuario='").append(usuario).append("',clave='").append(clave).append("'"));
        return consul.insert(sql);
    }
        
}