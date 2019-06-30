package cl.ejedigital.consultor;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;


public class ConsultaPreparada implements IConsultaManipulator {
	private Connection conn;
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	private double identityIndex;
	private final Logger logger;
	
	public ConsultaPreparada(Connection conn) {
		logger = Logger.getLogger(getClass().getName());
		this.conn = conn;
		identityIndex = -1;
		
	}
	
	public double getIdentityIndex() {
		return identityIndex;
	}
	
	
	 
	public int update(String consulta, Object[] params)  throws SQLException {
		Cronometro cro = new Cronometro();
		cro.start();
		try {
			pst = preparaStatement(consulta,params);
		
			int ok = pst.executeUpdate();

			ResultSet rs = pst.getGeneratedKeys();
			if (rs != null && rs.next()) {
				identityIndex = rs.getLong(1);
			}
			
			return ok;
		}
		catch (SQLException e) {
			logger.error("["+cro.GetTimeHHMMSS()+"] "+consulta, e);
			throw e;
		}
		finally {
			try {
				if(pst != null) {
					pst.close();
				}
				if(rs != null) {
					rs.close();
				}
			}
			catch (SQLException e) {
				logger.error("["+cro.GetTimeHHMMSS()+"] "+consulta, e);
				throw e;
			}
			
			
			
			logger.info("["+cro.GetTimeHHMMSS()+"] "+consulta );
		}
	}
	
	/**
	 * Ejecuta una cantidad N de vaciones de parámetros, para una misma consulta
	 * 
	 * @author Pancho
	 * @since 03-10-2018
	 * */
	
	public boolean executeBatch(CharSequence consulta, List<Object[]> querys) throws SQLException {
			
		try {	
			pst = conn.prepareStatement(consulta.toString());

			int pos = 0;
			for(Object[] params : querys) {
				preparaStatement(pst, params);
				
				pst.addBatch();
				pos++;
				
				if(pos % 1000 == 0 || pos == querys.size()) {
					pst.executeBatch();
				}

			}
			
			return true;
		}
		catch (SQLException e) {
			logger.error(consulta, e);
			throw e;
		}
		finally {
			try {
				if(pst != null) {
					pst.close();
				}
				if(rs != null) {
					rs.close();
				}
			}
			catch (SQLException e) {
				logger.error(consulta, e);
				throw e;
			}
			
			logger.info(consulta);
		}
	}
	
	public boolean insert(String consulta, Object[] params) throws SQLException {
		Cronometro cro = new Cronometro();
		cro.start();
		
		try {
			pst = preparaStatement(consulta,params);
		
			boolean ok = pst.executeUpdate() > 0;
			
			ResultSet rs = pst.getGeneratedKeys();
			if (rs != null && rs.next()) {
				identityIndex = rs.getLong(1);
			}
			
			return ok;
		}
		catch (SQLException e) {
			logger.error("["+cro.GetTimeHHMMSS()+"] "+consulta, e);
			throw e;
		}
		finally {
			try {
				if(pst != null) {
					pst.close();
				}
				if(rs != null) {
					rs.close();
				}
			}
			catch (SQLException e) {
				logger.error("["+cro.GetTimeHHMMSS()+"] "+consulta, e);
				throw e;
			}
			
			logger.info("["+cro.GetTimeHHMMSS()+"] "+consulta);
		}
	}
	
	public boolean execute(String consulta, Object[] params)  throws SQLException {
		Cronometro cro = new Cronometro();
		cro.start();
		try {
			pst = preparaStatement(consulta,params);
			pst.executeUpdate();
			return true;
		}
		catch (SQLException e) {
			logger.error("["+cro.GetTimeHHMMSS()+"] "+consulta, e);
			throw e;
		}
		finally {
			try {
				if(pst != null) {
					pst.close();
				}
				if(rs != null) {
					rs.close();
				}
			}
			catch (SQLException e) {
				logger.error("["+cro.GetTimeHHMMSS()+"] "+consulta, e);
				throw e;
			}

			logger.info("["+cro.GetTimeHHMMSS()+"] "+consulta );
		}
		
	}
	
	public int[] executeBatch(String consulta)  throws SQLException {
		Cronometro cro = new Cronometro();
		cro.start();
		
 
		try {
			Statement statement = conn.createStatement();
			String[] batchs = consulta.split("GO\n");
			
			for(String b : batchs) {
				statement.addBatch(b);	
			}
			
			int[] retornos = statement.executeBatch();
			 
			 return retornos;
			 
			 
		}
		catch (SQLException e) {
			logger.error("["+cro.GetTimeHHMMSS()+"] "+consulta, e);
			throw e;
		}
		finally {
			 
			logger.info("["+cro.GetTimeHHMMSS()+"] "+consulta );
		}
		
	}
	
	public ConsultaData getData(String consulta, Object[] params) throws SQLException {
		Cronometro cro = new Cronometro();
		cro.start();
		
		try {
			pst = preparaStatement(consulta,params);
		
			rs = pst.executeQuery();
			
			List<String> nombres = new ArrayList<String>();
			
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {			
				nombres.add(rs.getMetaData().getColumnName(i).toLowerCase());
			}
			ConsultaData data = new ConsultaData(nombres);
			
			
			DataFields fila = null;
			
			while(rs.next()) {
				fila = new DataFields();
				
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {			
					fila.put(rs.getMetaData().getColumnName(i).toLowerCase(), new Field(rs.getObject(i)));
				}
				
				data.add(fila);
			}
			
			data.markTimeGetData((long) cro.GetMilliseconds());			
			return data;

		}
		catch (SQLException e) {
			logger.error("["+cro.GetTimeHHMMSS()+"] "+consulta, e);
			throw e;
		}
		finally {
			try {
				if(pst != null) {
					pst.close();
				}
				if(rs != null) {
					rs.close();
				}
			}
			catch (SQLException e) {
				logger.error("["+cro.GetTimeHHMMSS()+"] "+consulta, e);
				throw e;
			}
			
			logger.info("["+cro.GetTimeHHMMSS()+"] "+consulta);
		}
	}	
	
