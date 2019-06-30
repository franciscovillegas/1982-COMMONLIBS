package portal.com.eje.orgpeople.manager;


public class datosColaborador
{

    public datosColaborador()
    {
        strRut = "";
        strNombre = "";
        strApellidoPaterno = "";
        strApellidoMaterno = "";
        strUnidad = "";
        strRol = "";
        intNivel = 0;
    }

    public int getNivel()
    {
        return intNivel;
    }

    public String getRut()
    {
        return strRut;
    }

    public String getNombre()
    {
        return strNombre;
    }

    public String getApellidoPaterno()
    {
        return strApellidoPaterno;
    }

    public String getApellidoMaterno()
    {
        return strApellidoMaterno;
    }

    public String getUnidad()
    {
        return strUnidad;
    }

    public String getRol()
    {
        return strRol;
    }

    public void setNivel(int intNivel)
    {
        this.intNivel = intNivel;
    }

    public void setRut(String strRut)
    {
        this.strRut = strRut;
    }

    public void setNombre(String strNombre)
    {
        this.strNombre = strNombre;
    }

    public void setApellidoPaterno(String strApellidoPaterno)
    {
        this.strApellidoPaterno = strApellidoPaterno;
    }

    public void setApellidoMaterno(String strApellidoMaterno)
    {
        this.strApellidoMaterno = strApellidoMaterno;
    }

    public void setUnidad(String strUnidad)
    {
        this.strUnidad = strUnidad;
    }

    public void setRol(String strRol)
    {
        this.strRol = strRol;
    }

    private String strRut;
    private String strNombre;
    private String strApellidoPaterno;
    private String strApellidoMaterno;
    private String strUnidad;
    private String strRol;
    private int intNivel;
}