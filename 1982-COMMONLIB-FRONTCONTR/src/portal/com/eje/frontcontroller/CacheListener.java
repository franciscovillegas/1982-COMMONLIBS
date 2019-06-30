package portal.com.eje.frontcontroller;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import cl.ejedigital.tool.cache.ICacheListener;



public class CacheListener implements ICacheListener {
	private Consulta consulta;
	
	public CacheListener(Connection conn) {
		consulta = new Consulta(conn);
	}
	
	public Object get(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object put(Object arg0, Object arg1) {
		

		String sql = "insert into eje_generico_recursos (fecha, recurso, esta_presente) values (getdate(),'"+arg0+"',1)";
			
		consulta.insert(sql);
			


		return null;
	}

	public Object remove(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
