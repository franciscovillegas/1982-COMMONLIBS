package portal.com.eje.daemon;

import java.lang.annotation.Annotation;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import cl.eje.model.generic.portal.Eje_daemon_inscritos;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.daemon.error.NoDaemonValidException;
import portal.com.eje.daemon.error.NoGroupValidException;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.CtrGeneric;
import portal.com.eje.portal.vo.util.Wheres;

public class DaemonProcess {
	Logger logger = Logger.getLogger(DaemonProcess.class);
	
	public static DaemonProcess getInstance() {
		return Util.getInstance(DaemonProcess.class);
	}
	
	public boolean checkCronExpresion(String cronExpresion) {
		ConsultaData data = getNextExecutions(cronExpresion);
		
		return (data != null && data.size() >= 0);
	}
	
	public String getNextExecutionString(String cronExpresion) {
		 return Formatear.getInstance().toDate( getNextExecution(cronExpresion), "dd/MM/yyyy HH:mm:ss");
	}
	public Date getNextExecution(String cronExpresion) {
		ConsultaData data = getNextExecutions(cronExpresion);
		
		Date next = null;
		if(data != null && data.next()) {
			next =  data.getDateJava("fecha");
		}
		
		return next;
	}
	
	public ConsultaData getNextExecutions(String cronExpresion) {
		ConsultaData data = ConsultaTool.getInstance().newConsultaData(new String[] { "fecha" });

		try {
			if (cronExpresion != null) {
				cronExpresion = cronExpresion.trim();

				CronScheduleBuilder ceb = CronScheduleBuilder.cronSchedule(cronExpresion);
				Trigger trigger = TriggerBuilder.newTrigger().withIdentity("dummyTriggerName", "group1")
						.startNow()
						.withSchedule(ceb).build();

				//List<Date> dates = TriggerUtils.computeFireTimes( (OperableTrigger)trigger, null, 10);
				
				Date next = trigger.getFireTimeAfter(Calendar.getInstance().getTime());
				int count = 0;
				while(next != null && count<= 25) {
					count++;
					
					String fecha1 = Formatear.getInstance().toDate(next, "yyyy-MM-dd HH:mm:ss");
					DataFields df = new DataFields();
					df.put("fecha", next);
					data.add(df);
					
					next = trigger.getFireTimeAfter(next);
				}
				

				
			}
		} catch (Exception e) {

		}
		
		return data;
	}
	
	public boolean checkClase(String clase) {
		boolean ok = false;
		
		if(clase != null) {
			try {
				Class.forName(clase);
				ok = true;
			} catch (ClassNotFoundException e) {
				
			}
		}
		
		return ok;
	}
	
	/**
	 * Crea o actualiza el proceso
	 * @throws NoDaemonValidException 
	 * @throws NoGroupValidException 
	 * @throws SQLException 
	 * @throws NullPointerException 
	 * */

	public boolean updOrAdd(DaemonDefinition daemonDef) throws NoDaemonValidException, NoGroupValidException, NullPointerException, SQLException {
		if(daemonDef == null) {
			throw new NoDaemonValidException("is null");
		}
		
		if(daemonDef.getIdGrupo() == null) {
			throw new NoGroupValidException("is null");
		}
		
		Collection<Eje_daemon_inscritos> vos = CtrGeneric.getInstance().getAllFromClass(Eje_daemon_inscritos.class, Wheres.where("id_daemon", "=", daemonDef.getIdDaemon()).build());
		boolean existe = (vos != null && vos.size() > 0);
		
		if(!existe) {
			Eje_daemon_inscritos daemon = new Eje_daemon_inscritos();
			daemon.setId_daemon( daemonDef.getIdDaemon() );
			daemon.setId_grupo( daemonDef.getIdGrupo() );
			daemon.setNombre(daemonDef.getNombre());
			daemon.setActivado(daemonDef.isActivado());
			daemon.setPeridiosidad(daemonDef.getCronExpresion());
			daemon.setFecha_creacion(ConsultaTool.getInstance().getNow());
			daemon.setIcono("../../images/btns/gear_in.ico");
			daemon.setEjecu_clase(daemonDef.getClase());
			daemon.setEjecu_llamada_url(daemonDef.getLlamadaUrl());
			daemon.setEjecu_dos(daemonDef.getDos());
			
			CtrGeneric.getInstance().add(daemon);
		}
		else {
			Eje_daemon_inscritos daemon = vos.iterator().next();
			
			daemon.setId_daemon( daemonDef.getIdDaemon() );
			daemon.setId_grupo(daemonDef.getIdGrupo());
			daemon.setNombre(daemonDef.getNombre());
			daemon.setActivado(daemonDef.isActivado());
			daemon.setPeridiosidad(daemonDef.getCronExpresion());
			daemon.setFecha_update(ConsultaTool.getInstance().getNow());
			daemon.setIcono(daemonDef.getIcono());
			daemon.setOrden(daemonDef.getOrden());
			daemon.setEjecu_clase(daemonDef.getClase());
			daemon.setEjecu_llamada_url(daemonDef.getLlamadaUrl());
			daemon.setEjecu_dos(daemonDef.getDos());
			
			CtrGeneric.getInstance().upd(daemon);
		}
		
		DaemonGuard.getInstance().restartDaemons();
		
		return true;
	}

	public boolean del(DaemonDefinition daemonDef) throws SQLException {
		boolean ok = DaemonManager.getInstance().delDaemon(daemonDef.getIdDaemon());
		
		DaemonGuard.getInstance().restartDaemons();
		
		return ok;
	}

	@SuppressWarnings("unchecked")
	public DaemonConfiguration getGetDaemonConfiguracion(String classPath) {
		Class<? extends IPPMDaemon> clase = null;
		try {
			if(classPath != null) {
				clase  =(Class<? extends IPPMDaemon>) Class.forName(classPath);	
			}
			
		} catch (ClassNotFoundException e) {
			//logger.error("ClassNotFoundException "+classPath);
		}
		
		return getGetDaemonConfiguracion(clase);
	}
	
	public DaemonConfiguration getGetDaemonConfiguracion(Class<? extends IPPMDaemon> demonio) {
		
		if(demonio != null) {
			Annotation ano = demonio.getAnnotation(DaemonConfiguration.class);
			if(ano instanceof DaemonConfiguration) {
				return ((DaemonConfiguration) ano);
			}
		}
		
		return null;
		
	}
}
