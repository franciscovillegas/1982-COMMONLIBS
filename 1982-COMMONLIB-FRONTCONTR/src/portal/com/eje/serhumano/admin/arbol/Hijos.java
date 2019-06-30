package portal.com.eje.serhumano.admin.arbol;

import java.sql.Connection;
import java.util.Vector;

import portal.com.eje.datos.Consulta;

// Referenced classes of package portal.com.eje.serhumano.admin.arbol:
//            Nodo

public class Hijos
{

    public Hijos()
    {
        Vector vecHijo = new Vector();
    }

    public Vector CreaHijo(String padre, Connection Conexion, int niv, String empresa)
    {
        String consu = null;
        String id = null;
        String desc = null;
        int nivel = 0;
        Vector ElHijo = new Vector();
        Consulta hijos = new Consulta(Conexion);
        consu = String.valueOf(String.valueOf((new StringBuilder("Select DISTINCT empresa, hijo, descr, padre from VIEW_arbol where padre='")).append(padre).append("' and empresa='").append(empresa).append("' ORDER BY descr")));
        hijos.exec(consu);
        Nodo hijo;
        for(; hijos.next(); ElHijo.addElement(hijo))
        {
            id = hijos.getString("hijo");
            desc = hijos.getString("descr");
            nivel = niv + 1;
            hijo = new Nodo(padre, id, desc, empresa);
            hijo.Agrega_Hijo(CreaHijo(hijos.getString("hijo"), Conexion, nivel, empresa));
        }

        hijos.close();
        return ElHijo;
    }
}