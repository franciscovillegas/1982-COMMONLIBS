package portal.com.eje.tools;


// Referenced classes of package portal.com.eje.tools:
//            Tools, Validar

public class Dato
{

    public Dato()
    {
        formatoFecha = "dd-MM-yyyy";
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
            datoRet = "'" + Tools.remplaza(dato.toString(), "'", "''") + "'";
        return datoRet;
    }

    public String fechaToDato(Object fecha)
    {
        String datoRet = "null";
        Validar valida = new Validar();
        valida.setFormatoFecha(formatoFecha);
        if(fecha != null)
            datoRet = valida.validarFecha(fecha);
        return datoRet;
    }

    public String fechaToDatoComillas(Object fecha)
    {
        String datoRet = "null";
        Validar valida = new Validar();
        valida.setFormatoFecha(formatoFecha);
        if(fecha != null)
            datoRet = "'" + valida.validarFecha(fecha, null) + "'";
        return datoRet;
    }

    static final String DATO_NULL = "null";
    private String formatoFecha;
}