package cl.ejedigital.tool.misc;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;

import cl.ejedigital.tool.cache.Cache;
 

/**
 * Copia incompleta de la clase: <br>
 * portal.com.eje.frontcontroller.resobjects.ResourceHtml <br/>
 * 
 * */

public class ImagenPath {
 
 
		private File realPath;
		private Cache mapCache;
		private Connection conn;
		private Map<String,String> registroArchivos;
		private String folderMaping;
  
 
		
		ImagenPath() {
			/*
			 * Parte de la base que las clases entán en WEF-INF/classes/
			 * Entonces para encontrar la carpeta de origen
			 * */
			
			if(ExistContext()) {
				URL logurl = this.getClass().getResource("/");
				
				realPath = new File(logurl.getFile());
				realPath = realPath.getParentFile();
				realPath = realPath.getParentFile();
			}
			else {
				URL logurl = this.getClass().getResource("/");
				
				realPath = new File(logurl.getFile());
			}
			
			folderMaping       = realPath.getPath().replaceAll("\\%20".intern()," ".intern()).concat(File.separator).concat("temporal");
			
		}
		
		public String getTemporalPath() {
			return folderMaping;
		}
		
		private boolean ExistContext() {
			Context initContext;
			try {
				initContext = new InitialContext();
				Context c = (Context)initContext;
				c.lookup("java:/comp/env"); 
			}
			catch (NoInitialContextException e) {
				return false;
			}
			catch (NamingException e) {
				return false;
			}
			
			return true;
		}
}
