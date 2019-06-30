package portal.com.eje.portal;

import java.lang.reflect.Field;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;

import org.apache.log4j.Logger;

import cl.eje.model.generic.portal.Eje_generico_modulo;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.cache.SoftCache;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.misc.SoftCacheLocator;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.cache.Cache;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.vo.CtrGeneric;
import portal.com.eje.portal.vo.util.Wheres;

public enum EModulos {
	//workflow_ingresos 				("wfscontratacion"			, true), // YA NO SE OCUPA, SE CAMBIÓ POR TRASLADO
	workflow_container 				("wfcontainer"			, true), 
	workflow_vacaciones 			("wfsvacaciones"		, true), //OK
	workflow_facturas 				("wffacturas"			, true), 
	workflow_pago_proveedores 		("wfspagoproveedores"	, true),
	workflow_aumento_renta 			("wfaumentorenta"		, true),
	//workflow_ausencia_laboral 		("wfsausentismo"		, true),
	workflow_ausentismo 			("wfsausentismo"		, true),/*se ocupa*/
	workflow_cambio_cargo 			("wfcambiocargo"		, true), // YA NO SE OCUPA, SE CAMBIÓ POR TRASLADO
	workflow_horas_extras 			("wfshorasextras"		, true),
	workflow_traslados 				("wfstraslados"			, true),	/*se ocupa*/
	workflow_honorarios 			("wfhonorarios"			, true),
	workflow_fontos_por_rendir 		("wfsfondosporrendir"	, true),
	workflow_de_cotazaciones 		("wfscotizador"			, true),
	workflow_control_dotacion 		("wfsdotacion"			, true),
	workflow_contratacion 			("wfscontratacion"		, true), /*se ocupa*/
	workflow_egresos 				("wfsegresos"			, true), /*se ocupa*/
	workflow_cargos 				("wfcargos"				, true), // YA NO SE OCUPA, SE CAMBIÓ POR TRASLADO 
	workflow_sorporte				("wfssoporte"			, true),
	workflow_ficha_tecnicas			("wfsfichatecnicas"		, true),/*se ocupa*/
	workflow_ficha_personal			("wfsfichapersonal"		, true),
	workflow_rys					("rys"					, true),/*se ocupa*/
	workflow_amonestaciones			("wfsamonestaciones"	, true),/*se ocupa*/
	workflow_anexos					("wfsanexos"			, true),/*se ocupa*/
	workflow_contenidos				("wfscontenidos"		, true),
	
	modulo_autoservicio				("portalrrhh"			, false), /*se ocupa*/
	modulo_autoservicio_movil		("movil"				, false),
	modulo_webmatico				("webmatico"			, false),
	modulo_gdd						("ggd"					, false),
	modulo_metas 					("metas"				, false),
	modulo_capacitacion 			("capacitacion"			, false),
	modulo_estructura_organizacional("eo"					, false),
	modulo_reclutamiento_seleccion	("rys"					, false),/*se ocupa*/
	modulo_encuestra_clima 			("clima"				, false),
	modulo_intranet 				("qs"					, false),
	modulo_anywhere_rest 			("ws1"					, false),
	modulo_anywhere_ws2				("ws2"					, false),
	modulo_mis 						("mis"					, false),
	modulo_incidencias_de_salud 	("incidenciasdesalud"	, false),
	elearning_admin 				("elearningadmin"		, false),
	
	modulo_sqlremote				("sqlremote"			, false),
	modulo_sii						("sii"					, false),
	
	portaldepersonas				("portaldepersonas"		, false),
	participa						("participa"			, false),
	capacitacion					("capacitacion"			, false),
	
	sw								("sw"					, false), //servicio_web_de_corona
	
	scheduler						("scheduler"			, false), /*está siendo ejecutado desde modo aplicación, sin contexto*/
	qsmseleccion					("qsmseleccion"			, true),
	aplication_mode					("aplication_mode"		, false),
	nodefinido						("nodefinido"			, false);
	; /*está siendo ejecutado desde modo aplicación, sin contexto*/
	private String contexto;
	private boolean isWorkflow;
	
	
	EModulos(String contexto, boolean isWorkflow) {
		this.contexto = contexto;
		this.isWorkflow = isWorkflow;
	}
	
	public String getContexo() {
		return this.contexto;
	}
	
	public boolean isWorkflow() {
		return this.isWorkflow;
	}
	
	public static EModulos getModulo(String context) {
		String key = "getModulo_"+context;
		EModulos modulo = (EModulos) SoftCacheLocator.getInstance(EModulos.class).get(key);

		if( context != null) {
			context = context.replace("/", "");
			Field[] fields = EModulos.class.getDeclaredFields();
			for(Field f : fields) {
				if( !f.getType().isAssignableFrom(EModulos.class) ) {
					continue;
				}
				
				EModulos e = Enum.valueOf(EModulos.class, f.getName());
				if(context.equals(e.getContexo())) {
					modulo = e;
					break;
				}
			}	
			SoftCacheLocator.getInstance(EModulos.class).put(key, modulo);
		}
		
		if(modulo != null) {
			return modulo;
		}
		else {
			//throw new RuntimeException("No fue posible determinar el módulo, al parecer el punto de montaje es diferente a todos los definidos en ["+EModulos.class.getName()+"]");
			return EModulos.nodefinido;
		}
		
	}
	
