package portal.com.eje.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

/**
 * @deprecated
 * */
public class Validar {
	
    public Validar()
    {
        formatoFecha = "dd/MM/yyyy";
    }

    public void setFormatoFecha(String formatoFecha)
    {
        this.formatoFecha = formatoFecha;
    }

    public String validarFecha(Object fecha)
    {
        return validarFecha(fecha, "&nbsp;");
    }

    public String validarFecha(Object fecha, String defecto)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatoFecha);
        if(fecha == null)
            return defecto;
        else
            return dateFormat.format(fecha);
    }

    public String validarFecha(String fecha)
    {
        return validarFecha(fecha, "&nbsp;");
    }

    public String validarFecha(String fecha, String defecto)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatoFecha);
        SimpleDateFormat dateFormat_bd = new SimpleDateFormat("yyyy-MM-dd");
        String valor;
        if(fecha == null)
            valor = defecto;
        else
            try
            {
                valor = dateFormat.format(dateFormat_bd.parse(fecha));
            }
            catch(ParseException e)
            {
                valor = defecto;
            }
        return valor;
    }

    public String validarDato(Object dato)
    {
        return validarDato(dato, "");
    }

    public String validarDato(Object dato, String valorDefecto)
    {
        if(dato == null || dato.equals(""))
            return valorDefecto;
        else
            return dato.toString();
    }

    public String cortarString(String dato, int largo)
    {
        if(dato == null)
            return "";
        dato = dato.trim();
        if(dato.length() > largo)
            dato = String.valueOf(String.valueOf(dato.substring(0, largo - 3))).concat("...");
        return dato;
    }

    public String validarCortarString(String dato, int largo, String temp)
    {
        if(dato == null)
            return temp;
        if(dato.length() > largo)
            dato = String.valueOf(String.valueOf(dato.substring(0, largo)));
        return dato;
    }

    public int validarNum(String num, int def)
    {
        int valor;
        try
        {
            valor = Integer.parseInt(num);
        }
        catch(Exception e)
        {
            valor = def;
        }
        return valor;
    }

    public int validarNum(int num, int def)
    {
        int valor;
        try
        {
            valor = num;
        }
        catch(Exception e)
        {
            valor = def;
        }
        return valor;
    }
    
    public int validarNum(String num)
    {
        return validarNum(num, 0);
    }
    
    public String nombreMes(String mes) {
            String nombre = "";
            switch(Integer.valueOf(mes)) {
            case 1:
            	nombre = "Enero";
                break;
            case 2:
            	nombre = "Febrero";
                break;
            case 3:
            	nombre = "Marzo";
                break;
            case 4:
            	nombre = "Abril";
                break;
            case 5:
            	nombre = "Mayo";
                break;
            case 6:
            	nombre = "Junio";
                break;
            case 7:
            	nombre = "Julio";
                break;
            case 8:
            	nombre = "Agosto";
                break;
            case 9:
            	nombre = "Septiembre";
                break;
            case 10:
            	nombre = "Octubre";
                break;
            case 11:
            	nombre = "Noviembre";
                break;
            case 12:
            	nombre = "Diciembre";
                break;
            }
            return nombre;
    }

    private String formatoFecha;
}