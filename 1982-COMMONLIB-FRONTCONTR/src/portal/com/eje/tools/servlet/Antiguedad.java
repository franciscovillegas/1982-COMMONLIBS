package portal.com.eje.tools.servlet;

import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

public class Antiguedad
    implements TemplateMethodModel
{

    public Antiguedad()
    {
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        String strAntiguedad = " ";
        String mesesx = (String)args.get(0);
        if(mesesx != null && mesesx != "&nbsp;")
        {
            int nmeses = Integer.parseInt(mesesx);
            int anos = nmeses / 12;
            int meses = nmeses % 12;
            if(anos >= 1 && anos > 1)
                strAntiguedad = String.valueOf(anos);
            if(meses > 0 && meses > 1)
                if(anos > 1)
                    strAntiguedad = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(strAntiguedad)))).append("-").append(String.valueOf(meses))));
                else
                    strAntiguedad = String.valueOf(meses);
        }
        return new SimpleScalar(strAntiguedad);
    }
}