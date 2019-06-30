package portal.com.eje.frontcontroller.resobjects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.servlet.http.HttpServletRequest;
import javax.sound.sampled.LineUnavailableException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.misc.MyFile;
import cl.ejedigital.tool.misc.WeakCacheLocator;
import cl.ejedigital.tool.properties.PropertiesTools;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.applistener.ContextInfo;
import portal.com.eje.manager.DataTypeSqlServer;
import portal.com.eje.manager.MngrDatabase;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.serhumano.httpservlet.MyTemplateUbication;


public class ResourceMapping {
 
	private File realPath;
	private final String folderMaping;
	private final String folderWorkTemporal;
	private ConsultaData allMappings; 
	
	
	public boolean clear() {
		if(allMappings != null) {
		 	allMappings = null;
		}
		 
		
		return true;
	}
	
	private ResourceMapping() {
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
			
		folderMaping       = realPath.getPath().replaceAll("\\%20".intern()," ".intern()).concat(File.separator).concat("mapping");
		folderWorkTemporal = realPath.getPath().replaceAll("\\%20".intern()," ".intern()).concat(File.separator).concat("WEB-INF").concat(File.separator).concat("workDirectory");
	}
	
	public String getFolderMappingPath() {
		return folderMaping;
	}
	
	public String getFolderTmpWorkPath() {
		return folderWorkTemporal;
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
	
	public static ResourceMapping getInstance() {
		return Util.getInstance(ResourceMapping.class);
	}
	
	
	
	/**
	 * @deprecated
	 * @since 2016-03-29
	 * @author SUPER-PANCHO
	 * Se deprecó debido a que es encesaria la petición (HttpServletRequest) para obtener correctamente el contexto.
	 * */
	
	public File getFile( String path) throws FileNotFoundException, SQLException {
		return getFile(null, path);
	}

	public File getFile(HttpServletRequest req, String path) throws FileNotFoundException, SQLException {
		return getFile(MyTemplateUbication.SrcAndWebContent, req, path);
	}
	
	public File getFile(MyTemplateUbication ubicacion, HttpServletRequest req, String path) throws FileNotFoundException, SQLException {
		return (File)getObjectFile(ubicacion, req, path).get("File");
	}
	
	public Map<String, Object> getObjectFile(MyTemplateUbication ubicacion, HttpServletRequest req, String path) throws FileNotFoundException, SQLException {
		if(path !=  null && path.indexOf("?") != -1) {
			path = path.substring(1, path.indexOf("?"));
		}
		
		String sContext = null;
		
		if(req != null) {
			sContext = req.getContextPath();
		}
		else {
			if(ContextInfo.getInstance().getServletContextName() !=null ) {
				sContext = ContextInfo.getInstance().getServletContextName();
			}
			else {
				try {
					throw new LineUnavailableException("Debes definir el siguiente listener \"portal.com.eje.applistener.InitContext\" en el web.xml, en esta clase de guarda el contexto, luego se usa para obtener los objetos de la zona de recursos \"scr\" ");
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		
		
		Map<String,Object> mapResultado = new HashMap<String, Object>();
		VoMapping vo = getMapping(sContext , path);
		File file = null;

		mapResultado.put("path_mapping_vigente", String.valueOf(vo.isVigente()));
		mapResultado.put("path_allow", String.valueOf(ubicacion));
		
		if(vo.isVigente()) {
			mapResultado.put("path_original", vo.getPathOriginal());
			mapResultado.put("path_mapeado", "/webcontent/mapping" + vo.getPathMapeado());
			
			file = new File(folderMaping + vo.getPathMapeado());		
			if(file.exists()) {
				mapResultado.put("path_found", "/webcontent/mapping" + vo.getPathMapeado());
			}
		}
		
		if(file == null || !file.exists()) {	
			
				if(MyTemplateUbication.Src == ubicacion ) {
					mapResultado.put("path_original", "/src" + path);
					
					file = getFileFromSrc(file, path);
					if(file.exists()) {
						mapResultado.put("path_found", "/src" + path);
					}
				}
				
				if(MyTemplateUbication.SrcAndWebContent == ubicacion) {
					try {
						file = getFileFromSrc(file, path);
						if(file.exists()) {
							mapResultado.put("path_found", "/src" + path);
						}
						
					}
					catch (FileNotFoundException e) {
						file = getFileFromWebContent(file, path);
						mapResultado.put("path_original", "/webcontent" + path);
						if(file.exists()) {
							mapResultado.put("path_found", "/webcontent" + path);
						}
					}
				}
				
				if(MyTemplateUbication.Webcontent == ubicacion) {
					file = getFileFromWebContent(file, path);
					mapResultado.put("path_original", "/webcontent" + path);
					if(file.exists()) {
						mapResultado.put("path_found", "/webcontent" + path);
					}
				}

				if(file == null || !file.exists()) {
					String error = "@@ ERROR MAPEO NOT FOUND ";
					mapResultado.put("path_error", error); 
				}
			
		}
		
		mapResultado.put("File", file);
		return mapResultado;	
	}
	
	
	private File getFileFromSrc(File file, String path) throws FileNotFoundException {
		file = urlFileCheck(path);
		
		if(file == null || !file.exists()) {
			throw new FileNotFoundException(path);
		}
		
		return file;
	}
	private File getFileFromWebContent(File file, String path) throws FileNotFoundException {
		file = new File(realPath.getPath().replaceAll("\\%20".intern()," ".intern()) + File.separator + path);

		if(file == null || !file.exists()) {
			
			file = new File( ContextInfo.getInstance().getRealPath("templates") + File.separator + path);
			
			if(file == null || !file.exists()) {
				throw new FileNotFoundException( path );	
			}
		}
		
		return file;
	}
	
	/**
	 * Método cuya funcion es rescatar un archivo desde un archivo JAR, debido a que en TOMCAT 5.5+ no se puede
	 * extraer leer directamente un archivo
	 * 
	 * 
	 * @since 10 Abril 2013
	 * 
	 * */
	
	private File urlFileCheck(String pathFile) throws FileNotFoundException {
		InputStream in = getClass().getResourceAsStream(pathFile);
		
		if(in == null) {
			throw new FileNotFoundException(pathFile);
		}
		
		File fileOut = new File(new StringBuilder(folderWorkTemporal + pathFile).toString());
		
		ResourceBundle proper = ResourceBundle.getBundle("cache".intern()); 
		boolean debug = false;
		try {
			debug = Boolean.parseBoolean(proper.getString("cache.filecache.enabled".intern()));
		}
		catch (NullPointerException e) {
			
		}
		catch (MissingResourceException e) {
			
		}
		catch (ClassCastException e) {
			
		}
		
		if( !fileOut.exists() || !debug) {
			if( !fileOut.getParentFile().exists()) {
//				File file = new File("d:\\juan\\");
//				file.mkdirs();
				
				fileOut.getParentFile().mkdirs();
				
			}
			
			MyFile.getInstance().writeFile(fileOut, in);
			
			if( fileOut == null || !fileOut.exists() ) {
				throw new FileNotFoundException(pathFile);
			}
		}
		
		
		return fileOut;
	}
	
	@SuppressWarnings("unchecked")
	private VoMapping getMapping(String conStr, String pathKey)  {
		if(pathKey == null || pathKey.indexOf("data:image/png;base64") != -1) {
			return  new VoMapping();
		}
		
		
		VoMapping vo = (VoMapping) WeakCacheLocator.getInstance(getClass()).get(pathKey);
		
		if(vo == null) {
			//String[] params = {pathKey, conStr};
			
			ConsultaData data = null;
			String jndi = "portal";
			
			try {
				PropertiesTools tool = new PropertiesTools();
				
				if(tool.existsBundle("generico")) {
					jndi = tool.getString(ResourceBundle.getBundle("generico".intern()), "generico.jndi.usa".intern(), "portal".intern());
				}
				
				if(conStr != null) {
					//data = ConsultaTool.getInstance().getData(jndi, QueryMapping.getMapping.getSql() ,params);
					data = getAllMappings(jndi, conStr, pathKey);
				} 
				else {
					if(data == null || !data.next()) {
						String[] paramsSmiple = {pathKey};
						data = ConsultaTool.getInstance().getData(jndi, QueryMapping.getMappingGenerica.getSql() ,paramsSmiple);
					}
				}
			}
			catch (SQLException e) {

				MngrDatabase db = new MngrDatabase();
				data = db.getDefinition("portal","eje_generico_mapping","nombre_contexto");
				if(data == null || !data.next()) {
					db.alterTableAdd("portal","eje_generico_mapping","nombre_contexto", DataTypeSqlServer.Varchar,100);
				}
				
				try {
					if(conStr != null) {
						//data = ConsultaTool.getInstance().getData(jndi, QueryMapping.getMapping.getSql() ,params);
						data = getAllMappings(jndi, conStr, pathKey);
					} 
					else {
						if(data == null || !data.next()) {
							String[] paramsSmiple = {pathKey};
							data = ConsultaTool.getInstance().getData(jndi, QueryMapping.getMappingGenerica.getSql() ,paramsSmiple);
						}
					}
				}
				catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			
			vo = new VoMapping();
			while(data != null && data.next()){
				vo.setPathOriginal(  data.getString("path_original".intern()) );
				vo.setPathMapeado(  data.getString("path_mapeado".intern())  );
				vo.setVigente( data.getBoolean("path_vigente".intern()) );
			}
			
			WeakCacheLocator.getInstance(getClass()).put("path",vo);
		}
		
		
		
		return vo;		
	}
	
	private ConsultaData getAllMappings(String jndi, String contexto, String pathKey) {
		if(allMappings == null) {
			synchronized (ResourceMapping.class) {
				if(allMappings == null) {
					String[] paramsSmiple = {contexto};
					try {
						allMappings = ConsultaTool.getInstance().getData(jndi, QueryMapping.getAllMappings.getSql() ,paramsSmiple);
					} catch (SQLException e) {
						e.printStackTrace();
					}		
				}
			}
			
		}
		
		
		ConsultaData dataFinal = null;
		
		if(allMappings != null) {
			dataFinal = new ConsultaData(allMappings.getNombreColumnas());
			allMappings.toStart();
			
			while(allMappings != null && allMappings.next()) {
				if(pathKey != null      && pathKey.equals(allMappings.getForcedString("path_original")) ) {
					if(contexto != null && contexto.equals(allMappings.getForcedString("nombre_contexto")) ) {
						dataFinal.add(allMappings.getActualData());
					}
				}
			}
		}
		
		return dataFinal;
	}
	
}

enum  QueryMapping {
	getAllMappings        ("SELECT id, path_original, path_mapeado, path_vigente,nombre_contexto FROM eje_generico_mapping WHERE path_vigente = 1 and ( nombre_contexto = ? ) "),
	getMapping            ("SELECT id, path_original, path_mapeado, path_vigente,nombre_contexto FROM eje_generico_mapping WHERE path_vigente = 1 and path_original = ? and  ( nombre_contexto = ? )"),
	getMappingGenerica    ("SELECT id, path_original, path_mapeado, path_vigente,nombre_contexto FROM eje_generico_mapping WHERE path_vigente = 1 and path_original = ? ");
	private String sql;
	
	QueryMapping(String sql) {
		this.sql = sql;
	}
	
	public String getSql() {
		return sql;
	}
}

class VoMapping {
	private String pathOriginal;
	private String pathMapeado;
	private boolean vigente;
	
	
	private String convertRutasWindowOrLinux(String ruta) {
		StringBuilder str = new StringBuilder();
		
		if(ruta != null) {

			for(int i = 0 ; i< ruta.length();i++) {
				String c = ruta.substring(i, i+1);
				
				if("/".equals(c)) {
					str.append(File.separator);
				}
				else {
					str.append(c);	
				}
				
			}

		}
		
		return str.toString();
	}
	
	public String getPathOriginal() {
		return convertRutasWindowOrLinux(pathOriginal);
	}
	
	public void setPathOriginal(String pathOriginal) {
		this.pathOriginal = pathOriginal;
	}
	
	public String getPathMapeado() {
		
		return convertRutasWindowOrLinux(pathMapeado);
	}
	
	public void setPathMapeado(String pathMapeado) {
		this.pathMapeado = pathMapeado;
	}
	
	public boolean isVigente() {
		return vigente;
	}
	
	public void setVigente(boolean vigente) {
		this.vigente = vigente;
	}
	
	public String toString() {
		return pathOriginal + "=[" +pathMapeado + ":"+vigente+"]";
	}
	
}
