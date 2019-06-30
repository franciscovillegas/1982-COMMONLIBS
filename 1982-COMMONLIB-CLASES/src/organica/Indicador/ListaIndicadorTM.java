package organica.Indicador;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import organica.datos.Consulta;
import organica.tools.OutMessage;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

public class ListaIndicadorTM
    implements TemplateMethodModel
{

    public ListaIndicadorTM(Connection con, Template template)
    {
        this.con = con;
        this.template = template;
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        ByteArrayOutputStream uddata = new ByteArrayOutputStream(40000);
        SimpleHash modelRoot = new SimpleHash();
        PrintWriter out = new PrintWriter(uddata);
        String item = (String)args.get(0);
        Consulta info = new Consulta(con);
        String consulta = String.valueOf(String.valueOf((new StringBuilder("select glosa,desc_item,formula,desc_ind from eje_ges_def_indicadores WHERE item='")).append(item).append("'")));
        OutMessage.OutMessagePrint("---->consulta: ".concat(String.valueOf(String.valueOf(consulta))));
        info.exec(consulta);
        modelRoot.put("det_indicador", info.getSimpleList());
        try {
			template.process(modelRoot, out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        out.close();
        info.close();
        return new SimpleScalar(uddata.toString());
    }

    private Connection con;
    private Template template;
}