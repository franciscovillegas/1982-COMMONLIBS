package cl.eje.qsmcom.service;

import java.util.HashMap;
import java.util.ResourceBundle;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.properties.PropertiesTools;
import cl.ejedigital.web.datos.ConsultaTool;

public class GatewayManager implements IGateway {

	public static IGateway					instance;
	private HashMap<String, String>	jndiList	= new HashMap<String, String>();

	private GatewayManager() {
		init();
	}
	
	private void init() {
		try {
			int i = 0;
			PropertiesTools proTools = new PropertiesTools();

			if(proTools.existsBundle("db")) {
				String nemo = proTools.getString(ResourceBundle.getBundle("db"),"mac.ws.nemo","");
				String[] params = {String.valueOf(nemo.toUpperCase())};
				ConsultaData data =
					ConsultaTool.getInstance().getData("portal_gateway",
						"SELECT tipo,jndi FROM eje_ges_jndi_empresa WHERE nemo = ? ",params);

				while(data.next()) {
					i++;
					if("portal".equals(data.getString("tipo"))) {
						jndiList.put("jndiPortal",data.getString("jndi"));
					}
					if("mac".equals(data.getString("tipo"))) {
						jndiList.put("jndiMac",data.getString("jndi"));
					}
					if("winper".equals(data.getString("tipo"))) {
						jndiList.put("jndiWinper",data.getString("jndi"));
					}
				}
				if(i == 0) {
					jndiList = null;
				}
			}

		}
		catch (Exception e) {
			System.out.println("[GatewayManager][GatewayManager][Exception] No puedo cargar lista de jndi");
		}
	}

	public static IGateway getInstance() {
		if(instance == null) {
			synchronized(GatewayManager.class) {
				if(instance == null) {
					instance = new GatewayManager();
				}
			}
		}

		return instance;
	}

	public String jndi(GatewayJndi key) {
		if(jndiList == null || jndiList.size() == 0) {
			init();
		}
	
		if(key != null && jndiList != null) {
			return (String) jndiList.get(key.toString());
		}
		return null;
	}

}