package cl.eje.generico.consoleremote.ws;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.eje.generico.RemoteBase;
import cl.eje.generico.ws.SQLremoteAnalizerToken;

/**
 * Clase en construcción, así que mientras será deprecada para no usarla hasta que la terminemos.
 * 
 * @deprecated
 * @author Pancho
 * @since 21-07-2017
 * */
public class SystemRemote extends RemoteBase {
	
	public static void main(String[] ags) {
		SystemRemote sr = SystemRemote.createNewRemoteSystem("http://127.0.0.1:8090/genericoWS");
		String[] commands = {"notepad.exe"};
		String[] command2 = {"cmd"};
		
		try {
			Process p = sr.exec(command2);
			System.out.print(p);
			
			sr.printConsole(sr.getConsoleIn(p), System.out);
			sr.printConsole(sr.getConsoleErr(p), System.out);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private SystemRemote(String urlRemote) {
		super(urlRemote);
	}
	
	public static SystemRemote createNewRemoteSystem(String urlRemote) {
		return new SystemRemote(urlRemote);
	}
	
	public Process exec(String[] commands) throws Exception {
		ConectorSystemRemoteProxy cf = (ConectorSystemRemoteProxy) getProxy(ConectorSystemRemoteProxy.class);
		String arrayCommand = getXml(commands);
		String s = cf.exec(SQLremoteAnalizerToken.getInstance().createToken(), arrayCommand);
		
		Process p = (Process) getObject(s);
		return p;
	}
	
	private List<String> getConsole(Process proc, String key) throws Exception {
		ConectorSystemRemoteProxy cf = (ConectorSystemRemoteProxy) getProxy(ConectorSystemRemoteProxy.class);
		String retorno = cf.getConsole(SQLremoteAnalizerToken.getInstance().createToken(),getXml(proc) );
		
		Map consolas = (Map)getObject(retorno);
		List<String> lista = new ArrayList<String>();
		
		if(consolas.get(key) != null ) {
			lista = (List)consolas.get(key);
		}
	
		return lista;
	}
	
	public List<String> getConsoleIn(Process proc) throws Exception {
		List<String> lista = getConsole(proc, "stdin");
 
		return lista;
	}
	
	public List<String> getConsoleErr(Process proc) throws Exception {
		List<String> lista = getConsole(proc, "stderr");
		 
		return lista;
	 
	}
	
	public void printConsole(List<String> consolaStack, PrintStream p) {
		if(consolaStack != null) {
			for(String s: consolaStack) {
				if(p != null) {
					p.println(s);
				}
			}
		}
	}
}
