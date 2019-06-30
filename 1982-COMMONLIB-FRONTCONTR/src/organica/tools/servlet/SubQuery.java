package organica.tools.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

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
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

// Referenced classes of package tools.servlet:
//            FormatoNumero, FormatoFecha, MesesToYM, GetDatos, 
//            SumColumna

public class SubQuery    implements TemplateMethodModel
{

    public SubQuery(Connection con, String templatePath)
    {
        this.con = con;
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
            Template template;
			try {
				template = html.getTemplate(strTemplate);
			}
			catch (SQLException e) {
				throw new ServletException(e);
			}
			
            ByteArrayOutputStream uddata = new ByteArrayOutputStream(40000);
            PrintWriter out = new PrintWriter(uddata);
            Validar valida = new Validar();
            SimpleHash modelRoot = new SimpleHash();
            SimpleList simplelist = new SimpleList();
            Consulta consul = new Consulta(con);
            consul.exec(strSql);
            Vector datosColumna[] = new Vector[consul.getColumnCount()];
            for(int column = 1; column <= consul.getColumnCount(); column++)
                datosColumna[column - 1] = new Vector();

            SimpleHash simplehash;
            for(; consul.next(); simplelist.add(simplehash))
            {
                simplehash = new SimpleHash();
                for(int column = 1; column <= consul.getColumnCount(); column++)
                {
                    String columnName = consul.getColumnName(column);
                    String columnValor = valida.validarDato(consul.getString(column));
                    datosColumna[column - 1].add(columnValor);
                    simplehash.put(columnName, columnValor);
                }

            }

            modelRoot.put("datos", simplelist);
            modelRoot.put("FNum", new FormatoNumero());
            modelRoot.put("FFecha", new FormatoFecha());
            modelRoot.put("MTYM", new MesesToYM());
            modelRoot.put("GetDatos", new GetDatos(con));
            modelRoot.put("SumColumna", new SumColumna(datosColumna));
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
        catch(ServletException e)
        {
            OutMessage.OutMessagePrint(e.getMessage());
            simpleScalar = new SimpleScalar("SubInforme no disponible");
        }
		catch (IOException e) {
			OutMessage.OutMessagePrint(e.getMessage());
            simpleScalar = new SimpleScalar("SubInforme no disponible");
		}
		
        return simpleScalar;
    }

 

    private Connection con;
}