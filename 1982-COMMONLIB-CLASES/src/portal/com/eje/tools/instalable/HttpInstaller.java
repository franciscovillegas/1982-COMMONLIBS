package portal.com.eje.tools.instalable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import portal.com.eje.portal.factory.Weak;
import portal.com.eje.tools.sqlfile.SqlInstallTool;

/**
 * Instala y deinstala modulos
 * 
 * @author Pancho
 * @since 06-06-2019
 * */
public class HttpInstaller {

	public static HttpInstaller getInstance() {
		return Weak.getInstance(HttpInstaller.class);
	}
	
 
	private boolean install(Connection conn, HttpServletRequest req, IInstalable instalable) throws SQLException, IOException {
		if(instalable == null) {
			throw new NullPointerException("Debe ser distinto de null");
		}
		if(conn == null) {
			throw new NullPointerException("Debe ser distinto de null");
		}
		
		
		
		
		return SqlInstallTool.getInstance().install(conn, req, instalable.getPathScriptInstall(),  getFirst(instalable), getOthers(instalable));
		
	}
	
	private String getFirst(IInstalable instalable) {
		String first = instalable.getPathScriptAdicional().get(0);
		return first;
	}
	
	private LinkedList<String> getOthers(IInstalable instalable) {
		LinkedList<String> others = new LinkedList<>();
		for(int i=1;i<instalable.getPathScriptAdicional().size();i++) {
			others.add(instalable.getPathScriptAdicional().get(i));
			
		}
		
		return others;
	}
	
	private boolean uninstall(Connection conn, HttpServletRequest req, IDesinstalable desinstalable) throws Exception  {
		boolean ok = SqlInstallTool.getInstance().unistall(conn, req, desinstalable.getPathScriptDeleteAll(), desinstalable.getPathScriptDropAll() );
		return ok;
	}
	
	public boolean uninstall(Connection conn, HttpServletRequest req, IModuloInstalable modulo) throws Exception {
		return uninstall(conn, req, modulo.getDesinstalador());
	}
	
	public boolean uninstall(Connection conn, HttpServletRequest req, Class<? extends IModuloInstalable> clase) throws Exception {
		IModuloInstalable modulo = Weak.getInstance(clase);
		return uninstall(conn, req, modulo.getDesinstalador());
	}
	
	public boolean install(Connection conn, HttpServletRequest req, IModuloInstalable modulo) throws NullPointerException, IOException, SQLException {
		return install(conn, req, modulo.getInstalador());
	}
	

	

	
	public boolean install(Connection conn, HttpServletRequest req, Class<? extends IModuloInstalable> clase) throws SQLException, IOException {
		IModuloInstalable modulo = Weak.getInstance(clase);
		return install(conn, req, modulo.getInstalador());
	}
}
