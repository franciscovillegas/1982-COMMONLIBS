package portal.com.eje.tools.error;


// Referenced classes of package portal.com.eje.tools.error:
//            GeneralException

public class MailSessionException extends GeneralException
{

    public MailSessionException(int error, String s)
    {
        super(error, s);
    }

    public static final int ERROR_SIN_CONTEXTO = 1;
    public static final int ERROR_MAILSESSION = 2;

    static 
    {
        errorSigla = "MS";
        errorDescs = new String[4];
        errorDescs[1] = "Contexto no disponible.";
        errorDescs[2] = "MailSession no disponible.";
    }
}