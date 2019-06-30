package cl.ejedigital.web.datos;

import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cl.ejedigital.consultor.ConnectionDefinition;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataMeta;
import cl.ejedigital.consultor.ConsultaDataMode;
import cl.ejedigital.consultor.ConsultaPreparada;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IConsultaDataRow;
import cl.ejedigital.consultor.IMetaData;
import cl.ejedigital.consultor.ISenchaPage;
import cl.ejedigital.consultor.def.JsonObject;
import cl.ejedigital.consultor.def.JsonObjects;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.tool.reflect.ClaseConstructor;
import cl.ejedigital.tool.singleton.Singleton;
import cl.ejedigital.tool.strings.ArrayFactory;
import cl.ejedigital.web.datos.error.DuplicateKeyException;
import cl.ejedigital.web.datos.map.ConsultaDataMap;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.ClaseConversor;
 


/**
 * ConsultaTool tiene el principal propósito de construir ConsultaData y manipular bases de datos. 
 * Estas bases de datos no son más que matrices obtenidas desde el origen o contruidas por VOs en formato de listas.
 * Tambien podrás con este objeto leer bases de datos, modificar datos o transformar listas de objetos es ConsultaData
 * 
 * @author Francisco
 * @since 9 Enero 2015
 * 
 * */

public class ConsultaTool implements Singleton, IConsultaTool {	
	private Logger logger = Logger.getLogger(getClass());
	

	
	private ConsultaTool() {

	}
	
	/**
	 * Retorna la única instancia de ConsultaTool
	 * 
	 * */
	
	public static ConsultaTool getInstance() {
		return Util.getInstance(ConsultaTool.class);
	}
	
	public static ConsultaToolCached getCached() {
		return ConsultaToolCached.getInstance();
	}

	
	/**
	 * DataFields con dos keys, value y text.
	 * */
	
	public ConsultaData getNewConsultaDataForcomboBox() {
		
		List<String> lista = new ArrayList<String>();
		lista.add("value");
		lista.add("text");
		
		return new ConsultaData(lista);
	}
	
	public ConsultaData getNewConsultaDataForNewId( double newid, String text) {
		ConsultaData data = ConsultaTool.getInstance().getNewConsultaDataForcomboBox();
		DataFields fields = new DataFields();
		fields.put("value"	, newid);
		fields.put("text"	, text);
		data.add(fields);
		
		return data;
	}
	
	public boolean sort(ConsultaData data, String colName, Order order ) {
		if(data != null) {			
			//ConsultaData newData = new ConsultaData(data.getNombreColumnas());
			Collections.sort(data.getData(), new OrdenCData(colName,order));
		}
		
		return true;
	}
	
	

	public ConsultaData getData(EModulos modulo, String jndi, CharSequence consulta, Object[] array) throws SQLException {
		return getDataIntern(modulo, jndi,consulta,array);
	}
	
	/*ConnectionDefinition */
	public ConsultaData getData(ConnectionDefinition conn, String consulta) throws SQLException {
		return getDataIntern(EModulos.getThisModulo(), conn,consulta,null);
	}
	
	/*JNDI*/
	public ConsultaData getData(String jndi, CharSequence consulta) throws SQLException {
		Cronometro cro = new Cronometro();
		cro.start();
		
		ConsultaData data = getDataIntern(EModulos.getThisModulo(), jndi,consulta,null);
		
		logger.debug("getData(String jndi, CharSequence consulta) "+cro.getTimeHHMMSS_milli()+ " ConsultaData:"+(data!=null?data.timeStr:null));
		return data;
	}
	
	public ConsultaData getData(String jndi, CharSequence consulta, Object[] parametros) throws SQLException {
		return getDataIntern(EModulos.getThisModulo(), jndi, consulta, parametros);
	}
	
	/*Connection*/
	public ConsultaData getData(Connection conn, CharSequence consulta) throws SQLException {
		return getDataIntern(EModulos.getThisModulo(), conn, consulta, null );
	}
	
	public ConsultaData getData(EModulos modulo, String jndi, CharSequence consulta) throws SQLException {
		return getDataIntern(modulo, jndi, consulta, null);
	}
	
	public ConsultaData getData(EModulos modulo, String jndi, String consulta, Object[] parametros) throws SQLException {
		return getDataIntern(modulo, jndi, consulta, parametros);
	}
	
	public ConsultaData getData(Connection conn, CharSequence consulta, Object[] parametros) throws SQLException {
		return getDataIntern(EModulos.getThisModulo(), conn, consulta, parametros);
	}
	
	private ConsultaData getDataIntern(EModulos modulo, Object connReference, CharSequence consulta, Object[] parametros) throws SQLException {
		Connection conn = null;
		ConsultaData data = null;
		
		Cronometro cro = new Cronometro();
		cro.start();
		
	 
		if(connReference == null || consulta == null || consulta.length() < 5) {
			throw new NullPointerException();
		}else{
		
			try {
				if( connReference instanceof String) {
					conn = DBConnectionManager.getInstance().getConnection(modulo, String.valueOf(connReference));	
				}
				else if(connReference instanceof Connection) {
					conn = (Connection) connReference;
				}
				else if(connReference instanceof ConnectionDefinition) {			
					conn = DBConnectionManagerDinamic.getInstance().getConnection( (ConnectionDefinition) connReference);	
				}
				
				ConsultaToolValidator.getInstance().checkReferenceNotNull(connReference, conn);
				
				ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
				data = consultaPreparada.getData(consulta.toString(),parametros);
				
			}
			catch (ConnectionException e) {
				logger.error(consulta);
				logger.error(buildParamString(parametros));
				logger.error(e);
				
				throw e;
			}
			catch (SQLException e) {
				logger.error(consulta);
				logger.error(buildParamString(parametros));
				logger.error(e);
				
				throw e;
			}
			finally {
				if( connReference instanceof String) {
					if ( conn != null) {
						DBConnectionManager.getInstance().freeConnection(modulo, String.valueOf(connReference),conn);
					}
				}
			}
		}
		
		logger.debug("getDataIntern(EModulos modulo, Object connReference, CharSequence consulta, Object[] parametros) "+cro.getTimeHHMMSS_milli()+" ConsultaData:"+(data!=null?data.timeStr:null));
		return data;
		
	}
	
