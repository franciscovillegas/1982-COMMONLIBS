package portal.com.eje.serhumano.search;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import freemarker.template.SimpleList;

public class Listas
{

    public Listas()
    {
    }

    public SimpleList getSimpleList(Connection Conexion, String Query)
    {
        Consulta lista = new Consulta(Conexion);
        lista.exec(Query);
        return lista.getSimpleList();
    }
}