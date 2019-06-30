package portal.com.eje.usuario.cargo;

import portal.com.eje.usuario.Usuario;

// Referenced classes of package portal.com.eje.usuario.cargo:
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
        FiltroCargo _tmp = this;
        return getFiltro(prefijo, user.getCargosVer());
    }

    public String getFiltro()
    {
        return getFiltro(null);
    }

    private Usuario user;
}