package portal.com.eje.indicadores.generar;

import portal.com.eje.tools.OutMessage;
import java.util.Vector;

public class NodoJer
{

    public NodoJer(String papa, String idNodo, String descNodo, int nivNodo)
    {
        Id = null;
        padre = null;
        Desc = null;
        Nivel = 0;
        Hijo = null;
        Rama = null;
        Id = idNodo;
        padre = papa;
        Desc = descNodo;
        Nivel = nivNodo;
        Hijo = null;
    }

    public NodoJer(String papa, String idNodo)
    {
        Id = null;
        padre = null;
        Desc = null;
        Nivel = 0;
        Hijo = null;
        Rama = null;
        Id = idNodo;
        padre = papa;
        Desc = null;
        Nivel = 0;
        Hijo = new Vector();
    }

    public void Agrega_Hijo(Vector Vhijo)
    {
        Hijo = Vhijo;
    }

    public String getIdNodo()
    {
        return Id;
    }

    public String getDescNodo()
    {
        return Desc;
    }

    public Vector getHijos()
    {
        return Hijo;
    }

    public void creaRama()
    {
        Vector rama = new Vector();
        Vector subRama = new Vector();
        rama.add(Id);
        for(int x = 0; x < Hijo.size(); x++)
        {
            subRama = ((NodoJer)Hijo.get(x)).getRama();
            for(int y = 0; y < subRama.size(); y++)
                rama.add(subRama.get(y));

        }

        Rama = rama;
    }

    public Vector getRama()
    {
        return Rama;
    }

    public void Muestra(String ini)
    {
        ini = ini + "| ";
        OutMessage.OutMessagePrint(ini + "+-IdNodo: " + Id);
        for(int x = 0; x < Hijo.size(); x++)
            ((NodoJer)Hijo.get(x)).Muestra(ini);

    }

    private String Id;
    private String padre;
    private String Desc;
    private int Nivel;
    private Vector Hijo;
    private Vector Rama;
}