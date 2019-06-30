package mis.httpservlet; 

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mis.html.HTMLdoc;
import mis.pila.Pila;

import organica.com.eje.ges.usuario.Usuario;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;

public class EntregaGrafo extends MyHttpServlet {
	public EntregaGrafo() {
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException, ServletException {
		doGet(req,resp);
	}
	
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    	throws IOException, ServletException {
    	ResourceBundle proper = ResourceBundle.getBundle("db");
    	Usuario user = Usuario.rescatarUsuario(req);
    	
    	String CallRuta = proper.getString("mis.versionJPG.url");
    	String periodo = req.getParameter("periodo");
    	String cliente = req.getParameter("cliente");
    	String idgrafo = req.getParameter("idgrafo");
    	String unidad = req.getParameter("unidad");
    	String esarea = req.getParameter("esarea");
    	String rut = user.getRutUsuario();
    	String RutaFisica = getServletContext().getRealPath("");
    	
    	CallRuta = CallRuta + "periodo=" + periodo + "&cliente=" + cliente + "&idgrafo="+idgrafo+"&unidad="+unidad+"&esarea="+esarea+"&rut="+rut; 
    	System.out.println("Ruta Grafo --->"+CallRuta);
    	
    	HTMLdoc htm = new HTMLdoc(CallRuta,getServletContext());
    	htm.SetrutSolicito(rut);
    	
    	OutputStream tmpOut = new ByteArrayOutputStream();
    	tmpOut = htm.doURLRequestWithImg(tmpOut);
        String tmpString = tmpOut.toString().replaceAll("/style/","../servlet/Tool?style/");
        tmpString = tmpString.replaceAll("/Style/","../servlet/Tool?style/");
        tmpString = tmpString.replaceAll("/scripts/","../servlet/Tool?scripts/");
        tmpString = tmpString.replaceAll("/images/","../servlet/Tool?images/");
    	
        retTexto(resp,tmpString);
    	
    }
}