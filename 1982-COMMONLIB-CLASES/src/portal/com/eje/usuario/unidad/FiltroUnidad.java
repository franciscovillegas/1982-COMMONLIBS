package portal.com.eje.usuario.unidad;

import portal.com.eje.usuario.Usuario;

// Referenced classes of package portal.com.eje.usuario.unidad:
//            VerUnidad

public class FiltroUnidad
{

    public FiltroUnidad(Usuario u)
    {
        user = u;
    }

    public VerUnidad getUnidadRel()
    {
        VerUnidad vu = null;
        vu = user.getUnidadesRel()[0];
        return vu;
    }

    public static String getFiltro(String prefijo, VerUnidad vus[])
    {
        String filtro = "";
        int largo = vus == null ? 0 : vus.length;
        for(int x = 0; x < largo; x++)
        {
            if(x > 0)
                filtro = filtro + " OR";
            filtro = filtro + " (" + vus[x].getFiltro(prefijo) + ")";
        }

        filtro = filtro + " ";
        return filtro;
    }

    public String getFiltro(String prefijo)
    {
        FiltroUnidad _tmp = this;
        return getFiltro(prefijo, user.getUnidadesVer());
    }

    public String getFiltro()
    {
        return getFiltro(null);
    }

    private Usuario user;
}