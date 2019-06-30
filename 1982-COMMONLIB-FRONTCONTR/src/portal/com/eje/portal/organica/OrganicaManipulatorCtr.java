package portal.com.eje.portal.organica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataMode;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Ctr;
import portal.com.eje.portal.organica.errors.CantDeleteUnidadException;
import portal.com.eje.portal.organica.errors.CantDeleteUnitsWithPeopleException;
import portal.com.eje.portal.organica.ifaces.IOrganicaManipulator;
import portal.com.eje.portal.transactions.ITransaction;
import portal.com.eje.portal.transactions.TransactionConnection;
import portal.com.eje.portal.transactions.TransactionTool;
import portal.com.eje.serhumano.user.Usuario;

/**
 * @since solo debe tener los métodos add,setVigente,updNombre, updPadre
 * */
public class OrganicaManipulatorCtr implements IOrganicaManipulator {

	public static IOrganicaManipulator getInstance() {
		return Ctr.getInstance(OrganicaManipulatorCtr.class);
	}

	@Override
	public boolean add(final Usuario u, final String unid_id, final String unid_id_parent, final String unid_desc) throws Exception {
		if(u == null || unid_id_parent == null || unid_desc == null) {
			throw new NullPointerException();
		}
		
		TransactionTool tool = new TransactionTool(true);
	 
		tool.doTransaction(new ITransaction() {
			
			@Override
			public boolean transaction(TransactionConnection cons, Map<String, Object> paramsVars, TransactionTool tool) throws Exception {
				Connection portal = cons.getConnection("portal");
				
				boolean existe = OrganicaManipulatorManager.getInstance().existUnidId(portal, unid_id);
				
				if (!existe) {
					OrganicaManipulatorManager.getInstance().addUnidad(portal, unid_id, unid_id_parent, unid_desc);
					OrganicaManipulatorManager.getInstance().updateReferences(portal, unid_id);
					OrganicaManipulatorManager.getInstance().addLog(portal, "add", u.getRutIdInt(), unid_id, null, unid_desc, unid_id_parent);
					
					return true;
				} else {
					return false;
				}
				
			}
		});
		
		if(tool.getException() != null) {
			throw tool.getException();
		}
		
		return tool.isOK();
	}

	@Override
	public boolean add(Usuario u, String unid_id_parent, String unid_desc) throws Exception{
		if(u == null || unid_id_parent == null || unid_desc == null) {
			throw new NullPointerException();
		}
		
		
		return add(u, null, unid_id_parent, unid_desc);
	}

	@Override
	public boolean setVigente(final Usuario u, final String unid_id, final boolean vigente) throws Exception{
		if(u == null || unid_id == null ) {
			throw new NullPointerException();
		}
		
		if(unid_id.equals("sinunidad")) {
			throw new CantDeleteUnidadException("No se puede eliminar la unidad 'sinunidad' ");
		}
		
		TransactionTool tool = new TransactionTool(true);
		 
		tool.doTransaction(new ITransaction() {
			
			@Override
			public boolean transaction(TransactionConnection cons, Map<String, Object> paramsVars, TransactionTool tool) throws Exception {
				Connection portal = cons.getConnection("portal");
				
				
				//boolean ok = OrganicaManipulatorManager.getInstance().setVigente(portal, unid_id, vigente);
				
				ConsultaData descendientes = OrganicaLocator.getInstance().getUnidadesDescendientes(portal, unid_id, true);
				List<String> listaUnidades = ConsultaTool.getInstance().getList(descendientes, ArrayList.class, "unid_id");
				int q = OrganicaManipulatorManager.getInstance().countPersonas(portal, listaUnidades);
				
				if(q != 0) {
					throw new CantDeleteUnitsWithPeopleException("Muchas de las unidades que se desean eliminar aún contienen personas ");
				}
				
				OrganicaManipulatorManager.getInstance().setVigente(portal, listaUnidades, vigente);
				
				OrganicaManipulatorManager.getInstance().addLog(portal, "setVigente", u.getRutIdInt(), unid_id, vigente, null, null);
				
				return true;
			}
		});
		
		if(tool.getException() != null) {
			throw tool.getException();
		}
		
		return tool.isOK();
	}

	@Override
	public boolean updNombre(final Usuario u, final String unid_id ,final String unid_desc) throws Exception {
		if(u == null || unid_id == null || unid_desc == null) {
			throw new NullPointerException();
		}
		
		TransactionTool tool = new TransactionTool(true);
		 
		tool.doTransaction(new ITransaction() {
			
			@Override
			public boolean transaction(TransactionConnection cons, Map<String, Object> paramsVars, TransactionTool tool) throws Exception {
				Connection portal = cons.getConnection("portal");
				
				OrganicaManipulatorManager.getInstance().setNombre(portal, unid_id, unid_desc);
				OrganicaManipulatorManager.getInstance().addLog(portal, "updNombre", u.getRutIdInt(), unid_id, null, unid_desc, null);
				
				return true;
			}
		});
		
		if(tool.getException() != null) {
			throw tool.getException();
		}
		
		return tool.isOK();
	}

	@Override
	public boolean updPadre(final Usuario u, final String unid_id, final String unid_id_parent) throws Exception {
		Assert.notNull(u);
		Assert.notNull(unid_id);
		Assert.notNull(unid_id_parent);
		
		TransactionTool tool = new TransactionTool(true);
		 
		tool.doTransaction(new ITransaction() {
			
			@Override
			public boolean transaction(TransactionConnection cons, Map<String, Object> paramsVars, TransactionTool tool) throws Exception {
				Connection portal = cons.getConnection("portal");
				
				boolean ok = OrganicaManipulatorManager.getInstance().setParent(portal, unid_id, unid_id_parent);
				OrganicaManipulatorManager.getInstance().addLog(portal, "updPadre", u.getRutIdInt(), unid_id, null, null, unid_id_parent);
				
				return ok;
			}
		});
		
		if(tool.getException() != null) {
			throw tool.getException();
		}
		
		return tool.isOK();
	}


}
