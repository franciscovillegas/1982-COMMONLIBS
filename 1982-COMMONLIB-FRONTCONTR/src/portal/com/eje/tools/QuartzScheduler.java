package portal.com.eje.tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.tools.QuartzSchedulerConfiguration.QuartzSchedulerConfigurationParam;
import portal.com.eje.tools.util.CronCreated;

public class QuartzScheduler {
	private Logger logger = Logger.getLogger(getClass());
	private int procesosLanzados;
	private List<Trigger> triggers;
	private boolean printByConsole;
	
	public QuartzScheduler() {
		
		triggers = new ArrayList<Trigger>();
		logger.debug("[SCHEDULER: Init]");
		
		QuartzSchedulerConfiguration qsc = new QuartzSchedulerConfiguration();
		printByConsole = qsc.getParamBoolean(QuartzSchedulerConfigurationParam.printByConsole);
	}
	
	public static QuartzScheduler getInstance() {
		return Util.getInstance(QuartzScheduler.class);
	}
	
	public void init(ServletConfig cfg) {
       getScheduler();
	
	}
	
	public void destroy() {
		destroy(0, null);
	}
	
	/**
	 * @deprecated
	 * */
	public void destroy(ServletConfig cfg) {
		destroy(0, cfg);
	}
	
	public void destroy(int repeticion, ServletConfig cfg) {
		logger.debug("[SCHEDULER: Try("+repeticion+") to destroy ]");
		 try {
			Scheduler s = QuartzSchedulerLocator.getDefaultScheduler();
			int pendings = 0;
			
	
			List<JobExecutionContext> stillsExecuting = s.getCurrentlyExecutingJobs();
			for(JobExecutionContext job : stillsExecuting) {
				logger.debug("\t[SCHEDULER: stillsExecuting - "+job.getTrigger().getKey()+"]");
				boolean deleted = s.deleteJob(job.getTrigger().getJobKey());
				logger.debug("\t[SCHEDULER: job deleted - "+deleted);
				try {
					boolean interrupt = s.interrupt(job.getTrigger().getJobKey());
					logger.debug("\t[SCHEDULER: job interrupted - "+interrupt);					
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				pendings++;
			}
			
			if(pendings == 0) {
				logger.debug("\t[SCHEDULER: no pending ] ");
				
			}
			else {
				logger.debug("\t[SCHEDULER: "+pendings+" pendings ] ");
			}
			
			s.shutdown(true);
			 
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		 
	}
	
	/**
	 * La clase QuartzSchedulerLocator entrega la única instancia de Scheduler
	 * @deprecated
	 * @see portal.com.eje.tools.QuartzSchedulerLocator
	 * */
	public Scheduler getScheduler() {
		try {
			return QuartzSchedulerLocator.getDefaultScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void doInstaJob(Object o, String pathClass, String metodoName, Class<?>[] defMetod, Object[] defValues) {
		doInstaJob(o, pathClass, metodoName, defMetod, defValues, null);
	}
	
	public Map<String,Object> doInstaJob(Object o, String pathClass, String metodoName, Class<?>[] defMetod, Object[] defValues, JobListener listener) {

		if(pathClass == null || metodoName == null ) {
			return null;
		}
		
		
		String key 		= pathClass.concat("_").concat(String.valueOf(Math.random()));
		
		JobKey jobKeyA = new JobKey("job_".concat(key), metodoName);
		JobDetail job = JobBuilder.newJob(CallerProcess.class)
		    .withIdentity(jobKeyA)
		    .usingJobData("pathClass"	, pathClass)
		    .usingJobData("metodoName"	, metodoName)
		    .usingJobData("paramDef"	, MyXstream.getXstream().toXML(defMetod))
		    .usingJobData("paramValues"	, MyXstream.getXstream().toXML(defValues))
		    .build();
		
 
 
		Date runTime = evenMinuteDate(new Date()  );

		Trigger trigger =  TriggerBuilder
			.newTrigger()
		    .withIdentity("trigger_".concat(key), metodoName)
		    .startAt(runTime)
		    .build();
		triggers.add(trigger);
		
		try {
			lanzaProceso();
			
			if( listener != null) {
				QuartzSchedulerLocator.getInstance().addListener(job, listener);
			}
			
			QuartzSchedulerLocator.getDefaultScheduler().scheduleJob(job,trigger);
			
		}
		catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("Trigger", trigger);
		map.put("JobDetail", job);
		
		return map;
	}
	
	public Map<String,Object> doForeverJob(String pathClass, String metodoName, Class<?>[] defMetod, Object[] defValues, int everyHowManySeconds) {

		if(pathClass == null || metodoName == null ) {
			return null;
		}
		
 
		String key 		= pathClass.concat("_").concat(String.valueOf(Math.random()));
		
		JobKey jobKeyA = new JobKey("job_".concat(key), metodoName);
		JobDetail job = JobBuilder.newJob(CallerProcess.class)
		    .withIdentity(jobKeyA)
		    .usingJobData("pathClass"	, pathClass)
		    .usingJobData("metodoName"	, metodoName)
		    .usingJobData("paramDef"	, MyXstream.getXstream().toXML(defMetod))
		    .usingJobData("paramValues"	, MyXstream.getXstream().toXML(defValues))
		    .build();
		
 
		 ScheduleBuilder<?> scheduleBuilder = SimpleScheduleBuilder.
	                simpleSchedule().
	                withIntervalInSeconds(everyHowManySeconds).
	                repeatForever();
		 
		Date runTime = evenMinuteDate(new Date()  );

		Trigger trigger =  TriggerBuilder
			.newTrigger()
		    .withIdentity("trigger_".concat(key), metodoName)
		    .startAt(runTime)
		    .withSchedule(scheduleBuilder)

		    .build();
		triggers.add(trigger);
		
		try {
			lanzaProceso();
			QuartzSchedulerLocator.getDefaultScheduler().scheduleJob(job,trigger);
			
		}
		catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("Trigger", trigger);
		map.put("JobDetail", job);
		
		return map;
	}
	
	public CronCreated doJobByCron(String pathClass, String metodoName, Class<?>[] defMetod, Object[] defValues, String peridiosidad) {
		if(pathClass == null || metodoName == null ) {
			return null;
		}
		

		String key 		= pathClass.concat("_").concat(String.valueOf(Math.random()));
		
		JobKey jobKeyA = new JobKey("job_".concat(key), metodoName);
		JobDetail job = JobBuilder.newJob(CallerProcess.class)
		    .withIdentity(jobKeyA)
		    .usingJobData("pathClass"	, pathClass)
		    .usingJobData("metodoName"	, metodoName)
		    .usingJobData("paramDef"	, MyXstream.getXstream().toXML(defMetod))
		    .usingJobData("paramValues"	, MyXstream.getXstream().toXML(defValues))
		    .build();
		
 
		 
		Date runTime = evenMinuteDate(new Date()  );
		CronScheduleBuilder ceb = CronScheduleBuilder.cronSchedule(peridiosidad);
		
		Trigger trigger =  TriggerBuilder
			.newTrigger()
		    .withIdentity("trigger_".concat(key), metodoName)
		    .startAt(runTime)
		    .withSchedule(ceb)

		    .build();
		triggers.add(trigger);
		
		try {
			lanzaProceso();
			QuartzSchedulerLocator.getDefaultScheduler().scheduleJob(job,trigger);
			
		}
		catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		CronCreated cronCreated = new CronCreated(trigger, job);	
		return cronCreated;
		
	}

	
	private void lanzaProceso() {
		procesosLanzados++;
		
		if(printByConsole) {
			logger.debug("[{QuartzScheduler} (+1) Total hilos Lanzados: "+procesosLanzados+"]");
		}
	}
	
	public void procesoTermino(JobExecutionContext arg0) {
		procesosLanzados--;
		
		if(printByConsole) {
			logger.debug("[{QuartzScheduler} (-1) Total hilos Lanzados: "+procesosLanzados+"]");
		}
		
	}
	
	private Date evenMinuteDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, ((int)(Math.random() * 10)));

		return cal.getTime();
	}
	
	public boolean deleteJob(JobDetail jobDetail) throws SchedulerException {
		if(jobDetail != null) {
			QuartzSchedulerLocator.getDefaultScheduler().deleteJob(jobDetail.getKey());
		
		}
		return true;
	}
	
	public boolean interrupt(JobDetail jobDetail) throws SchedulerException {
		if(jobDetail != null) {
			QuartzSchedulerLocator.getDefaultScheduler().interrupt(jobDetail.getKey());
		
		}
		return true;
	}
	
	public boolean unscheduleJob(TriggerKey triggerKey) throws SchedulerException {
		if(triggerKey != null) {
			QuartzSchedulerLocator.getDefaultScheduler().unscheduleJob(triggerKey);
		
		}
		return true;
	}


	
	
}


class JListener implements JobListener {
	private Logger logger = Logger.getLogger(getClass());
	private boolean printByConsole;
	private Map<JobDetail,JobListener> listeners;
	private Scheduler scheduler;
	
	public JListener(Scheduler scheduler) {
		QuartzSchedulerConfiguration qsc = new QuartzSchedulerConfiguration();
		printByConsole = qsc.getParamBoolean(QuartzSchedulerConfigurationParam.printByConsole);
		
		this.scheduler = scheduler;
		listeners = new HashMap<JobDetail, JobListener>();
	}

	public void addListener(JobDetail job, JobListener listener) {
		if(job != null && listener != null) {
			listeners.put(job, listener);
		}
	}

	public String getName() {
		return "ejemplo1";
	}

	public void jobExecutionVetoed(JobExecutionContext arg0) {
		if(printByConsole) {
			logger.debug("jobExecutionVetoed-->"+getJobLegible(arg0));	
		}
		
		if(listeners.get(arg0.getJobDetail()) != null) {
			try {
				listeners.get(arg0.getJobDetail()).jobExecutionVetoed(arg0);
			}
			catch(Exception e) {
				
			}
		}
	}

	public void jobToBeExecuted(JobExecutionContext arg0) {
		if(printByConsole) {
			logger.debug("jobToBeExecuted-->"+getJobLegible(arg0));
		}
		
		if(listeners.get(arg0.getJobDetail()) != null) {
			try {
				listeners.get(arg0.getJobDetail()).jobToBeExecuted(arg0);
			}
			catch(Exception e) {
				
			}	
		}
	}
	
	public void jobWasExecuted(JobExecutionContext arg0, JobExecutionException arg1) {
		QuartzScheduler.getInstance().procesoTermino(arg0);
	 	
		if(printByConsole) {
			logger.debug("jobWasExecuted-->"+getJobLegible(arg0));
		}
		
 
		if(listeners.get(arg0.getJobDetail()) != null) {
			try {
				listeners.get(arg0.getJobDetail()).jobWasExecuted(arg0, arg1);
				
			}
			catch(Exception e) {
				
			}
		}	
			
		
		
		
		try {
			boolean tieneNext = false;
			List<? extends Trigger> listT = scheduler.getTriggersOfJob(arg0.getJobDetail().getKey());
			for (Trigger t : listT) {
				if (t.getNextFireTime() != null) {
					tieneNext = true;
					break;
				}
			}

			if (tieneNext == false) {
				listeners.remove(arg0.getJobDetail());
				QuartzScheduler.getInstance().deleteJob(arg0.getJobDetail());
			}

		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		 
	}
	
	private String getJobLegible(JobExecutionContext job) {
	 
		return "{key:<"+job.getJobDetail().getKey()+">}{"+job.getJobDetail().getJobDataMap().getString("pathClass")+ "."+job.getJobDetail().getJobDataMap().getString("metodoName")+"()}";
		 
	}
 
}