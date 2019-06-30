package portal.com.eje.usuario.cargo;


public class VerCargo
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
            prefijo = prefijo + ".";
        String filtro = "(" + prefijo + "empresa = '" + getEmpresa() + "')" + " AND (" + prefijo + "cargo = '" + getCargo() + "')";
        return filtro;
    }

    public String getFiltro()
    {
        return getFiltro(null);
    }

    public String toString()
    {
        String f = "(E: " + getEmpresa() + ", C: " + getCargo() + ")";
        return f;
    }

    private String empresa;
    private String cargo;
}