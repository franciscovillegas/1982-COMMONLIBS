package portal.com.eje.tools.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

/**
 * @deprecated
 * @see portal.com.eje.tools.LogoCliente
 * */
public class GetImagenEmpresa
    implements TemplateMethodModel
{
		private String empresa;

    public GetImagenEmpresa(Usuario user)
    {
    	
    	if(user != null && user.esValido()){
    		this.empresa = user.getEmpresa();	
    	}
        
    }
    
    public GetImagenEmpresa(String empresa)
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
    		String logoNemo = (String)args.get(0);
    		String[] logoPartes = logoNemo.split("\\."); 
    		ResourceBundle proper = ResourceBundle.getBundle("portal");
    		String empresasString = "";
    		
    		try {
    			empresasString = proper.getString("portal.empresas.logospersonalizados");	
    		}
    		catch (NullPointerException e) {
    			System.out.println("WARNING- sin logos diferenciados.");
    		}
    		catch (MissingResourceException e) {
    			System.out.println("WARNING- sin logos diferenciados.");
    		}
    		catch (ClassCastException e) {
    			System.out.println("WARNING- sin logos diferenciados.");
    		}
    		
    		
    		ArrayList lista = new ArrayList();
    		
    		
    		if(empresasString != null) {
    			String[] empresasArray = empresasString.split("\\,");
    			for(String  s : empresasArray) {
    				lista.add(s);
    			}
    		}
    		
    		try {
    			if(logoPartes.length == 2 && empresasString != null &&  lista.indexOf(empresa) != -1) {
    				return new SimpleScalar(logoPartes[0] + empresa + "." + logoPartes[1]);
    			}
	        }
	        catch(Exception e) {
	            OutMessage.OutMessagePrint("GetProp --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
	        }
    	}
    	
    	ret = new SimpleScalar((String)args.get(0));
        return ret;
    }

    private ResourceBundle prop;
}