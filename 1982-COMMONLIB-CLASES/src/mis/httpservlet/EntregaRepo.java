package mis.httpservlet; 

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mis.MyProxyDemo;

import organica.com.eje.ges.usuario.Usuario;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class EntregaRepo extends MyHttpServlet {
	public EntregaRepo() {
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException, ServletException {
		doGet(req,resp);
	}
	
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    	throws IOException, ServletException {
    	ResourceBundle proper = ResourceBundle.getBundle("db");
    	Usuario user = Usuario.rescatarUsuario(req);
    	
    	String CallRuta = proper.getString("mis.versionREPO.url");
    	String periodo = req.getParameter("periodo");
    	String cliente = proper.getString("mis.IDCliente_de_este_portal");
    	String idrep = req.getParameter("idrep");
    	String unidad = req.getParameter("unidad");
    	String esarea = req.getParameter("esarea");
    	String excel = req.getParameter("excel");
    	String rut = user.getRutUsuario();
    	
    	String rutaExcel = "ServletCallMisRepo?periodo=" + periodo + "&cliente=" + cliente + "&idrep="+idrep+"&unidad="+unidad+"&esarea="+esarea+"&rut="+rut+"&excel=1";
    	CallRuta = CallRuta + "periodo=" + periodo + "&cliente=" + cliente + "&idrep="+idrep+"&unidad="+unidad+"&esarea="+esarea+"&rut="+rut; 


    	System.out.println(CallRuta);
        MyProxyDemo pd = new MyProxyDemo();
        
        OutputStream tmpOut = new ByteArrayOutputStream();
        tmpOut = pd.doURLRequest(CallRuta,tmpOut);
        String tmpString = tmpOut.toString().replaceAll("/style/","../servlet/Tool?style/");
        tmpString = tmpString.replaceAll("/Style/","../servlet/Tool?style/");
        tmpString = tmpString.replaceAll("/scripts/","../servlet/Tool?scripts/");
        tmpString = tmpString.replaceAll("/images/","../servlet/Tool?images/");
        tmpString = tmpString.replaceAll("document.frmmis.submit\\(\\);","document.location = '"+rutaExcel+"'");
                
        
	    if ("1".equals(excel)){
	    	retExcel(resp,tmpString,"reporte.xls");
	    }
	    else {
	        retTexto(resp,tmpString);
    	}
    }
}