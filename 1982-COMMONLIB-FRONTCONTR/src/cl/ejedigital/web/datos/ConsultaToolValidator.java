package cl.ejedigital.web.datos;

import java.sql.Connection;

import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Util;

class ConsultaToolValidator {

	public static ConsultaToolValidator getInstance() {
		return Util.getInstance(ConsultaToolValidator.class);
	}
	
	public void checkConnectionNotNull(Connection conn) {
		if(conn == null) {
			throw new ConnectionException(getErrorNullConnection(conn));
		}
	}
	
	public void checkReferenceNotNull(Object connReference, Connection conn) {
		if(connReference == null || conn == null) {
			throw new ConnectionException(getErrorNullReference(connReference));
		}
	}
	
	public void checkReferenceNotNull(EModulos modulo, Object connReference, Connection conn) {
		if(modulo == null || connReference == null || conn == null) {
			throw new ConnectionException(getErrorNullReference(connReference));
		}
	}
	
	private String getErrorNullConnection(Connection conn) {
		return new StringBuilder().append("La conexión es null ").toString();
	}
	
	private String getErrorNullReference(Object connReference) {
		return new StringBuilder().append("La conexión del JDNI \"").append(connReference).append("\" es null ").toString();
	}
	
	private String getErrorNullReference(EModulos modulo, Object connReference) {
		return new StringBuilder().append("Para el modulo \"").append(modulo.toString()).append("\", la conexión del JDNI \"").append(connReference).append("\" es null ").toString();
	}
	
}
