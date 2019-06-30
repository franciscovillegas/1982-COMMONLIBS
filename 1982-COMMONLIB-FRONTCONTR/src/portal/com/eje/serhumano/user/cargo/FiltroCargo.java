package portal.com.eje.serhumano.user.cargo;

import portal.com.eje.serhumano.user.Usuario;

// Referenced classes of package portal.com.eje.serhumano.user.cargo:
//            VerCargo

public class FiltroCargo
{

    public FiltroCargo(Usuario u)
    {
        user = u;
    }

    public static String getFiltro(String prefijo, VerCargo vus[])
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

    private Usuario user;
}