	public List<ConsultaData> getDatas(Object connReference, CharSequence consulta, Object[] parametros) throws SQLException {
		return privateGetDatas(connReference, consulta, parametros);
	}
	
	public List<ConsultaData> getDatas(Object connReference, CharSequence consulta, List<Object[]> parametros) throws SQLException {
		return privateGetDatas(connReference, consulta, parametros);
	}
	
	private List<ConsultaData> privateGetDatas(Object connReference, CharSequence consulta, Object parametros) throws SQLException {
		if(!(parametros instanceof List) && !(parametros instanceof Object[])  ) {
			throw new SQLException("No es ni List<Object[]> ni  Object[] ");
		}
		
		Connection conn = null;
		List<ConsultaData> data = null;
		
		if(connReference == null || consulta == null || consulta.length() < 5) {
			return null;
		}
		
		try {
			if( connReference instanceof String) {
				conn = DBConnectionManager.getInstance().getConnection(String.valueOf(connReference));	
			}
			else if(connReference instanceof Connection) {
				conn = (Connection) connReference;
			}
			else if(connReference instanceof ConnectionDefinition) {			
				conn = DBConnectionManagerDinamic.getInstance().getConnection( (ConnectionDefinition) connReference);	
			}
			
			ConsultaToolValidator.getInstance().checkReferenceNotNull(connReference, conn);
			
			ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
			
			if((parametros instanceof List)) {
				data = consultaPreparada.getDatas(consulta.toString(), (List<Object[]>)parametros);
			}
			
			if(parametros instanceof Object[]) {
				data = consultaPreparada.getDatas(consulta.toString(), (Object[])parametros);
			}
			
			return data;
		}
		catch (ConnectionException e) {
			logger.error(consulta);
			logger.error(e);
			
			throw e;
		}
		catch (SQLException e) {
			logger.error(consulta);
			logger.error(e);
			
			throw e;
		}
		finally {
			if( connReference instanceof String) {
				if ( conn != null) {
					DBConnectionManager.getInstance().freeConnection(String.valueOf(connReference),conn);
				}
			}
		}
		
	}
	
	public int update(String jndi, CharSequence consulta) throws SQLException {
		return update(jndi,consulta,null);
	}
	
	public int update(String jndi, CharSequence consulta, Object[] parametros) throws SQLException {
		Connection conn = null;
		
		try {
			conn = DBConnectionManager.getInstance().getConnection(jndi);
			
			ConsultaToolValidator.getInstance().checkReferenceNotNull(jndi, conn);
			
			ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
			return consultaPreparada.update(consulta.toString(),parametros);
			
			
		}
		catch (ConnectionException e) {
			throw e;
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			if ( conn != null) {
				DBConnectionManager.getInstance().freeConnection(jndi,conn);
			}
		}
		
	}
	
	public int update(Connection conn , CharSequence consulta) throws SQLException {
		return update(conn,consulta,null);
	}
	
	public int update(Connection conn , CharSequence consulta, Object[] parametros) throws SQLException {
		try {
			
			ConsultaToolValidator.getInstance().checkConnectionNotNull(conn);
			
			ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
			return consultaPreparada.update(consulta.toString(),parametros);
			
			
		}
		catch (ConnectionException e) {
			throw e;
		}
		catch (SQLException e) {
			throw e;
		}
		finally {

		}
		
	}
	

	public int update(EModulos modulo, String jndi, CharSequence consulta, Object[] parametros) throws SQLException {
		Connection conn = DBConnectionManager.getInstance().getConnection(modulo, jndi);
		try {
			ConsultaToolValidator.getInstance().checkReferenceNotNull(modulo, jndi, conn);
			
			ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
			return consultaPreparada.update(consulta.toString(),parametros);
		}
		catch (ConnectionException e) {
			throw e;
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			DBConnectionManager.getInstance().freeConnection(modulo, "jndi", conn);
		}
		
	}

	
	public boolean execute(String jndi, String consulta) throws SQLException {
		return execute(jndi,consulta,null);
	}
	
	public boolean execute(String jndi, String consulta, Object[] parametros) throws SQLException {
		Connection conn = null;
		
		try {
			conn = DBConnectionManager.getInstance().getConnection(jndi);
			
			ConsultaToolValidator.getInstance().checkReferenceNotNull(jndi, conn);
			
			ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
			return consultaPreparada.execute(consulta,parametros);
			
		}
		catch (ConnectionException e) {
			throw e;
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			if ( conn != null) {
				DBConnectionManager.getInstance().freeConnection(jndi,conn);
			}
		}
		
	}
	
	public boolean execute(Connection conn , String consulta) throws SQLException {
		return execute(conn,consulta,null);
	}
	
	public boolean execute(Connection conn , String consulta, Object[] parametros) throws SQLException {
		try {
			ConsultaToolValidator.getInstance().checkConnectionNotNull(conn);

			ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
			return consultaPreparada.execute(consulta, parametros);
		}
		catch (ConnectionException e) {
			throw e;
		}
		catch (SQLException e) {
			throw e;
		}
		finally {

		}
		
	}

	
	public boolean executeBatchs(String jndiOrConnection, CharSequence consulta, List<Object[]> querys) throws SQLException {
		 
		return this.executeBatchs(EModulos.getThisModulo(), jndiOrConnection, consulta, querys);
		 
	}
	
	public boolean executeBatchs(EModulos modulo, String jndiOrConnection, CharSequence consulta, List<Object[]> querys) throws SQLException {
 
		Connection conn = DBConnectionManager.getInstance().getConnection(modulo, (String)jndiOrConnection);
		try {
			ConsultaToolValidator.getInstance().checkReferenceNotNull(modulo, jndiOrConnection, conn);
			
			return executeBatchs(conn, consulta, querys);
		}
		finally {
			DBConnectionManager.getInstance().freeConnection(modulo, (String)jndiOrConnection, conn);
		}
		 
	}
	
