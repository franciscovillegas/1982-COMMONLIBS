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

import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

// Referenced classes of package Indicador:
//            DetIndicador

public class TMUnidEncargado {

    public TMUnidEncargado(Connection con, String templatePath)
    {
        this.con = con;
        resourceHtml = new ResourceHtml();
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        String strTemplate = args.get(0).toString();
        String strEmpresa = args.get(1).toString();
        String strUnidad = args.get(2).toString();
        String strPeriodo = args.get(3).toString();
        SimpleScalar simpleScalar;

        Template template;
		try {
			template = resourceHtml.getTemplate(strTemplate);
			
			ByteArrayOutputStream uddata = new ByteArrayOutputStream(40000);
	        PrintWriter out = new PrintWriter(uddata);
	        SimpleHash modelRoot = new SimpleHash();
	        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT nombre, cargo_desc, anexo, e_mail, foto FROM view_unidad_encargado WHERE (unid_empresa = '")).append(strEmpresa).append("') AND (unid_id = '").append(strUnidad).append("') AND").append(" (periodo = ").append(strPeriodo).append(")")));
	        Consulta consul = new Consulta(con);
	        consul.exec(sql);
	        if(consul.next())
	        {
	            modelRoot.put("tiene", "SI");
	            modelRoot.put("nombre", consul.getString("nombre"));
	            modelRoot.put("cargo", consul.getString("cargo_desc"));
	            modelRoot.put("anexo", consul.getString("anexo"));
	            modelRoot.put("e_mail", consul.getString("e_mail"));
	            modelRoot.put("foto", consul.getString("foto"));
	        }
	        consul.close();
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