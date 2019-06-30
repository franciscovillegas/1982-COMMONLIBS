package portal.com.eje.tools.servlet;

import java.util.List;

import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

public class MesesToYM
    implements TemplateMethodModel
{

    public MesesToYM()
    {
        valida = new Validar();
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        int meses = 0;
        try
        {
            meses = Integer.parseInt(valida.validarDato(args.get(0), "0"));
        }
        catch(NumberFormatException e)
        {
            OutMessage.OutMessagePrint("Exception en MesesToYM --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        int year = meses / 12;
        int mes = meses % 12;
        String ano = "a\361o";
        String me = "mes";
        if(mes > 1)
            me = "meses";
        if(year > 1)
            ano = "a\361os";
        if(args.size() > 1 && args.get(1) != null)
            return new SimpleScalar(String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(String.valueOf(year))))).append(" ").append(ano).append(" ").append(String.valueOf(mes)).append(" ").append(me))));
        else
            return new SimpleScalar(String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(String.valueOf(year))))).append("-").append(String.valueOf(mes)))));
    }

    private Validar valida;
}