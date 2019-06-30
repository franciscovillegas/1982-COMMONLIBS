package portal.com.eje.daemon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.util.Assert;

import cl.eje.model.generic.portal.Eje_daemon_inscritos;
import portal.com.eje.applistener.ContextInfo;
import portal.com.eje.daemon.vo.TreeDaemon;
import portal.com.eje.daemon.vo.UnidadDaemon;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Static;
import portal.com.eje.portal.organica.vo.IUnidadGenerica;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.tools.QuartzScheduler;
import portal.com.eje.tools.util.CronCreated;

public class DaemonGuard {
	private boolean isStart;
	private Logger logger = Logger.getLogger(DaemonGuard.class);
	private Map<Integer,CronCreated > jobsCreated;
	
	private DaemonGuard() {
		jobsCreated = new HashMap<Integer, CronCreated >();
	}
	
	public static DaemonGuard getInstance() {
		return Static.getInstance(DaemonGuard.class);
	}
	

	public synchronized void startIfThisContextIsActive() {
		int thisModulo = ParametroLocator.getInstance().getIDModulo();
		String contextName = ParametroLocator.getInstance().getClienteContext()+ParametroLocator.getInstance().getModuloContext();
		
		if (EModulos.getThisModulo() == EModulos.scheduler && PPMDaemon.getInstance().isActive(thisModulo) && !isStart) {
			logger.info("[ @@ DAEMON MANAGER START] on ["+contextName+"]");
			startDaemons();
		}
		 
	}

	/**
	 * Si el guardian ha iniciado los daemons entonces los detiene y los vuelve a iniciar.  </br>
	 * Si es que no se ha iniciado antes no hace nada, para eso debe ocuparse el método startDaemons. </br>
	 * Este método sirve cuando se han agregado o eliminado demonios de la definición.
	 * 
	 * @author Pancho
	 * @since 18-10-2018
	 * 
	 * */
	public synchronized boolean restartDaemons() {
		if(this.isStart) {
			stopDaemons();
			startDaemons();
		}
		
		return true;
	}
	
	/**
	 * Retorna true si es que los demonios están corriendo, si no, false
	 * 
	 * @author Pancho
	 * @since 18-10-2018
	 * */
	public boolean isStarted() {
		return new Boolean(this.isStart);
	}
	
	public synchronized boolean stopDaemons() {

		try {
			for(Entry<Integer, CronCreated> entry : this.jobsCreated.entrySet()) {
				
				JobDetail jobDetail = entry.getValue().getJob();
				Trigger trigger = entry.getValue().getTrigger();
				
				logger.debug("Daemon "+entry.getValue().getEje_daemon_inscritos().getId_daemon()+" "+entry.getValue().getEje_daemon_inscritos().getNombre()+" stop ");
				
				if(trigger != null) {
					QuartzScheduler.getInstance().unscheduleJob(trigger.getKey());
				}
				
				if(jobDetail != null) {
					QuartzScheduler.getInstance().interrupt(jobDetail);
					QuartzScheduler.getInstance().deleteJob(jobDetail);					
				}
			}
			
			this.jobsCreated.clear();
			
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		isStart = false;
		
		
		return true;
	}
	
	/**
	 * Construye los daemons a ejecutar, y los almacena en un map del objeto. si este método se llama por segunda vez entonces no hace nada. <br/>
	 * 
	 * @author Pancho
	 * @since 18-10-2018
	 * 
	 * */
	private synchronized boolean startDaemons() {
		if(this.isStart) {//si está iniciado retorna y no se inicia dos veces
			return false;
		}
		
		try {
			TreeDaemon tree = DaemonGrupo.getInstance().getGruposTree();
			List<IUnidadGenerica> genericas = tree.getRoot().findChilds("tipo", "proceso");
			
			for(IUnidadGenerica u : genericas) {
				UnidadDaemon daemon = (UnidadDaemon) u;
				if(!daemon.getActivado()) {
					continue;
				}
				
				Assert.isTrue(this.jobsCreated.get(daemon.getIdDaemon()) == null,"No debe haberse creado el job antes, solo debe estar una vez definido");
				
				Class[] defs = {Eje_daemon_inscritos.class};
				Object[] params = {daemon.getVoImported()};
				CronCreated create = QuartzScheduler.getInstance().doJobByCron(PPMDaemonCaller.class.getCanonicalName(), "doActions", defs, params, daemon.getPeridiosidad());
				create.setEje_daemon_inscritos((Eje_daemon_inscritos) daemon.getVoImported());
				
				String next = DaemonProcess.getInstance().getNextExecutionString(daemon.getPeridiosidad());
				
				logger.debug("Daemon "+daemon.getIdDaemon()+" "+daemon.getName()+" started, next ejecution= "+next);
				
				this.jobsCreated.put(daemon.getIdDaemon(), create);
			}
			
			this.isStart = true;
		}
		catch(Exception e) {
			logger.error(e);
		}
		
		
		
		
		return true;
	}
}
