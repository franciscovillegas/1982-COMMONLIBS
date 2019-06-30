package organica.Indicador;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Validar;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

// Referenced classes of package Indicador:
//            DetIndicador, ValorIndicador

public class TMUnidEstado implements TemplateModel {

    public TMUnidEstado(Connection con, String templatePath, DetIndicador indicador)
    {
        this.con = con;
        resourceHtml = new ResourceHtml();
        this.indicador = indicador;
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        String strTemplate = (String)args.get(0);
        String strIndic = (String)args.get(1);
        String strUnidad = (String)args.get(2);
        String strPeriodo = (String)args.get(3);
        String strEmpresa = (String)args.get(4);
        String strTipo = (String)args.get(5);
        boolean semaforoVerde = args.get(6) != null;
        boolean semaforoAmarillo = args.get(7) != null;
        boolean semaforoRojo = args.get(8) != null;
        OutMessage.OutMessagePrint("Template:".concat(String.valueOf(String.valueOf(strTemplate))));
        OutMessage.OutMessagePrint("Indic:".concat(String.valueOf(String.valueOf(strIndic))));
        OutMessage.OutMessagePrint("Unidad:".concat(String.valueOf(String.valueOf(strUnidad))));
        OutMessage.OutMessagePrint("Periodo:".concat(String.valueOf(String.valueOf(strPeriodo))));
        OutMessage.OutMessagePrint("Empresa:".concat(String.valueOf(String.valueOf(strEmpresa))));
        OutMessage.OutMessagePrint("Tipo:".concat(String.valueOf(String.valueOf(strTipo))));
        OutMessage.OutMessagePrint("Color Verde:".concat(String.valueOf(String.valueOf(semaforoVerde))));
        OutMessage.OutMessagePrint("Color Amarillo:".concat(String.valueOf(String.valueOf(semaforoAmarillo))));
        OutMessage.OutMessagePrint("Color Rojo:".concat(String.valueOf(String.valueOf(semaforoRojo))));
        SimpleScalar simpleScalar;

        Template template = null;
        
		try {
			template = resourceHtml.getTemplate(strTemplate);
		
	        ByteArrayOutputStream uddata = new ByteArrayOutputStream(40000);
	        PrintWriter out = new PrintWriter(uddata);
	        Validar valida = new Validar();
	        SimpleHash modelRoot = new SimpleHash();
	        SimpleList simplelist = new SimpleList();
	        modelRoot.put("indic", strIndic);
	        modelRoot.put("peri", strPeriodo);
	        modelRoot.put("empresa", strEmpresa);
	        modelRoot.put("tipo_indic", strTipo);
	        modelRoot.put("unid_id", strUnidad);
	        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT ur.uni_rama, u.unid_desc, ti.")).append(indicador.getCampo()).append(" AS valor").append(" FROM eje_ges_unidad_rama ur INNER JOIN").append(" eje_ges_unidades u ON ur.empresa = u.unid_empresa AND ").append(" ur.uni_rama = u.unid_id INNER JOIN").append(" ").append(indicador.getTabla()).append(" ti ON ").append(" ur.empresa = ti.empresa AND ur.uni_rama = ti.unidad").append(" WHERE (ur.empresa = '").append(strEmpresa).append("') AND (ur.unidad = '").append(strUnidad).append("') AND").append(" (ur.tipo = 'R') AND (ti.periodo = ").append(strPeriodo).append(") AND ").append(" (ti.uni_rama = '").append(strTipo).append("')")));
	        OutMessage.OutMessagePrint(">>--> sql UnidEstado\n".concat(String.valueOf(String.valueOf(sql))));
	        Consulta consul = new Consulta(con);
	        consul.exec(sql);
	        modelRoot.put("semV", semaforoVerde);
	        modelRoot.put("semA", semaforoAmarillo);
	        modelRoot.put("semR", semaforoRojo);
	        modelRoot.put("item", indicador.getNombre());
	        do
	        {
	            if(!consul.next())
	                break;
	            SimpleHash simplehash = new SimpleHash();
	            boolean mostrar = false;
	            float valor = consul.getFloat("valor");
	            ValorIndicador valIndic = new ValorIndicador(indicador, valor);
	            if(valIndic.getSemaforo() == "V" && semaforoVerde)
	                mostrar = true;
	            else
	            if(valIndic.getSemaforo() == "A" && semaforoAmarillo)
	                mostrar = true;
	            else
	            if(valIndic.getSemaforo() == "R" && semaforoRojo)
	                mostrar = true;
	            else
	            if(!valIndic.existeValor())
	                mostrar = true;
	            if(mostrar)
	            {
	                simplehash.put("unid_id", consul.getString("uni_rama"));
	                simplehash.put("unid_desc", consul.getString("unid_desc"));
	                if(valIndic.existeValor())
	                {
	                    simplehash.put("existe_valor", "SI");
	                    simplehash.put("foto", valIndic.getImagen());
	                    simplehash.put("valor", valIndic.getValorIndic());
	                    simplehash.put("quetramo", valIndic.getQueTramo());
	                    simplehash.put("formula", indicador.getFormula());
	                    simplehash.put("desc_item", indicador.getDescItem());
	                    simplehash.put("glosa", indicador.getGlosa());
	                    simplehash.put("desctramo", valIndic.getDescTramo());
	                    simplehash.put("quetramo", valIndic.getQueTramo());
	                    simplehash.put("tinf", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(indicador.getTramoInf1())))).append(" - ").append(indicador.getTramoInf2()))));
	                    simplehash.put("tmed", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(indicador.getTramoMedio1())))).append(" - ").append(indicador.getTramoMedio2()))));
	                    simplehash.put("tsup", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(indicador.getTramoSup1())))).append(" - ").append(indicador.getTramoSup2()))));
	                }
	                simplelist.add(simplehash);
	            }
	        } while(true);
	        modelRoot.put("unidades", simplelist);
	        try {
				template.process(modelRoot, out);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        out.close();
	        consul.close();
	        simpleScalar = new SimpleScalar(uddata.toString());
		}
		catch (FileNotFoundException e) {
			 OutMessage.OutMessagePrint(e.getMessage());
	         simpleScalar = new SimpleScalar("PLantilla no disponible...");
	         
		}
		catch (ServletException e) {
			 OutMessage.OutMessagePrint(e.getMessage());
	         simpleScalar = new SimpleScalar("Excepcion desconocida...");
	         
		}
		catch (IOException e) {
			 OutMessage.OutMessagePrint(e.getMessage());
	         simpleScalar = new SimpleScalar("Excepcion desconocida...");
		}
		catch (SQLException e) {
			 OutMessage.OutMessagePrint(e.getMessage());
	         simpleScalar = new SimpleScalar("Excepcion desconocida...");
		}
    
        return simpleScalar;
    }

    

    private Connection con;
    private ResourceHtml resourceHtml;
    private DetIndicador indicador;
}