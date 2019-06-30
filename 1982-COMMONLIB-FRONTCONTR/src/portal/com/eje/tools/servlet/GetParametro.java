package portal.com.eje.tools.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.parametro.ParametroKey;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroType;
import portal.com.eje.portal.parametro.ParametroValue;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;

public class GetParametro implements TemplateMethodModel {
    
	public static GetParametro getInstance() {
		return Util.getInstance(GetParametro.class);
	}
	
    public GetParametro()
    {

    }

    public boolean isEmpty()
    {
        return false;
    }

    public TemplateModel exec(List args)
    {
    	/*SIEMPRE TENDRÁ 4 PARAMETROS
    	 * 0) CONTEXTO ACEPTADO
    	 * 1) PARÁMETRO
    	 * 2) KEY DEL VALOR
    	 * 3) VALOR POR DEFECTO
    	 * */
    	
    	if(args != null && args.size() == 4) {
    		try {
	    		String c = (String) args.get(0);
	    		String[] cc = null;
	    		
	    		if(c != null) {
	    			cc = c.split("\\|");
	    		}
	    		
	    		String p = (String) args.get(1);
	    		String k = (String) args.get(2);
	    		String v = (String) args.get(3);
	    		
	    		EModulos thisMod = EModulos.getThisModulo();
	    		
	    		if(c!= null) {
	    			c = c.replaceAll("\\/", "");
	    		}
	    		
	    		boolean esEsteModulo = false;
	    		
	    		if ("".equals(c)) {
	    			esEsteModulo = true;
	    		}else{
		    		for(String contexto : cc) {
		    			if(thisMod.equals(EModulos.getModulo("/"+contexto))) {
		    				esEsteModulo = true;
		    			}
		    		}
	    		}
	    		
	    		if(esEsteModulo) {
	    			ParametroKey pk   = ParametroLocator.getInstance().getParamKey( thisMod, p);
	        		ParametroValue pv = ParametroLocator.getInstance().getValor(thisMod, pk.getNemo(), k);
	        		
	        		if(pv == null) {
	        			//Francisco: siempre será String
	        			ParametroLocator.getInstance().addParam( pk, ParametroValue.buildParametro(k, v));
	        			
	        			return new SimpleScalar(v);
	        		}
	        		else {
	        			if(pv.getIdType() ==  ParametroType.tipo_String.getId()) {
	        				return new SimpleScalar(pv.getValue());
	        			}
	        			else if(pv.getIdType() ==  ParametroType.tipo_Imagen.getId()) {
	        				return new SimpleScalar("data:image/png;base64,"+pv.getBase64file());
	        			}
	        			else {
	        				return new SimpleScalar("Error parámetro (No Type Def) ");
	        			}
	        		}
	    		}
	    		else {
	    			return new SimpleScalar("");
	    		}
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    			return new SimpleScalar("Error parámetro (Exception)");
    		}
    	}
    	
    	return new SimpleScalar("Error parámetro");
    }


}