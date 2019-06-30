package portal.com.eje.tools.servlet;

import java.util.List;
import java.util.ResourceBundle;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import portal.com.eje.portal.factory.Util;

public class ToUpperCase	
    implements TemplateMethodModel
{
		private String empresa;

	public static ToUpperCase getInstance() {
		return Util.getInstance(ToUpperCase.class);
	}
	
    public ToUpperCase()
    {
    	
    	 
        
    }
    
    public ToUpperCase(String empresa)
    {
    	
    	this.empresa = empresa;
    }
    
    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
    	SimpleScalar ret = null;
    	
    	if(args != null && args.size() >= 1) {
    		try {
    			ret = new SimpleScalar( ((String)(args.get(0))).toUpperCase() );
    		} catch (NullPointerException e) {
    			ret =  new SimpleScalar("");
    		}
    		
    		return ret;
    	}
    	
    	ret = new SimpleScalar("");
        return ret;
    }

    private ResourceBundle prop;
}