package cl.ejedigital.tool.validar.ifaces;

import java.text.SimpleDateFormat;


public class FormatoFecha implements IFormatoFecha {
	public static FormatoFecha ANSI = new FormatoFecha("yyyyMMdd hh:mm:ss");
	public static FormatoFecha MIDDLEGUION_YYYY_MM_DD = new FormatoFecha("yyyy-MM-dd hh:mm:ss");
	public static FormatoFecha MIDDLEGUION_DD_YYYY_MM = new FormatoFecha("dd-yyyy-MM hh:mm:ss");
	public static FormatoFecha DOWNGUION_YYYY_MM_DD = new FormatoFecha("yyyy_MM_dd hh:mm:ss");
	public static FormatoFecha DOWNGUION_DD_YYYY_MM = new FormatoFecha("dd_yyyy_MM hh:mm:ss");
	public static FormatoFecha SLASH_YYYY_MM_DD = new FormatoFecha("yyyy/MM/dd hh:mm:ss");
	public static FormatoFecha SLASH_DD_YYYY_MM = new FormatoFecha("dd/yyyy/MM hh:mm:ss");
	public static FormatoFecha BACKSLASH_YYYY_MM_DD = new FormatoFecha("yyyy\\MM\\dd hh:mm:ss");
	public static FormatoFecha BACKSLASH_DD_YYYY_MM = new FormatoFecha("dd\\yyyy\\MM hh:mm:ss");
	private String formato;
	
	private FormatoFecha(String formato) {
		this.formato = formato;
	}
	
	public SimpleDateFormat getFormato() {
		SimpleDateFormat f = new SimpleDateFormat(formato);
		return null;
	}

}
