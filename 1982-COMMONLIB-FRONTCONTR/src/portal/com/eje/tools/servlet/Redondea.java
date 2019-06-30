package portal.com.eje.tools.servlet;

import java.util.List;
import java.util.Vector;

import portal.com.eje.tools.Tools;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

public class Redondea
    implements TemplateMethodModel
{

    public Redondea()
    {
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        Vector valor = new Vector();
        valor = Tools.separaLista((String)args.get(0), ".");
        if(valor.size() > 1)
        {
            String entera = (String)valor.elementAt(0);
            String decimal = (String)valor.elementAt(1);
            decimal = decimal.substring(0, 3);
            return new SimpleScalar(String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(entera)))).append(".").append(decimal))));
        } else
        {
            String entera = (String)valor.elementAt(0);
            return new SimpleScalar(entera);
        }
    }
}