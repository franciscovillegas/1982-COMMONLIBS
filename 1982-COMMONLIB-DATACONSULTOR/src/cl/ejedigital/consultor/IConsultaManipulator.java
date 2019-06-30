package cl.ejedigital.consultor;

import java.sql.SQLException;


public interface IConsultaManipulator {
	
	public int update(String consulta, Object[] params)  throws SQLException;
	
	public boolean insert(String consulta, Object[] params) throws SQLException;
	
	public boolean execute(String consulta, Object[] params)  throws SQLException;
	
	public ConsultaData getData(String consulta, Object[] params) throws SQLException;
	
	
}
