package cl.ejedigital.tool.validar;

import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.WordUtils;

import cl.ejedigital.ExceptionNotImplemented;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.tool.validar.ifaces.IFormatoFecha;
import cl.ejedigital.tool.validar.ifaces.IValidarDato;


/**
 * @author Administrador
 */
public class Validar implements IValidarDato {
	private static IValidarDato instance;
	
	public static IValidarDato getInstance() {
		if(instance == null) {
			synchronized(Validar.class) {
				if(instance == null) {
					instance = new Validar();
				}				
			}
		}
		
		return instance;
	}
	
	/**
	 * @deprecated
	 * 
	 * */
	public Validar() {
	}


	public String validarDato(Object dato) {
		return validarDato(dato,"");
	}

	public Object validarObjeto(Object dato, Object valorDefecto) {
		if(dato == null) {
			return valorDefecto;
		}			
		else
			return dato.toString();
	}
	
	public String validarDato(Object dato, String valorDefecto) {
		Object o = validarObjeto(dato, valorDefecto);
		if(o != null) {
			return o.toString();
		}
		
		return null;
	}
	
	public String validarDato(Object dato, int largoMaximo, String valorDefecto) {
		Object o = validarObjeto(dato, valorDefecto);
		if(o != null) {
			MyString my = new MyString();
			return my.cortarStringSS(o.toString(), largoMaximo);
		}
		
		return null;
	}


	public String validarDato_wCamelCase(Object dato) {
		return WordUtils.capitalizeFully(validarDato(dato));
	}

	public String validarDato_wCamelCase(Object dato, String valorDefecto) {
		return WordUtils.capitalizeFully(validarDato(dato,valorDefecto));
	}

	public String validarDato_wCamelCase(Object dato, int largoMaximo, String valorDefecto) {
		return WordUtils.capitalizeFully(validarDato(dato, largoMaximo, valorDefecto));
	}

	/**
	 * @deprecated 2016-04-04 
	 * */
	public String validarFecha(String strOrigen, IFormatoFecha formatoOrigen) {
		throw new ExceptionNotImplemented();
	}

	/**
	 * @deprecated 2016-04-04 
	 * */
	public String convertFecha(String strOrigen, IFormatoFecha formatoOrigen, IFormatoFecha formatoDestino) {
		throw new ExceptionNotImplemented();
	}
	
	public String validarFecha(String strOrigen, String patternIn, String patternOut, String valorDefecto) {
		String fechaSalida = null;
		
		if(strOrigen != null) {
			try {
				fechaSalida = Formatear.getInstance().toAnotherDate(strOrigen, patternIn, patternOut);
			}
			catch(Exception e) {
				fechaSalida = valorDefecto;
			}
		}
		else {
			fechaSalida = valorDefecto;
		}
		
		return fechaSalida; 
	}
	
	public Date validarDate(String strOrigen, String patternIn, Date valorDefecto) {
		Date fechaSalida = null;
		
		if(strOrigen != null) {
			try {
				fechaSalida= Formatear.getInstance().toDate(strOrigen, patternIn);
			}
			catch(Exception e) {
				fechaSalida = valorDefecto;
			}

		}
		else {
			fechaSalida = valorDefecto;
		}
		
		return fechaSalida; 
	}




	public String cutString(String dato, int largo) {
		if(dato == null)
			return "";
		dato = dato.trim();
		if(dato.length() > largo && dato.length() > 3) {
			dato = String.valueOf(dato.substring(0,largo - 3)).concat("...");
		}
		else {
			return cutStringSinComillas(dato, largo);
		}
			
		return dato;
	}
	
	@Override
	public String cutStringSinComillas(String dato, int largo) {
		if(dato == null)
			return "";
		dato = dato.trim();
		if(dato.length() > largo) {
			dato = dato.substring(0,largo);
		}
		else {
			dato = dato.substring(0,dato.length());
		}
		return dato;
	}

	public float validarFloat(String n, float def) {
		try {
			return Float.parseFloat(n);
		}
		catch (Exception e) {
			return def;
		}
	}

	public float validarFloat(String n) {
		try {
			return Float.parseFloat(n);
		}
		catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Valida doubles que vienen con "."
	 * */
	public int validarInt(String n, int def) {
		try {
			if(n!= null && n.indexOf(".") != -1) {
				return Integer.parseInt(n.substring(0, n.lastIndexOf(".")));
			}
			else {
				return Integer.parseInt(n);
			}
		}
		catch (Exception e) {
			
			return def;
		}
	}

	public int validarInt(String n) {
		try {
			return Integer.parseInt(n);
		}
		catch (Exception e) {
			return 0;
		}
	}

	public long validarLong(String n, long def) {
		try {
			return Long.parseLong(n);
		}
		catch (Exception e) {
			return def;
		}
	}

	public long validarLong(String n) {
		try {
			return Long.parseLong(n);
		}
		catch (Exception e) {
			return 0;
		}
	}

	public double validarDouble(String n, double def) {
		try {
			return Double.parseDouble(n);
		}
		catch (Exception e) {
			return def;
		}
	}

	public double validarDouble(String n) {
		try {
			return Double.parseDouble(n);
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	public char validarChar(String c, char def) {
		try {
			return c.toCharArray()[0];
		}
		catch (Exception e) {
			return def;
		}
	}

	public char validarChar(String num) {
		try {
			return num.toCharArray()[0];
		}
		catch (Exception e) {
			return ' ';
		}
	}
	
	/**
	 * El dígito verificador debe ngresarse con minuscula
	 * 
	 * */	
	public boolean validarRut(int rut, char dv) {
		int m = 0, s = 1;
		boolean ok = false;
		
		try {
			for (; rut != 0; rut /= 10) {
				s = (s + rut % 10 * (9 - m++ % 6)) % 11;
			}
			
			ok = dv == (char) (s != 0 ? s + 47 : 75);
		} catch(Exception e) {

		}
		
		return ok;
	}
	
	public boolean validarRut(int rut, String dv) {
		boolean ok = false;
	
		try {
			ok = validarRut(rut,dv.toUpperCase().toCharArray()[0]);	
		} catch (Exception e) {
			
		}
		
		return ok;
	}



	public Boolean validarBoolean(String valor, Boolean def) {
		Boolean ok = def;
		
		try {
			ok = Boolean.valueOf(valor.toString());
		} catch (Exception e) {
			
		}
		
		return ok;
	}

	public String validaResourceBundle_getString(String propertyFile, String propertyParam, String defaultValue) {
		String retString = null;
		
		try {
			ResourceBundle rb = ResourceBundle.getBundle(propertyFile);
			retString = rb.getString(propertyParam);
		}
		catch(NullPointerException e) {
			retString = null;
		}
		catch(MissingResourceException e) {
			retString = null;
		}
		catch(ClassCastException e) {
			retString = null;
		}
		
		return retString;	
	}


	
}