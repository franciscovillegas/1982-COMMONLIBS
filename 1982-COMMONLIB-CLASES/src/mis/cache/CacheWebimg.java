package mis.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import mis.tools.MyFile;

public class CacheWebimg{

	private File destFile;
	
	public CacheWebimg(String RutPersonita,String Strurl,ServletContext sc) throws IOException,UnknownServiceException {
		String VAR_url=null;
		String VAR_PathFiles=null;
		String VAR_PathFolder=null;
		String VAR_ContextoMIS=null;
		String FinalOutput=null;
	
		ResourceBundle proper = ResourceBundle.getBundle("db");
		VAR_ContextoMIS = proper.getString("mis.serverJGP.url");
		VAR_url = VAR_ContextoMIS + Strurl;
		VAR_PathFolder=proper.getString("mis.portal.carpetacache");
		VAR_PathFiles = sc.getRealPath(VAR_PathFolder);
		URL url = new URL(VAR_url);
		URLConnection urlCon = url.openConnection();
		InputStream is = urlCon.getInputStream();

		destFile = new MyFile(VAR_PathFiles + Strurl.substring(Strurl.lastIndexOf("/"),Strurl.length())).getAbsoluteFile();
		FileOutputStream fos = new FileOutputStream(destFile);

		// buffer para ir leyendo.
		byte [] array = new byte[1000];

		// Primera lectura y bucle hasta el final
		int leido = is.read(array);
		while (leido > 0) {
		   fos.write(array,0,leido);
		   leido=is.read(array);
		}

		// Cierre de conexion y fichero.
		is.close();
		fos.close();
	}
	
	public File getFileImg() {
		return destFile;
	}
	
}
