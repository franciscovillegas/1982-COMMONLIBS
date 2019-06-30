package organica.com.eje.ges.Buscar;

import organica.datos.Consulta;
import freemarker.template.SimpleList;
import java.sql.Connection;

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