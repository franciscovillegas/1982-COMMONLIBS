package cl.ejedigital.web.datos;

import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;

public interface IConsultaTool {

	public ConsultaData getData(String jndi, CharSequence sql, Object[] paramsArray) throws SQLException;
	
}
