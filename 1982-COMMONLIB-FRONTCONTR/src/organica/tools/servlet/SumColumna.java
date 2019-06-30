package organica.tools.servlet;

import java.util.List;
import java.util.Vector;

import organica.tools.Validar;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

public class SumColumna
    implements TemplateMethodModel
{

    public SumColumna(Vector datosColumna[])
    {
        valida = new Validar();
        this.datosColumna = datosColumna;
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        int col = Integer.parseInt(valida.validarDato(args.get(0), "0"));
        int sum = 0;
        for(int x = 0; x < datosColumna[col].size(); x++)
            sum += Integer.parseInt(valida.validarDato((String)datosColumna[col].get(x), "0"));

        return new SimpleScalar(String.valueOf(sum));
    }

    private Validar valida;
    private Vector datosColumna[];
}