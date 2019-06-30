package organica.tools.servlet;

import java.util.List;

import organica.tools.OutMessage;
import organica.tools.Tools;

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
        String valor = "0";
        if(args.size() == 1)
            valor = Tools.setFormatNumber((String)args.get(0));
        else
            try
            {
                valor = Tools.setFormatNumber((String)args.get(0), Integer.parseInt((String)args.get(1)));
            }
            catch(NumberFormatException e)
            {
                OutMessage.OutMessagePrint("FormatoNumero: ".concat(String.valueOf(String.valueOf(e.getMessage()))));
            }
        return new SimpleScalar(valor);
    }
}