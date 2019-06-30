package portal.com.eje.frontcontroller.ioutils;

import java.io.File;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.ioutils.ioutilbolsadegatos.IOClaseWebBolsaDeGatos;
import portal.com.eje.portal.factory.SingleFactory;
import portal.com.eje.portal.factory.SingleFactoryType;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.tools.excel.xlsx.Xlsx2CsvWe.CellExcelBasicTypes;

public class IOUtilBolsaDeGatos extends IOUtil {

	public static IOUtilBolsaDeGatos getIntance() {
	return Weak.getInstance(IOUtilBolsaDeGatos.class);
}
	
	public double createBolsaDeGatosByRut(IIOClaseWebLight io, String paramNameFileInput, int idtipo, String tipo, String descripcion, int periodo) throws Exception {
		File file = io.getUtil(IOUtilParam.class).getParamFile(io, paramNameFileInput);
		String error = null;
		
		if(!file.exists()) {
			error = "Archivo no existe";
		}
		else if(!file.getName().substring(file.getName().lastIndexOf("."), file.getName().length()).equals(".xlsx")) {
			error = "La existensión del archivo debe ser xlsx.";
		}
		else {
		
			/*OBTIENE DATA*/
			Map<String, CellExcelBasicTypes> definition = new LinkedHashMap<String, CellExcelBasicTypes>();
			for(int i = 1 ; i < 200 ; i++) {
				definition.put(String.valueOf(i), CellExcelBasicTypes.String );	
			}
			ConsultaData data = io.getUtil(IOUtilExcel.class).getParamExcelPrivate(io ,paramNameFileInput, false, 0, definition);
			data.transformFirstRowIntoHeader();
			/*END OBTIENE*/
			
			/*OBTIENE ID Y NOMBRE DEL ARCHIVO*/
			int idFile = io.getUtil(IOUtilFile.class).importParamFile(io, paramNameFileInput, tipo);
			/*END ONBTIENE*/
			
			IOClaseWebBolsaDeGatos mc = SingleFactory.getFactory(SingleFactoryType.UTIL).getInstance(IOClaseWebBolsaDeGatos.class);
			
			return mc.createBolsaByRut(io, io.getUsuario().getRutIdInt(), io.getUsuario().getName(), idFile, idtipo, descripcion, periodo, data);
		}
		
		if(error != null) {
			throw new Exception(error);
		}
		
		return -1;
	}

	/**
	 * Borra bolsa de gatos
	 * 
	 * */
	public boolean removeBolsaDeGatosByRut(IOClaseWeb io, int idUpload) throws SQLException {
		
		
		String tabla = null;
		
		try {
			String sql = "SELECT nombre_tabla FROM eje_generico_bolsadegatos WHERE id_upload = ? ";
			Object[] params = {idUpload};
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
			
			if(data != null && data.next()) {
				tabla = data.getString("nombre_tabla");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		
		{
			String sql = "DELETE FROM eje_generico_bolsadegatos_referencias WHERE id_upload = ? ";
			Object[] params = {idUpload};
			try {
				ConsultaTool.getInstance().update("portal", sql, params);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}
		
		if(tabla != null) {
			String sql = "if exists (select * from sysobjects where name='"+tabla+"' and xtype='U') DROP TABLE "+tabla;
			
			try {
				ConsultaTool.getInstance().update("portal", sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}
		
		{
			String sql = "DELETE FROM eje_generico_bolsadegatos WHERE id_upload = ? ";
			
			try {
				Object[] params = {idUpload};
				ConsultaTool.getInstance().update("portal", sql,params);
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
			
			return true;
		}
		
	}

	/**
	 * Retorna una bolsa de gatos
	 * @throws SQLException 
	 */
 
	public void getBolsaDeGatosByRut(IOClaseWeb io, int rut, int idUpload) throws SQLException {
		String tableName= null;
		
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT id_upload,usuario_rut,usuario_nombre,process_time,fecha_upload,descripcion,id_file,cant_registros,nombre_tabla,id_type ");
			sql.append(" FROM eje_generico_bolsadegatos ");
			sql.append(" WHERE  id_upload = ? ");
			
			Object[] params = { idUpload}; 
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
			if(data != null && data.next()) {
				tableName = data.getString("nombre_tabla");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		
		String sql = "SELECT * FROM sys.columns WHERE Name = N'rut' AND OBJECT_ID = OBJECT_ID(N'"+tableName+"')";
		ConsultaData data = ConsultaTool.getInstance().getData("portal", sql);
		if(data != null && data.next()) {
		
			try {
				sql = "SELECT * FROM "+tableName+" WHERE rut like (?) ";
				Object[] params = {"%"+rut+"%"};
				ConsultaData data2 = ConsultaTool.getInstance().getData("portal", sql, params);
				io.retSenchaJson(data2);
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
		else {
			throw new SQLException("No existe el campo rut, el excel no lo tenía ");
		}
				
		
	}
	
}
