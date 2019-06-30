package portal.com.eje.portal.tool;

import portal.com.eje.portal.parametro.ParametroLocator;

 /**
  * Se debe ocupar portal.com.eje.portal.EModulos
  * 
  * @see portal.com.eje.portal.EModulos
  * @deprecated
  * */
public enum Aplications {
	/* 
	 * EN JDBC/<nombre> debe siempre ir el nombre de la conexión donde está el módulo
	 * EN el contexto de la aplicación debe ir en el segundo parámetro
	 */
	portal("jdbc/portal", "/portalrrhh"),
	webmatic("jdbc/portal" , "/webmatico"),
	quicksite("jdbc/qs" , "/qs" ),
	wfsdotacion("jdbc/mac", "/wfsdotacion"),
	wfsegresos("jdbc/mac", "/wfsegresos"),
	wfstraslados("jdbc/mac", "/wfstraslados"),
	wfscontratacion("jdbc/mac", "/wfscontratacion"),
	wfsausentismo("jdbc/mac", "/wfsausentismo"),
	wfsfichapersonal("jdbc/mac", "/wfsfichapersonal"),
	wfspreingreso("jdbc/mac", "/wfspreingreso"),/* 2016-10-06, Marcelo ya está eliminar */
	wfsrys("jdbc/mac", "/rys"); 
	
	private String jdbc;
	private String contexto;
	
	Aplications(String JDBC, String contexto) {
		this.jdbc = JDBC;
		this.contexto = contexto;
	}
	
	String getJDBC() {
		return this.jdbc;
	}
	
	String getConnectionName() {
		return this.jdbc.split("/")[1];
	}
	
	public String toString() {
		return getJDBC();
	}
	
	public String getContext() {
		return this.contexto;
	}
	
	public boolean isThis() {
		return (getContext().equals( ParametroLocator.getInstance().getModuloContext() ));
	}
}
