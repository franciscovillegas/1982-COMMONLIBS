package portal.com.eje.serhumano.search;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleList;

public class EjecutaBusquedaAlfabeticaOrganica
{

    public EjecutaBusquedaAlfabeticaOrganica(Connection Conexion, String Query)
    {
        Consulta Search = new Consulta(Conexion);
        Search.exec(Query);
        OutMessage.OutMessagePrint("buscar --> " + Query);
        simplelist = Search.getSimpleList();
        Search.close();
    }

    public SimpleList EjecutaBusquedaAlfabeticaOrganica(Connection Conexion, String Query)
    {
        simplelist = new SimpleList();
        Consulta Search = new Consulta(Conexion);
        Search.exec(Query);
        OutMessage.OutMessagePrint("buscar --> ".concat(String.valueOf(String.valueOf(Query))));
        simplelist = Search.getSimpleList();
        Search.close();
        return simplelist;
    }

    public SimpleList getSimpleList()
    {
        return simplelist;
    }

    private SimpleList simplelist;
}