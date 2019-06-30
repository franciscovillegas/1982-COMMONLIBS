package organica.orggenerica;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import organica.orggenerica.jerarquia.IJerarquiaNodo;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;


public class FuncionJQueryBuildOrganica implements TemplateMethodModel {
	private IJerarquiaNodo rootNode;
	private Template template;
	
	public FuncionJQueryBuildOrganica(Template template,IJerarquiaNodo rootNode) {
		this.rootNode = rootNode;
		this.template = template;
	}
	
	public boolean isEmpty() throws TemplateModelException {
		// TODO Auto-generated method stub
		return false;
	}

	public TemplateModel exec(List arg0) throws TemplateModelException {
	    ByteArrayOutputStream uddata = new ByteArrayOutputStream(40000);
        SimpleHash modelRoot = new SimpleHash();
        PrintWriter out = new PrintWriter(uddata);
		
        if(rootNode.isRootNode()) {
	        modelRoot.put("id",StringEscapeUtils.escapeHtml("nodo_"+ rootNode.getId()));
	        modelRoot.put("classHtml", StringEscapeUtils.escapeHtml(rootNode.getClassHtml()));
        }
        
        List<IJerarquiaNodo> hijos = rootNode.getHijos();
        
        SimpleList lista = new SimpleList();
        
        for(IJerarquiaNodo hijo : hijos) {
        	SimpleHash hash = new SimpleHash();
        	hash.put("classHtml", StringEscapeUtils.escapeHtml(hijo.getClassHtml()));
        	hash.put("nombre", StringEscapeUtils.escapeHtml(hijo.getNombre()));
        	hash.put("id", StringEscapeUtils.escapeHtml(hijo.getId()));
        	hash.put("nodoAbierto", "closed" );
        	
        	if(hijo.getHijos().size() > 0) {
        		hash.put("LoadArbol",new FuncionJQueryBuildOrganica(template,hijo));
        	}
        	
        	lista.add(hash);
        }
        
        modelRoot.put("hijos",lista);
        
        try {
			template.process(modelRoot,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        out.close();
		
		return new SimpleScalar(uddata.toString());
	}

}
