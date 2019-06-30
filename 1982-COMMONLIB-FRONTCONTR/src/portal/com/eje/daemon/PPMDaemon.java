package portal.com.eje.daemon;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataMode;
import cl.ejedigital.tool.misc.WeakCacheLocator;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.PPMException;
import portal.com.eje.portal.factory.Static;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroValue;

public class PPMDaemon {
//	private String PPMDaemonClassCaller;

	private String paramNemo;
	private Logger log = Logger.getLogger(PPMDaemon.class);
	
	private PPMDaemon() {
		
		
//		PPMDaemonClassCaller = "portal.com.eje.daemon.PPMDaemonCaller";
		paramNemo = PPMDaemon.class.getCanonicalName();
//		
//		try {
//			CheckBDexecute execute = new CheckBDexecute();
//			execute.executeFile("sql20180804_19_20_portal_crea_tablas_daemons.sql");
//		}  catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), paramNemo);
		} catch (PPMException e) {
			e.printStackTrace();
		}
	}

	public static PPMDaemon getInstance() {
		return Static.getInstance(PPMDaemon.class);
	}

	/**
	 * Retorna boolean si el módulo entragado como parámetro está activo
	 * */
	public boolean isActive(int idModulo) {

		return idModulo == getIdModuloActive();
	}

	/**
	 * Obtiene el módulo activo en donde se ejecuta el scheduler, si no está las tablas o no está activo ninguno retornará -1
	 * 
	 * @since 18-10-2018
	 * @param
	 * */
	public int getIdModuloActive() {
		int idModuloActive = -1;

		String sql = "if( exists (select * from sysobjects where name='eje_daemon_generico_modulo' and xtype='U')) \n"
					+ "SELECT TOP 1 id_modulo FROM eje_daemon_generico_modulo WHERE activado = 1 \n"
					+ " else select id_modulo = -1 \n";

		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql);
			if (data != null && data.next()) {
				idModuloActive = data.getInt("id_modulo");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return idModuloActive;

	}
	
	public synchronized boolean desactive() {
		StringBuilder sql = new StringBuilder();
		sql.append(" delete from eje_daemon_generico_modulo ");
		try {
			ConsultaTool.getInstance().update("portal", sql.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DaemonGuard.getInstance().stopDaemons();
		
		
		return true;
	}

	/**
	 * Activa el modulo para que el scheduler comienze a funcionar
	 * 
	 * @since 18-10-2018
	 * */
	public boolean setActive(int idModulo) {
		DaemonGuard.getInstance().stopDaemons();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into eje_daemon_generico_modulo (fecha, id_modulo, activado) values (getdate() , ? , 1) ");

		Object[] params = { idModulo };
		try {
			ConsultaTool.getInstance().update("portal", sql.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DaemonGuard.getInstance().startIfThisContextIsActive();
		
		return true;
	}

	public ConsultaData getDaemons() {
		String paramNemo = this.getClass().getCanonicalName();
		int idModulo = getIdModuloActive();
		List<ParametroValue> pvs = ParametroLocator.getInstance().getValores(EModulos.getModuloById(idModulo), paramNemo);
		
		ConsultaData data = ConsultaTool.getInstance().buildConsultaData(pvs);
		
		if(data != null) {
			
			data.setMode(ConsultaDataMode.CONVERSION);
			while(data.next() ) {
				Class classDaemon = null;
				try {
					classDaemon = Class.forName(data.getString("dataadic1"));
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				addDaemonConfigurationToConsultaData(data, classDaemon);
			}
			
			data.toStart();
		}

		return data;
	}
	
	/**
	 * Método anterior, cuando se tenía los daemon inscritos en una propiedad de parámetros
	 * 
	 * @deprecated
	 * @since 18-10-2018
	 * */
	public boolean updDaemon(String clase, String trueOrFalse) {
		ParametroLocator.getInstance().setParam(EModulos.getThisModulo(), paramNemo, clase, trueOrFalse );
		return true;
	}
	
	
	/**
	 * Método anterior, cuando se tenía los daemon inscritos en una propiedad de parámetros
	 * 
	 * @deprecated
	 * @since 18-10-2018
	 * */
	public boolean delDaemon(String clase) {
		ParametroLocator.getInstance().deleteKey(EModulos.getThisModulo(), paramNemo, clase );
		return true;
	}
	
	/**
	 * Método anterior, cuando se tenía los daemon inscritos en una propiedad de parámetros
	 * 
	 * @deprecated
	 * @since 18-10-2018
	 * */
	
	public DaemonConfiguration addDaemonConfigurationToConsultaData(ConsultaData data, Class<? extends IPPMDaemon> daemon) {
		if(daemon != null && data != null) {

			List<String> systemMethods = getSystemMetodoName();
			
			Annotation ano =daemon.getAnnotation(DaemonConfiguration.class);
			if(ano instanceof DaemonConfiguration) {
				DaemonConfiguration conf = (DaemonConfiguration)ano;
				
				Method[] metodos = conf.getClass().getDeclaredMethods();
				for(Method m : metodos) {
					if(!systemMethods.contains(m) ) {
						if(!data.getNombreColumnas().contains(m.getName())) {
							data.getNombreColumnas().add(m.getName().toLowerCase());
						}
						
						data.getActualData().put(m.getName().toLowerCase(), conf.pathIdeConfiguration() );					
					
					}
				}
				
				return conf;
			}
		}
		
		return null;
	}
	
	/**
	 * Método anterior, cuando se tenía los daemon inscritos en una propiedad de parámetros
	 * 
	 * @deprecated
	 * @since 18-10-2018
	 * */
	private List<String> getSystemMetodoName() {
		String key = "getSystemMetodoName()";
		List<String> systemMethods = (List<String>) WeakCacheLocator.getInstance(this.getClass()).get(key);
		
		if(systemMethods == null) {
			systemMethods = new ArrayList<String>();
			systemMethods.add("equals");
			systemMethods.add("toString");
			systemMethods.add("hashCode");
			systemMethods.add("annotationType");
			
			WeakCacheLocator.getInstance(this.getClass()).put(key, systemMethods);
		}

		return systemMethods;
	}
}
