package cl.ejedigital.tool.app;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import cl.ejedigital.tool.properties.IPropertiesTools;
import cl.ejedigital.tool.properties.PropertiesTools;

public class MyAplicationWeb implements IMyAplication {
	private final int nivelesBackDefault = 2;
	private int nivelesBack;
	
	MyAplicationWeb() {
	}
	
	
	public String getPathProyecto() {
		
		IPropertiesTools properTools = new PropertiesTools();
		
		if(!properTools.existsBundle("app")) {
			nivelesBack = nivelesBackDefault;
			
		} else {
			ResourceBundle proper =  ResourceBundle.getBundle("app");
			nivelesBack = properTools.getInt(proper, "app.clases.backnivel", nivelesBackDefault);
			
		}
		
		URL url = MyAplicationWeb.class.getProtectionDomain().getCodeSource().getLocation();
		
		File path = null;
		
		if(url != null) {
			String fullPath = url.getPath();
			fullPath = new File(fullPath).getAbsolutePath();
			
			path = new File(fullPath.substring(0, fullPath.lastIndexOf(File.separator)));
			
			for(int i = 0; i < nivelesBack ; i++) {
				path = new File(path.getParent());
			}
			
			return path.getAbsolutePath();
		}
		else {
			return null;
		}

	}
	
	public String getPathClasses() {
		IPropertiesTools properTools = new PropertiesTools();
		
		
		if(!properTools.existsBundle("app")) {
			nivelesBack = nivelesBackDefault;
			
		} else {
			ResourceBundle proper =  ResourceBundle.getBundle("app");
			nivelesBack = properTools.getInt(proper, "app.clases.backnivel", nivelesBackDefault);
			
		}
		
		URL url = MyAplicationWeb.class.getProtectionDomain().getCodeSource().getLocation();
		
		File path = null;
		
		if(url != null) {
			String fullPath = url.getPath();
			fullPath = new File(fullPath).getAbsolutePath();

			path = new File(fullPath.substring(0, fullPath.lastIndexOf(File.separator)));
			path = new File(path.getParent());
			path = new File(path + File.separator + "classes");
			
			return path.getAbsolutePath();
		}
		else {
			return null;
		}

	}
	
	public String getPathProyecto( String pathRelative) {
		return new File(getPathProyecto() + File.separator + pathRelative).getAbsolutePath();
	}


	public String getPathClasses(String pathRelative) {
		return new File(getPathClasses() + File.separator + pathRelative).getAbsolutePath();
	}
	
	
}
