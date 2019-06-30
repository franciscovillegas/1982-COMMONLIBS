package portal.com.eje.frontcontroller;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.frontcontroller.ioutils.IOUtilTracking;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.parametro.EParametroCore;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.tools.ClaseGenerica;
import portal.com.eje.tools.QuartzScheduler;
 

public class IOClaseWebSchedulerManager {
	private static IOClaseWebSchedulerManager instance;
	private Cronometro cro = null;
	private int secondToRefresh;
	private Logger log;
	private Map<String,String> trackingFunc;
	
	private IOClaseWebSchedulerManager() {
		secondToRefresh = (60 * 15); // cada 15 minutos
		log = Logger.getLogger(this.getClass());
		cro = new Cronometro();
		
		loadTrackingFunc();
	}
	
	/**
	 * El tracking solo funciona con servlets, pero si hay una url propia de tool o de EjeS o de EjeI se podrá trackear con este método, solo basta con agregar la referencia a eje_ges_tracking_func con campo isbeforerequest = 1
	 * */
	private void loadTrackingFunc() {
		trackingFunc = new HashMap<String, String>();
		String sql = " select url, descripcion from eje_ges_tracking_func where isbeforerequest = 1 ";
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql);
			while(data != null && data.next()) {
				trackingFunc.put(data.getString("url"), data.getString("descripcion"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
		
	public static IOClaseWebSchedulerManager getInstance() {
		if(instance == null) {
			synchronized (IOClaseWebSchedulerManager.class) {
				if(instance == null) {
					instance = new IOClaseWebSchedulerManager();
				}
			}
		}
		
		return instance;
	}
	
	public void tryToExec(IIOClaseWebLight io) {
		if(mustExec()) {
			synchronized (IOClaseWebSchedulerManager.class) {
				if(mustExec()) {
					execClases(io);
					cro.start();
				}
			}
		}
	}
	
	/**
	 * Tracking para los html=<página>
	 * @deprecated
	 * @author Pancho
	 * @since 01-06-2018
	 * */
	
	public void trackingBeforeResponse(IIOClaseWebLight io) {
		if( io.getUsuario().esValido()) {
			String call = io.getReq().getServletPath() + "?" + io.getReq().getQueryString();
			String d = this.trackingFunc.get(call);
			if( d != null) {
				io.getUtil(IOUtilTracking.class).insTracking(io , call, d, null);
			}
		}
		
	}
	
	private boolean mustExec() {
		boolean mustSend = false;
  
		mustSend = (cro.isStopped() || cro.GetMilliseconds() >= secondToRefresh * 1000);
		
		return mustSend;
	}
	
	/**
	 * Cada 15 minutos ejecuta el objeto
	 * */
	private boolean execClases(IIOClaseWebLight io) {
		Cronometro localCronometro = new Cronometro();
		String strClazzs = ParametroLocator.getInstance().getCoreValue(EParametroCore.ioprocess_every_15minutes);
		
		if(strClazzs != null) {
		 
			String[] clazz = strClazzs.split("\\;");
			for(String c : clazz) {
				/*
				ClaseGenerica cg = Util.getInstance(ClaseGenerica.class);
				Class[] defs = {};
				Object[] objs = {};
				*/
				//try {
					//cg.cargaConstructor(c, defs, objs);
					//Object o = cg.getNew(c, defs, objs);
					//if(o != null) {
						log.info("["+c+"]  Iniciando ejecución");
						
						
						Class[]  defMetod = {};
						Object[] defValues = {};
						
						QuartzScheduler.getInstance().doInstaJob("Hola", 
																 c,"doProcess",
																 defMetod,defValues);
						
						 
						
						log.info("["+c+"]  [" + localCronometro.GetTimeHHMMSS()+"]");
						/*
					}
					else {
						log.error("Error al ejecutar el procesoIO, el objeto cargado es NULL");
					}
					*/
					
//				} catch (ClassNotFoundException e) {
//					log.error("Error al ejecutar el procesoIO", e);
//					e.printStackTrace();
//				} catch (NoSuchMethodException e) {
//					log.error("Error al ejecutar el procesoIO", e);
//					e.printStackTrace();
//				} catch (InstantiationException e) {
//					log.error("Error al ejecutar el procesoIO", e);
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					log.error("Error al ejecutar el procesoIO", e);
//					e.printStackTrace();
//				} catch (InvocationTargetException e) {
//					log.error("Error al ejecutar el procesoIO", e);
//					e.printStackTrace();
//				} catch (Exception e) {
//					log.error("Error al ejecutar el procesoIO", e);
//					e.printStackTrace();
//				}
			}
		}
		 
		return true;
	}

	
}
