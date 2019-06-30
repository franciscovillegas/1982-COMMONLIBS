package portal.com.eje.serhumano.admin.arbol;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.user.UsuarioSuperNodo;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.serhumano.admin.arbol:
//            Hijos, creaArbol, Nodo

public class Organica
{

    public Organica()
    {
    }

    public static void main(String args[])
    {
        Vector vecHijo = new Vector();
        Hijos vhijos = new Hijos();
        int nivel = 0;
        String desc = "Corporaci\363n";
        String id = "SE";
        String padre = "X";
        connMgr = DBConnectionManager.getInstance();
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        arbol = new creaArbol(Conexion);
        proper = ResourceBundle.getBundle("db");
        try
        {
            empresa = proper.getString("empresa.name");
            id = proper.getString("id_empresa.name");
            desc = proper.getString("desc_empresa.name");
        }
        catch(MissingResourceException e)
        {
            OutMessage.OutMessagePrint("Exepcion : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        OutMessage.OutMessagePrint("Inicio Organica");
        String sql;
        if(empresa != null)
        {
            if(!empresa.equals(""))
                vecHijo.add(arbol.getNodo(id, empresa));
        } else
        {
            Consulta empresas = new Consulta(Conexion);
            sql = "Select * from eje_ges_empresa where (padre_empresa IS NULL) ";
            OutMessage.OutMessagePrint("sql : ".concat(String.valueOf(String.valueOf(sql))));
            empresas.exec(sql);
            for(; empresas.next(); vecHijo.add(arbol.getNodo(empresas.getString("empresa"), empresas.getString("descrip"))));
            empresas.close();
        }
        Nodo nodoSuperPadre = new Nodo(padre, id, desc);
        nodoSuperPadre.tipoNodo = "R";
        nodoSuperPadre.Agrega_Hijo(vecHijo);
        Consulta consul = new Consulta(Conexion);
        sql = "SELECT empresa, descrip, padre_unidad, padre_empresa FROM eje_ges_empresa WHERE (NOT (padre_empresa IS NULL)) ORDER BY padre_empresa, padre_unidad";
        OutMessage.OutMessagePrint("sql E/U : ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        Nodo nodoBuscado = null;
        while(consul.next()) 
        {
            String padreEmp = consul.getString("padre_empresa");
            String padreUnid = consul.getString("padre_unidad");
            Nodo nodoEmp = new Nodo(padreUnid, consul.getString("empresa"), consul.getString("descrip"), padreEmp);
            nodoEmp.tipoNodo = "E";
            nodoEmp.Agrega_Hijo(arbol.getNodo(nodoEmp.getIdNodo(), nodoEmp.getDescNodo()).getHijos());
            if(padreUnid == null)
                nodoBuscado = nodoSuperPadre.BuscarEmpresa(padreEmp);
            else
                nodoBuscado = nodoSuperPadre.BuscarNodo(padreUnid, padreEmp);
            if(nodoBuscado != null)
                nodoBuscado.getHijos().add(0, nodoEmp);
            else
                OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder(" El nodo (E:")).append(padreEmp).append(", U:").append(padreUnid).append(") no existe"))));
        }
        consul.close();
        UsuarioSuperNodo.setSuperNodo(nodoSuperPadre);
        connMgr.freeConnection("portal", Conexion);
        connMgr.release();
        OutMessage.OutMessagePrint("Fin Organica ------ oo ------");
    }

    private static IDBConnectionManager connMgr;
    private static ResourceBundle proper;
    private static String empresa = null;
    private static creaArbol arbol;

}