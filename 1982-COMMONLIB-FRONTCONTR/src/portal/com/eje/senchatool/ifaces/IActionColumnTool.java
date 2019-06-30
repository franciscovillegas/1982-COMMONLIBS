package portal.com.eje.senchatool.ifaces;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.genericosenchaconf.ifaces.IActionColumn;

public interface IActionColumnTool {

	
	public ConsultaData getActionColumnsData(String paquete, Class<? extends IActionColumn> definicion);
	
	public ConsultaData getActionColumnsData(String paquete, Class<? extends IActionColumn> definicion, boolean includeTypeDef );
	
}
