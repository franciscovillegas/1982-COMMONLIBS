package organica.com.eje.ges.usuario.cargo;

import java.io.Serializable;


public class VerCargo implements Serializable
{

    public VerCargo()
    {
        empresa = null;
        cargo = null;
    }

    public VerCargo(String emp, String carg)
    {
        empresa = null;
        cargo = null;
        empresa = emp;
        cargo = carg;
    }

    public String getEmpresa()
    {
        return empresa;
    }

    public String getCargo()
    {
        return cargo;
    }

    public String getFiltro(String prefijo)
    {
        if(prefijo == null || "".equals(prefijo.trim()))
            prefijo = "";
        else
            prefijo = String.valueOf(String.valueOf(prefijo)).concat(".");
        String filtro = String.valueOf(String.valueOf((new StringBuilder("(")).append(prefijo).append("empresa = '").append(getEmpresa()).append("')").append(" AND (").append(prefijo).append("cargo = '").append(getCargo()).append("')")));
        return filtro;
    }

    public String getFiltro()
    {
        return getFiltro(null);
    }

    public String toString()
    {
        String f = String.valueOf(String.valueOf((new StringBuilder("(E: ")).append(getEmpresa()).append(", C: ").append(getCargo()).append(")")));
        return f;
    }

    private String empresa;
    private String cargo;
}