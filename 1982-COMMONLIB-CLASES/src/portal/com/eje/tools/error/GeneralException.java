package portal.com.eje.tools.error;


public abstract class GeneralException extends Exception
{

    protected static String getErrorDesc(int error)
    {
        String descr = "No definido.";
        if(error > 0 && error <= errorDescs.length)
            descr = errorDescs[error];
        return "[" + errorSigla + "_" + error + "] " + descr;
    }

    public GeneralException(int error, String s)
    {
        super(getErrorDesc(error) + " \n\t " + s);
        errorNum = 0;
        errorNum = error;
        fillInStackTrace();
    }

    public int getErrorNum()
    {
        return errorNum;
    }

    public String getErrorDesc()
    {
        return getErrorDesc(errorNum);
    }

    public static final int SIN_ERROR = 0;
    protected static String errorDescs[];
    protected static String errorSigla = "";
    protected int errorNum;

}