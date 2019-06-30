package portal.com.eje.tools.servlet;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import portal.com.eje.datos.Consulta;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Validar;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

// Referenced classes of package portal.com.eje.tools.servlet:
//            FormatoNumero

public class SubQuery implements TemplateMethodModel {
	HttpServletRequest req;
	
    public SubQuery(Connection con, HttpServletRequest req, String templatePath) {
        this.con = con;
        this.req = req;
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
        String strTemplate = args.get(0).toString();
        String strSql = args.get(1).toString();
        SimpleScalar simpleScalar;
        try
        {
        	ResourceHtml html = new ResourceHtml();
            Template template = html.getTemplate(req, strTemplate);
            ByteArrayOutputStream uddata = new ByteArrayOutputStream(40000);
            PrintWriter out = new PrintWriter(uddata);
            Validar valida = new Validar();
            SimpleHash modelRoot = new SimpleHash();
            SimpleList SimpleList = new SimpleList();
            Consulta consul = new Consulta(con);
            consul.exec(strSql);
            SimpleHash simplehash;
            for(; consul.next(); SimpleList.add(simplehash))
            {
                simplehash = new SimpleHash();
                for(int column = 1; column <= consul.getColumnCount(); column++)
                {
                    String columnName = consul.getColumnName(column);
                    String columnValor = valida.validarDato(consul.getString(column));
                    if(consul.getColumnType(column) == -100)
                        simplehash.put(columnName, Tools.setFormatNumber(columnValor));
                    else
                        simplehash.put(columnName, columnValor);
                }

            }

            modelRoot.put("datos", SimpleList);
            modelRoot.put("FNum", new FormatoNumero());
            template.process(modelRoot, out);
            out.close();
            consul.close();
            simpleScalar = new SimpleScalar(uddata.toString());
        }
        catch(ServletException e)
        {
            System.out.println(e.getMessage());
            simpleScalar = new SimpleScalar("SubInforme no disponible");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            simpleScalar = new SimpleScalar("Template  no disponible");
        }
        return simpleScalar;
    }

   
    private Connection con;
}