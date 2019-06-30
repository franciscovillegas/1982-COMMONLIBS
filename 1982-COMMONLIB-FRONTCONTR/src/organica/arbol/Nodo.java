package organica.arbol;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

import java.io.Serializable;
import java.util.Vector;
import organica.tools.OutMessage;

public class Nodo implements Serializable
{

    public static Nodo getNodoRaiz(String descrip)
    {
        Nodo n = new Nodo("X", "Raiz", descrip);
        n.tipoNodo = "R";
        return n;
    }

    public Nodo()
    {
        IdEmpresa = null;
        Nivel = 0;
        Rama = null;
        Id = null;
        padre = null;
        Desc = null;
        Nivel = 0;
        Hijo = new Vector();
        tipoNodo = null;
    }

    public Nodo(String papa, String idNodo, String descNodo, int nivNodo)
    {
        IdEmpresa = null;
        Nivel = 0;
        Rama = null;
        Id = idNodo;
        padre = papa;
        Desc = descNodo;
        Nivel = nivNodo;
        Hijo = new Vector();
        tipoNodo = "U";
    }

    public Nodo(String papa, String idNodo, String desc, String empresa)
    {
        Rama = null;
        Id = idNodo;
        IdEmpresa = empresa;
        padre = papa;
        Desc = desc;
        Nivel = 0;
        Hijo = new Vector();
        tipoNodo = "U";
    }

    public Nodo(String papa, String idNodo, String desc)
    {
        IdEmpresa = null;
        Rama = null;
        Id = idNodo;
        padre = papa;
        Desc = desc;
        Nivel = 0;
        Hijo = new Vector();
        tipoNodo = "U";
    }

    public void Agrega_Hijo(Vector Vhijo)
    {
        Hijo = Vhijo;
    }

    public String getIdNodo()
    {
        return Id;
    }

    public String getEmpresa()
    {
        return IdEmpresa;
    }

    public String getDescNodo()
    {
        return Desc;
    }

    public Vector getHijos()
    {
        return Hijo;
    }

    public void Muestra(String ini)
    {
        ini = String.valueOf(String.valueOf(ini)).concat("| ");
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(ini)))).append("+-IdNodo: ").append(Id).append(" ").append(Desc))));
        for(int x = 0; x < Hijo.size(); x++)
            ((Nodo)Hijo.get(x)).Muestra(ini);

    }

    public Nodo BuscarNodo(String IDnodo, String codEmpresa)
    {
        Nodo ret = null;
        if(Id.equals(IDnodo) && IdEmpresa.equals(codEmpresa))
        {
            ret = this;
        } else
        {
            int x = 0;
            do
            {
                if(x >= Hijo.size())
                    break;
                ret = ((Nodo)Hijo.get(x)).BuscarNodo(IDnodo, codEmpresa);
                if(ret != null)
                    break;
                x++;
            } while(true);
        }
        return ret;
    }

    public Nodo BuscarEmpresa(String IdEmpresa)
    {
        Nodo ret = null;
        if(Id.equals(IdEmpresa) && "E".equals(tipoNodo))
        {
            ret = this;
        } else
        {
            int x = 0;
            do
            {
                if(x >= Hijo.size())
                    break;
                ret = ((Nodo)Hijo.get(x)).BuscarEmpresa(IdEmpresa);
                if(ret != null)
                    break;
                x++;
            } while(true);
        }
        return ret;
    }

    public SimpleHash getSimpleHasch()
    {
        SimpleHash nod = new SimpleHash();
        nod.put("Id", Id);
        nod.put("No", Desc);
        nod.put("Pa", padre);
        nod.put("Em", IdEmpresa);
        nod.put("Ti", tipoNodo);
        SimpleList Lista = new SimpleList();
        int largo = Hijo.size();
        for(int x = 0; x < largo; x++)
        {
            SimpleHash nod_hijo = ((Nodo)Hijo.get(x)).getSimpleHasch();
            nod_hijo.put("NU", x != largo - 1);
            Lista.add(nod_hijo);
        }

        nod.put("Li", Lista);
        return nod;
    }

    public Nodo getNodoSinHijos()
    {
        Nodo n = new Nodo(padre, Id, Desc, IdEmpresa);
        n.tipoNodo = tipoNodo;
        return n;
    }

    public Vector getRama(String IDnodo, String codEmpresa)
    {
        Vector ret = null;
        boolean yoSoy = false;
        if(Id.equals(IDnodo))
            if(codEmpresa != null)
                yoSoy = IdEmpresa.equals(codEmpresa);
            else
                yoSoy = true;
        if(yoSoy)
        {
            ret = new Vector();
            ret.add(0, this);
        } else
        {
            int x = 0;
            do
            {
                if(x >= Hijo.size())
                    break;
                ret = ((Nodo)Hijo.get(x)).getRama(IDnodo, codEmpresa);
                if(ret != null)
                {
                    ret.add(0, this);
                    break;
                }
                x++;
            } while(true);
        }
        return ret;
    }

    public static final String NODO_EMPRESA = "E";
    public static final String NODO_UNIDAD = "U";
    public static final String NODO_RAIZ = "R";
    private String Id;
    private String padre;
    private String Desc;
    private String IdEmpresa;
    private int Nivel;
    private Vector Hijo;
    private Vector Rama;
    public String tipoNodo;
}