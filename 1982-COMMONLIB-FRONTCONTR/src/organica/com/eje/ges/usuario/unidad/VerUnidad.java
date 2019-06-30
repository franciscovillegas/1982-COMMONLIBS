package organica.com.eje.ges.usuario.unidad;

import java.io.Serializable;


public class VerUnidad implements Serializable
{

    public VerUnidad()
    {
        empresa = null;
        unidad = null;
        ver = null;
    }

    public VerUnidad(String emp, String uni, String ver)
    {
        empresa = null;
        unidad = null;
        this.ver = null;
        empresa = emp;
        unidad = uni;
        this.ver = ver;
    }

    public VerUnidad(String emp, String uni)
    {
        empresa = null;
        unidad = null;
        ver = null;
        empresa = emp;
        unidad = uni;
        ver = "R";
    }

    public String getEmpresa()
    {
        return empresa;
    }

    public String getUnidad()
    {
        return unidad;
    }

    public String getQueVer()
    {
        return ver;
    }

    public String getFiltro(String prefijo)
    {
        if(prefijo == null || "".equals(prefijo.trim()))
            prefijo = "";
        else
            prefijo = String.valueOf(String.valueOf(prefijo)).concat(".");
        String filtro = String.valueOf(String.valueOf((new StringBuilder("(")).append(prefijo).append("empresa = '").append(getEmpresa()).append("')").append(" AND (").append(prefijo).append("unidad = '").append(getUnidad()).append("')").append(" AND (").append(prefijo).append("tipo = '").append(getQueVer()).append("')")));
        return filtro;
    }

    public String getFiltro()
    {
        return getFiltro(null);
    }

    public String toString()
    {
        String f = String.valueOf(String.valueOf((new StringBuilder("(E: ")).append(getEmpresa()).append(", U: ").append(getUnidad()).append(", T: ").append(getQueVer()).append(")")));
        return f;
    }

    public static final String VER_UNIDAD = "U";
    public static final String VER_RAMA = "R";
    private String empresa;
    private String unidad;
    private String ver;
}