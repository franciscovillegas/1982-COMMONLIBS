package portal.com.eje.daemon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cl.eje.model.generic.portal.Eje_daemon_inscritos;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.misc.Formatear;
import mis.MyProxyDemo;
import mis.MyProxyDemo.TipoUrlRequest;
import portal.com.eje.daemon.error.DaemonAlreadyStarted;
import portal.com.eje.daemon.error.DaemonIsNotStarted;
import portal.com.eje.daemon.ifaces.IDaemonCaller;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.Static;
import portal.com.eje.tools.ClaseGenerica;

/**
 * Clase que gatilla el proceso, el cron llama a esta clase y esta clase llama a
 * la clase, la url o el camando dos
 * 
 * @since 18-10-2018
 * @author Pancho
 */
public class PPMDaemonCaller extends AbsPPMDaemonCanLog implements IDaemonCaller {
	private Logger logger = Logger.getLogger(PPMDaemonCaller.class);
	private Map<Integer, Map<String, Date>> mapEjecucion;

	private PPMDaemonCaller() {
		mapEjecucion = new HashMap<Integer, Map<String, Date>>();
	}

	public static PPMDaemonCaller getInstance() {
		return Static.getInstance(PPMDaemonCaller.class);
	}

	private void markInicio(Eje_daemon_inscritos inscritos) throws DaemonAlreadyStarted {
		logger.debug("markInicio "+inscritos.getId_daemon()+" "+inscritos.getNombre());
		
		if (inscritos == null) {
			throw new NullPointerException();
		}

		if (getInicio(inscritos) != null && getTermino(inscritos) == null) {
			throw new DaemonAlreadyStarted("demonio id_daemon=" + inscritos.getId_daemon() + " ya está en ejecución, inició en " + Formatear.getInstance().toDate(getInicio(inscritos), "dd/MM/yyyy HH:mm:ss"));
		}

		set("inicio", inscritos, new Date());
		set("termino", inscritos, null);
	}

	private void markTermino(Eje_daemon_inscritos inscritos) throws DaemonIsNotStarted {
		logger.debug("markTermino "+inscritos.getId_daemon()+" "+inscritos.getNombre());
		
		if (inscritos == null) {
			throw new NullPointerException();
		}

		if (getInicio(inscritos) == null || getTermino(inscritos) != null) {
			throw new DaemonIsNotStarted("demonio id_daemon=" + inscritos.getId_daemon() + "  no está iniciado ");
		}

		// set("inicio", inscritos, new Date());
		set("termino", inscritos, new Date());
	}

	private Date getInicio(Eje_daemon_inscritos inscritos) {
		Map<String, Date> map = mapEjecucion.get(inscritos.getId_daemon());
		if (map != null) {
			return map.get("inicio");
		}

		return null;
	}

	private Date getTermino(Eje_daemon_inscritos inscritos) {
		Map<String, Date> map = mapEjecucion.get(inscritos.getId_daemon());
		if (map != null) {
			return map.get("termino");
		}

		return null;
	}

	private void set(String key, Eje_daemon_inscritos inscritos, Date date) {
		Map<String, Date> map = mapEjecucion.get(inscritos.getId_daemon());
		if (map == null) {
			mapEjecucion.put(inscritos.getId_daemon(), new HashMap<String, Date>());
			map = mapEjecucion.get(inscritos.getId_daemon());
		}

		map.put(key, date);
	}

	@Override
	public void doActions(Eje_daemon_inscritos inscritos) {
		logger.debug(" [START DAEMON " + inscritos.getId_daemon() + "]  " + inscritos.getNombre());
		Cronometro cro = new Cronometro();
		cro.start();

		try {
			PPMDaemonCaller.getInstance().markInicio(inscritos);
			DaemonManager.getInstance().updateLastDateExecution(inscritos.getId_daemon());

			ejecuClase(inscritos);
			ejecuUrl(inscritos);
			ejecuDos(inscritos);


		} catch (SQLException e) {
			super.logError(e.getMessage(), inscritos.getId_daemon());
		
		} catch (DaemonAlreadyStarted e) {
			super.logError(e.getMessage(), inscritos.getId_daemon());
		
		} catch (Exception e) {
			super.logError(e.getMessage(), inscritos.getId_daemon());
		
		} finally {
			
			try {
				DaemonManager.getInstance().updateLastTimeExecution(inscritos.getId_daemon(), cro.GetMilliseconds());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			
			}
			logger.debug(" [END DAEMON " + inscritos.getId_daemon() + "]  " + inscritos.getNombre() + "  en " + cro.GetMilliseconds());
			
			try {
				PPMDaemonCaller.getInstance().markTermino(inscritos);
			} catch (DaemonIsNotStarted e) {
				// TODO Auto-generated catch block
			
			}
		}

	}

