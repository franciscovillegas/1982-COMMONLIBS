package portal.com.eje.tools;

import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import portal.com.eje.portal.factory.Util;

public class QuartzSchedulerLocator {
	private SchedulerFactory factory;
	private Scheduler defaultSheduler;
	private Throwable e;
	private JListener listener;
	
	private QuartzSchedulerLocator() {
		 factory =  new StdSchedulerFactory();
		
	}
	
	public static QuartzSchedulerLocator getInstance() {
		return Util.getInstance(QuartzSchedulerLocator.class);
	}
	
	public static Scheduler getDefaultScheduler() throws SchedulerException {
		 
		return getInstance().getDefault();
	}
	
	private Scheduler getDefault() throws SchedulerException {
		if(defaultSheduler == null) {
			synchronized (QuartzSchedulerLocator.class) {
				if(defaultSheduler == null) {
					defaultSheduler = factory.getScheduler(); 
					
					listener = new JListener(defaultSheduler);
					 
					defaultSheduler.getListenerManager().addJobListener(listener);
					defaultSheduler.start();
				}
			}
		}
		
		return defaultSheduler;
	}
	
	public Throwable getLastError() {
		return e;
	}
	
	public void addListener(JobDetail job, JobListener jobListener) {
		listener.addListener(job, jobListener);
	}
}
