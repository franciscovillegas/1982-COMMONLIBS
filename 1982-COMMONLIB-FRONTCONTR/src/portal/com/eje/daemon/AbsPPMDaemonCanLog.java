package portal.com.eje.daemon;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.apache.log4j.Level;

import cl.eje.model.generic.portal.Eje_daemon_generico_log;
import cl.ejedigital.tool.validar.Validar;
import portal.com.eje.daemon.PPMDaemonDefinition.PPMDaemonDefinitionParam;
import portal.com.eje.portal.factory.Ctr;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.vo.CtrGeneric;

public abstract class AbsPPMDaemonCanLog {
	protected String server;
	protected StringBuilder log;
	
	public AbsPPMDaemonCanLog() {
		InetAddress address;
		try {
			address = InetAddress.getLocalHost();
			server = address.getHostName();	
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log = new StringBuilder();
	}
	
	public void logInfo(CharSequence msg, int idDaemon) {
		log(msg, Level.INFO, idDaemon);
	}
	
	public void logDebug(CharSequence msg, int idDaemon) {
		log(msg, Level.DEBUG, idDaemon);
	}
	
	protected void logError(CharSequence msg, int idDaemon) {
		log(msg, Level.ERROR, idDaemon);
	}
		
	protected void log(CharSequence msg, Level level, int idDaemon) {
		PPMDaemonDefinition ppmDef = new PPMDaemonDefinition();
		
		if(ppmDef.getParamBoolean(PPMDaemonDefinitionParam.printByConsole)) {
			System.out.println(String.valueOf(level)+" "+msg);
		}
		
		if(!ppmDef.getParamBoolean(PPMDaemonDefinitionParam.logAll)) {
			return;
		}
		
		Eje_daemon_generico_log log = new Eje_daemon_generico_log();
		log.setClase(this.getClass().getCanonicalName());
		log.setId_modulo(ParametroLocator.getInstance().getIDModulo());
		log.setFecha(Calendar.getInstance().getTime());
		log.setServer(Validar.getInstance().cutStringSinComillas(server,50));
		log.setMessage(Validar.getInstance().cutStringSinComillas(msg.toString(),250));
		log.setLevel_int(level.toInt());
		log.setId_daemon(idDaemon);
		
		CtrGeneric ctrGeneric = Ctr.getInstance(CtrGeneric.class);
		Collection cols = new ArrayList();
		cols.add(log);
		
		try {
			ctrGeneric.updOrAdd(cols);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		this.log.append(msg).append("\n");
	}
	
	public StringBuilder getLog() {
		return new StringBuilder(log.toString());
	}

	public void clear() {
		this.log.delete(0, log.length());
	}
	
}
