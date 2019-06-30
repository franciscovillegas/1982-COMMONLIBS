package portal.com.eje.portal.trabajador;

import java.sql.Connection;

import cl.ejedigital.web.datos.DBConnectionManager;
import freemarker.template.SimpleHash;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.serhumano.menu.bean.FichaPersonalBean_webmatico;

public class ManagerDatosPrevisionales implements IManagerDatosPrevisionales {

	
	
	
	@Override
	public SimpleHash getDatosPrevisionales(int rutInt) {
		Connection connection = null;
		SimpleHash simplehash = null;
		
		try {
			connection = DBConnectionManager.getInstance().getConnection("portal");
			String rut = String.valueOf(rutInt);
			
			FichaPersonalBean_webmatico fp = FichaPersonalBean_webmatico.getInstance();
		    datosRut userRut = new datosRut(connection, String.valueOf( rut ));
		    simplehash = fp.getDatosPrev(connection,rut);
		    simplehash.put("rut", userRut.Rut);
		    simplehash.put("cargo", userRut.Cargo);
		    
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			DBConnectionManager.getInstance().freeConnection("portal", connection);
		}
		
		return simplehash;
	}

}
