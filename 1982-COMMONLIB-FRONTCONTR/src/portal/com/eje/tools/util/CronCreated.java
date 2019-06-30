package portal.com.eje.tools.util;

import java.util.Calendar;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Trigger;

import cl.eje.model.generic.portal.Eje_daemon_inscritos;
import cl.ejedigital.tool.misc.Cronometro;

public class CronCreated {
	private Trigger trigger;
	private JobDetail job;
	private Date fecCrate;
	private Cronometro cro;
	private Eje_daemon_inscritos eje_daemon_inscritos;
	
	public CronCreated(Trigger trigger, JobDetail job) {
		super();
		this.trigger = trigger;
		this.job = job;
		this.cro = new Cronometro();
		this.cro.start();
		
		this.fecCrate = Calendar.getInstance().getTime();
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public JobDetail getJob() {
		return job;
	}

	public Date getFecCrate() {
		return fecCrate;
	}
	
	public Cronometro getCronometro() {
		try {
			return (Cronometro) this.cro.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			
			return null;
		}
	}

	public Eje_daemon_inscritos getEje_daemon_inscritos() {
		return (Eje_daemon_inscritos) eje_daemon_inscritos.clone();
	}

	public void setEje_daemon_inscritos(Eje_daemon_inscritos eje_daemon_inscritos) {
		this.eje_daemon_inscritos = eje_daemon_inscritos;
	}
	
	
}
