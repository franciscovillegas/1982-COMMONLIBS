// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 17:25:46
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Formatear.java

package portal.com.eje.tools;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Deprecada porque no se mejoró, se debe ocupar @see
 * 
 * @since 19-06-2018
 * @author Pancho
 * @deprecated
 * @see cl.ejedigital.tool.misc.Formatear
 * */
public class Formatear {

	/**
	 * Se debe usar la clase del paquete cl
	 * @deprecated
	 * */
    public Formatear()
    {
    }

    public static String fecha(String formato, Date fecha)
    {
        String ret = "";
        try
        {
            SimpleDateFormat f = new SimpleDateFormat(formato);
            ret = f.format(fecha);
        }
        catch(Exception exception) { }
        return ret;
    }

    public static String fecha(String formato)
    {
        return fecha(formato, new Date());
    }

    public static String fecha(String formato, java.sql.Date fecha)
    {
        return fecha(formato, fecha != null ? new Date(fecha.getTime()) : null);
    }

    public static String numero(int numero)
    {
        String ret = "0";
        try
        {
            ret = NumberFormat.getInstance(Locale.GERMAN).format(new Integer(numero));
        }
        catch(Exception exception) { }
        return ret;
    }
    
    public static String numero(BigInteger numero)
    {
        String ret = "0";
        try
        {
            ret = NumberFormat.getInstance(Locale.GERMAN).format(numero);
        }
        catch(Exception exception) { }
        return ret;
    }
    
    public static String numero(BigDecimal numero)
    {
        String ret = "0";
        try
        {
            ret = NumberFormat.getInstance(Locale.GERMAN).format(numero);
        }
        catch(Exception exception) { }
        return ret;
    }
    
    public static String numero(Long numero)
    {
        String ret = "0";
        try
        {
            ret = NumberFormat.getInstance(Locale.GERMAN).format(numero);
        }
        catch(Exception exception) { }
        return ret;
    }

    public static String numero(float numero, int cantDec)
    {
        String ret = "0";
        try
        {
            NumberFormat f = DecimalFormat.getInstance(Locale.GERMAN);
            f.setMinimumFractionDigits(cantDec);
            f.setMaximumFractionDigits(cantDec);
            ret = f.format(new Float(numero));
        }
        catch(Exception exception) { }
        return ret;
    }
    
	public static String toAnsiDate(String formatdate, String date) {
		String dateAnsi = null;
		if("".equals(date)) {
			return date;
		}
		if("MM/dd/yyyy".equals(formatdate)) {
			dateAnsi = date.substring(6,10).concat(date.substring(3,5)).concat(date.substring(0,2));
		}
		return dateAnsi;
	}
}