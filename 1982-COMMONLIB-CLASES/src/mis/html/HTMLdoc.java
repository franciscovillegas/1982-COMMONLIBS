package mis.html;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import mis.cache.CacheWebimg;

public class HTMLdoc {
	private ServletContext sc; 
	private String Var_URL = null;
	
	private String Var_RutSolicita=null;
	private InputStream in = null;
	private HTMLjerarquia jerarquia = null;
	
	public HTMLdoc(String strURL, ServletContext sc) {
		try {
			Var_URL = strURL;
			this.sc = sc;
			URL urlOriginal;
			urlOriginal = new URL(strURL);
			URLConnection c = urlOriginal.openConnection();
			c.connect();
			in = c.getInputStream();
			jerarquia = new HTMLjerarquia(in); 
			jerarquia.CargarTodosLosNodos();
		}
		catch (IOException e) {		
			e.printStackTrace();
		}
	}
	
	public void SetrutSolicito(String Rut) {
		//Este rut se antepone como prefijo en el nombre de la imagen para 
		//luego identificarla mas facilmente
		Var_RutSolicita = Rut;
	}
	
	public HTMLiterator GetIterator(HTMLtag tag) {
		HTMLiterator ite =null;
		ite= new HTMLiterator(jerarquia.GetNodoCabezera(),tag);
		return ( ite );
	}
	
	public String GetrutSolicito() {
		if(Var_RutSolicita==null) {
			return ("SR");
		}
		else {
			return (Var_RutSolicita);
		}
	}
	
	public OutputStream doURLRequestWithImg(OutputStream stream) 
		throws IOException {
		ResourceBundle proper = ResourceBundle.getBundle("db");
		String folderImg = proper.getString("mis.portal.carpetacache");
		
		HTMLpropiedad p = null;
		CacheWebimg img=null;
		for(HTMLiterator i = new HTMLiterator(jerarquia.GetNodoCabezera(),HTMLtag.IMG);i.valor != null;i.Siguiente()) {
			p = i.valor.GetPropiedad(HTMLproperty.SRC);
			
			try {
				img = new CacheWebimg(this.GetrutSolicito(),p.GetValor(),sc);
				p.SetValor("'../"+folderImg+"/"+img.getFileImg().getName()+"'");
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		 
		char[] pagina = jerarquia.GetPagina().toCharArray();
		 
		for (int i=0;i<pagina.length;i++) {
			stream.write((int) pagina[i]);
		}
		return (stream);
	}
}