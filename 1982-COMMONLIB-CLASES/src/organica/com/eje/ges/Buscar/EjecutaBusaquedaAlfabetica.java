package organica.com.eje.ges.Buscar;

import java.sql.Connection;

import organica.datos.Consulta;
import organica.tools.OutMessage;

import freemarker.template.SimpleList;

public class EjecutaBusaquedaAlfabetica
{

    public EjecutaBusaquedaAlfabetica(Connection Conexion, String Query)
    {
        Consulta Search = new Consulta(Conexion);
        Search.exec(Query);
        OutMessage.OutMessagePrint("buscar --> ".concat(String.valueOf(String.valueOf(Query))));
        simplelist = Search.getSimpleList();
        Search.close();
    }

    public SimpleList getSimpleList()
    {
        return simplelist;
    }

    private SimpleList simplelist;
}