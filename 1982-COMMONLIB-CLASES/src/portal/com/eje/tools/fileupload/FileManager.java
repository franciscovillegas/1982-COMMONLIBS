package portal.com.eje.tools.fileupload;

import java.sql.Connection;
import java.sql.SQLException;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.fileupload.vo.EjeFileUnicoTipo;
import portal.com.eje.tools.fileupload.vo.EjeFilesUnico;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaPreparada;
import cl.ejedigital.web.datos.ConsultaTool;

/**
 * Cambiado por su homologá en el paquete cl.ejedigital.web.
 * 
 * @deprecated 
 * 
 * */
final class FileManager {
	
    FileManager() {

    }

/**
 * Inserta registro en la tabla eje_files_unico
 * @return
 */

	synchronized public int insertFile(EjeFilesUnico eje ) {
		int newId = getMaxIdInforme() + 1;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO eje_files_unico ");
		sql.append(" (id_file, id_tipo, fecha_subida , rut_subida, name_original , name_unic , bytes, machine_name ) VALUES (");
		sql.append(newId).append(", ");
		sql.append(eje.getIdTipo().getId()).append(",");
		sql.append("getdate(),");
		sql.append(eje.getRutSubida()).append(",'");
		sql.append(eje.getNameOriginal()).append("','");
		sql.append(eje.getNameUnic()).append("',");
		sql.append(eje.getBytes()).append(",");
		
		if(eje.getMachineName() != null) {
			sql.append("'").append(eje.getMachineName()).append("')");
		} 
		else {
			sql.append("").append(eje.getMachineName()).append(")");
		}
		
		try {
			ConsultaTool.getInstance().insert("portal",sql.toString());
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newId;
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
			ConsultaData con = ConsultaTool.getInstance().getData("portal",sql.toString());
			if(con.next()) {
				resulName = true;
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
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
			ConsultaData con = ConsultaTool.getInstance().getData("portal",sql.toString());
			if(con.next()) {
				resulName = true;
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
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
		sql.append(" SELECT * ");
		sql.append(" FROM eje_files_unico ");
		sql.append(" WHERE id_file = " );
		sql.append(idFile);
		
		ConsultaData con = ConsultaTool.getInstance().getData("portal",sql.toString());
		
		EjeFilesUnico file = null;
		if(con.next()) {
			file = new EjeFilesUnico(con.getInt("ID_FILE"),
					EjeFileUnicoTipo.getEjeFileUnicoTipo(con.getInt("ID_TIPO")),
					String.valueOf(con.getDateJava("FECHA_SUBIDA")), 
					con.getInt("RUT_SUBIDA"),
					con.getString("NAME_ORIGINAL"),
					con.getString("NAME_UNIC"), 
					con.getLong("BYTES"),
					con.getString("MACHINE_NAME"));
			
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
		int maxId=-1;
		
		try {
			ConsultaData con = ConsultaTool.getInstance().getData("portal",sql.toString());
			if(con.next()) {
				maxId = con.getInt("maximo");
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return maxId;
	}
	
	
	public ConsultaData getFiles(EjeFileUnicoTipo tipoFile){
		
		String sql = " select ID_FILE,ID_TIPO,FECHA_SUBIDA ,RUT_SUBIDA,NAME_ORIGINAL,NAME_UNIC,BYTES,MACHINE_NAME,TEXT "
			.concat(" from EJE_FILES_UNICO ")
			.concat(" WHERE ID_TIPO = ? ")
			.concat(" ORDER BY ID_FILE DESC ");
		
		String[] params = { String.valueOf(tipoFile.getId()) };
		
		ConsultaData data = null;
		
		try {
			data = ConsultaTool.getInstance().getData("portal",sql.toString(),params);
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return data;
	}
	
	
	public boolean updateGlosa(int idArchivo, String glosa){
		
		String sql = " UPDATE EJE_FILES_UNICO "
			.concat(" SET TEXT = ? ")
			.concat(" WHERE ID_FILE = ? ");
		
		String[] params = { glosa , String.valueOf(idArchivo) };
		
		boolean ok = false;
		
		try {
			ok = ConsultaTool.getInstance().update("portal",sql,params) > 0;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ok;
	}
	
	public boolean delFile(int idArchivo){
		
		String sql = " DELETE FROM EJE_FILES_UNICO "
			.concat(" WHERE ID_FILE = ? ");
		
		String[] params = { String.valueOf(idArchivo) };
		

		boolean ok = false;
		
		try {
			ok = ConsultaTool.getInstance().update("portal",sql,params) > 0;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ok;
	}
	

}


