package portal.com.eje.tools.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.frontcontroller.IOClaseWebLight;

public class GetParam implements TemplateMethodModel {
    private HttpServletRequest request;
    private IIOClaseWebLight cweb;
	
    public GetParam(HttpServletRequest req)
    {
        request = req;
    }
    
    public GetParam(IIOClaseWebLight cweb)
    {
    	this.cweb = cweb;
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
    	if(request != null) {
    		return new SimpleScalar(request.getParameter(args.get(0).toString()));
    	}
    	else {
    		return new SimpleScalar(cweb.getParamString(args.get(0).toString(),""));
    	}
    }


}