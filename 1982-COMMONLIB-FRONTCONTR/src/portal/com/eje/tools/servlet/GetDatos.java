package portal.com.eje.tools.servlet;

import java.sql.Connection;
import java.util.List;

import portal.com.eje.datos.Consulta;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

public class GetDatos
    implements TemplateMethodModel
{

    public GetDatos(Connection con)
    {
        this.con = con;
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        String strSql = (String)args.get(0);
        Consulta consul = new Consulta(con);
        consul.exec(strSql);
        freemarker.template.SimpleList datos = consul.getSimpleList();
        consul.close();
        return datos;
    }

    private Connection con;
}