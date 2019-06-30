package portal.com.eje.tools;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

// Referenced classes of package portal.com.eje.tools:
//            Validar, Formatear

public class FechaManager
{

    public FechaManager()
    {
    }

    public static Date toSqlDate(String dia, String mes, String agno)
    {
        Date fecha = null;
        try
        {
            int d = Integer.parseInt(dia);
            int m = Integer.parseInt(mes);
            int a = Integer.parseInt(agno);
            GregorianCalendar gc = new GregorianCalendar(a, m - 1, d);
            fecha = new Date(gc.getTime().getTime());
        }
        catch(Exception e)
        {
            fecha = null;
        }
        return fecha;
    }

    public static java.util.Date toUtilDate(String fecha, String formato)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
        java.util.Date dFecha = null;
        try
        {
            dFecha = dateFormat.parse(fecha);
        }
        catch(Exception e)
        {
            dFecha = null;
        }
        return dFecha;
    }

    public static Date toSqlDate(String fecha, String formato)
    {
        Date dFecha = null;
        try
        {
            dFecha = new Date(toUtilDate(fecha, formato).getTime());
        }
        catch(Exception e)
        {
            dFecha = null;
        }
        return dFecha;
    }

    public static int toInt(java.util.Date fecha)
    {
        return (new Validar()).validarNum(Formatear.fecha("yyyyMMdd", fecha), 0);
    }
}