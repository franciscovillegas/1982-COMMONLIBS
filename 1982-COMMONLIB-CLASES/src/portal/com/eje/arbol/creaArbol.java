package portal.com.eje.arbol;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import java.sql.Connection;
import java.util.Vector;

// Referenced classes of package portal.com.eje.arbol:
//            Nodo, Hijos

public class creaArbol
{

    public creaArbol(Connection conn)
    {
        creaArbol _tmp = this;
        Conexion = conn;
    }

    public Nodo getNodo(String empresa, String descripcion)
    {
        Empresa = new Nodo("R", empresa, descripcion, empresa);
        Empresa.tipoNodo = "E";
        String sql = null;
        String id = null;
        String padre = null;
        String desc = "";
        String IDEmpresa = empresa;
        int nivel = 0;
        Hijos vhijos = new Hijos();
        Consulta hijos = new Consulta(Conexion);
        Consulta hijos2 = new Consulta(Conexion);
        Vector NE = new Vector();
        sql = "SELECT DISTINCT empresa, hijo, descr, padre FROM VIEW_arbol WHERE (padre = '0') AND empresa = '" + IDEmpresa + "' ORDER BY descr";
        OutMessage.OutMessagePrint("sql nodo padre  " + sql);
        hijos.exec(sql);
        Nodo nodoPadre;
        for(; hijos.next(); NE.add(nodoPadre))
        {
            Vector vecHijo = new Vector();
            id = hijos.getString("hijo");
            desc = hijos.getString("descr");
            padre = "0";
            nodoPadre = new Nodo(padre, id, desc, IDEmpresa);
            sql = "Select DISTINCT empresa, hijo, descr, padre from VIEW_arbol where padre <> '0' and padre='" + id + "' and empresa='" + IDEmpresa + "' ORDER BY descr";
            OutMessage.OutMessagePrint("sql 1 nivel " + sql);
            hijos2.exec(sql);
            Nodo hijo;
            for(; hijos2.next(); vecHijo.add(hijo))
            {
                id = hijos2.getString("hijo");
                desc = hijos2.getString("descr");
                padre = hijos2.getString("padre");
                hijo = new Nodo(padre, id, desc, IDEmpresa);
            }

            nivel++;
            for(int x = 0; x < vecHijo.size(); x++)
            {
                Vector VEC = vhijos.CreaHijo(((Nodo)vecHijo.get(x)).getIdNodo(), Conexion, nivel, IDEmpresa);
                ((Nodo)vecHijo.get(x)).Agrega_Hijo(VEC);
            }

            nodoPadre.Agrega_Hijo(vecHijo);
        }

        Empresa.Agrega_Hijo(NE);
        hijos.close();
        hijos2.close();
        OutMessage.OutMessagePrint(" ---------------   :) FIN arbol");
        return Empresa;
    }

    private static Connection Conexion;
    private static Nodo Empresa;
}