	public List<ConsultaData> getDatas(String consulta, List<Object[]> querys) throws SQLException {
		 StringBuilder sql = new StringBuilder();
		 
		 List<Object> params = new LinkedList<Object>();
		 for(Object[] paramsToIn : querys) {
			 sql.append(consulta).append("\n");
			 
			 for(Object o : paramsToIn) {
				 params.add(o);
			 }
		 }
		 
		 return getDatas(sql, params.toArray());
	}	
	
	public List<ConsultaData> getDatas(CharSequence consulta, Object[] params) throws SQLException {
		Cronometro cro = new Cronometro();
		cro.start();
		
		try {
			pst = preparaStatement(consulta.toString(),params);
			
			
			boolean results = pst.execute();
			
			List<ConsultaData> datas = new LinkedList<ConsultaData>();
			
			while(results) {
				rs = pst.getResultSet();
				List<String> nombres = new ArrayList<String>();
				
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {			
					nombres.add(rs.getMetaData().getColumnName(i).toLowerCase());
				}
				ConsultaData data = new ConsultaData(nombres);
				
				
				DataFields fila = null;
				
				while(rs.next()) {
					fila = new DataFields();
					
					for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {			
						fila.put(rs.getMetaData().getColumnName(i).toLowerCase(), new Field(rs.getObject(i)));
					}
					
					data.add(fila);
				}
				
				data.markTimeGetData((long) cro.GetMilliseconds());			
				datas.add(data);
				
				results = pst.getMoreResults();
			}
		
			return datas;

		}
		catch (SQLException e) {
			logger.error("["+cro.GetTimeHHMMSS()+"] "+consulta, e);
			throw e;
		}
		finally {
			try {
				if(pst != null) {
					pst.close();
				}
				if(rs != null) {
					rs.close();
				}
			}
			catch (SQLException e) {
				logger.error("["+cro.GetTimeHHMMSS()+"] "+consulta, e);
				throw e;
			}
			
			logger.info("["+cro.GetTimeHHMMSS()+"] "+consulta);
		}
	}	
	
	private PreparedStatement  preparaStatement(String consulta, Object[] params) throws SQLException {
		PreparedStatement pst = conn.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
		
		return preparaStatement(pst, params);
	}

	private PreparedStatement preparaStatement(PreparedStatement pst, Object[] params) throws SQLException {
		int cont = 1;
		if(params != null) {
			for(Object a : params) {
				
				if(a instanceof Boolean) {
					pst.setBoolean(cont++,(Boolean)a );
				}
				else if(a instanceof byte[]) {
					pst.setBytes(cont++,(byte[])a );
				}
				else if(a instanceof Byte) {
					pst.setByte(cont++,(Byte)a );
				}
				else if(a instanceof Short) {
					pst.setShort(cont++,(Short)a );
				}
				else if(a instanceof Integer) {
					pst.setInt(cont++,(Integer)a );
				}
				else if(a instanceof Long) {
					pst.setLong(cont++,(Long)a );
				}
				else if(a instanceof Float) {
					pst.setFloat(cont++,(Float)a );
				}
				else if(a instanceof Double) {
					pst.setDouble(cont++,(Double)a );
				}
				else if(a instanceof java.util.Date) {
					;
					pst.setString(cont++, Formatear.getInstance().toDate((java.util.Date)a, "yyyyMMdd HH:mm:ss.SSS") );
				}
				else if(a instanceof java.sql.Date) {
					pst.setDate(cont++,(Date)a );
				}
				else if(a instanceof java.sql.Time) {
					pst.setTime(cont++,(Time)a );
				}
				else if(a instanceof java.sql.Timestamp) {
					pst.setTimestamp(cont++,(Timestamp)a );
				}
				else if(a instanceof String) {
					pst.setString(cont++,(String)a );
				}
				else if(a instanceof Blob) {
					pst.setBlob(cont++,(Blob)a );
				}
				else if(a instanceof BigDecimal) {
					pst.setBigDecimal(cont++,(BigDecimal)a );
				}
				else if(a instanceof Character) {
					pst.setObject(cont++, a.toString(), java.sql.Types.VARCHAR ,1);
					
					 
				}
				else if(a instanceof Enum) {
					pst.setString(cont++, a.toString() );
				}
				else if(a == null) {
					pst.setNull( cont++, java.sql.Types.VARCHAR );
				}
				
				else {
					throw new SQLException("[ERROR GRAVISIMO] El parametro en el indice \""+cont+"\"("+a+") no está definido como tipo.");
				}
			}
		}
		
		return pst;
	}

	private ConsultaData buildConsultaData(ResultSet rs) throws SQLException {
		List<String> nombres = new ArrayList<String>();
		
		for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {			
			nombres.add(rs.getMetaData().getColumnName(i).toLowerCase());
		}
		ConsultaData data = new ConsultaData(nombres);
		
		
		DataFields fila = null;
		
		while(rs.next()) {
			fila = new DataFields();
			
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {			
				fila.put(rs.getMetaData().getColumnName(i).toLowerCase(), new Field(rs.getObject(i)));
			}
			
			data.add(fila);
		}
		
		data.markTimeGetData();	
 
		
		return data;
	}
}
