package portal.com.eje.tools.error;

import java.io.Serializable;

public class GeneralError
    implements Serializable
{

    public GeneralError()
    {
        this(0, "OK.");
    }

    public GeneralError(int cod, String descrip)
    {
        codigo = cod;
        descripcion = descrip;
    }

    public GeneralError(int cod)
    {
        this(cod, "Error n\272 " + cod);
    }

    public int getCodigo()
    {
        return codigo;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public String toString()
    {
        return "[" + codigo + "] " + descripcion;
    }

    public boolean isCodigoError(int cod)
    {
        return codigo == cod;
    }

    public boolean isOk()
    {
        return isCodigoError(0);
    }

    private static final int COD_OK = 0;
    private int codigo;
    private String descripcion;
}