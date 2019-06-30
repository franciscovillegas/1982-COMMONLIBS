package portal.com.eje.tools.servlet;

import java.util.Date;
import java.util.List;

import portal.com.eje.tools.Validar;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

public class FormatoFecha
    implements TemplateMethodModel
{

    public FormatoFecha()
    {
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        Validar val = new Validar();
        Date fecha = new Date();
        val.setFormatoFecha(args.get(0).toString());
        String valor_retorno;
        if(args.size() == 1)
            valor_retorno = val.validarFecha(fecha);
        else
            valor_retorno = val.validarFecha((String)args.get(1));
        return new SimpleScalar(valor_retorno);
    }
}