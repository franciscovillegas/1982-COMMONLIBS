package cl.ejedigital.web.datos.util;

import org.apache.commons.lang.NotImplementedException;

import cl.ejedigital.web.datos.ifaces.IDriverTool;
import portal.com.eje.portal.factory.Util;

class DriverToolJtds implements IDriverTool {
	//private final String url = "jdbc:jtds:sqlserver://@@bd";
	private final String driver = "net.sourceforge.jtds.jdbc.Driver";
	
	public static DriverToolJtds getInstance() {
		return Util.getInstance(DriverToolJtds.class);
	} 
	
	@Override
	public String getClassConnectionDriver() {
		return driver;
	}

	@Override
	public String getUrl(String host, String puerto, String conexionbd) {
		throw new NotImplementedException();
	}

	@Override
	public String getDataBaseFromUrl(String url) {
		String ret = null;
		String[] valores = url.split("/");
		if(valores.length > 0) {
			ret = valores[valores.length-1];	
			
			/*15-06-2018 instances*/
			if(ret != null && ret.indexOf(";") != -1) {
				ret = ret.split("\\;")[0];
				
			}
		}
		return ret;
	}

}
