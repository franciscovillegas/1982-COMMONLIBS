package portal.com.eje.senchatool;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.genericosenchaconf.ifaces.IActionColumn;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.senchatool.ifaces.IActionColumnTool;
import portal.com.eje.tools.javabeans.JavaBeanTool;
import portal.com.eje.tools.paquetefactory.PaqueteFactory;

class ViewIdeActionColumnsTool implements IActionColumnTool {

	public static IActionColumnTool getInstance() {
		return Util.getInstance(ViewIdeActionColumnsTool.class);
	}
	
	public ConsultaData getActionColumnsData(String paquete, Class<? extends IActionColumn> definicion) {
		return getActionColumnsData(paquete, definicion, false);
	}
	
	public ConsultaData getActionColumnsData(String paquete, Class<? extends IActionColumn> definicion, boolean includeTypeDef ) {
		return JavaBeanTool.getInstance().getDataFromObject(PaqueteFactory.getInstance().getObjects(paquete, definicion));
	}
	
}
