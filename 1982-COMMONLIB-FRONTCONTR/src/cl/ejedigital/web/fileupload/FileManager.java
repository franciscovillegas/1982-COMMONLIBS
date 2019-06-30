package cl.ejedigital.web.fileupload;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.fileupload.vo.EjeFileUnicoTipo;
import cl.ejedigital.web.fileupload.vo.EjeFilesUnico;


final class FileManager {
	
    FileManager() {
    }

	public boolean existContext() {
		Context initContext;
		boolean ok = true;
		
		try {
			initContext = new InitialContext();
			Context c = (Context)initContext;
			c.lookup("java:/comp/env"); 
		}
		catch (NoInitialContextException e) {
			ok = false;
		}
		catch (NamingException e) {
			ok = false;
		}
				
		return ok;
	}
/**
 * Inserta registro en la tabla eje_files_unico
 * @return
 */
 

	synchronized public int insertFile(EjeFilesUnico eje ) {
		int newId = getMaxIdInforme() + 1;
 
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" INSERT INTO eje_files_unico ");
			sql.append(" (id_file, id_tipo, text, fecha_subida , rut_subida, name_original , name_unic , bytes, machine_name, full_hdd_Path, relative_path_to_webcontent, isweb ) ");
			sql.append(" VALUES ");
			sql.append(" (?, ?, ?, getdate(), ?, ?, ?, ?, ?, ?, ?, ?) ");
			
