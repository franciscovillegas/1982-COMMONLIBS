package cl.ejedigital.web.frontcontroller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.tool.reflect.ClaseGenerica;
import cl.ejedigital.web.MyHttpServlet;

/**
 * @deprecated
 * @since 2015-06-23
 * @author Francisco
 * */
public class ServletGenerico extends MyHttpServlet {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3330922018691970861L;
	private static String PARAM_CLASE = "claseweb";
	
	
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    	procesaPeticion("doPost".intern(),req,resp);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {
    	procesaPeticion("doGet".intern(),req,resp);
    }

    
    private void procesaPeticion(String tipo,HttpServletRequest req,HttpServletResponse resp) {
    	
    	
    	try {
    		IOClaseWeb p = new IOClaseWeb(req,resp,this);
    		
    		if(p.getUsuario().esValido()) {
    			
    	    	
		    	if(p.getIn().getMapParams().get(PARAM_CLASE) != null) {
			    	ClaseGenerica clase = new ClaseGenerica();
			    	Class<?>[] consDefClass= {IOClaseWeb.class};
			    	Object[] consParams = {p};
			    	clase.cargaConstructor(p.getIn().getParamString(PARAM_CLASE,""),consDefClass,consParams);
			    	
			    	Class<?>[] methDefClass= {};
			    	Object[] methParams = {};
			    	clase.ejecutaMetodo(tipo,methDefClass,methParams);
		    	}
		    	else {
		    		resp.sendError(HttpServletResponse.SC_NOT_FOUND);
					
		    	}
    		}
    		else {
    			
    		}

    	}
    	catch (ClassNotFoundException e) {
    		e.printStackTrace();
		}
		catch (SecurityException e) {
			throw e;
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
    	
    }
        
}