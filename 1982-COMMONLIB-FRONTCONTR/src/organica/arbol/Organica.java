package organica.arbol;

import java.sql.Connection;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;

// Referenced classes of package arbol:
//            creaArbol, Nodo

public class Organica
{

    public Organica()
    {
    }

    public static void main(String args[])
    {
    	String control_proceso = args[0];
    	Vector vecHijo = new Vector();
        int nivel = 0;
        String desc = "Holding";
        String id = "SE";
        String padre = "X";
        String flag_organica="sociedad";
        connMgr = DBConnectionManager.getInstance();
        Connection Conexion = connMgr.getConnection("portal");
        arbol = new creaArbol(Conexion);
        proper = ResourceBundle.getBundle("DataFolder");
        try
        {
            flag_organica = proper.getString("tipo.organica");
            empresa = proper.getString("empresa.name");
            id = proper.getString("id_empresa.name");
            desc = proper.getString("desc_empresa.name");
        }
        catch(MissingResourceException e)
        {
            OutMessage.OutMessagePrint("Excepcion : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
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

        Nodo tmp;
        if( control_proceso.equals("app_adm_cp") || control_proceso.equals("app_adm_gp") ) {
            tmp = MuestraOrganica(Conexion,"sociedad",nodoSuperPadre); 
        }
        else {
        	if( flag_organica.equals("sociedad") )
                tmp = MuestraOrganica(Conexion,"sociedad",nodoSuperPadre); 
        	else
                tmp = MuestraOrganica(Conexion,"corporativo",nodoSuperPadre); 
        }

        Usuario.SuperNodo = tmp;
        connMgr.freeConnection("portal", Conexion);
        connMgr.release();
        OutMessage.OutMessagePrint("Fin Organica ------ oo ------");
    }

    private static Nodo MuestraOrganica(Connection Conexion, String tipo_organica,Nodo nodoSuperPadre) {
        Consulta consul = new Consulta(Conexion);
        String sql = "SELECT empresa, descrip, padre_unidad, padre_empresa FROM eje_ges_empresa WHERE (NOT (padre_empresa IS NULL)) ORDER BY padre_empresa, padre_unidad";
        OutMessage.OutMessagePrint("sql E/U : ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        Nodo nodoBuscado = null;
        boolean flag = false;
    	if( tipo_organica.equals("corporativo") ) {  //corporativa 
            while(consul.next()) {
            	String padreEmp = consul.getString("padre_empresa");
                String padreUnid = consul.getString("padre_unidad");
                Nodo nodoEmp = new Nodo(padreUnid, consul.getString("empresa"), consul.getString("descrip"), padreEmp);
                Nodo nodoHijoEmp = new Nodo();
                nodoEmp.tipoNodo = "E";
                nodoEmp.Agrega_Hijo(arbol.getNodo(nodoEmp.getIdNodo(), nodoEmp.getDescNodo()).getHijos());
                try {
                    nodoHijoEmp = (Nodo)nodoEmp.getHijos().get(0);
                    flag = true;
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    flag = false;
                }
                if(flag) {
                    nodoEmp = nodoHijoEmp;
                    if(padreUnid == null)
                        nodoBuscado = nodoSuperPadre.BuscarEmpresa(padreEmp);
                    else
                        nodoBuscado = nodoSuperPadre.BuscarNodo(padreUnid, padreEmp);
                    if(nodoBuscado != null)
                        nodoBuscado.getHijos().add(0, nodoEmp);
                    else
                        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder(" El nodo (E:")).append(padreEmp).append(", U:").append(padreUnid).append(") no existe"))));
                }
            }
    	}
    	else {
            while(consul.next()) {
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
    	}
        consul.close();
        return nodoSuperPadre;
    }
    
    private static IDBConnectionManager connMgr;
    private static ResourceBundle proper;
    private static String empresa = null;
    private static creaArbol arbol;

}