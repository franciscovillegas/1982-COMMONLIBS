package portal.com.eje.carpelect.jerarquia;

import java.sql.Connection;

import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.tools.jqtree.AJqTreeMaker;
import portal.com.eje.tools.jqtree.JqNodo;
import portal.com.eje.tools.jqtree.JqNodos;
import portal.com.eje.tools.portal.ToolNewOrganicaIO;
import portal.com.eje.tools.portal.ToolNewOrganicaIO.NodoUnidad;




public class TreeOrganicaPerfilada extends AJqTreeMaker {

	public TreeOrganicaPerfilada(IOClaseWeb ioClaseWeb) {
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
			String unidad = org.getUnidadWhereIsEncargado(Validar.getInstance().validarInt(super.ioClaseWeb.getUsuario().getRutId(),-1));
			
			JqNodo nodoJq = null;
			
			if(super.ioClaseWeb.getUsuario().tieneApp("adm")) {
				nodoJq = org.getNodoPadre(conn, tipoOrg);
			}
			else {
				NodoUnidad nodo = org.getUnidad(unidad);
				nodoJq = new JqNodo(nodo.getUnidDesc(), nodo.getUnidId());
			}
			
			
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
