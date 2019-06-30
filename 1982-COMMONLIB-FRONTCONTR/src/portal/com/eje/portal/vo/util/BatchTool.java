package portal.com.eje.portal.vo.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import error.BadParameterException;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.BatchSql;
import portal.com.eje.portal.vo.BatchSqlSingleton;

public class BatchTool {

	public static BatchTool getInstance() {
		return Util.getInstance(BatchTool.class);
	}
	
	public List<ConsultaData> execBatch(Object jndiOrConn, BatchSql batch) throws SQLException {
		if (batch == null) {
			throw new NullPointerException();
		}
		validaJndiOrConn(jndiOrConn);
		
		List<ConsultaData> retorno = null;

		if (String.class.isAssignableFrom(jndiOrConn.getClass())) {
			retorno = ConsultaTool.getInstance().getDatas((String) jndiOrConn, batch.getSql(), batch.getParams());
		} else if (Connection.class.isAssignableFrom(jndiOrConn.getClass())) {
			retorno = ConsultaTool.getInstance().getDatas((Connection) jndiOrConn, batch.getSql(), batch.getParams());
		}

		return retorno;
	}
	
	public List<ConsultaData> execBatch(Object jndiOrConn, BatchSqlSingleton batch) throws SQLException {
		if (batch == null) {
			throw new NullPointerException();
		}
		validaJndiOrConn(jndiOrConn);
		

		List<ConsultaData> retorno = null;

	
		if (String.class.isAssignableFrom(jndiOrConn.getClass())) {
			retorno = ConsultaTool.getInstance().getDatas((String) jndiOrConn, batch.getSqlConcatenate(), batch.getParamConcatenate());
		} else if (Connection.class.isAssignableFrom(jndiOrConn.getClass())) {
			retorno = ConsultaTool.getInstance().getDatas((Connection) jndiOrConn, batch.getSqlConcatenate(), batch.getParamConcatenate());
		}

		return retorno;
	}
	
	private void validaJndiOrConn(Object jndiOrConn)  {
		if (jndiOrConn == null) {
			throw new NullPointerException();
		}
		
		if(!(jndiOrConn instanceof String) && !(jndiOrConn instanceof Connection) ) {
			throw new BadParameterException("No es ni String.class ni Connection.class ");
		}
	}
	

}
