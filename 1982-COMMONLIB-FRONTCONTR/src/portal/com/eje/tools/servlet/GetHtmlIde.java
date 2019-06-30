package portal.com.eje.tools.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.web.FreemakerTool;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import portal.com.eje.serhumano.httpservlet.MyTemplateUbication;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

public class GetHtmlIde implements TemplateMethodModel {
    private HttpServletRequest request;
    private IOClaseWeb cweb;
	private SimpleHash modelRoot;
    
    public GetHtmlIde(SimpleHash modelRoot, HttpServletRequest req)
    {
    	this.modelRoot = modelRoot;
        request = req;
    }
    
    public GetHtmlIde(IOClaseWeb cweb)
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
    	
    	if(args != null && args.size() >= 1) {
    		
    		try {
				Template t = ResourceHtml.getInstance().getTemplate(MyTemplateUbication.Webcontent, request, (String)args.get(0), false);
				flujo = "<!--"+(String)args.get(0)+"--> \n" + FreemakerTool.getInstance().templateProcess(t, modelRoot);
				
			} catch (IOException e) {
				return new SimpleScalar("@@GetHtml [IOException "+e.getMessage()+"]");
			} catch (ServletException e) {
				return new SimpleScalar("@@GetHtml [ServletException "+e.getMessage()+"]");
			} catch (SQLException e) {
				return new SimpleScalar("@@GetHtml [SQLException "+e.getMessage()+"]");
			} catch (Exception e) {
				return new SimpleScalar("@@GetHtml [Exception "+e.getMessage()+"]");
			}
    		
    	}
    	
    	return new SimpleScalar(flujo);
    }


}