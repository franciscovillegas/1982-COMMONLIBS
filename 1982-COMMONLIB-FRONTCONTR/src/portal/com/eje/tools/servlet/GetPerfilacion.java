package portal.com.eje.tools.servlet;

import java.util.List;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import portal.com.eje.frontcontroller.IIOClaseWebLight;

/**
 * Retornará un String "true" o "false" si la persona tiene el "app_id" que se le ingresa como parámetro <br/>
 * Solo recibe un parámetro el "app_id" sin adicionales, hay lugares donde se le agrega al app_id un "app"  entonces en vez de preguntar , como por ejemplo,
 * por sh  se pregunta por app_sh, <b>este no es el caso. </b><br/>
 * Siempre la pregunta es con el rut de la sesión <br/> <br/>
 * <br/>
 * Ejemplo : GetPerfilacion("qsm_adm")
 * 
 * GetPerfilacion()
 * 
 * @author Francisco
 * @since 04-05-2018 11:30
 * */

public class GetPerfilacion implements TemplateMethodModel {
    private IIOClaseWebLight io;
	
    public GetPerfilacion(IIOClaseWebLight io)
    {
    	this.io = io;
    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
    	/*SIEMPRE TENDRÁ 4 PARAMETROS
    	 * 0) la opcion
    	 * 1) el valor
 
    	 * */
    	
    	if(args != null && args.size() == 1) {
    		try {
	    		String o = (String) args.get(0);
	    	
	    		if(io.getUsuario().tieneApp(o)) {
	    			return new SimpleScalar("true");
	    		}
	    		else {
	    			return new SimpleScalar("false");
	    		}
	    		/*
	    		ConsultaData data = PerfilLocator.getInstance().getAtribuciones(io.getUsuario().getRutIdInt()	);
	    		
	    		if(data != null && data.next()) {
	    			String valor = data.getForcedString(o);
	    			
	    			if("1".equals(valor)) {
	    				return new SimpleScalar("true");
	    			}
	    			else {
	    				return new SimpleScalar("");
	    			}
	    		}
	    		*/
	     
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    			return new SimpleScalar("Error parámetro (Exception)");
    		}
    	}
    	
    	return new SimpleScalar("Error parámetro");
    }


}