package portal.com.eje.usuario.unidad;


public class VerUnidad
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
            prefijo = prefijo + ".";
        String filtro = "(" + prefijo + "empresa = '" + getEmpresa() + "')" + " AND (" + prefijo + "unidad = '" + getUnidad() + "')" + " AND (" + prefijo + "tipo = '" + getQueVer() + "')";
        return filtro;
    }

    public String getFiltro()
    {
        return getFiltro(null);
    }

    public String toString()
    {
        String f = "(E: " + getEmpresa() + ", U: " + getUnidad() + ", T: " + getQueVer() + ")";
        return f;
    }

    public static final String VER_UNIDAD = "U";
    public static final String VER_RAMA = "R";
    private String empresa;
    private String unidad;
    private String ver;
}