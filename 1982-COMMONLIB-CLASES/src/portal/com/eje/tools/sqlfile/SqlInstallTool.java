package portal.com.eje.tools.sqlfile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Util;

public class SqlInstallTool {

	
	public static SqlInstallTool getInstance() {
		return Util.getInstance(SqlInstallTool.class);
	}
	/**
	 * Hecho para desinstalar, eliminar datos y drop tablas, se debe incluir el path de los dos sqls, sql de deleteall y sql de drop all <br/>
	 * los paths al estar dentro de src, se manejan con / <br/>
	 * Ej: "/cl/eje/view/senchab/ejeb_capacitacion_configurador/sqls/deletefromall.sql"
	 * 
	 * @author Pancho
	 * @throws Exception 
	 * @since 23-05-2019
	 * */
	public boolean unistall(Connection conn , HttpServletRequest req, String pathSqlDeleteAll, String pathSqlDropAll) throws Exception {
		
		for (int i = 0; i < 15; i++) {
			try {
				SqlFileTool.getInstance().batchFile(conn, req, pathSqlDeleteAll);
			} 
			catch (SQLException e) {
				
			}catch (Exception e) {
				throw e;
			}
		}
		
		return dropAll(conn, req, pathSqlDropAll, (byte)10);
		
	}
	
	private boolean dropAll(Connection conn , HttpServletRequest req, String pathSqlDropAll, byte ntimes) {
		for (byte i = 0; i < ntimes; i++) {
			dropAll(conn, req, pathSqlDropAll);
		}

		return true;
	}
	
	private boolean dropAll(Connection conn , HttpServletRequest req, String pathSqlDropAll) {
		 
		
		boolean ok = true;
		int cantDrops = 0;
		try {
			
			String sql = SqlFileTool.getInstance().getSql(req, pathSqlDropAll);

			Scanner scanner = new Scanner(sql);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				try {
					ConsultaTool.getInstance().executeBatch(conn, line);
					
					cantDrops++;
				} catch (SQLException e) {

				}

			}
			scanner.close();
		} catch (Exception e) {

		}

		return cantDrops > 0;
	}
	
	
	public boolean install(Connection connCap, HttpServletRequest req, String pathInstallSql, String pathIInsertBasicsSql) throws SQLException, IOException {
		return install(connCap, req, pathInstallSql, pathIInsertBasicsSql, null);
	}
	
	@SuppressWarnings("unused")
	public boolean install(Connection connCap, HttpServletRequest req, String pathInstallSql, String pathIInsertBasicsSql, List<String> procedures) throws SQLException, IOException {
		boolean retorno = false;
		 
		if(procedures != null && procedures.size() > 0) {
			for(String pathSql : procedures) {
				int[] ok3 =SqlFileTool.getInstance().batchFile(connCap, req, pathSql);	
			}
		}
		
			int[] ok = SqlFileTool.getInstance().batchFile(connCap, req, pathInstallSql);
			
			int[] ok2 =SqlFileTool.getInstance().batchFile(connCap, req, pathIInsertBasicsSql);
			

			
			retorno = true;
		 

		return retorno;
		
	}
	
}
