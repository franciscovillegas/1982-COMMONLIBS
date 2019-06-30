package portal.com.eje.tools;


public abstract class ManagersBase
{

    public ManagersBase()
    {
        setError();
    }

    protected void setError(String msg, Throwable t)
    {
        mensajeError = msg;
        excepcion = t;
    }

    protected void setError(String msg)
    {
        setError(msg, null);
    }

    protected void setError()
    {
        setError("");
    }

    public String getError()
    {
        return mensajeError;
    }

    public Throwable getException()
    {
        return excepcion;
    }

    protected String mensajeError;
    protected Throwable excepcion;
}