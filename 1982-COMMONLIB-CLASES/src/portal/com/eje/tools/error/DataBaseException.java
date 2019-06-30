package portal.com.eje.tools.error;


// Referenced classes of package portal.com.eje.tools.error:
//            GeneralException

public class DataBaseException extends GeneralException
{

    public DataBaseException(int error, String s)
    {
        super(error, s);
    }

    public static final int ERROR_SIN_CONTEXTO = 1;
    public static final int ERROR_DATASOURCE = 2;
    public static final int ERROR_CONEXION = 3;

    static 
    {
        errorSigla = "DB";
        errorDescs = new String[4];
        errorDescs[1] = "Contexto no disponible.";
        errorDescs[2] = "DataSource no disponible.";
        errorDescs[3] = "Problemas al obtener conexi\363n.";
    }
}