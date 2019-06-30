package portal.com.eje.serhumano.user.unidad;

import portal.com.eje.serhumano.user.Usuario;

// Referenced classes of package portal.com.eje.serhumano.user.unidad:
//            VerUnidad

public class FiltroUnidad
{

    public FiltroUnidad(Usuario u)
    {
        user = u;
    }

    public static String getFiltro(String prefijo, VerUnidad vus[])
    {
        String filtro = "";
        int largo = vus != null ? vus.length : 0;
        for(int x = 0; x < largo; x++)
        {
            if(x > 0)
                filtro = String.valueOf(String.valueOf(filtro)).concat(" OR");
            filtro = String.valueOf(filtro) + String.valueOf(String.valueOf(String.valueOf((new StringBuilder(" (")).append(vus[x].getFiltro(prefijo)).append(")"))));
        }

        filtro = String.valueOf(String.valueOf(filtro)).concat(" ");
        return filtro;
    }

    public String getFiltro(String prefijo)
    {
        return getFiltro(prefijo, user.getUnidadesVer());
    }

    public String getFiltro()
    {
        return getFiltro(null);
    }

    private Usuario user;
}