package cl.eje.generico.ws;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import com.thoughtworks.xstream.XStream;

import portal.com.eje.tools.security.Encrypter;

public class SQLremoteAnalizerToken {
	private static SQLremoteAnalizerToken instance;
	
	private SQLremoteAnalizerToken() {
		
	}
	
	public static SQLremoteAnalizerToken getInstance() {
		if(instance == null) {
			synchronized (SQLremoteAnalizerToken.class) {
				if(instance == null) {
					instance = new SQLremoteAnalizerToken();
				}
			}
		}
		
		return instance;
	}
	
	public String createToken() {
		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			InetAddress address = InetAddress.getLocalHost();
			String server = address.getHostName();
			ResourceBundle rs = ResourceBundle.getBundle("token");
			
			map.put("machine"   	 , server);
			map.put("time_token"	 , Calendar.getInstance().getTime());
			map.put("keypassword"	 , rs.getString("accept.keypassword"));
			
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		XStream xs = new XStream();
		String xml = xs.toXML(map);
		
		Encrypter enc = new Encrypter();
		String xmlEncripted = enc.encrypt(xml);
				
		return xmlEncripted;
	}
	
	public boolean validateToken(String token) {
		boolean ok = true;
		
		try  {
			XStream xs = new XStream();
			Encrypter enc = new Encrypter();
			Map<String, Object> xml = (Map<String, Object>)xs.fromXML(enc.decrypt(token));
			
			ResourceBundle rs = ResourceBundle.getBundle("token");
			String strAcceptMachine = rs.getString("accept.machine");
			String remoteMachine 	= (String) xml.get("machine");
			String remoteKeyPassword 		= (String) xml.get("keypassword");
			String strAcceptPassword		= (String) xml.get("keypassword");
			
			boolean permite = false;
			{ 
				//VERIFICA QUE LA MÁQUINA SEA CONOCIDA, LO HACE POR EL NOMBRE DE ESTA
				if(strAcceptMachine != null && "*".equals(strAcceptMachine.trim())) {
					permite = true;
				}
				else if(strAcceptMachine != null && !"".equals(strAcceptMachine.trim())) {
					String[] machines = strAcceptMachine.split("\\,");
					
					for(String m :  machines) {
						if(m != null &&  remoteMachine != null && m.equals(remoteMachine) ) {
							permite = true;
						}
					}
				}
				
				if(!permite) {
					System.out.println("La máquina \""+remoteMachine+"\" no es conocida");
					ok = false;
				}
				else {
					ok &= true;	
				}
			}
			
			boolean mismoKeyPassword = false;
			{
				//VERIFICA QUE EL KEYPASSWORD SEA CONOCIDO
				if(strAcceptPassword != null &&  remoteKeyPassword != null && strAcceptPassword.equals(remoteKeyPassword)) {
					mismoKeyPassword = true;
				}
				else  {
					mismoKeyPassword = false;
				}
				
				if(!mismoKeyPassword) {
					ok = false;
					System.out.println("Las keyPassword no son iguales:    [LOCAL LKP: \""+strAcceptPassword+"\" REMOTE LKP:"+remoteKeyPassword+"]");
				}
				else {
					ok &= true;	
				}
			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
			ok = false;
		}
		
		
		return ok;
	}
}
