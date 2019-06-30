package cl.ejedigital.web.frontcontroller.resobjects;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaPreparada;
import cl.ejedigital.tool.cache.Cache;
import cl.ejedigital.tool.cache.CacheLocator;
import cl.ejedigital.tool.singleton.Singleton;
import cl.ejedigital.web.CacheWebTypes;
import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.IDBConnectionManager;

/**
 * @deprecated
 * @since 2015-06-23
 * @author Francisco
 * */
public class ResourceMapping implements Singleton {
	private static ResourceMapping instance;
	private File realPath;
	private Cache mapCache;
	private IDBConnectionManager dbConnection;
	
	private ResourceMapping() {
		this.dbConnection = DBConnectionManager.getInstance();
		
		URL logurl = this.getClass().getResource("/");
		realPath = new File(logurl.getFile());
		realPath = realPath.getParentFile();
		realPath = realPath.getParentFile();
		
		mapCache = CacheLocator.getInstance(CacheWebTypes.CACHEMAPPINGRESOURCES);
	}
	
	public static ResourceMapping getInstance() {
		if(instance == null) {
			synchronized(ResourceMapping.class) {
				if(instance == null) {
					instance = new ResourceMapping();
				}
			}			
		}
		
		return instance;
	}
	
	
	public File getFile(String path) throws FileNotFoundException, SQLException {
		VoMapping vo = (VoMapping)mapCache.get(path);
		File file = null;
		
		if(vo == null) {
			vo = getMapping(path);
			mapCache.put("path",vo);
		}

		if(vo.isVigente()) {
			file = new File(realPath.getPath().replaceAll("\\%20"," ") + File.separator + "mapping" + vo.getPathMapeado());

			if(file == null || !file.exists()) {
				throw new FileNotFoundException( vo.getPathMapeado());
			}
		}
		else {
			try {
				file = urlFileCheck(path);
				
				if(file == null || !file.exists()) {
					throw new FileNotFoundException(path);
				}
			}
			catch (FileNotFoundException e) {
				file = new File(realPath.getPath().replaceAll("\\%20"," ") + File.separator + path);

				if(file == null || !file.exists()) {
					throw new FileNotFoundException( path );
				}
			}
		}
		
		return file;	
	}
	
	private File urlFileCheck(String pathFile) throws FileNotFoundException {
		File file = null;

		URL url = getClass().getResource(pathFile);

		if(url == null) {
			throw new FileNotFoundException(pathFile);
		}
		
		try {		
			file = new File(url.toURI());
			
		} catch (URISyntaxException e) {
			file = new File(url.getPath());
		}
		
		return file;
	}
	
	private synchronized VoMapping getMapping(String pathKey)  {
		VoMapping vo = new VoMapping();
		Connection connPortal = dbConnection.getConnection("portal");

		String sql = "SELECT id, path_original, path_mapeado, path_vigente ";
		sql += " FROM eje_generico_mapping ";
		sql += " WHERE path_original = ? ";
		String[] params = {pathKey};
		
		ConsultaPreparada consulta = new ConsultaPreparada(connPortal);
		ConsultaData data = null;
		try {
			data = consulta.getData(sql,params);
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(data.next()){	
			vo.setPathOriginal(data.getString("path_original"));
			vo.setPathMapeado(data.getString("path_mapeado"));
			vo.setVigente(data.getBoolean("path_vigente"));
		}

		dbConnection.freeConnection("portal",connPortal);
		
		return vo;		
	}
	
	
}

class VoMapping {
	private String pathOriginal;
	private String pathMapeado;
	private boolean vigente;
	
	
	public String getPathOriginal() {
		return pathOriginal;
	}
	
	public void setPathOriginal(String pathOriginal) {
		this.pathOriginal = pathOriginal;
	}
	
	public String getPathMapeado() {
		return pathMapeado;
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
