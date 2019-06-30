package cl.ejedigital.tool.validar.ifaces;

import java.util.Date;


public interface IValidarDato {

	
    public String validarFecha(String strOrigen, IFormatoFecha formatoOrigen);
    
    public String convertFecha(String strOrigen, IFormatoFecha formatoOrigen, IFormatoFecha formatoDestino);
    
    public String validarFecha(String strOrigen, String patternIn, String patternOut, String valorDefecto);

    public Date validarDate(String strOrigen, String patternIn, Date valorDefecto);
    
    public String cutString(String dato, int largo);
    
    public String cutStringSinComillas(String dato, int largo);
    
    public Object validarObjeto(Object dato, Object valorDefecto);

    public String validarDato(Object dato);

    public String validarDato(Object dato, String valorDefecto);
    
    public String validarDato(Object dato, int largoMaximo, String valorDefecto);
    
    public String validarDato_wCamelCase(Object dato);
    
    public String validarDato_wCamelCase(Object dato, String valorDefecto);
   
    public String validarDato_wCamelCase(Object dato, int largoMaximo, String valorDefecto);
    
    public float validarFloat(String n, float def);
    
    public float validarFloat(String n);
    
    public int validarInt(String n, int def);
    
    public int validarInt(String n);
    
    public long validarLong(String n, long def);
    
    public long validarLong(String n);
    
    public double validarDouble(String n, double def);
    
    public double validarDouble(String n);
    
    public char validarChar(String c, char def);
    
    public char validarChar(String num);
    
    public Boolean validarBoolean(String valor, Boolean def);

	public String validaResourceBundle_getString(String string, String string2, String string3);
      
	
}