	public static EModulos getModuloById(int intIdModulo) {
		String key = "getModuloById_"+intIdModulo;
		EModulos modulo = (EModulos) SoftCacheLocator.getInstance(EModulos.class).get(key);
 
		if(modulo == null) {
			StringBuilder strSQL = new StringBuilder();
 
			
			strSQL.append("select contexto \n")
			.append("from eje_generico_modulo \n")
			.append("where id_modulo=").append(intIdModulo);
			
			try {
				ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
				while (data!=null && data.next()){
					modulo = getModulo(data.getString("contexto").substring(1));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			SoftCacheLocator.getInstance(EModulos.class).put(key, modulo);
		}

		return modulo;
		
	}
	
	
	public static EModulos getThisModulo(IDomainDetecter io) {
		String key = "getThisModulo_"+io.getContext();
		EModulos thisModulo = (EModulos) SoftCacheLocator.getInstance(EModulos.class).get(key);
		
		if(thisModulo == null) {
			Field[] fields = EModulos.class.getDeclaredFields();
			for(Field f : fields) {
				if( !f.getType().isAssignableFrom(EModulos.class) ) {
					continue;
				}
				
				EModulos e = Enum.valueOf(EModulos.class, f.getName());
				if(io.getContext().indexOf(e.getContexo()) != -1)  {
					thisModulo= e;
					break;
				}	
			}
			
			SoftCacheLocator.getInstance(EModulos.class).put("getThisModulo_"+io.getContext(), thisModulo);
		}

		if(thisModulo != null) {
			return thisModulo;
		}
		else {
			//throw new RuntimeException("No fue posible determinar el módulo, al parecer el punto de montaje es diferente a todos los definidos en ["+EModulos.class.getName()+"]");
			return EModulos.nodefinido;
		}
		
	}
	
	/**
	 * Se usa para determinar que NO exista el contexto
	 * @since 2018-04-04
	 * @author Pancho
	 * */
	private static boolean existContext() {
		Context initContext;
		boolean ok = true;
		
		try {
			initContext = new InitialContext();
			Context c = (Context)initContext;
			c.lookup("java:/comp/env"); 
		}
		catch (NoInitialContextException e) {
			ok = false;
		}
		catch (NamingException e) {
			ok = false;
		}
				
		return ok;
	}
	
	public static EModulos getThisModulo() {
		
		EModulos retorno = null;
		try {
			Class[] def = {};
			Object[] params = {};
			retorno = Cache.weak(EModulos.nodefinido, "privateGetThisModulo", def, params, EModulos.class);
			if(retorno == null) {
				retorno = EModulos.nodefinido;
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	private static EModulos privateGetThisModulo() {
		Logger logger = Logger.getLogger(EModulos.class);
		Cronometro cro = new Cronometro();
		cro.start();
		
		 
		EModulos retorno = null;
		
	 
		if(!existContext()) {
			retorno = EModulos.aplication_mode;
		}
		else {
			String moduloContext = ParametroLocator.getInstance().getModuloContext();
			if(moduloContext != null) {
				String c = moduloContext.replaceAll("/", "");
				Field[] fields = EModulos.class.getDeclaredFields();
				for(Field f : fields) {
					if( !f.getType().isAssignableFrom(EModulos.class) ) {
						continue;
					}
					
					EModulos e = Enum.valueOf(EModulos.class, f.getName());
					if(e.getContexo() != null)  {
						if(e.getContexo().equals(c))  {								
							retorno = e; 
						}
					}
				}
			}
			else {
				
			}
		}	
		
		logger.debug("privateGetThisModulo "+cro.getTimeHHMMSS_milli());
		return retorno;
	}
	
	public int getId() {
		SoftCache sc = SoftCacheLocator.getInstance(EModulos.class);
		Integer intIdModulo = (Integer)sc.get("getId_"+this.contexto);
		
		if(intIdModulo == null) {
			StringBuilder strSQL = new StringBuilder();
			 
			strSQL.append("select id_modulo \n")
			.append("from eje_generico_modulo \n")
			.append("where contexto='/").append(this.contexto).append("'");
			
			try {
				ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
				while (data!=null && data.next()){
					intIdModulo = data.getInt("id_modulo");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			sc.put("getId_"+this.contexto, intIdModulo);
		}


		return intIdModulo != null ? intIdModulo : 0;
	}
	
	public Eje_generico_modulo getGenericoModulo() {
		Eje_generico_modulo mod = null;
		try {
			Class<?>[] def = {};
			Object[] params = {};
			mod = Cache.weak(this, "privateGenericoModulo", def, params, Eje_generico_modulo.class);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mod;
	}
	
	private Eje_generico_modulo privateGenericoModulo() {
		
		Eje_generico_modulo mod = null;
		try {
			Wheres w = Wheres.where("contexto", "=", "/"+this.contexto);
			mod = CtrGeneric.getInstance().getFromClass(Eje_generico_modulo.class, w.build());
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mod;
	}
	
	public String toString() {
		return this.contexto;
	}
	
	public static void clearCache() {
		SoftCacheLocator.getInstance(EModulos.class).clear();
	}

	
}