	public boolean executeBatchs(Connection conn, CharSequence consulta, Object[] params) throws SQLException {
		List<Object[]> querys = new ArrayList<>();
		querys.add(params);
		
		return executeBatchs(conn, consulta, querys);
	}
	
	public boolean executeBatchs(Connection conn, CharSequence consulta, List<Object[]> querys) throws SQLException {
		 
		ConsultaToolValidator.getInstance().checkConnectionNotNull(conn);
	 
		ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
		return consultaPreparada.executeBatch(consulta, querys);
		 
		 
	}
	
	public boolean insert(String jndi, CharSequence consulta) throws SQLException {
		return insert(jndi,consulta,null);
	}
	
	public boolean insert(String jndi, CharSequence consulta, Object[] parametros) throws SQLException {
		Connection conn = null;
		
		try {
			conn = DBConnectionManager.getInstance().getConnection(jndi);
 
			ConsultaToolValidator.getInstance().checkReferenceNotNull(jndi, conn);
			
			ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
			return consultaPreparada.insert(consulta.toString(),parametros);
			 
			 
			
			
			
		}
		catch (ConnectionException e) {
			throw e;
		}
		catch (DataTruncation e) {
			//printErrorDataTruncation(conn, consulta, parametros);
			throw e;
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			if ( conn != null) {
				DBConnectionManager.getInstance().freeConnection(jndi,conn);
			}
		}
		
	}
	
	public boolean insert(Connection conn, String consulta) throws SQLException {
		return insert(conn,consulta,null);
	}
	
	public boolean insert(Connection conn, String consulta, Object[] parametros) throws SQLException {
		try {
			ConsultaToolValidator.getInstance().checkConnectionNotNull(conn);
			
			ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
			return consultaPreparada.insert(consulta,parametros);
			
			
		}
		catch (ConnectionException e) {
			throw e;
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			
		}
	}
	
	
	public double insertIdentity(String jndi, String consulta) throws SQLException {
		return insertIdentity(jndi,consulta,null);
	}
	
	public double insertIdentity(String jndi, CharSequence consulta, Object[] parametros) throws SQLException {
		Connection conn = null;
		
		try {
			conn = DBConnectionManager.getInstance().getConnection(jndi);
			
			ConsultaToolValidator.getInstance().checkReferenceNotNull(jndi, conn);
			
			ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
			boolean ok = consultaPreparada.insert(consulta.toString(),parametros);
			
			if(ok) {
				return consultaPreparada.getIdentityIndex();
			}
			else {
				return -1;
			}	
			 
			
		}
		catch (ConnectionException e) {
			throw e;
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			if ( conn != null) {
				DBConnectionManager.getInstance().freeConnection(jndi,conn);
			}
		}
		
	}
	
	public double insertIdentity(Connection conn, String consulta) throws SQLException {
		return insertIdentity(conn,consulta,null);
	}
	
	public double insertIdentity(Connection conn, CharSequence consulta, Object[] parametros) throws SQLException {
		try {
			ConsultaToolValidator.getInstance().checkConnectionNotNull(conn);
			
			ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
			boolean ok = consultaPreparada.insert(consulta.toString(),parametros);
			
			if(ok) {
				return consultaPreparada.getIdentityIndex();
			}
			else {
				return -1;
			}
		}
		catch (ConnectionException e) {
			throw e;
		}
		catch (DataTruncation e) {
			//printErrorDataTruncation(conn, consulta, parametros);
			throw e;
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			
		}
	}
	
	public int[] executeBatch(String jndi, String consulta) throws SQLException {
		Connection conn = null;
		
		try {
			conn = DBConnectionManager.getInstance().getConnection(jndi);
			
			ConsultaToolValidator.getInstance().checkReferenceNotNull(jndi, conn);
			
			ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
			return consultaPreparada.executeBatch(consulta);
		}
		catch (ConnectionException e) {
			throw e;
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			DBConnectionManager.getInstance().freeConnection(jndi, conn);
		}
	}
	
	public int[] executeBatchWithUse(String jndi, String databaseToUse, String sql) throws SQLException {
		Connection conn = null;
		
		try {
			conn = DBConnectionManager.getInstance().getConnection(jndi);
			ConsultaToolValidator.getInstance().checkReferenceNotNull(jndi, conn);
			
			String bdOrigen = conn != null ? conn.getCatalog() : null;
			
			int[] retornos = null;
			
			if(bdOrigen != null && canUseDatabase(conn, databaseToUse)) {
				try {
					executeBatch(conn, "use "+databaseToUse);
				
			
					ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
					retornos = consultaPreparada.executeBatch(sql);;
						
				}
				finally {
					executeBatch(conn, "use "+bdOrigen);	
				}
			}
			
			return retornos;
		}
		catch (ConnectionException e) {
			throw e;
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			DBConnectionManager.getInstance().freeConnection(jndi, conn);
		}
		
	}
	
