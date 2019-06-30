package organica.com.eje.ges.gestion; 

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

public class ReportesOrganicaAjax extends MyHttpServlet {
	public ReportesOrganicaAjax() {
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
		throws IOException, ServletException {
		doGet(req,resp);
	}
	
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    	throws IOException, ServletException {
    	String unidad = req.getParameter("unidad");
        String recursivo = req.getParameter("recursivo");
        String tipo = req.getParameter("tipo");
        String empresa = req.getParameter("empresa");
        String periodo = req.getParameter("periodo");
        String page = req.getParameter("page")==null ? "1":req.getParameter("page");
        String CallParam = "periodo=" + periodo; 
    	SimpleHash modelRoot = new SimpleHash();
    	
    	modelRoot.put("url", "/firstfactors/portalrrhh/servlet/ReportesOrganica");
    	modelRoot.put("param", "unidad="+unidad+"&recursivo="+recursivo+"&tipo="+tipo+"&empresa="+empresa+"&periodo="+periodo+"&page="+page);
    	modelRoot.put("param1", "param="+CallParam+"|excel=1&excel=1&tipo=repo");
    	super.retTemplate(resp,"Gestion/Reportes/carga.htm",modelRoot);
    }
}