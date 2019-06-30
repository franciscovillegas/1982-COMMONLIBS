package portal.com.eje.portal.transactions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cl.ejedigital.consultor.ConnectionDefinition;
import cl.ejedigital.web.datos.DBConnectionManager;
 
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.transactions.error.ConnectionsAlreadyStartedException;
import portal.com.eje.tools.deprecates.AssertWarn;

public class TransactionConnection {
	private int cant;
	private Map<String,ConnectionContainer> conns;
	private boolean restringeCommint;
	private EModulos contextOriginal;
	private EModulos context;
	private final String mac = "mac";
	private final String portal = "portal";
	private final String dotacion = "dotacion";
	
	TransactionConnection(boolean restringeCommint) {
		this.restringeCommint = restringeCommint;
		conns = new HashMap<String, ConnectionContainer>();
		
		 setContext(EModulos.getThisModulo());
	}
	
	public Connection getConnection(String jndi) {
		return getConnection( context , jndi);
	}
	
	public Connection getConnection(EModulos modulo, String jndi) {

		Connection conn = null;
		
		try {
			String catalogOfNewConnection = JndiToolCached.getInstance().getUrlConnection(modulo, jndi);
			
			for(Entry<String, ConnectionContainer> entry : conns.entrySet()) {
				boolean mismoJndi = entry.getValue().getJndi().equals(jndi);
				boolean mismoModulo = entry.getValue().getModulo() == modulo;
				boolean mismaBD = entry.getValue().getConn().getCatalog().equals( catalogOfNewConnection );
				boolean esMac = "mac".equals(jndi);
				
				if(mismaBD) {
					//ultima agregación, se puede compartir por catalog
					conn = entry.getValue().getConn();
					break;
				}
				else if(!esMac && mismoJndi) {
					//para los jndi distintos de MAC, se puede reutilizar la conección con el mismo jndi para distintos modulos
					//ej : dotacion , portal, egresos
					conn = entry.getValue().getConn();
					break;
				}
				else if(esMac && mismoJndi && mismoModulo ) {
					// los jndi MAC deben ser siempre del mismo modulo
					conn = entry.getValue().getConn();
					break;
				}
				 
			}
			
			if(conn == null) {
				
				conn =  DBConnectionManager.getInstance().getConnection(modulo, jndi);	
				
				if(restringeCommint) { 
					conn.setAutoCommit(false);
				}
				
				conns.put(new StringBuilder().append(jndi).append("_").append(cant).toString(), new ConnectionContainer(modulo, jndi, conn));
				cant++;
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return conn;
	}
	

	
	boolean tryFreeConnections(boolean statusCommit) {
		boolean okNow = false;
		
		try {
			while(stillExistNoEnded()) {
				freeConnections(statusCommit);	
			}
			
		}
		catch(Exception e) {
			okNow=false;
			e.printStackTrace();
		}
		finally {
			try {
				while(stillExistNoEnded()) {
					freeConnections(statusCommit);	
				}
				//intenta cerrar por segunda vez
			}
			finally {
				while(stillExistNoEnded()) {
					freeConnections(statusCommit);	
				}
				//intenta cerrar por tercera vez
			}
		}
	 
		okNow=true;
		
		return okNow;
	}
	
	private void freeConnections(boolean statusCommit) {
		for(Entry<String, ConnectionContainer> entry : conns.entrySet()) {
			ConnectionContainer cc = entry.getValue();
			
			if( !cc.isEnded() ) {
				try {
					if(restringeCommint) {
						if(statusCommit) {
							cc.getConn().commit();
						}
						else {
							cc.getConn().rollback();
						}	
					}
					cc.getConn().setAutoCommit(true);
				
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				DBConnectionManager.getInstance().freeConnection(cc.getModulo(), cc.getJndi(), cc.getConn());
				cc.setEnded(true);
			}
		}
	}

	public void setRestringeCommit(boolean restringeCommint) throws ConnectionsAlreadyStartedException {
		if(conns.size() > 0) {
			throw new ConnectionsAlreadyStartedException();
		}
		else {
			this.restringeCommint = restringeCommint;
		}
	}
	
	public void setDefaultContext() {
		setContext( contextOriginal );
	}

	public void setContext(EModulos modulo) {
		if(modulo != null) {
			this.context = modulo;
			
			if(contextOriginal != null) {
				contextOriginal = modulo;
			}
		}
	}

	public EModulos getContext() {
		return this.context;
	}
	
	public Connection getMac() {
		return getConnection(mac);
	}

	public Connection  getPortal() {
		return getConnection(portal);
	}
	
	@Override
	public String toString() {
		return context.toString();
	}

	/**
	 * Debe se llamado por getMac o getConnection("mac") la dotación y cualquier otro jndi con el tiempo esaparecerá
	 * @author Pancho
	 * @deprecated
	 * 
	 * */
	public Connection getDotacion() {
		AssertWarn.isTrue(getClass(), false, "jndi rompe la regla de los workflows, debe llamarse con 'mac' y una clase debe hacer la simulación. ");
		return getConnection(dotacion);
	}
	
	private boolean stillExistNoEnded() {
		boolean exist = true;
		
		boolean almostOne_withNoEnded = false;
		for(Entry<String, ConnectionContainer> entry : conns.entrySet()) {
			if(!entry.getValue().isEnded()) {
				almostOne_withNoEnded = true;
				break;
			}
		}
		
		if(!almostOne_withNoEnded) {
			exist = false;
		}
		
		return exist;
	}
}
