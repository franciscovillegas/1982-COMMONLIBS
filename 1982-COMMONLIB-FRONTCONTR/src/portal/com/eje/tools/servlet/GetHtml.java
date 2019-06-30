package portal.com.eje.tools.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.web.FreemakerTool;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

public class GetHtml implements TemplateMethodModel {
    private HttpServletRequest request;
    private IOClaseWeb cweb;
	private SimpleHash modelRoot;
    
    public GetHtml(SimpleHash modelRoot, HttpServletRequest req)
    {
    	this.modelRoot = modelRoot;
        request = req;
    }
    
    public GetHtml(IOClaseWeb cweb)
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
				Template t = ResourceHtml.getInstance().getTemplate(request, (String)args.get(0));
//				for(int i=1;i<args.size();i++) {
//					String param = (String)args.get(1);
//					if(param != null) {
//						String var = param.substring(0, param.indexOf("="));
//						String value = param.substring(param.indexOf("=") +1, param.length());
//						
//						modelRoot.put(var, value);
//					}
//				}
//				
//				
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