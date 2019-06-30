package cl.ejedigital.tool.app;

import java.util.ResourceBundle;

import cl.ejedigital.tool.properties.PropertiesTools;

public class MyAplication {
	public static IMyAplication app;
	
	public static IMyAplication getInstance() {
		if(app == null) {
			PropertiesTools tools = new PropertiesTools();
			boolean esWeb = true; 
			
			if(tools.existsBundle("app")) {
				ResourceBundle proper = ResourceBundle.getBundle("app");
				String esWebProp = tools.getString(proper, "app.esweb", "true");
				
				if(!esWebProp.toLowerCase().equals("true")) {
					esWeb = false;
				}				
				
			}
			
			if(esWeb) {
				app = new MyAplicationWeb();
			}
			else {
				app = new MyAplicationJava();
			}
		}
		
		return app;
	}
	
	
}
