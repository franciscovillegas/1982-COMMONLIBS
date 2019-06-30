package portal.com.eje.portal.cargo;

import cl.ejedigital.consultor.ConsultaData;

public interface ICargo {

	public ConsultaData getCargo() ;
	
	public ConsultaData getCargo(String strFiltro) ;
	
	public ConsultaData getCargo(Integer cargo) ;
	
	public ConsultaData getCargo(Integer cargo, Integer empresa) ;
	
	public ConsultaData getCargo(Integer cargo, Integer empresa, String strFiltro) ;
	
}
