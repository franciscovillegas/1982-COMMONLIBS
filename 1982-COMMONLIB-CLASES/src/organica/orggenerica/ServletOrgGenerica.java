package organica.orggenerica;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.orggenerica.jerarquia.IJerarquiaNodo;
import organica.orggenerica.jerarquia.JerarquiaNodo;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.ClaseGenerica;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;


public class ServletOrgGenerica extends MyHttpServlet {

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException, ServletException {
		doGet(req,resp);
	}
	
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    	throws IOException, ServletException {
    	
    	try {
	    	resp.setHeader("Expires", "0");
	        resp.setHeader("Pragma", "no-cache");
	        resp.setHeader("Cache-Control", "no-cache");
			resp.setContentType("text/html; charset=iso-8859-1");
	    	
	    	String path = req.getParameter("path");
	    	String claseJerarquia = req.getParameter("claseJerarquia");
	    	String cssImports = req.getParameter("css");
	    	String jsImports = req.getParameter("js");
	    	String titulo = req.getParameter("titulo");
	    	Usuario user = SessionMgr.rescatarUsuario(req);
	    	
	    	if(path == null) {
	    		path = "treeView/styleFolder/";
	    	}
	    	
	    	if(claseJerarquia == null) {
	    		claseJerarquia = "organica.orggenerica.jerarquia.PersonalizadaJerarquiaPortal";
	    	}
	    	
	    	if(titulo == null) {
	    		titulo = "Tree View";
	    	}
	    	
	    	List<String> css = new ArrayList<String>();
	    	if(cssImports == null) {
	    		css.add("screen.css");
	    		css.add("jquery.treeview.folder.css");
	    	}
	    	else {
	    		String[] imp = cssImports.split("\\;");
	    		for(String cssParam : imp ) {
	    			css.add(cssParam);
	    		}
	    	}
	    	
	    	List<String> js = new ArrayList<String>();
	    	if(jsImports == null) {
	    		js.add("treeview.event.parentListener.js");
	    	}
	    	else {
	    		String[] imp = jsImports.split("\\;");
	    		for(String jsParam : imp ) {
	    			js.add(jsParam);
	    		}
	    	}
	    	
	    	ClaseGenerica claseGen = new ClaseGenerica();
	    	
	    	Class<?>[] argumentosBuilder = {};
	    	Object[] parametrosBuilder = {};
	    	claseGen.cargaConstructor(claseJerarquia,argumentosBuilder,parametrosBuilder);
	    	
	    	Class<?>[] argumentosMethod = {Usuario.class};
	    	Object[] parametrosMethod = {user};
	    	IJerarquiaNodo rootNode = (JerarquiaNodo) claseGen.ejecutaMetodo("getJerarquia",argumentosMethod,parametrosMethod);
	    	
	    	Template tempTree = getTemplate(path+"treeHtml.html");
	    	
	    	SimpleHash modelRoot = new SimpleHash();
	    	modelRoot.put("titulo",titulo);
	    	modelRoot.put("path",path);
	    	modelRoot.put("LoadArbol",new FuncionJQueryBuildOrganica(tempTree,rootNode));
	    	modelRoot.put("rootNode","nodo_"+rootNode.getId());
	    	
	    	addList(modelRoot,"cssImport",css);
	    	addList(modelRoot,"jsImport",js);
	
	    	super.retTemplate(resp,path+"contenedorTreeHtml.html",modelRoot);
    	} 
    	catch (Exception e) {
    		
    	}
    }
    
    private void addList(SimpleHash modelRoot, String freeMakerlistName, List<String> lista) {
    	SimpleList sList = new SimpleList();
    	
    	for(String obj : lista ) {
    		SimpleHash hash = new SimpleHash();
    		hash.put("String",obj);
    		
    		sList.add(hash);
    	}
    	
    	modelRoot.put(freeMakerlistName,sList);
    }
    
}