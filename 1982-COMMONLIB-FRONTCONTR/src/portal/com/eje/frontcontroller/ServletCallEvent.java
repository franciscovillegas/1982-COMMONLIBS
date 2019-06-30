package portal.com.eje.frontcontroller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;

import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.tool.properties.PropertiesTools;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;


public class ServletCallEvent {
	private static ServletCallEvent instance;
	private String bundleName;
	private String sql;
	private String keyString;
	private String keyCTrackingString;
	private String jndiDefault;
	private Boolean conClassTracking;
	private String fecANSII;
	private String server;
	
	private ServletCallEvent() {
		 sql = "INSERT INTO eje_generico_cweb_reg (rut, tiempo, fecha_ini, fecha_fin, server, clase, parametros) VALUES (?,?,?,?,?,?,?)";	
		 bundleName = "generico";
		 keyString = "generico.jndi.usa";
		 keyCTrackingString = "generico.tracking.enabled";
		 jndiDefault = null;
		 fecANSII = "yyyyMMdd HH:mm:ss";
		 
		InetAddress address;
		try {
			address = InetAddress.getLocalHost();
			server = address.getHostName();
		}
		catch (UnknownHostException e) {
			server = "N/D";
		}

	}
	
	public static ServletCallEvent getInstance() {
		if(instance == null) {
			synchronized(ServletCallEvent.class) {
				if(instance == null) {
					instance = new ServletCallEvent();
				}
			}
		}
		
		return instance;
	}
	
	private String getJndiDefault() {
		
		if(jndiDefault == null) {
			synchronized(ServletCallEvent.class) {
				if(jndiDefault == null) {
					PropertiesTools tool = new PropertiesTools();
					
					if(tool.existsBundle(bundleName)) {
						ResourceBundle b = ResourceBundle.getBundle(bundleName);
						jndiDefault = tool.getString(b,keyString,"");
					}
					else {
						jndiDefault ="";
					}
				}
			}
		}
		
		return jndiDefault;	
	}
	
	private boolean getConClassTracking() {
		
		if(conClassTracking == null) {
			synchronized(ServletCallEvent.class) {
				if(conClassTracking == null) {
					PropertiesTools tool = new PropertiesTools();
					
					if(tool.existsBundle(bundleName)) {
						ResourceBundle b = ResourceBundle.getBundle(bundleName);
						conClassTracking = new Boolean(tool.getString(b,keyCTrackingString,"false"));
					}
					else {
						conClassTracking =false;
					}
				}
			}
		}
		
		return conClassTracking;	
	}
	
	public boolean finishRequest(Class<?> c, String parametros, String rut, double timeInMillis, Calendar fecIni, Calendar fecFin) {
		
		if(getConClassTracking()) {

			Object[] params = {rut,
								((double) timeInMillis),
								Formatear.getInstance().toDate(fecIni.getTime(), fecANSII),
								Formatear.getInstance().toDate(fecFin.getTime(), fecANSII),
								Validar.getInstance().cutString(server,50),
								Validar.getInstance().cutString(c.getName(), 150),
								Validar.getInstance().cutString(parametros,500)};
			try {
				return ConsultaTool.getInstance().insert( getJndiDefault(), sql, params);
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
}