	private void ejecuClase(Eje_daemon_inscritos inscritos) {
		if (inscritos.getEjecu_clase() != null && !"".equals(inscritos.getEjecu_clase())) {
			try {
				// ZONA Ejecu_clase
				Object o = ClaseGenerica.getInstance().getNew(inscritos.getEjecu_clase());

				if (o != null && IPPMDaemon.class.isAssignableFrom(o.getClass())) {
					IPPMDaemon daemon = (IPPMDaemon) o;
					
					logger.debug(EModulos.getThisModulo()+" ejecutando :"+daemon.getClass());
					CharSequence cs = daemon.doActions();

					if (cs != null && !"".equals(cs.toString())) {
						super.logInfo(cs.toString(), inscritos.getId_daemon());
					}
				}

			} catch (ClassNotFoundException e) {
				super.logError(e.getMessage(), inscritos.getId_daemon());
			
				logger.error(EModulos.getThisModulo(), e);
			} catch (NoSuchMethodException e) {
				super.logError(e.getMessage(), inscritos.getId_daemon());
			
				logger.error(EModulos.getThisModulo(), e);
			} catch (InstantiationException e) {
				super.logError(e.getMessage(), inscritos.getId_daemon());
			
				logger.error(EModulos.getThisModulo(), e);
			} catch (IllegalAccessException e) {
				super.logError(e.getMessage(), inscritos.getId_daemon());
			
				logger.error(EModulos.getThisModulo(), e);
			} catch (InvocationTargetException e) {
				super.logError(e.getMessage(), inscritos.getId_daemon());
			
				logger.error(EModulos.getThisModulo(), e);
			} catch (Exception e) {
				super.logError(e.getMessage(), inscritos.getId_daemon());
			
				logger.error(EModulos.getThisModulo(), e);
			}
		}
	}

	private void ejecuUrl(Eje_daemon_inscritos inscritos) {
		if (inscritos.getEjecu_llamada_url() != null && !"".equals(inscritos.getEjecu_llamada_url())) {
			try {
				// ZONA ejecuUrl

				String url = (inscritos.getEjecu_llamada_url());
				MyProxyDemo my = new MyProxyDemo();
				CharSequence cs = my.doURLRequest(url, TipoUrlRequest.doGet);

				if (cs != null && !"".equals(cs.toString())) {
					super.logInfo(cs.toString(), inscritos.getId_daemon());
				}
			} catch (Exception e) {
				super.logError(e.getMessage(), inscritos.getId_daemon());
			
			}
		}
	}

	private void ejecuDos(Eje_daemon_inscritos inscritos) {
		if (inscritos.getEjecu_dos() != null && !"".equals(inscritos.getEjecu_dos())) {
			try {
				// ZONA ejecuDos

				Runtime rt = Runtime.getRuntime();
				
				Process process = rt.exec(Formatear.getInstance().tTrim(inscritos.getEjecu_dos()).split(" "));
				
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				StringBuilder aux = new StringBuilder();
				
				for (String line = br.readLine(); line != null; line = br.readLine()) {
					aux.append(line).append("\n");
				}
				process.waitFor();
 				rt.runFinalization();
				br.close();

				if (aux != null && !"".equals(aux)) {
					super.logInfo(aux, inscritos.getId_daemon());
				}
			} catch (Exception e) {
				super.logError(e.getMessage(), inscritos.getId_daemon());
			
			}
		}
	}
}
