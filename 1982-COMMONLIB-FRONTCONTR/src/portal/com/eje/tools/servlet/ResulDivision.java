package portal.com.eje.tools.servlet;

import java.util.List;

import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

public class ResulDivision
    implements TemplateMethodModel
{

    public ResulDivision()
    {
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        int dividendo = Integer.parseInt((String)args.get(0));
        int divisor = Integer.parseInt((String)args.get(1));
        int entera = dividendo / divisor;
        int decimal = dividendo % divisor;
        OutMessage.OutMessagePrint("******>resto= ".concat(String.valueOf(String.valueOf(String.valueOf(decimal)))));
        String cadena = String.valueOf(decimal);
        cadena = cadena.substring(0, 2);
        OutMessage.OutMessagePrint("------>parte decimal: ".concat(String.valueOf(String.valueOf(cadena))));
        return new SimpleScalar(String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(String.valueOf(entera))))).append(".").append(cadena))));
    }
}