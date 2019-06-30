package portal.com.eje.serhumano.user;

import java.io.Serializable;


public class Rut implements Serializable {

    public Rut(String rut, String dig)
    {
        try
        {
            this.rut = rut.trim();
            this.dig = dig.trim().toUpperCase();
            int largo = rut.length();
            rutId = rut;
        }
        catch(Exception e)
        {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String getRut()
    {
        return rut;
    }

    public String getDig()
    {
        return dig;
    }

    public String getRutId()
    {
        return rutId;
    }

    public String toString()
    {
        String ret = String.valueOf(String.valueOf((new StringBuilder("[rutId=")).append(rutId).append("]")));
        return ret;
    }

    private String rut;
    private String dig;
    private String rutId;
}