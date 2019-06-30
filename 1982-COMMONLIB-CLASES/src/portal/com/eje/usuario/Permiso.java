package portal.com.eje.usuario;

import java.util.Vector;

public class Permiso
{

    public Permiso(String accesoId)
    {
        accId = accesoId;
        accTipo = "N";
        vecRuts = null;
    }

    public Permiso(String accesoId, String accesoTipo, String accesoPuedeVer)
    {
        accId = accesoId;
        accTipo = accesoTipo;
        if(esTipoRestringido())
        {
            accPudeVer = accesoPuedeVer.equals("S");
            vecRuts = new Vector();
        }
    }

    public void agregarRut(String rut)
    {
        vecRuts.add(rut);
    }

    public int cantidadRut()
    {
        return vecRuts.size();
    }

    public String getPuedeVer()
    {
        if(accPudeVer)
        {
            Permiso _tmp = this;
            return "S";
        } else
        {
            Permiso _tmp1 = this;
            return "N";
        }
    }

    public String getId()
    {
        return accId;
    }

    public String getRut(int cual)
    {
        return (String)vecRuts.get(cual);
    }

    public Vector getRutsVector()
    {
        return vecRuts;
    }

    public boolean esTipoNormal()
    {
        return accTipo.equals("N");
    }

    public boolean esTipoRestringido()
    {
        return accTipo.equals("R");
    }

    public String toString()
    {
        String resul = "AccesoId: " + accId + "\tTipo: " + accTipo;
        if(esTipoRestringido())
            resul = resul + "\tPuedeVer: " + accPudeVer + "\nRuts: " + vecRuts.toString();
        return resul;
    }

    public boolean esRutRestringido(String strRut)
    {
        boolean resul = false;
        int x = 0;
        do
        {
            if(x >= cantidadRut())
                break;
            System.out.println("------->RUT Restringido: " + getRut(x));
            if(getRut(x).equals(strRut))
            {
                resul = true;
                break;
            }
            x++;
        } while(true);
        System.out.println("------->RUT Restringido?: " + resul);
        return resul;
    }

    public boolean puedeVerRut(String strRut)
    {
        boolean resul;
        if(esRutRestringido(strRut))
            resul = accPudeVer;
        else
            resul = !accPudeVer;
        return resul;
    }

    public static final String TIPO_NORMAL = "N";
    public static final String TIPO_RESTRINGE_RUT = "R";
    public static final String VER_SOLO_LISTA_RUT = "S";
    public static final String VER_TODO_MENOS_LISTA_RUT = "N";
    private String accId;
    private String accTipo;
    private boolean accPudeVer;
    private Vector vecRuts;
}