package portal.com.eje.usuario;

import java.util.Vector;

// Referenced classes of package portal.com.eje.usuario:
//            Permiso

public class ListaPermisos
{

    public ListaPermisos()
    {
        vecPermisos = null;
        vecPermisos = new Vector();
    }

    public void agregarPermiso(Permiso permiso)
    {
        vecPermisos.add(permiso);
    }

    public int cantidadPermisos()
    {
        return vecPermisos.size();
    }

    public Permiso getPermiso(int cual)
    {
        return (Permiso)vecPermisos.get(cual);
    }

    public Permiso getPermiso(String accesoId)
    {
        Permiso permiso = null;
        int x = 0;
        do
        {
            if(x >= cantidadPermisos())
                break;
            if(getPermiso(x).getId().equals(accesoId))
            {
                permiso = getPermiso(x);
                break;
            }
            x++;
        } while(true);
        return permiso;
    }

    public boolean existePermiso(String accesoId)
    {
        boolean existe = getPermiso(accesoId) != null;
        return existe;
    }

    public Vector getPermisosVector()
    {
        return vecPermisos;
    }

    public String toString()
    {
        String resul = "";
        for(int x = 0; x < cantidadPermisos(); x++)
            resul = resul + "\n" + getPermiso(x).toString();

        return resul;
    }

    private Vector vecPermisos;
}