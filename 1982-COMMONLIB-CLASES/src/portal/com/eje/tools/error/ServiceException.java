package portal.com.eje.tools.error;


// Referenced classes of package portal.com.eje.tools.error:
//            GeneralException

public class ServiceException extends GeneralException
{

    public ServiceException(int error, String s)
    {
        super(error, s);
    }

    public static final int ERROR_SERV_NO_DISP = 1;
    public static final int ERROR_CONEXION = 2;
    public static final int ERROR_EJECUCION = 3;
    public static final int ERROR_DATOS_NO_DISP = 4;
    public static final int ERROR_EJECUCION_PA = 5;

    static 
    {
        errorSigla = "SRV";
        errorDescs = new String[6];
        errorDescs[1] = "Servicio no disponible.";
        errorDescs[2] = "Problemas con la conexi\363n a la Base de Datos.";
        errorDescs[3] = "Problemas al ejecutar servicio.";
        errorDescs[4] = "Datos no disponibles.";
        errorDescs[5] = "Problemas al ejecutar Procedimiento Almacenado";
    }
}