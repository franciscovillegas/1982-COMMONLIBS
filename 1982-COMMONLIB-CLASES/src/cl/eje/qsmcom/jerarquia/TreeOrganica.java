package cl.eje.qsmcom.jerarquia;

import java.sql.Connection;

import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.tools.jqtree.AJqTreeMaker;
import portal.com.eje.tools.jqtree.JqNodo;
import portal.com.eje.tools.jqtree.JqNodos;
import portal.com.eje.tools.portal.ToolOrganicaIO;




public class TreeOrganica extends AJqTreeMaker {

	public TreeOrganica(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	private void getJerarquia(JqNodos nodos) {
		IDBConnectionManager dbConn = DBConnectionManager.getInstance();
		Connection conn = dbConn.getConnection("portal");
		
		try {
			ToolOrganicaIO org = new ToolOrganicaIO();
			JqNodo nodoJq = org.getNodoPadre(conn);
			
			org.getSubTree(conn, nodoJq);
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
	
	
	

	public JqNodos getTree() {
		JqNodos nodos = new JqNodos();
		getJerarquia(nodos);
		
		return nodos;
	}

}
