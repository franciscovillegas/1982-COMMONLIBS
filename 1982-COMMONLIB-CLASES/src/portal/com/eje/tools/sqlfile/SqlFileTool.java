package portal.com.eje.tools.sqlfile;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;

import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.frontcontroller.resobjects.ResourceMapping;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.serhumano.httpservlet.MyTemplateUbication;

/**
 * Executa batchs en que están en formato SQL
 * 
 * @deprecated Se debe usar SqlResource en este mismo paquete
 * @author Pancho
 * @since 14-05-2019
 * */
public class SqlFileTool {

	public static SqlFileTool getInstance() {
		return Util.getInstance(SqlFileTool.class);
	}
	
	/**
	 * 
	 * Executa una archivo sql como si fuera un batch <br/>
	 * utiliza ConsultaTool.getInstance().executeBatch(conn, sql);
	 * 
	 * @since 14-05-2019
	 * */
	public int[] batchFile(Connection conn, HttpServletRequest req, String path) throws SQLException, IOException {
		
		int[] res = null;
		
		if(path != null && conn != null && req != null) {
			String sql = getSql(req, path);
			res = ConsultaTool.getInstance().executeBatch(conn, sql);	
		}
		else {
			res = new int[1];
			res[0] = -1;
		}
		
			 
		return res;
	}
	
	public String getSql(HttpServletRequest req, String path) throws SQLException, IOException {
		Map<String, Object> f2 = ResourceMapping.getInstance().getObjectFile(MyTemplateUbication.SrcAndWebContent, req, path);
		
		String sql = null;
		if(f2 != null) {
			File f = ((File)f2.get("File"));
			sql = FileUtils.readFileToString(f);
		}
		
		return sql;
	}
	
	
	
}