	public boolean canUseDatabase(Connection conn, String db) {
		boolean can = false;
		try {
			getData(conn, "select top 1 * from "+db+".sys.columns");
			can = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return can;
	}
	
	public boolean canUseDatabase(String jndi, String db) {
		boolean can = false;
		try {
			getData(jndi, "select top 1 * from "+db+".sys.columns");
			can = true;
		} catch (SQLException e) {
			 logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}
		
		return can;
	}
	
	public boolean executeBatch(Connection conn, CharSequence consulta,Object[] params) throws SQLException {
		try {
			ConsultaToolValidator.getInstance().checkConnectionNotNull(conn);
			
			ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
			
			List<Object[]> querys = new ArrayList<>();
			if(params == null) {
				querys.add(params);
			}
			
			return consultaPreparada.executeBatch(consulta, querys);
		}
		catch (ConnectionException e) {
			throw e;
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
 
		}
	}
	
	public int[] executeBatch(Connection conn, CharSequence consulta) throws SQLException {
		try {
			ConsultaToolValidator.getInstance().checkConnectionNotNull(conn);
			
			ConsultaPreparada consultaPreparada = new ConsultaPreparada(conn);
			return consultaPreparada.executeBatch(consulta.toString());
		}
		catch (ConnectionException e) {
			throw e;
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
 
		}
	}
	
	
	public String getFirsStringtValue(ConsultaData data, String campoName){
		String r = null;
		
		if(data != null) {
			int pos = data.getPosition(); 
			if(data.next()) {
				return data.getForcedString(campoName);
			}
			
			data.setPosition(pos);
		}
		return r;
	}
	
	public <T> T getFirstValue(ConsultaData data, String campoName, Class<T> classTo){
		Object r = null;
		
		if(data != null) {
			int pos = data.getPosition(); 
			//data.toStart();
			
			if(data.next()) {
				r = ClaseConversor.getInstance().getObject( data.getObject(campoName), classTo);
			}
			
			data.setPosition(pos);
		}
		
		return ClaseConversor.getInstance().getObject(r, classTo);
	}
	
	public String getFirsStringtValue(ConsultaData data, String campoName, String defValue) {
		if(data != null) {
			int pos = data.getPosition(); 
			
			if(data.next()) {
				return data.getForcedString(campoName);
			}
			
			data.setPosition(pos);
		}
		
		return defValue;
		
	}
	
	public double getSum(ConsultaData data, String campoName) {
		double total = 0;
		
		if(data != null) {
			int pos = data.getPosition();
			
			while(data.next()) {
				total += Double.parseDouble(String.valueOf(data.getObject(campoName)));
			}
			
			data.setPosition(pos);
		}
		
		return total;
	}
	
	/**
	 * Crear una ConsultaData
	 * @author Francisco
	 * @since 26.05.2018
	 * */
	public ConsultaData buildConsultaData(String[] cols) {
		List<String> campos = new ArrayList<String>();
		for(String c : cols) {
			campos.add(c);
		}
		
		return new ConsultaData(campos);
	}
	
	/**
	 * Crear una ConsultaData
	 * @author Francisco
	 * @since 26.05.2018
	 * */
	
	public ConsultaData buildConsultaData(Boolean valor) {
		String[] cols = {"boolean"};
		ConsultaData data = buildConsultaData(cols);
		DataFields df = new DataFields();
		df.put("boolean", valor);
		data.add(df);
		
		return data;
	}
	/**
	 * Contruye un objeto ConsultaData en base a una lista de objetos. Todo estos objetos deben tener implementado la interface IConsultaDataRow
	 * en caso contrario arrojará la siguiente excepción WrongParserException
	 * 
	 * @author Francisco
	 * @since 9 de Enero del 2015
	 * 
	 * */
	
	public ConsultaData buildConsultaData(List listaDeRows) {
		ConsultaData data = null;
		
		if(listaDeRows != null && listaDeRows.size() > 0) {
			for( Object oRow : listaDeRows) {
				if(! (oRow instanceof IConsultaDataRow)){ 
					try {
						throw new Exception("[Debe ser del tipo "+IConsultaDataRow.class+"]");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				IConsultaDataRow row = (IConsultaDataRow) oRow;
				DataFields fields = row.toDataField();
				
				if( data == null) {
					Set<String> set = fields.keySet();
					List<String> nombreColumnas = new ArrayList<String>();
					for(String key : set) {
						nombreColumnas.add(key);
					}
					
					data = new ConsultaData(nombreColumnas);
				}
				
				
				data.add(fields);
				
			}
		}

		if( data == null ) {
			List<String> columnas = new ArrayList<String>();
			columnas.add("value");
			data = new ConsultaData(columnas);
		}
		
		return data;
	}
	
	
	public ConsultaData  getTableDefinition(String jndi, String table) {
		Connection conn = null;
		try {
			conn = DBConnectionManager.getInstance().getConnection(jndi);
			
			return getTableDefinition(conn, table);
		}
		catch (ConnectionException e) {
			throw e;
		}
		finally {
			if ( conn != null) {
				DBConnectionManager.getInstance().freeConnection(jndi,conn);
			}
		}
	}
	
	public ConsultaData getTableDefinition(Connection conn, String table) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.name as Tabla, c.name as campo, ti.name as 'tipo', c.max_length as 'longitud' ");
		sql.append(" from sys.columns c ");
		sql.append(" 	inner join sys.tables t on c.object_id = t.object_id ");
		sql.append(" 	inner join sys.types ti on c.system_type_id = ti.system_type_id ");
		sql.append(" where t.name = ? ");
		
		Object[] o = {table};
				 
		try {
			return getData(conn, sql.toString(), o);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Hecha para SQL, sin sensibilidad a mayusculas, debido a los nombres de columnas
	 * */
	private void printErrorDataTruncation(Connection conn, String consulta, Object[] parametros) {
		try {
			String[] partes  = consulta.toLowerCase().split("into"); //inset into bla (1,2,3) values (?,?,?)
			String[] partes2 = partes[1].split("values");
			String tableName = partes2[0].trim();
			
			List<String> definition = new LinkedList<String>();
			ConsultaData dataDefinition = null; 
			
			if(tableName.indexOf("(") != -1 ) {
				String[] partes3 = tableName.split("\\(");
				tableName = partes3[0];
				
				partes3[1] = partes3[1].replaceAll("\\(", "");
				partes3[1] = partes3[1].replaceAll("\\)", "");
				String[] campos = partes3[1].split("\\,");
				
				for(String c: campos) {
					definition.add(c);
				}
				
				dataDefinition = getTableDefinition(conn, tableName);
			}
			else {
				dataDefinition = getTableDefinition(conn, tableName);
				while(dataDefinition != null && dataDefinition.next()) {
					definition.add(dataDefinition.getString("campo"));
				}
				dataDefinition.toStart();
			}
			
			StringBuilder error = new StringBuilder();
			
			Map<String, Map<String,Object>> defMap = new HashMap<String, Map<String,Object>>();
			while(dataDefinition != null && dataDefinition.next()) {
				Map<String,Object> mapValues = new HashMap<String,Object>();
				
				Set<String> sets = dataDefinition.getActualData().keySet();
				for(String s : sets) {
					mapValues.put(s, dataDefinition.getObject(s));
				}
				
				defMap.put(dataDefinition.getString("campo").toLowerCase(), mapValues);
			}
			
			
			int i=0;
			for(String c : definition) {
				if(defMap.get(c) == null) {
					System.out.println("@@@ No encontré columna");
				}
				String campo 		 = c;
				String tipo 		 = (String)defMap.get(c).get("tipo");
				int    longitud		 = (Integer) defMap.get(c).get("longitud");
				String value 		 = String.valueOf(parametros[i]); 
				value = value == null? value = "" : value; 
						
				if("varchar".equals(tipo) && value.length() > longitud) {
					
					error.append("@@@@ Error :"+campo)
						 .append(" tipo:").append(tipo)
						 .append(" largo:").append(longitud)
						 .append(" valorToInsert:").append(value.length()).append("\n");
				}
				i++;
			}
						
			throw new SQLException(error.toString());
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("@@@@ no fue posible determinar el error de la consulta ");
		}
		
	}
	
	/**
	 * Retorna un ArrayFactory con todos los elementos de una columna perteneciente a una ConsultaData<br/>
	 * Hay que tener cuidado con la cantidad de valores que trae la ConsultaData
	 * 
	 * @since 2015-06-23
	 * 
	 * 
	 * */
	
	public ArrayFactory getArrayFactory(ConsultaData data, String columna) {
		ArrayFactory array = new ArrayFactory();
		
		if(data != null) {
			int pos = data.getPosition();
			while(data.next()) {
				array.add(data.getForcedString(columna));
			}
			
			data.setPosition(pos);
		}
		
		return array;
	}
	
	
	public JsonObjects getObjects(ConsultaData data) {
		JsonObjects obs = new JsonObjects();
		
		if(data != null) {
			int lastPos = data.getPosition();
			data.toStart();
			while(data.next()) {
				obs.add(getObject(data.getActualData()));
			}
			
			data.setPosition(lastPos);
		}
		
		return obs;
	}
	
	public JsonObject getObject(DataFields data) { 
		JsonObject obs = new JsonObject();
		
		if(data != null) {
			 
			Set<String> set = data.keySet();
			for(String key: set) {
				obs.put(key, data.get(key).getObject());
			}
			
		}
		
		return obs;
	}
	
	public ConsultaData filtroPorColumnas(ConsultaData data, String word, List<String> campos) {
		if(campos != null ) {
			String[] camposS = new String[campos.size()];
			campos.toArray(camposS);
			return filtroPorColumnas(data, word, camposS);
		}
		
		return null;
		
	}
	/**
	 * Retorna un consultadata que es un subconjunto del consultadata inicial, el resultrado es el filtro de cada registro que contiene el caracter Word para campo indicado.<br/>
	 * Consideraciones:<br/>
	 * 
	 * <ul>
	 * 		<li>Los campos no son sensibles a mayusculas</li>
	 * 		<li>La búsqueda reconoce palabras dentro del parametro word. Ej: Francisco Villegas T.</li>
	 * </ul>
	 * */

	public ConsultaData filtroPorColumnas(ConsultaData data, String word, String[] campos) {
		ConsultaData resultado = null;
		
		if( word == null || word.length() == 0 ) {
			return data;
		}
		
		word = word.toLowerCase();
	
		String[] words = word.trim().split(" ");
		
		if(data != null && campos != null  ) {
			int pos = data.getPosition();
			data.toStart();
			
			resultado = new ConsultaData(data.getNombreColumnas());
			while(data.next()) {
				
				 
				StringBuilder rowString = new StringBuilder();
				
				for(String c: campos) {
					if(c != null) {
						String valor = data.getForcedString(c);
						if(valor != null) {
							valor = valor.toLowerCase();
							rowString.append(valor).append(";");
						}
					}
				}
				
				boolean rowOK = true;
				
				for(String w : words) {
					rowOK &= rowString.toString().indexOf(w) != -1;
				}
				
				if(rowOK) {
					resultado.add(data.getActualData());
				}
			}
			
			data.setPosition(pos);
		}
		
		return resultado;
		
	}

	public ConsultaData capitalizeColumnas(ConsultaData data, String[] cols) {
		if(cols != null && data != null) {
			int pos = data.getPosition();
			data.toStart();
			
			while(data.next()) {
				for(String c: cols) {
					if( data.getField(c).getObject() instanceof String ) {
						data.getActualData().put(c, WordUtils.capitalizeFully(data.getForcedString(c)));
					}
						
				}
				
			}
			
			data.setPosition(pos);
		}
		
		return data;
	}
	
	public ConsultaData capitalizeColumnas(ConsultaData data) {
		if(data != null) {
			String[] cols =  data.getNombreColumnas().toArray(new String[data.getNombreColumnas().size()]);
			return capitalizeColumnas(data, cols);
		}
		
		return null;
	}

	public ConsultaData fromJson(String texto, String[] defColumns) {
		ConsultaData data = null;
		try {
			List<Map<String, String>>		 datos 		=  null;
			
			try {
				 
				datos = new Gson().fromJson(texto, (new TypeToken<List<Map<String, String>>>(){}).getType());
			}
			catch(Exception e) {
				try {
					datos = new Gson().fromJson(texto,  List.class);
				} 
				catch(Exception e2) {
					Map map = new Gson().fromJson(texto, (new TypeToken<Map<String, String>>(){}).getType());
					
					if(map != null) {
						datos = new LinkedList<Map<String, String>>();
						datos.add(map);
					}
				}
			}
			//System.out.println(json);
			if(datos == null || datos.size() == 0) return null;
			
			List<String>	 columnas 	= new ArrayList<String>();
			for(Map map : datos) {
				DataFields fields = new DataFields();
				Set<String> sets = map.keySet();
				for(String key: sets) {
					if(columnas.indexOf(key) == -1 ){
						columnas.add(key);	
					}
				}
			}
			
			if(columnas != null && columnas.size() > 0) {
				data = new ConsultaData(columnas);
				for(Map map : datos) {
					DataFields fields = new DataFields();
					Set<String> sets = map.keySet();
					for(String key: sets) {
						fields.put(key, new Field(map.get(key)));
					}
					data.add(fields);
				}
			}
		}
		catch(Exception e) {
			System.out.println("@@@@Error "+e.getMessage());
		}
		
		return data;
	}
	
	/**
	 * @deprecated
	 * */
	public ConsultaData fromJson(String texto) {
		ConsultaData data = null;
		try {
			List<Map>		 datos 		=  null;
			
			try {
				 
				datos = new Gson().fromJson(texto, (new TypeToken<List<Map<String, String>>>(){}).getType());
			}
			catch(Exception e) {
				try {
					datos = new Gson().fromJson(texto,  List.class);
				} 
				catch(Exception e2) {
					Map map = new Gson().fromJson(texto, (new TypeToken<Map<String, String>>(){}).getType());
					
					if(map != null) {
						datos = new LinkedList<Map>();
						datos.add(map);
					}
				}
			}
			//System.out.println(json);
			if(datos == null || datos.size() == 0) return null;
			
			List<String>	 columnas 	= new ArrayList<String>();
			boolean first = true;
			for(Map map : datos) {
				if(first ) {
					Set<String> sets = map.keySet();
					for(String key: sets) {
						if(columnas.indexOf(key) == -1 ){
							columnas.add(key);	
						}
					}
					first = false;
				}
				else {
					break;
				}
			}
			
			if(columnas != null && columnas.size() > 0) {
				data = new ConsultaData(columnas);
				for(Map map : datos) {
					DataFields fields = new DataFields();
					Set<String> sets = map.keySet();
					for(String key: sets) {
						fields.put(key, new Field(map.get(key)));
					}
					data.add(fields);
				}
			}
			
			data.markTimeGetData();
		}
		catch(Exception e) {
			logger.debug(e);
		}
		
		return data;
		
	}

	/**
	 * 
	 * 
	 * Traspone una ConsultaData completa, sin importar el puntero en la que esa esté. <br/>
	 * Como regla la cantidad de nuevas columnas debe coincidir con el total de registros originales + 1, por ejemplo, si la ConsultaData original solo  tiene 2 registros entonces, el número de newCols debe ser 3 = 2 (Cantidad de registros de la ConsultaData) + 1 (cabeceras) <br/>
	 * <br/>
	 * ConsultaData original<br/><br/>
	 * a  ,	b  ,c  ,  d,  e<br/>
	 * 11 , 12 ,13 , 14, 15<br/>
	 * 21 , 22, 23 , 24, 25<br/> 
	 * <br/>
	 * Al final del  método quedaría así:<br/>
	 * newName 1, newName 2, newName 3<br/>
	 * a , 11, 21 <br/>
	 * b , 12, 22 <br/>
	 * c , 13, 23 <br/>
	 * d , 14, 24 <br/>
	 * e , 15, 25 <br/>
	 * 
	 * @author Pancho
	 * @since 2017-07-19
	 * 
	 * */
	public ConsultaData trasponer(ConsultaData data, LinkedList<String> newCols, Map<String,String> mappingColsName) {
		ConsultaData newData = null;
		
		if(data != null && newCols != null) {
			if( data.size() + 1 == newCols.size()) {
				
				newData = new ConsultaData(newCols);
				
				if (mappingColsName!=null) {
					for (Map.Entry<String, String> campo : mappingColsName.entrySet()) {
						int posCol = 0;
						DataFields fields = new DataFields();
						fields.put(newCols.get(posCol++), campo.getValue());
						
						data.toStart();
						while(data.next()) {
							fields.put(newCols.get(posCol++), data.getObject(campo.getKey()));
						}
						
						newData.add(fields);
					}
				}else {
					List<String> nombreColumnas = data.getNombreColumnas();
					
					for(String nc : nombreColumnas) {
						int posCol = 0;
						DataFields fields = new DataFields();
						
						if(mappingColsName.get(nc) == null) { 
							fields.put(newCols.get(posCol++), nc);
						}
						else {
							fields.put(newCols.get(posCol++), mappingColsName.get(nc));
						}
						
						data.toStart();
						while(data.next()) {
							fields.put(newCols.get(posCol++), data.getObject(nc));
						}
						
						newData.add(fields);
					}
				}
			}
		}
		
		
		return newData;
	}
	
	/**
	 * Retona una Collection con todos los valores del campo
	 * 
	 * @author Pancho
	 * @since 25-06-2018
	 * */

	public <T> T getList(ConsultaData data, Class<? extends Collection> T, String fieldName) {
		
		Class[] definicion = {};
		Object[] parametros = {};
		T t = (T)ClaseConstructor.getNewFromClass(T, definicion, parametros);
		if(data != null) {
			int pos = data.getPosition();
			data.toStart();
			while(data != null && data.next()) {
				((Collection) t).add(data.getObject(fieldName));
			}
			
			data.setPosition(pos);
		}
		return t;	
	}
	
	/**
	 * Retona una Collection con todos los valores del campo, y cada uno de ellos se tranforma en la Class final
	 * 
	 * @author Pancho
	 * @since 25-06-2018
	 * */
	public <T> T getList(ConsultaData data ,  Class<? extends Collection> T, String campo, Class clazzFinal) {
		Class[] definicion = {};
		Object[] parametros = {};
		T t = (T)ClaseConstructor.getNewFromClass(T, definicion, parametros);
		if(data != null) {
			int pos = data.getPosition();
			data.toStart();
			while(data != null && data.next()) {
				
				Object o = data.getObject(campo);
				o = ClaseConversor.getInstance().getObject(o, clazzFinal);
				
				((Collection) t).add(o);
			}
			
			data.setPosition(pos);
		}
		return t;	
	}
	

	/**
	 * Convierte todos los cambois que Timestamp en Objetos String con el formato indicado
	 * 
	 * @author Pancho
	 * @since 22-06-2018
	 * */
	public ConsultaData formatDate(ConsultaData data, String stringOutFormat){
		if(data != null && stringOutFormat != null) {
			int pos = data.getPosition();
			
			data.toStart();
			while(data.next()) {
				for(int i = 0; i<data.getNombreColumnas().size();i++) {
					String nc = data.getNombreColumnas().get(i);
					Object o = data.getObject(nc);
					if(o instanceof Timestamp) {
						Date d = data.getDateJava(nc);
						data.getActualData().put(nc, Formatear.getInstance().toDate(d, stringOutFormat));
					}
				}
			}
			
			data.setPosition(pos);
		}
		
		return data;
	}

	/**
	 * Retorna una date con la fecha de getDate()
	 * @author Pancho
	 * @since 08-08-2018
	 * */
	public Date getNow() {
		return getNow(null);
	}
	
	/**
	 * Retorna una date con la fecha de getDate() <br/>
	 * Si conn == null entonces se tomará el JNDI portal
	 * 
	 * @author Pancho
	 * @since 08-08-2018
	 * */
	public Date getNow(Connection conn) {
		String sql = "select fecha=getdate()";
		
		try {
			ConsultaData data = null;
			if(conn == null) {
				data = ConsultaTool.getInstance().getData("portal", sql);
			}
			else {
				data = ConsultaTool.getInstance().getData(conn, sql);
			}
			
			if(data != null && data.next()) {
				return data.getDateJava("fecha");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public ConsultaData getDataPagged(String jndi, CharSequence sql, Object[] params, ISenchaPage page, String pk) throws SQLException {
		List<String> pksList = new ArrayList<String>();
		pksList.add(pk);
		return getDataPaggedIntern(jndi, sql, params, page, pksList);
	}
	
	public ConsultaData getDataPagged(Connection conn, CharSequence sql, Object[] params, ISenchaPage page, String pk) throws SQLException {
		List<String> pksList = new ArrayList<String>();
		pksList.add(pk);
		return getDataPaggedIntern(conn, sql, params, page, pksList);
	}
	
	public ConsultaData getDataPagged(String jndi, String sql, Object[] params, ISenchaPage page, List<String> pksList) throws SQLException {
		return getDataPaggedIntern(jndi, sql, params, page, pksList);
	}
	
	public ConsultaData getDataPagged(Connection conn, String sql, Object[] params, ISenchaPage page, List<String> pksList) throws SQLException {
		return getDataPaggedIntern(conn, sql, params, page, pksList);
	}

	private ConsultaData getDataPaggedIntern(Object connOrJndi, CharSequence sql, Object[] params, ISenchaPage page, List<String> pksList) throws SQLException {
		if(params == null) {
			params = new Object[0];
		}
		
		
		List<ConsultaData> dataList = null;
		ConsultaData dataPagged = null;
		 
		if(connOrJndi != null && sql != null) {
			Map<String,Object> map = ConsultaToolUtils.getIntance().getPaginaValues(connOrJndi, sql.toString(), pksList, page, params);
			
			String catalog		 = (String)map.get("StringCatalog");
			String jndi			 = (String)map.get("StringJndi");
			String stringSql	 = (String)map.get("StringSql");
			String stringOrginal = (String)map.get("StringOriginal");
			String stringSqlCount= (String)map.get("StringSqlCount");
			Object[] objectArray = (Object[])map.get("Object[]");
			Boolean pagginado	 = (Boolean)map.get("BooleanPagged");
			Boolean ordenado	 = (Boolean)map.get("BooleanSort");
			
			if(pagginado) {
				stringSqlCount += "\n"+stringSql;
				Object[] allParams = ConsultaToolUtils.getIntance().getConcatenateParam(params, objectArray);

				dataList = getDatas(connOrJndi, stringSqlCount, allParams);
				 
				 if(dataList != null && (dataList.size() == 2 || dataList.size() == 3)) {
					 ConsultaData dataCount = dataList.get(0);
					 dataPagged = dataList.get(1);
					 
					 if(dataCount != null && dataCount.next()) {
						 IMetaData meta = new ConsultaDataMeta();
						 
						 meta.setCatalog(catalog);
						 meta.setJndi(jndi);
						 meta.setPagged(pagginado);
						 meta.setSorted(ordenado);
						 meta.setPage(page);
						 meta.setPaggedTotalCount(dataCount.getInt("total_count"));
						 meta.setSqlFullData(stringOrginal);
						 meta.setSqlPaggedData(stringSql);
						 meta.setSqlCountData(stringSqlCount);
						 meta.setParams(params);
						 
						 dataPagged.setPreCountTotal(dataCount.getInt("total_count"));
						 
						 
						 dataPagged.setMetaData(meta);
						 dataPagged.markTimeGetData();
					 }
				 }

			}
			 
			if(dataPagged == null) {
				dataPagged = getDataIntern(EModulos.getThisModulo(), connOrJndi, stringSql, objectArray);
				 
				 if( dataPagged != null ) {
					 IMetaData meta = new ConsultaDataMeta();
					 
					 meta.setCatalog(catalog);
					 meta.setJndi(jndi);
					 meta.setPagged(pagginado);
					 meta.setSorted(ordenado);
					 meta.setPage(page);
					 meta.setSqlFullData(stringOrginal);
					 meta.setSqlPaggedData(stringSql);
					 meta.setSqlCountData(stringSqlCount);
					 meta.setParams(params);
					 
					 dataPagged.setPreCountTotal(dataPagged.size());
					 dataPagged.setMetaData(meta);
					 dataPagged.markTimeGetData();
				 }
			}
		
			if(dataPagged != null) {
				dataPagged.markTimeGetData();
			}
		}
		
		
		return dataPagged;
	}

	public boolean existTable(Connection conn, String tableName) {
		boolean si = false;
		try {
			String sql = "select * from sysobjects where name= ?  and xtype='U'";
			Object[] params = {tableName};
			ConsultaData data = ConsultaTool.getInstance().getData(conn, sql, params);
			si = (data != null && data.next());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return si;
	}

	public ConsultaData newConsultaData(String[] campos) {
		if(campos != null && campos.length > 0) {
			
			List<String> fields = new ArrayList<String>();
			
			for(String c : campos) {
				fields.add(c);
			}
			
			return new ConsultaData(fields);
		}
		
		return null;
	}
	
	private String buildParamString(Object[] params) {
		StringBuilder str = new StringBuilder("[");
		
		if(params != null) {
			for(int i = 0 ; i < params.length ; i++) {
				if(!"[".equals(str.toString())) {
					str.append(",");
				}
				str.append(ClaseConversor.getInstance().getObject(params[i], String.class));
			}
		}

		str.append("]");
		return str.toString();
	}

	/**
	 * Retorna un Map con solo un DataField por key, más de un value por key producirá el error DuplicateKeyException
	 * 
	 * @author Pancho
	 * @throws DuplicateKeyException 
	 * @since 25-09-2018
	 * */
	public <T>ConsultaDataMap <T> getMap(ConsultaData data, String nombreCampo, Class<T> claseKey) throws DuplicateKeyException {
		ConsultaDataMap<T>  map = null;
		
		if(data != null) {
			int pos = data.getPosition();
			
			map = new ConsultaDataMap(data);
			 
			data.setMode(ConsultaDataMode.CONVERSION);
			
			while(data.next()) {
				Object key = null;
				
				if(data.existField(nombreCampo)) {
					 key = ClaseConversor.getInstance().getObject( data.getObject(nombreCampo) , claseKey);
				}
				
				if(map.containsKey(key)) {
					throw new DuplicateKeyException("La key "+key+" ya existe en el Map final, para obtener mejores resultados use getMapList(ConsultaData, Class<? extends Map>, Class<? extends List>,  String, Class<String>) ");
				}
				
				map.put((T) ClaseConversor.getInstance().getObject( key , claseKey), data.getActualData(), true);
			}
			
			data.setPosition(pos);
		}
		
		return map;
	}
	
	/**
	 * Retorna un Map con solo un DataField por key, no genera error si hay más de una key
	 * 
	 * @author Pancho
	 * @throws DuplicateKeyException 
	 * @since 25-09-2018
	 * */
	public <T>ConsultaDataMap <T> getMapForce(ConsultaData data, String nombreCampo, Class<T> claseKey) throws DuplicateKeyException {
		ConsultaDataMap<T>  map = null;
		
		if(data != null) {
			int pos = data.getPosition();
			
			map = new ConsultaDataMap(data);
			 
			data.setMode(ConsultaDataMode.CONVERSION);
			
			while(data.next()) {
				Object key = null;
				
				if(data.existField(nombreCampo)) {
					 key = ClaseConversor.getInstance().getObject( data.getObject(nombreCampo) , claseKey);
				}
				
				map.put((T) ClaseConversor.getInstance().getObject( key , claseKey), data.getActualData(), true);
			}
			
			data.markTimeGetData();
			data.setPosition(pos);
		}
		
		return map;
	}
	
	public Map<Object, ConsultaData> getMapList(ConsultaData data, Class<? extends Map> classMap, Class<? extends List> classList,  String nombreCampo, Class<String> claseKey) { 
		throw new NotImplementedException();
	}

	/**
	 * Retorna un Map con varios DataFields por key
	 * 
	 * @author Pancho
	 * @since 26-09-2018
	 * */
	@SuppressWarnings("unchecked")
	public <T> ConsultaDataMap<T> getMapList(ConsultaData data, String nombreCampo, Class<T> claseKey)  {
		ConsultaDataMap<T> map = null;
		
		if(data != null) {
			int pos = data.getPosition();
			
			map = new ConsultaDataMap<T>(data);
			 
			data.setMode(ConsultaDataMode.CONVERSION);
			
			while(data.next()) {
				Object key = null;
				
				if(data.existField(nombreCampo)) {
					 key = ClaseConversor.getInstance().getObject( data.getObject(nombreCampo) , claseKey);
				}
				
				map.put((T)key, data.getActualData(), false);
			}
			
			data.setPosition(pos);
		}
		
		return map;
		
	}
	
	/**
	 * Cambia el un campo por otro, elmina el anterior
	 * */
	public void replaceCampo(ConsultaData data, String oldCampo, String newCampo) {
		if(data != null && oldCampo != null && newCampo != null) {
			int pos = data.getPosition();
			data.toStart();
			if(!data.getNombreColumnas().contains(newCampo)) {
				data.getNombreColumnas().add(newCampo);	
			}
			
			while(data != null && data.next()) {
				Object oldValue = data.getObject(oldCampo);
				data.getActualData().remove(oldCampo);
				data.getActualData().put(newCampo, oldValue);
			}
			
			if(data.getNombreColumnas().contains(oldCampo)) {
				data.getNombreColumnas().remove(oldCampo);
			}
			
			data.setPosition(pos);
		}
	}

	/**
	 * Retorna un Date a partir de la BD con una agregación de tiempo adicional
	 * @author Pancho
	 * */
	public Date getNowAdd(int timeCalendar, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime( getNow() );
		cal.add(timeCalendar, amount);
		
		return cal.getTime();
	}

	/**
	 * Desde una ConsultaData, genera una lista de valores String. <br/>
	 * Supongamos que tenemos una consulta a eje_ges_trabajador, quieres obtener francisco,juan,miguel, enrique, etc. <br/>
	 * Entonces llamaremos a este método concatenateValues(data, "nombre");
	 * 
	 * @since 29-05-2019
	 * @author Pancho
	 * */
	public String concatenateValues(ConsultaData data, String campo) {
		StringBuilder str = new StringBuilder();
		
		if(data != null && campo != null ) {
			int pos = data.getPosition();
			try {
				while(data.next()) {
					if(data.existField(campo)) {
						String valor = data.getString(campo);
						
						if(str.length() > 0) {
							str.append(",");
						}
						str.append(valor);
					}
					
				}
			}
			finally {
				data.setPosition(pos);
			}
			
		}
		
		return str.toString();
	}

 
}
