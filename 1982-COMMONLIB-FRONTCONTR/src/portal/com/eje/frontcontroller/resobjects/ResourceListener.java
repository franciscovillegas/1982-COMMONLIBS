package portal.com.eje.frontcontroller.resobjects;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;


public class ResourceListener implements IResourceListener {
	Consulta consulta;
	
	public ResourceListener(Connection debug) {
		consulta = new Consulta(debug);
	}
	
	public void fileNotFound(String path) {
		String sql = "insert into eje_generico_recursos (fecha, recurso, esta_presente) values (getdate(),'"+path+"',0)";
		
		consulta.insert(sql);
		
	}

}
