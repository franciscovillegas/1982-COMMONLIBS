package portal.com.eje.tools.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.web.FreemakerTool;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import portal.com.eje.serhumano.httpservlet.MyTemplateUbication;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

/**
 * Imprime un html sin comentario inicial y sin espacio. No arroja error
 * @author Pancho
 * @since 26-06-2019
 * */
public class GetHtmlLikeVariable implements TemplateMethodModel {
    private HttpServletRequest request;
    private IOClaseWeb cweb;
	private SimpleHash modelRoot;
    
    public GetHtmlLikeVariable(SimpleHash modelRoot, HttpServletRequest req)
    {
    	this.modelRoot = modelRoot;
        request = req;
    }
    
    public GetHtmlLikeVariable(IOClaseWeb cweb)
    {
    	this.cweb = cweb;
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
    	String flujo = "";
    	
    	if(args != null && args.size() >= 1 && (String)args.get(0) != null && !((String)args.get(0)).equals("")) {
    		
    		try {
				Template t = ResourceHtml.getInstance().getTemplate(MyTemplateUbication.Webcontent, request, (String)args.get(0), false);
				flujo = MyString.getInstance().quitaEspacios(FreemakerTool.getInstance().templateProcess(t, modelRoot));
				
			} catch (IOException e) {
				flujo= e.toString();
			} catch (ServletException e) {
				flujo= e.toString(); 
			} catch (SQLException e) {
				flujo= e.toString();
			} catch (Exception e) {
				flujo= e.toString();
			}
    		
    	}
    	
    	return new SimpleScalar(flujo);
    }


}