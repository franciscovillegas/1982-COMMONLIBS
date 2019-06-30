package cl.ejedigital.web.datos.util;

import cl.ejedigital.web.datos.ifaces.IDriverTool;
import portal.com.eje.portal.factory.Util;

class DriverToolMSSqlServer implements IDriverTool {
	private final String urlPattern = "jdbc:sqlserver://@@host:@@port;databaseName=@@bd" ;
	private String replaceHost = "@@host";
	private String replacePuerto = "@@port";
	private String replaceBD = "@@bd";
	/**
	 * Para jdk11+
	 * */
	
	public static DriverToolMSSqlServer getInstance() {
		return Util.getInstance(DriverToolMSSqlServer.class);
	}
	
	public String getClassConnectionDriver() {
		//drivers de sql
		return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	}
	
	public String getUrl(String host, String puerto, String conexionbd) {
		String retorno = null;
		
		if(host != null && puerto != null && conexionbd != null) {
			retorno = urlPattern.replaceAll(replaceHost, host);
			retorno = retorno.replaceAll(replacePuerto, String.valueOf(puerto));
			retorno = retorno.replaceAll(replaceBD, conexionbd);
		}
		
		return retorno;
	}

	@Override
	public String getDataBaseFromUrl(String url) {
		
		String sub = url.substring(url.indexOf("databaseName"), url.length());
		int indexPC = sub.indexOf(";") > 0 ? sub.indexOf(";") : sub.length();
		String def = sub.substring(0, indexPC );
		String bdNameIncInstance = def.substring(sub.indexOf("=") +1 , def.length());
		int indexS = sub.indexOf("\\") > 0 ? sub.indexOf("\\") : bdNameIncInstance.length();
		String bdName = bdNameIncInstance.substring(0, indexS );
		return bdName;
	}

}
