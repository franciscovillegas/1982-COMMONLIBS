package portal.com.eje.frontcontroller.ioutils.ioutilbolsadegatos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.frontcontroller.ioutils.IOUtilConnection;

public class IOClaseWebBolsaDeGatos {
 
	
	public IOClaseWebBolsaDeGatos() {
		 
	}
	
	public double createBolsaByRut(IIOClaseWebLight io, int usuario, String nombreUsuarioUpload, int idFile, int tipo, String descripcion, int periodo, ConsultaData data) throws SQLException {
		Cronometro cro = new Cronometro();
		cro.start();
		
		double idUpload = -1;
		
		String sql = "INSERT INTO eje_generico_bolsadegatos (usuario_rut, usuario_nombre, id_type, id_file, descripcion, fecha_upload, cant_registros) values (?,?,?,?,?, getdate(),?)";
		String sqlReferencias = "INSERT INTO eje_generico_bolsadegatos_referencias (id_upload, rut) values (?,?)";
		
		Connection conn = io.getUtil(IOUtilConnection.class).getConnection(io, "portal");
		boolean ok = false;
		try {
			conn.setAutoCommit(false);
			
			
			{
				/*CREATE ID*/
				Object[] params = {usuario,nombreUsuarioUpload, tipo, idFile, descripcion, data.size()};
				idUpload = ConsultaTool.getInstance().insertIdentity(conn, sql, params);
			}
			
			{
				/*CREA TABLA DATA Y REFENCIA EL NOMBRE*/
				createDataTable(conn, idUpload, data);
				
				sql = "UPDATE eje_generico_bolsadegatos SET nombre_tabla = ? WHERE id_upload  = ? ";
				Object[] params = {getTableName(idUpload), idUpload};
				ConsultaTool.getInstance().update(conn, sql, params);
			} 
			
			{
		 		/*CREATE RELACION RUT*/
				while(data != null && data.next()) {
					String rut = null;
					
					if(data.existField("rut")) {
						rut = data.getForcedString("rut");	
					}
					
					Object[] params = {idUpload, rut};
					ConsultaTool.getInstance().insert(conn, sqlReferencias, params);
					 
				}
			}
			

			insertData(conn, idUpload, data);
			
			{
				sql = "UPDATE eje_generico_bolsadegatos SET process_time = ? WHERE id_upload  = ? ";
				Object[] params = {cro.GetMilliseconds(), idUpload};
				ConsultaTool.getInstance().update(conn, sql, params);
				
				ok =true;
			} 
			
			 
		}
		catch(SQLException e) {
			ok = false;
			throw e;
		} 
		finally {
			if(ok) {
				conn.commit();
			}
			else {
				conn.rollback();
			}
			
			if(conn != null) {
				conn.setAutoCommit(false);
			}
			
			io.getUtil(IOUtilConnection.class).freeConnection(io, "portal", conn);
		}
		
		
		return idUpload;
	}
	
	private void insertData(Connection conn, double idCorr, ConsultaData data) throws SQLException {
		List<String> nCols = data.getNombreColumnas();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO ").append(getTableName(idCorr)).append(" ( ");
		
		for(String col : nCols) {
			if(col != null && !col.equals("")) {
				sql.append("[").append(col).append("]").append(",");
			}
		}
		
		sql.append(" id_upload ) \n");
		sql.append(" values \n").append("(");
		
		for(String col : nCols) {
			if(col != null && !col.equals("")) {
				sql.append(" ?, ");	
			}
		}
		
		sql.append( idCorr ).append(" ) \n");
		
		int pos = data.getPosition();
		
		data.toStart();
		while(data.next()) {
			List<Object> values = new ArrayList<Object>();
			
			for(String col : nCols) {
				if(col != null && !col.equals("")) {
					values.add(data.getForcedString(col));
				}
			}
			
			 
			ConsultaTool.getInstance().insert(conn, sql.toString(), values.toArray());
			 
		}
		
		data.setPosition(pos);
		
	}
	
	private String getTableName(double idCorr) {
		StringBuilder sql = new StringBuilder();
		sql.append("eje_golsadegato_byrut_").append((int) idCorr).append("");
		
		return sql.toString();
	}
	
	private void createDataTable(Connection conn, double idCorr, ConsultaData data) throws SQLException {
		
		List<String> nCols = data.getNombreColumnas();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" CREATE TABLE ").append(getTableName(idCorr)).append(" ( \n");
		sql.append(" 							id_corr      int    IDENTITY(1,1), \n");

		
		for(String col : nCols) {
			if(col != null && !col.equals("")) {
				sql.append("[").append(col).append("] varchar(50)    NULL, \n");
			}
		}
		
		sql.append(" 							id_upload    int    NOT NULL  \n");
		sql.append(" 							) ");
	
	 
		ConsultaTool.getInstance().insert(conn, sql.toString() );
		 
	}
}
