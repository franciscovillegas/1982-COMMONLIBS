package cl.ejedigital.web;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.tool.properties.IPropertiesTools;
import cl.ejedigital.tool.properties.PropertiesTools;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
 



public class PropertiesManipulateByRequest {
	private final String paramBase;
	private final String listBase;
	private SimpleHash modelRoot;
	
	public PropertiesManipulateByRequest(SimpleHash modelRoot) {
		paramBase  = "portprop";
		listBase = "portlist";
		this.modelRoot =  modelRoot;
	}
	
	
    public void manipulate(HttpServletRequest req) {
    	String properties = req.getParameter(paramBase);
    	String listas = req.getParameter(listBase);
    	
    	if(properties != null) {
    		addHashes(properties,modelRoot);
    	}
    	
    	if(listas != null) {
    		addLista(listas,modelRoot);
    	}
    }
    
    private void addHashes(String properties, SimpleHash modelRoot) {
    	if(properties != null) {
    		String props[] = properties.split("\\,");
    		
    		for(String prop : props) {
    			if(prop != null && prop.indexOf("[") != -1 && prop.indexOf("]") != -1) {
    				String propiedad = prop.substring(0,prop.indexOf("["));
    				String valor = prop.substring(prop.indexOf("[")+1,prop.indexOf("]"));
    				
    				if(propiedad != null) {
    					putKey(modelRoot,propiedad,valor);
    				}
    			}
    		}
    	}
    }
    
    private void putKey(SimpleHash hash,String propName, String keyBaseTOput) {
    	IPropertiesTools propers = new PropertiesTools();
    	if(propers.existsBundle(propName)) {
    		ResourceBundle proper = ResourceBundle.getBundle(propName);
    		
    		List<String> lista = propers.getKeys(proper, keyBaseTOput );
    		
    		for(String key : lista) {
    			hash.put(key, propers.getString(proper, keyBaseTOput +key,""));
    		}
    	}
    }
    
    
    private void addLista(String listasParam, SimpleHash modelRoot) {
    	SimpleList simpleList = new SimpleList();
    	
    	if(listasParam != null) {
    		String listas[] = listasParam.split("\\,");
    		
    		for(String lista : listas) {
    			if(lista != null && lista.indexOf("[") != -1 && lista.indexOf("]") != -1) {
    				String propiedad = lista.substring(0,lista.indexOf("["));
    				String valor = lista.substring(lista.indexOf("[")+1,lista.indexOf("]"));
    				
    				if(propiedad != null) {
    					
    					putLista(simpleList,propiedad,valor);

    				}
    			}
    		}
    	}
    	
    	modelRoot.put("lista",simpleList);
    }
    
    private void putLista(SimpleList simpleList,String propName, String keyBaseTOput) {
    	IPropertiesTools propers = new PropertiesTools();
    	if(propers.existsBundle(propName)) {
    		ResourceBundle proper = ResourceBundle.getBundle(propName);
    		
    		List<String> lista = propers.getKeys(proper, keyBaseTOput );
    		
    		
    		for(String key : lista) {
    			SimpleHash hash = new SimpleHash();
    			
    			hash.put("key",key);
    			hash.put("value", propers.getString(proper, keyBaseTOput +key,""));
    			simpleList.add(hash);
    		}
			
    	}
    }
	
	
}
