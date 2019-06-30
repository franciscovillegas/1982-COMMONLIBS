package organica.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;

// Referenced classes of package tools:
//            OutMessage

public class Validar
{

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
                String valor2 = "";
                if(formatoFecha.substring(0, 7) != null && "dd 'de'".equals(formatoFecha.substring(0, 7)))
                {
                    valor2 = valor.substring(7, valor.length());
                    valor = valor.substring(0, 6);
                    if(!"".equals(valor2))
                    {
                        valor2 = valor2.trim();
                        if(valor2.equals("January"))
                            valor2 = "Enero";
                        if(valor2.equals("February"))
                            valor2 = "Febrero";
                        if(valor2.equals("March"))
                            valor2 = "Marzo";
                        if(valor2.equals("April"))
                            valor2 = "Abril";
                        if(valor2.equals("May"))
                            valor2 = "Mayo";
                        if(valor2.equals("June"))
                            valor2 = "Junio";
                        if(valor2.equals("July"))
                            valor2 = "Julio";
                        if(valor2.equals("August"))
                            valor2 = "Agosto";
                        if(valor2.equals("September"))
                            valor2 = "Septiembre";
                        if(valor2.equals("October"))
                            valor2 = "Octubre";
                        if(valor2.equals("November"))
                            valor2 = "Noviembre";
                        if(valor2.equals("December"))
                            valor2 = "Diciembre";
                        valor = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(valor)))).append(" ").append(valor2)));
                    }
                }
            }
            catch(ParseException e)
            {
                valor = defecto;
                OutMessage.OutMessagePrint(e.getMessage());
            }
        return valor;
    }

    public String validarDato(Object dato)
    {
        return validarDato(dato, "&nbsp;");
    }

    public String validarDato(Object dato, String valorDefecto)
    {
        if(dato == null)
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

    private String formatoFecha;
}