package cl.eje.generico.ws;

import java.rmi.RemoteException;

import com.thoughtworks.xstream.XStream;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.tools.security.Encrypter;

public class SQLremoteAnalizer {
	private String urlSW;
	private Encrypter enc;	
	
	public static void main(String[] params) {
 
		SQLremoteAnalizer sw			= new SQLremoteAnalizer("http://200.48.137.21:8090/lucky/sqlremote"); //sw de ejemplo
		
		try { 
			ConsultaData data = sw.getData("SELECT * FROM rrhh_pm.e010maestro");
			if(data != null) {
				data.printTableOverConsole();
			}
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SQLremoteAnalizer() {
		this(null);
	}
	
	
	public SQLremoteAnalizer(String urlSW) {
		this.urlSW = urlSW;
		 enc = new Encrypter();
	}
 
	public Integer update(String query) throws Exception {
		ConectorProxy proxy = getProxy();
		String xml_encriptado = proxy.update(SQLremoteAnalizerToken.getInstance().createToken(), query);
		
		return (Integer) getObject(xml_encriptado);
	}

	 
	public Boolean delete(String query) throws Exception {
		ConectorProxy proxy = getProxy();
		String xml_encriptado = proxy.delete(SQLremoteAnalizerToken.getInstance().createToken(), query);
		
		return (Boolean) getObject(xml_encriptado);
	}

 
	public Boolean insert(String query) throws Exception {
		ConectorProxy proxy = getProxy();
		String xml_encriptado = proxy.insert(SQLremoteAnalizerToken.getInstance().createToken(), query);
		
		return (Boolean) getObject(xml_encriptado);
	}

 
	public ConsultaData getData(String query) throws Exception {
		ConectorProxy proxy = getProxy();
		String xml_encriptado = proxy.getData(SQLremoteAnalizerToken.getInstance().createToken(), query);
		
		return (ConsultaData) getObject(xml_encriptado);
	}
	
	private Object getObject(String xml_encriptado) throws Exception {
		XStream xs = new XStream();
		String xml = enc.decrypt(xml_encriptado);
		Object o =  xs.fromXML( xml );
		
		if(o instanceof ConsultaData) {
			return (ConsultaData) o;	
		}
		else if(o instanceof Exception) {
			Exception e = (Exception) o;
			
			Exception eFinal = new Exception("PROBLEMA REMOTO", e);
			throw eFinal;
		}
		else if(o instanceof RuntimeException) {
			RuntimeException e = (RuntimeException) o;
			
			Exception eFinal = new Exception("PROBLEMA REMOTO", e);
			throw eFinal;
		}
		else {
			System.out.println("[PROBLEMA GRAVE]");
			return null;
		}
		
	}
	
	private ConectorProxy getProxy() {
		if(this.urlSW == null) {
			return new ConectorProxy();
		}
		else {
			return new ConectorProxy(this.urlSW, true);
		}
		
	}
}
