package portal.com.eje.tools.servlet;

import java.util.List;

import portal.com.eje.tools.Tools;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

public class FormatoNumero
    implements TemplateMethodModel
{

    public FormatoNumero()
    {
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        if((String)args.get(0) != "&nbsp") {
        	String nota = (String)args.get(0);
        	String[] partes = nota.split("\\.");
            return new SimpleScalar(Tools.setFormatNumber(partes[0]));
        } else {
            return new SimpleScalar("&nbsp");
        }
    }
}