			Object[] params = {newId, getIDfromType(eje), eje.getText(), eje.getRutSubida(), eje.getNameOriginal(), eje.getNameUnic(),eje.getBytes(), eje.getMachineName(), eje.getFullHddPath(), eje.getRelativePathToWebcontent(), eje.isIsweb()}; 
		 
			
			ConsultaTool.getInstance().insert("portal",sql.toString(), params);
		}
		catch (SQLException e) {
			
			if(manipulateSQLError(e)) {
				insertFile(eje);	
			}
			else {
				e.printStackTrace();
			}
			
		
		}

		return newId;
	}
	
	/**
	 * Retornará true cuando se haya hecho alguna modificación en la BD para que se vuelva a llamar el método
	 * */
	private boolean manipulateSQLError(SQLException e) {
		
		if(e.getMessage().indexOf("full_hdd_Path") != -1 && (e.getMessage().indexOf("no es válido.") != -1 || e.getMessage().indexOf("Invalid column name") != -1) ) {
			StringBuilder sql = new StringBuilder();
			sql.append(" IF NOT EXISTS(SELECT * FROM   INFORMATION_SCHEMA.COLUMNS WHERE  TABLE_NAME = 'eje_files_unico' AND COLUMN_NAME = 'full_hdd_Path') ");
			sql.append(" alter table eje_files_unico add full_hdd_Path varchar(250) ");
			
			try {
				ConsultaTool.getInstance().update("portal", sql.toString());
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			return true; 
		}
		else if(e.getMessage().indexOf("relative_path_to_webcontent") != -1 && (e.getMessage().indexOf("no es válido.") != -1 || e.getMessage().indexOf("Invalid column name") != -1)) {
			StringBuilder sql = new StringBuilder();
			sql.append(" IF NOT EXISTS(SELECT * FROM   INFORMATION_SCHEMA.COLUMNS WHERE  TABLE_NAME = 'eje_files_unico' AND COLUMN_NAME = 'relative_path_to_webcontent') ");
			sql.append(" alter table eje_files_unico add relative_path_to_webcontent varchar(250) ");
			
			try {
				ConsultaTool.getInstance().update("portal", sql.toString());
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			return true;
		}
		if(e.getMessage().indexOf("isweb") != -1 && (e.getMessage().indexOf("no es válido.") != -1 || e.getMessage().indexOf("Invalid column name") != -1)) {
			StringBuilder sql = new StringBuilder();
			sql.append(" IF NOT EXISTS(SELECT * FROM   INFORMATION_SCHEMA.COLUMNS WHERE  TABLE_NAME = 'eje_files_unico' AND COLUMN_NAME = 'isweb') ");
			sql.append(" alter table eje_files_unico add isweb bit ");
			
			try {
				ConsultaTool.getInstance().update("portal", sql.toString());
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			return true;
		}
		else {
			return false;
		}
	}
	
	private int getIDfromType(EjeFilesUnico eje) {
		if(eje.getIdTipoObject() instanceof  EjeFileUnicoTipo) {
			return eje.getIdTipo().getId();
		}
		else if(eje.getIdTipoObject() instanceof  String) {
			int idTipo = getIDfromGlosaTipo((String)eje.getIdTipoObject());
			
			if(idTipo == -1) {
				idTipo = createIDfromGlosaTipo((String)eje.getIdTipoObject());
			}
			
			return idTipo;
		}
		else {
			return -1;
		}
	}
	
	private int getIDfromGlosaTipo(String glosaTipo) {
		if(glosaTipo != null) {
			glosaTipo = glosaTipo.trim();
		}
		
		Connection conn = DBConnectionManager.getInstance().getConnection("portal");
		try {
			String sql = "select id_tipo, nombre, descripcion from EJE_FILES_UNICO_TIPO where ltrim(rtrim(nombre)) = ? ";
			Object[] params = {glosaTipo};
			 
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
			if( data != null && data.next()) {
				return data.getInt("id_tipo");
			}
			
		}
		catch(Exception e) {
			
		}
		finally {
			if(conn != null) {
				DBConnectionManager.getInstance().freeConnection("portal", conn);
			}
		}
		
		return -1;
	}
	
	private int createIDfromGlosaTipo(String glosa) {
		boolean ok = false;
		int newID = 1;
		
		Connection conn = DBConnectionManager.getInstance().getConnection("portal");
		try {
			conn.setAutoCommit(false);
			
			{
				String sql = "select maximo=max(id_tipo) from EJE_FILES_UNICO_TIPO";
				ConsultaData dataNewID = ConsultaTool.getInstance().getData(conn, sql);
				
				if(dataNewID != null && dataNewID.next()) {
					newID = dataNewID.getInt("maximo") + 1;
				}
			}
			
			{
				String sql = "insert into EJE_FILES_UNICO_TIPO (id_tipo, nombre, descripcion) values (?,?,?)";
				Object[] params = {newID, glosa , null };
				ok = ConsultaTool.getInstance().insert(conn, sql, params);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(conn != null) {
				try {
					if(!ok) {
						conn.rollback();
					}
					else {
						conn.commit();
					}
				
					conn.setAutoCommit(true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				DBConnectionManager.getInstance().freeConnection("portal", conn);
			}
		}
		
		if(ok) {
			return newID;	
		}
		else {
			return -1;
		}
	}
	
/**
 * Valida si existe ya existe el registro
 * @param nameUnico
 * @return
 */
	public boolean existeNombreUnico(String nameUnico ) {

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT 1 as countName ");
		sql.append(" FROM eje_files_unico ");
		sql.append(" WHERE name_unic = '" );
		sql.append(nameUnico).append("' ");
		
		boolean resulName = false;
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",sql.toString());
			if(data.next()) {
				resulName = true;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return resulName;
	}
	
	public boolean existeIdFile(int idFile) {

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT 1 as countName ");
		sql.append(" FROM eje_files_unico ");
		sql.append(" WHERE id_file = ").append(idFile);
		
		boolean resulName = false;
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",sql.toString());
			if(data.next()) {
				resulName = true;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return resulName;
	}
	
/**
 * Devuelve el map desde un select * from
 * @throws Exception 
 */	
	public EjeFilesUnico getFile( int idFile ) throws Exception {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT id_file, fecha_subida, rut_subida, name_original,  name_unic, ")
		.append(" 			bytes, machine_name, text, id_tipo, text ,  full_hdd_Path, relative_path_to_webcontent, isweb ")
		.append(" FROM eje_files_unico ");
		sql.append(" WHERE id_file = ");
		sql.append(idFile);
		EjeFilesUnico file = null;
		
		try {
			ConsultaData con = ConsultaTool.getInstance().getData("portal",sql.toString());
			
			if(con.next()) {
				file = new EjeFilesUnico(con.getInt("id_file"),
						EjeFileUnicoTipo.getEjeFileUnicoTipo(con.getInt("id_tipo")),
						String.valueOf(con.getDateJava("fecha_subida")), 
						con.getInt("rut_subida"),
						con.getString("name_original"),
						con.getString("name_unic"), 
						con.getLong("bytes"),
						con.getString("machine_name"),
						con.getString("text"),
						con.getString("full_hdd_Path"),
						con.getString("relative_path_to_webcontent"),
						con.getBoolean("isweb")
						);
								
			}
		}
		catch (SQLException e) {
			if(manipulateSQLError(e)) {
				return getFile(idFile);
			}
			else {
				e.printStackTrace();
			}
		}	
		
	    return file;
	}
	
	
/**
 * Devuelve el máximo id_file
 */
	public int getMaxIdInforme() {

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT isnull(max(id_file),0) as maximo ");
		sql.append(" FROM eje_files_unico ");
		int maxId = 0;
		 
		try {
			ConsultaData con = ConsultaTool.getInstance().getData("portal",sql.toString());
			if(con.next()) {
		    	maxId = con.getInt("maximo");
		    }
		}
		catch (SQLException e) {
			e.printStackTrace();
		}	

	    return maxId;
	}
	
	/**
	 * No debería llamarse este método con un objeto construido, dado que el constructor va a ir cambiando con el tiempo
	 * @deprecated
	 * */
	public ConsultaData getFiles(EjeFileUnicoTipo tipoFile){
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ID_FILE,ID_TIPO,FECHA_SUBIDA ,RUT_SUBIDA,NAME_ORIGINAL,NAME_UNIC,BYTES,MACHINE_NAME,TEXT, full_hdd_Path, relative_path_to_webcontent, isweb ");
		sql.append(" FROM EJE_FILES_UNICO ");
		sql.append(" WHERE ID_TIPO = ? ");
		sql.append(" ORDER BY ID_FILE DESC ");
		
		String[] params = { String.valueOf(tipoFile.getId()) };
		
		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("portal",sql.toString(),params);
		}
		catch (SQLException e) {
			if(manipulateSQLError(e)) {
				return getFiles(tipoFile);
			}
			else {
				e.printStackTrace();
			}
		}
		
		
		return data;
	}
	
	
	public boolean updateGlosa(int idArchivo, String glosa){
		
		String sql = " UPDATE EJE_FILES_UNICO "
			.concat(" SET TEXT = ? ")
			.concat(" WHERE ID_FILE = ? ");
		
		String[] params = { glosa , String.valueOf(idArchivo) };
		try {
			ConsultaTool.getInstance().update("portal", sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
 
		return true;
	}
	
	public boolean delFile(int idArchivo){
		
		String sql = " DELETE FROM EJE_FILES_UNICO "
			.concat(" WHERE ID_FILE = ? ");
		
		String[] params = { String.valueOf(idArchivo) };

		boolean ok = false;
		
		try {
			ok = ConsultaTool.getInstance().update("portal",sql.toString(),params) > 0;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ok;
	}
	

}


