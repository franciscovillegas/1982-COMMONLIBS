package portal.com.eje.daemon;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.portal.factory.Static;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.tools.ClaseGenerica;
import portal.com.eje.tools.QuartzScheduler;

/**
 * Antes se usaba, era la clase singleton que llamaba a los otros procesos, ahora se usa DaemonCaller
 * @deprecated
 * @see PPMDaemonCaller
 * */
public class PPMDaemonInstance extends AbsPPMDaemonCanLog implements IPPMDaemonManager,  JobListener {
	
	private Map<String,PPMADaemon> daemons;
	
	public PPMDaemonInstance() {

	
		daemons=new HashMap<String,PPMADaemon>();
	}
	
	public void manageAllDaemons() {
		PPMDaemonInstance ppmDaemon = Static.getInstance(PPMDaemonInstance.class);
		ppmDaemon.managerAllDaemonsUnicInstance();		
	}
	
	public synchronized void  managerAllDaemonsUnicInstance() {
		
		loadDaemons();
		for(String daemonKeyClass : daemons.keySet()) {
			try {
				PPMADaemon daemon = daemons.get(daemonKeyClass);
				
				if(daemon.getEndDate() == null && daemon.getInitDate() == null) {
					programaEjecucionDaemon(daemon);
				}
			}
			catch(Exception e) {
				this.logError(e.getMessage(), 0);
				e.printStackTrace();
			}
		}
	}
	
	private void programaEjecucionDaemon(PPMADaemon daemon) {
		String classString = daemon.getClassDaemon().getCanonicalName();
		this.logInfo(classString+" programado ", 0);
		
		Class[] defDoActions = {};
		Object[] paramDoActions = {};
		daemon.setInitDate(Calendar.getInstance().getTime());
		daemon.getCro().start();
		QuartzScheduler.getInstance().doInstaJob("job", classString, "doActions", defDoActions, paramDoActions, this);	
	}
	
	private void jobToBeExecutedDaemon(PPMADaemon daemon) {
		String classString = daemon.getClassDaemon().getCanonicalName();
		this.logInfo(classString+" jobToBeExecuted ", 0);
		
	}
	
	private void jobWasExecutedDaemon(PPMADaemon daemon) {
		String classString = daemon.getClassDaemon().getCanonicalName();
		this.logInfo(classString+" jobWasExecuted in "+daemons.get(classString).getCro().getTimeHHMMSS_milli(), 0);
		
		daemon.setInitDate(null);
		daemon.setEndDate(null);
	}
	
	private void jobExecutionVetoedDaemon(PPMADaemon daemon) {
		String classString = daemon.getClassDaemon().getCanonicalName();
		this.logInfo(classString+" jobExecutionVetoed ", 0);
		
		daemon.setInitDate(null);
		daemon.setEndDate(null);
	}
	
	
	
	private void loadDaemons() {
		
		ConsultaData daemonsData = PPMDaemon.getInstance().getDaemons();
		
		while(daemonsData != null && daemonsData.next()) {
			String claseStr = daemonsData.getString("dataadic1");
			boolean trueOrFalse = daemonsData.getBoolean("value");
			
			if(trueOrFalse) {
				try {
					tryTOCreateDaemon(claseStr);
				}
				catch(ClassNotFoundException e) {
					logError("ClassNotFoundException "+claseStr + " " + e.getMessage(), 0);
				}
				catch(NoSuchMethodException e) {
					logError("NoSuchMethodException "+claseStr + " " + e.getMessage(), 0);
				}
				catch(InstantiationException e) {
					logError("InstantiationException "+claseStr + " " + e.getMessage(), 0);
				}
				catch(IllegalAccessException e) {
					logError("IllegalAccessException "+claseStr + " " + e.getMessage(), 0);
				}
				catch(InvocationTargetException e) {
					logError("InvocationTargetException "+claseStr + " " + e.getMessage(), 0);
				}
				catch(Exception e) {
					logError("Exception "+claseStr + " " + e.getMessage(), 0);
				}
				
			}
		}
	}
	
	private void tryTOCreateDaemon(String claseStr) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		ClaseGenerica cg = Util.getInstance(ClaseGenerica.class);
		Class clase = Class.forName(claseStr);
		
		if( daemons.get(claseStr) == null) {
			 
			List<Class> listaIfaces = new ArrayList();
			for(Class i : clase.getInterfaces()) {
				listaIfaces.add(i);
			}
			
			if(listaIfaces.contains(IPPMDaemon.class)) {
				logInfo(claseStr+" loaded ", 0);
				
				PPMADaemon aDaemon = new PPMADaemon();
				aDaemon.setClassDaemon(clase);
				daemons.put(claseStr, aDaemon);	
			}
			else {
				logError(claseStr + " no implementa IPPMDaemon", 0);
			}
			 
		}
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext arg0) {
		String classStr = ( (String)arg0.getJobDetail().getJobDataMap().get("pathClass") );
		if(daemons.get(classStr) != null) {
			jobExecutionVetoedDaemon(daemons.get(classStr));
		}
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext arg0) {
		String classStr = ( (String)arg0.getJobDetail().getJobDataMap().get("pathClass") );
		if(daemons.get(classStr) != null) {
			jobToBeExecutedDaemon(daemons.get(classStr));
		}
	}

	@Override
	public void jobWasExecuted(JobExecutionContext arg0, JobExecutionException arg1) {
		String classStr = ( (String)arg0.getJobDetail().getJobDataMap().get("pathClass") );
		if(daemons.get(classStr) != null) {
			jobWasExecutedDaemon(daemons.get(classStr));
		}
		
	}
}
