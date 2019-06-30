package organica.bci.traspaso.tools;

import organica.tools.Tools;
import organica.tools.Validar;

public class Dato
{

    public Dato()
    {
        formatoFecha = "MM/dd/yyyy";
    }

    public Dato(String formatoFechaBD)
    {
        formatoFecha = formatoFechaBD;
    }

    public String objectToDato(Object dato)
    {
        String datoRet = "null";
        if(dato != null)
            datoRet = (String)dato;
        return datoRet;
    }

    public String objectToDatoComillas(Object dato)
    {
        String datoRet = "null";
        if(dato != null)
            datoRet = String.valueOf(String.valueOf((new StringBuilder("'")).append(Tools.remplaza(dato.toString(), "'", "''")).append("'")));
        return datoRet;
    }

    public String fechaToDatoComillas(Object fecha)
    {
        String datoRet = "null";
        Validar valida = new Validar();
        valida.setFormatoFecha(formatoFecha);
        if(fecha != null)
            datoRet = String.valueOf(String.valueOf((new StringBuilder("'")).append(valida.validarFecha(fecha)).append("'")));
        return datoRet;
    }

    static final String DATO_NULL = "null";
    private String formatoFecha;
}