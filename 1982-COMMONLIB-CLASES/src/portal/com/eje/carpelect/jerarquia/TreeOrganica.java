package portal.com.eje.carpelect.jerarquia;

import java.sql.Connection;

import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.tools.jqtree.AJqTreeMaker;
import portal.com.eje.tools.jqtree.JqNodo;
import portal.com.eje.tools.jqtree.JqNodos;
import portal.com.eje.tools.portal.ToolNewOrganicaIO;




public class TreeOrganica extends AJqTreeMaker {

	public TreeOrganica(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	public JqNodos getTree() {
		JqNodos nodos = new JqNodos();
		getJerarquia(nodos);
		
		return nodos;
	}
	
	
	private void getJerarquia(JqNodos nodos) {
		IDBConnectionManager dbConn = DBConnectionManager.getInstance();
		Connection conn = dbConn.getConnection("portal");
		int tipoOrg = super.ioClaseWeb.getParamNum("tipoOrg", 0);
		
		try {
			ToolNewOrganicaIO org = new ToolNewOrganicaIO();
			JqNodo nodoJq = org.getNodoPadre(conn, tipoOrg);
			
			org.getSubTree(conn, nodoJq, tipoOrg);
			nodos.add(nodoJq);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(conn != null) {
				dbConn.freeConnection("portal",conn);
			}
			
		}
	}
		



}
