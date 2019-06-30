package organica.Indicador;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import organica.datos.Consulta;
import organica.tools.OutMessage;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

public class IndicadorTM
    implements TemplateMethodModel
{

    public IndicadorTM(Connection con, Template template)
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
        OutMessage.OutMessagePrint("argumento brut :".concat(String.valueOf(String.valueOf((String)args.get(0)))));
        SimpleHash modelRoot = new SimpleHash();
        String grupo = (String)args.get(0);
        String item = (String)args.get(1);
        String indicador = (String)args.get(2);
        String semaforo = (String)args.get(3);
        String unid_rama = (String)args.get(4);
        String periodo = (String)args.get(5);
        String unidad = (String)args.get(6);
        OutMessage.OutMessagePrint("grupo:       ".concat(String.valueOf(String.valueOf(grupo))));
        OutMessage.OutMessagePrint("item:        ".concat(String.valueOf(String.valueOf(item))));
        OutMessage.OutMessagePrint("inicador:    ".concat(String.valueOf(String.valueOf(indicador))));
        OutMessage.OutMessagePrint("semaforo:    ".concat(String.valueOf(String.valueOf(semaforo))));
        OutMessage.OutMessagePrint("unid_rama:   ".concat(String.valueOf(String.valueOf(unid_rama))));
        OutMessage.OutMessagePrint("periodo:     ".concat(String.valueOf(String.valueOf(periodo))));
        OutMessage.OutMessagePrint("unidad:      ".concat(String.valueOf(String.valueOf(unidad))));
        PrintWriter out = new PrintWriter(uddata);
        Consulta info = new Consulta(con);
        Consulta dato = new Consulta(con);
        String consulta = String.valueOf(String.valueOf((new StringBuilder("select * from eje_ges_def_indicadores where grupo='")).append(grupo).append("' and item='").append(item).append("' and indicador='").append(indicador).append("'")));
        OutMessage.OutMessagePrint("---->consulta: ".concat(String.valueOf(String.valueOf(consulta))));
        info.exec(consulta);
        info.next();
        consulta = String.valueOf(String.valueOf((new StringBuilder("select ")).append(info.getString("indicador")).append(" from ").append(info.getString("tabla")).append(" where ").append(" empresa='").append(grupo).append("' and unidad='").append(unidad).append("' and uni_rama='").append(unid_rama).append("' and periodo=").append(periodo)));
        OutMessage.OutMessagePrint("---->consulta: ".concat(String.valueOf(String.valueOf(consulta))));
        dato.exec(consulta);
        dato.next();
        OutMessage.OutMessagePrint("---->Dato Indicador : ".concat(String.valueOf(String.valueOf(dato.getString(info.getString("indicador"))))));
        OutMessage.OutMessagePrint("---->Dato Tramo inf : ".concat(String.valueOf(String.valueOf(info.getString("tramo_inf2")))));
        OutMessage.OutMessagePrint("---->Dato Tramo med1: ".concat(String.valueOf(String.valueOf(info.getString("tramo_medio1")))));
        OutMessage.OutMessagePrint("---->Dato Tramo med2: ".concat(String.valueOf(String.valueOf(info.getString("tramo_medio2")))));
        OutMessage.OutMessagePrint("---->Dato Tramo sup : ".concat(String.valueOf(String.valueOf(info.getString("tramo_sup1")))));
        OutMessage.OutMessagePrint("---->Dato Semaforo  : ".concat(String.valueOf(String.valueOf(info.getString(semaforo)))));
        if(dato.getInt(info.getString("indicador")) >= info.getInt("tramo_sup1"))
            semaforo = info.getString("imag_sup");
        if(dato.getInt(info.getString("indicador")) >= info.getInt("tramo_medio1") && dato.getInt(info.getString("indicador")) <= info.getInt("tramo_medio2"))
            semaforo = info.getString("imag_med");
        if(dato.getInt(info.getString("indicador")) <= info.getInt("tramo_inf2"))
            semaforo = info.getString("imag_inf");
        SimpleList Lista = new SimpleList();
        SimpleHash Temp = new SimpleHash();
        Temp.put("valor", dato.getString(info.getString("indicador")));
        Temp.put("semaforo", semaforo);
        Temp.put("inf2", info.getString("tramo_inf2"));
        Temp.put("sup1", info.getString("tramo_sup1"));
        Temp.put("glosa", info.getString("glosa"));
        Temp.put("descitem", info.getString("desc_item"));
        Temp.put("destramo", info.getString("desc_tramo_inf"));
        Lista.add(Temp);
        modelRoot.put("datos", Lista);
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