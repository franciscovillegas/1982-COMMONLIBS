package portal.com.eje.portal.parametro;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import cl.eje.model.generic.portal.Eje_generico_modulo;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.misc.MappingFactory;
import cl.ejedigital.tool.misc.SoftCacheLocator;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.datos.DBConnectionManagerNoContextPool;
import cl.ejedigital.web.datos.error.DuplicateKeyException;
import cl.ejedigital.web.datos.ifaces.IDriverTool;
import cl.ejedigital.web.datos.util.DriverConnectionTool;
import portal.DeprecateException;
import portal.com.eje.cache.Cache;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.PPMBadParameterException;
import portal.com.eje.portal.PPMException;
import portal.com.eje.portal.vo.ClaseConversor;
import portal.com.eje.portal.vo.CtrGeneric;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.portal.vo.util.Wheres;

/**
 * 
 * Administra los parámetros y la conexión baso el mismo esquema.
 * */

public class Parametro implements IParametro, Runnable, IParametroListener {
	private Integer idClienteByContext;
	private String  clienteContext;
	private Integer idModuloByContext;
	private String moduloContext;
	private Map<String, DBConnectionManagerNoContextPool> connectionsPools;
	private Cronometro croLastCheck;
	private Cronometro croLastReconized;
	private IParametroListener localListener;;
	private Logger logger = Logger.getLogger(getClass());
	
	Parametro() {
		connectionsPools = new LinkedHashMap<String, DBConnectionManagerNoContextPool>();
		croLastCheck = new Cronometro();
		croLastReconized = new Cronometro();
		croLastReconized.start();
		
		localListener = new ParametroDefaultListener();
	}
	
	public List<ParametroValue> getValues(EModulos modulo, String param) {
		return getValores(param);
	}
	
	public File getFile(EModulos modulo, String param) {
		throw new NotImplementedException();
	}
	
	public ParametroValue getValue(EModulos modulo, String param) {
		List<ParametroValue> lista = getValores(param);
		
		if(lista != null && lista.size() > 0) {
			return lista.get(0);
		}
		
		return new ParametroValue();
	}

	public ParametroValue getValue(EModulos modulo, String param, String valor) {
		throw new DeprecateException("2016-08-04 EL método ya no está vigente, al cambiar el modelo hay método que ya no son útiles.");
		
		/*
		ParametroValue value = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select v.* "); 
		sql.append(" from eje_generico_modulo m ");
		sql.append(" 	inner join eje_generico_param p on m.id_modulo = p.id_modulo ");
		sql.append(" 	inner join eje_generico_param_value v on p.id_corr_param = v.id_corr_param ");
		sql.append(" where m.nombre = ? and p.nemo = ? and v.value = ? ");
		
		Object[] paramsSelect = {String.valueOf(modulo), param, valor };
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql.toString(), paramsSelect );
			
			while( data != null && data.next() ) {
				value = new ParametroValue();
				value.setValue(data.getString("value"));
				value.setDataAdic1(data.getString("data_adic1"));
				value.setDataAdic2(data.getString("data_adic2"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return value;
		*/
	}

	@Override
	public List<ParametroValue> getValores(EModulos mods, String nemoParam) {
		String sql = "SELECT p.nemo, pv.id_corr_value, pv.id_cliente, pv.id_param, pv.id_type, pv.pos, pv.valor, pv.data_adic1, pv.data_adic2, pv.base64file, pv.base64width, pv.base64height, pv.base64weigth ";
		sql += " FROM eje_generico_param_value  pv ";
		sql += " 		INNER JOIN eje_generico_param p on pv.id_param = p.id_param ";
		sql += " WHERE pv.id_cliente = ? and p.nemo = ? and p.id_modulo = ? ";
		
		List<ParametroValue> lista = new ArrayList<ParametroValue>();
		try {
			 
			int idCliente = -1;
			int idModulo  = -1;
			
			if(mods == null) {
				idCliente = this.getIDCliente();
				idModulo  = this.getIDModulo();	
			}
			else {
				Map<String,Object> map = getModulo("/" + mods.getContexo());
			
				idCliente = this.getIDCliente();
				idModulo = (Integer) map.get("Integer");
			}
			
			Object[] params = {idCliente, 
							   nemoParam, 
							   idModulo};
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
			while(data != null && data.next()) {
				ParametroValue pv = new ParametroValue();
				
				pv.setIdCliente(data.getInt("id_cliente"));
				pv.setIdParam(data.getInt("id_param"));
				pv.setIdType(data.getInt("id_type"));
				pv.setNemo(data.getString("nemo"));
				pv.setValue(data.getString("valor"));
				pv.setDataAdic1(data.getString("data_adic1"));
				pv.setDataAdic2(data.getString("data_adic2"));
				pv.setBase64file(data.getClobAsString("base64file"));
				pv.setBase64width(data.getForcedString("base64width"));
				pv.setBase64height(data.getForcedString("base64height"));
				pv.setBase64weight(data.getForcedString("base64weigth"));
				
				lista.add(pv);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ModuloNotRecognizedException e) {
			e.printStackTrace();
		}
		
		return lista;
	}
	
	@Override
	public List<ParametroValue> getValores(String nemoParam) {
		return ParametroLocator.getInstance().getValores(null, nemoParam);
	}
	
	@Override
	public ParametroValue getValor(String nemoParam, String key) {
		return getValor(null, nemoParam, key);
	}
	

	@Override
	public ParametroValue getValor(EModulos mods, String nemoParam, String key) {
		if( EModulos.getThisModulo() == EModulos.nodefinido) {
			return null;
		}
		
		if(key == null) {
			throw new NullPointerException("Parámetro key no puede ser null");
		}
		List<ParametroValue> values = ParametroLocator.getInstance().getValores(mods, nemoParam);
		for(ParametroValue p : values) {
			if(key.equals(p.getDataAdic1())) {
				return p;
			}
		}
		
		return null;
	}

	
	/**
	 * @deprecated
	 * @since 12-11-2018
	 * */
	private void isConnectionDiferent(String paramNemo) {
		boolean existe      	   = connectionsPools.get(paramNemo) != null;
		if( existe && croLastCheck.GetMilliseconds() >= 30000) {
			/*¿SERÁ LA DEFINICIÓN DIFERENTE?*/
			
			List<ParametroValue> lista = getValores(paramNemo);
			
			DBConnectionManagerNoContextPool pool = connectionsPools.get(paramNemo);
			
			if(pool != null) {
				Map map = pool.getDefinition();
				boolean okUrl 		= getParam(lista, "url").equals(map.get("url"));
				boolean okUser		= getParam(lista, "username").equals(map.get("user"));
				boolean okPassword  = getParam(lista, "password").equals(map.get("password"));
				boolean okMaxconn   = getParam(lista, "maxconn").equals(map.get("maxConn"));
				
			    if(!okUrl || !okUser || !okPassword || !okMaxconn) {
			    	//connectionsPools.get(paramNemo).release();
			    	connectionsPools.put(paramNemo, null);
			    	
			    	System.out.println("@@ [Reconectando al JNDI:"+paramNemo+"]");
			    }
			}
		}
	}
	
	private void createPool(EModulos modulo, String paramNemo) throws SQLException {
		//isConnectionDiferent(paramNemo);
		if(modulo != null && paramNemo != null) {
			String keyForConnection = modulo.getContexo() +"."+paramNemo;
			
			if(connectionsPools.get(keyForConnection) == null) {
				synchronized (Parametro.class) {
					
					if(connectionsPools.get(keyForConnection) == null) {
						croLastCheck.start();
						List<ParametroValue> lista = getValores(modulo, paramNemo);
						
	 					
						if(lista == null || lista.size() == 0) {
							throw new SQLException("No posible obtener los datos de la conexión. ["+keyForConnection+"]");
						}
						else {
							DBConnectionManagerNoContextPool pool = null;
		 				
							try {
								
								checkParam(lista, "drivers");
								checkParam(lista, "url");
								checkParam(lista, "username");
								checkParam(lista, "password");
								checkParam(lista, "maxconn");
								
								if(!"".equals(getParam(lista, "drivers"))  &&
								   !"".equals(getParam(lista, "url")) 	   &&
								   !"".equals(getParam(lista, "username")) &&
								   !"".equals(getParam(lista, "password")) &&
								   !"".equals(getParam(lista, "maxconn"))  ) {
										
									Driver driver = (Driver)Class.forName(getParam(lista, "drivers")).newInstance();
									DriverManager.registerDriver(driver);
							        
									pool = new DBConnectionManagerNoContextPool(paramNemo, lista );
							
								}
							} catch (InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							
					        if(pool != null) {
					        	connectionsPools.put(keyForConnection, pool);
					        }
						}
					}
				}
			}
		}
		
	}
	
	private void checkParam(List<ParametroValue> lista, String param) throws SQLException {
		if("".equals(getParam(lista, param))) {throw new SQLException("El parametro \""+param+"\" no está definido ");}
	}
	
	@Override
	public Connection getConnection(String nemoParam) throws SQLException {
		return getConnection(EModulos.getThisModulo(), nemoParam);
	}
	

	@Override
	public Connection getConnection(EModulos mods, String nemoParam) throws SQLException {
		Cronometro cro = new Cronometro();
		cro.start();
		
		Connection conn = null;
		
		if(nemoParam != null && mods != null) {
			//System.out.println("[Disponibles -1]");
			nemoParam = "conexion."+nemoParam;
			logger.debug("Creando conexión para :"+nemoParam);
			
			createPool(mods, nemoParam);
			//logger.debug("getConnection(EModulos mods, String nemoParam) "+nemoParam+" withPool:"+cro.getTimeHHMMSS_milli());
			
			String keyParam = mods.getContexo()+"."+nemoParam;

			DBConnectionManagerNoContextPool pool = connectionsPools.get(keyParam);
			if( pool != null) {
				conn = pool.getConnection();
			}
			
		}
		
		//logger.debug("getConnection(EModulos mods, String nemoParam) "+nemoParam+" end:"+cro.getTimeHHMMSS_milli());
		return conn;
	}
	
	@Override
	public boolean cannConnect(EModulos mods, String nemoParam) {
		Cronometro cro = new Cronometro();
		cro.start();
		
		boolean ok = false;
		
		if(nemoParam != null) {
			nemoParam = "conexion."+nemoParam;
		
			try {
				createPool(mods, nemoParam);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.debug("cannConnect(EModulos mods, String nemoParam) "+nemoParam+" withPool:"+cro.getTimeHHMMSS_milli());
			
			String keyParam = mods.getContexo()+"."+nemoParam;

			DBConnectionManagerNoContextPool pool = connectionsPools.get(keyParam);
			if( pool != null) {
				ok = pool.cannConnect();
			}
		}
		
		logger.debug("cannConnect(EModulos mods, String nemoParam) "+nemoParam+" end:"+cro.getTimeHHMMSS_milli());
		return ok;
	}
	
	@Override
	public void freeConnection(String nemoParam, Connection conn) throws SQLException {
		freeConnection(EModulos.getThisModulo(), nemoParam, conn);
	}
	
	@Override
	public void freeConnection(EModulos modulos, String nemoParam, Connection conn) throws SQLException {
		if(nemoParam != null) {
			//System.out.println("[Disponibles +1]");
			
			nemoParam = modulos.getContexo()+".conexion."+nemoParam;
			DBConnectionManagerNoContextPool pool = connectionsPools.get(nemoParam);
			
			if (pool==null) {
				System.out.println(new StringBuilder().append(EModulos.getThisModulo()).append("El jndi '").append(nemoParam).append("' no existe, no se puede liberar la conexión"));
			}else{
				pool.freeConnection(conn);
			}
		}
			
	}
	
	public Integer getIDCliente() {
		if(this.idModuloByContext == null && EModulos.getThisModulo() == EModulos.nodefinido) {
			return -1;
		}
			/*
			 * Solo si es distinto a aplication_mode entonces puede generar este tipo de error
			 * */
		else	if(this.idClienteByContext == null) {
				 
					System.out.println("@@ [Parametro] [wait...]");
					//this.getClass().wait();
					System.out.println("@@ [Parametro] [end wait...]");
					
				 if(EModulos.getThisModulo() != EModulos.aplication_mode) {
					 throw new NullPointerException("El id de cliente aún no ha sido establecido, las posibles causas son dos, el contexto de la Aplicación no está reconocido como cliente o se ejecutó tarde el método de reconocimiento (Difícil que sea esto segundo ).");	 
				 }
				 else {
					 /*2018-04-04 Francisco - trampilla*/
					 recognizeCliente("/localhost/aplication_mode/aplication_mode",1);
				 }
			}
				
			return this.idClienteByContext;
	 
	}
	
	public Integer getIDModulo() {
		 
		if(this.idModuloByContext == null && EModulos.getThisModulo() == EModulos.nodefinido) {
			return -1;
		}
			/*
			 * Solo si es distinto a aplication_mode entonces puede generar este tipo de error
			 * if(this.idModuloByContext == null) {
			 * */
		else if(this.idModuloByContext == null) {
				 
					System.out.println("@@ [Parametro] [wait...]");
					//this.getClass().wait();
					System.out.println("@@ [Parametro] [end wait...]");
				 
				 if(EModulos.getThisModulo() != EModulos.aplication_mode) {
					 throw new NullPointerException("El id de módulo aún no ha sido establecido, las posibles causas son dos, el contexto de la Aplicación no está reconocido como módulo o se ejecutó tarde el método de reconocimiento (Difícil que sea esto segundo ).");	 
				 }
				 else {
					 try {
						recognizeModulo("/localhost/aplication_mode/aplication_mode",1);
					} catch (ModuloNotRecognizedException e) {
						e.printStackTrace();
					}
				 }
				 
			}
		
		return this.idModuloByContext;
		 
		
	}
	
	public void recognizeIDCliente(ServletConfig sc) {
		
		recognizeIDCliente(sc.getServletContext());
	}
	
	public void recognizeIDCliente(ServletContext sc) {
		if((idClienteByContext == null || idModuloByContext == null) && croLastReconized.GetMilliseconds() >= 30000) {
			idClienteByContext = null;
			idModuloByContext = null;
			
			croLastReconized.start();
		}
		
		if(!existContext()) {
			System.out.println(this.getClass().getCanonicalName()+" no existe contexto");
		}
		
		if(idClienteByContext == null) {
			synchronized (Parametro.class) {
				if(idClienteByContext == null) {
					
					
					try {
						String fullPath = sc.getContextPath();
						
						if(fullPath == null) {
							throw new NullPointerException("No fue posible determinar el nombre de contexto, por ende no es posible determinar el cliente de esta aplicación.");
						}
						else {

							try {
								InitialContext initContext = new InitialContext();
								Context c = (Context)initContext;
								c.lookup("java:/comp/env"); 
								c.getEnvironment();
							}
							catch (NoInitialContextException e) {
								 
							}
							catch (NamingException e) {
								 
							}
							
							recognizeCliente(fullPath,1);
							recognizeModulo(fullPath,2);
						}
						
						System.out.println("@@ [Parametro] Variables liberadas");
						 
						 
					} catch (ClientNotRecognizedException e) {
	
						System.out.println(e);
					} catch (ModuloNotRecognizedException e) {
			
						System.out.println(e);
					}
					 
					
				}
			}
		}
	}
	
	/**
	 * /beme/webmatico
	 * */
	private void recognizeCliente(String fullPath, int pos) throws ClientNotRecognizedException {
		if(fullPath != null) {
			String[] partes = fullPath.split("/");
			
			if(partes.length > pos) {
				Map<String,Object> map = getCliente("/" +partes[pos]);
				
				this.idClienteByContext = (Integer) map.get("Integer");
				this.clienteContext  = (String) map.get("String");
			}
		}
		
	}
	
	private Map<String,Object> getCliente(String context) throws ClientNotRecognizedException {
		String key = "getCliente"+context;
		Map<String,Object> map = (Map<String,Object>)SoftCacheLocator.getInstance(this.getClass()).get(key);
		
		if(map == null) {
			if(!DBConnectionManager.getInstance().canConnect("portal")) {
				return new HashMap<String,Object>();
			}
			
			map = new HashMap<String, Object>();
			/*SIEMPRE EN LA POSICIÓN 2 ESTARÁ EL CONTEXTO DEL CLIENTE*/
			String sql = "SELECT * FROM eje_generico_cliente  WHERE contexto = ? and vigente = 1 ";
			try {
				Object[] params = {context};
				ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
				
				if(data != null && data.next()) {
					
					map.put("Integer", data.getInt("id_cliente"));
					map.put("String", data.getString("contexto"));

				}
				else {
					if(context != null && context.equals("/"+EModulos.aplication_mode.getContexo())) {
						/*2018-04-04 EL MODULO aplicacion_mode SE genera automaticamente en la BD*/
						sql = "insert into eje_generico_cliente (nombre, contexto, fec_update, descripcion, vigente) values (?, ?, getdate(), ?, ?) ";
						Object[] params2 = {context.split("/")[1], context, "Aplicacion Mode ON", 1};
						ConsultaTool.getInstance().insert("portal", sql, params2);
	 
						return getCliente(context);
					}
					else {
						/*2018-04-04 Cualquier otro ciente generará error y debe ser creado previamente en la BD*/
						throw new ClientNotRecognizedException("Al parecer el cliente \""+ context + "\" no está definido");
					}
					
					
				}
												
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			SoftCacheLocator.getInstance(this.getClass()).put(key, map);
		}
		
		
		return map;
	}
	
	/**
	 * /beme/webmatico
	 * */
	private void recognizeModulo(String fullPath, int pos) throws ModuloNotRecognizedException {
		if(fullPath != null) {
			String[] partes = fullPath.split("/");
			if(partes != null && partes.length > pos) {
				Map<String,Object> map = getModulo("/" +partes[pos] );
				
				this.idModuloByContext = (Integer) map.get("Integer");
				this.moduloContext  = (String) map.get("String");
			}
		}
		
	}
	
	protected Map<String,Object> getModulo(String context) throws ModuloNotRecognizedException {
		String key = "getModulo"+context;
		Map<String,Object> map = (Map<String,Object>)SoftCacheLocator.getInstance(this.getClass()).get(key);
		
		if(map == null) {
			if(DBConnectionManager.getInstance().canConnect("portal")) {
				map = new HashMap<String, Object>();
				/*SIEMPRE EN LA POSICIÓN 3 ESTARÁ EL CONTEXTO DEL MÓDULO*/
				String sql = "SELECT * FROM eje_generico_modulo  WHERE contexto = ? and vigente = 1  ";
				try {
					Object[] params = {context};
					ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
					
					if(data != null && data.next()) {
						map.put("Integer", data.getInt("id_modulo"));
						map.put("String", data.getString("contexto"));
					}
					else {
						if(context != null && context.equals("/"+EModulos.aplication_mode.getContexo())) {
							/*2018-04-04 EL MODULO aplicacion_mode SE genera automaticamente en la BD*/
							sql = "insert into eje_generico_modulo (nombre, contexto, fec_update, descripcion, vigente) values (?, ?, getdate(), ?, ?) ";
							Object[] params2 = {EModulos.aplication_mode.getContexo(), "/"+EModulos.aplication_mode.getContexo(), "Aplicacion Mode ON", 1};
							ConsultaTool.getInstance().insert("portal", sql, params2);
							
							return getModulo(context);
						}
						else {
							/*2018-04-04 Cualquier otro módulo generará error y debe ser creado previamente en la BD*/
							throw new ModuloNotRecognizedException("Al parecer el módulo \""+ context + "\" no está definido");
						}
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				SoftCacheLocator.getInstance(this.getClass()).put(key, map);
			}
		}
		
		if(map == null) {
			map = new HashMap<String,Object>();
		}
		
		return map;
	}
	
	
	private String getParam(List<ParametroValue> listaParam, String param) {
		for(ParametroValue vp: listaParam) {
			if(vp.getDataAdic1() != null && vp.getDataAdic1().equals(param)) {
				return Validar.getInstance().validarDato(vp.getValue(), "");
			}
		}
		
		return "";
	}

	@Override
	public String getClienteContext() {
		return clienteContext;
	}

	@Override
	public String getModuloContext() {
		return moduloContext;
	}

	@Override
	public void run() {
		System.out.println("Run");
	}

	@Override
	public String getContext() {
		return  getClienteContext() + getModuloContext();
	}

	@Override
	public String getCoreValue(EParametroCore p) {
		if(p == null) {
			return null;
		}
		
		String sql = " SELECT * FROM eje_generico_param_god WHERE core_param = ? ";
		Object[] params = {String.valueOf(p)};
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
			while(data != null && data.next()) {
				return data.getString("valor");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Integer getIDModuloFromBD(EModulos modulo) {
		String sql0 = "select id_modulo from eje_generico_modulo where vigente = 1 and contexto = '/"+modulo.getContexo()+"'";
		try {
			
			ConsultaData dataModulo = ConsultaTool.getInstance().getData("portal", sql0);
			if(dataModulo != null && dataModulo.next()) {
				return dataModulo.getInt("id_modulo");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Integer getIDParamFromBD(Integer idMoldulo, String nemo) {
		String sql0 = "SELECT id_param from eje_generico_param where id_modulo = ? and nemo = ? ";
		try {
			Object[] params = {idMoldulo, nemo};
			ConsultaData dataModulo = ConsultaTool.getInstance().getData("portal", sql0, params);
			if(dataModulo != null && dataModulo.next()) {
				return dataModulo.getInt("id_param");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public ParametroKey getParamKey(EModulos modulo, String key) {
		int idModulo = 0 ;
		ParametroKey pKey = null;
		
		String sql0 = "select id_modulo from eje_generico_modulo where vigente = 1 and contexto = '/"+modulo.getContexo()+"'";
		try {
			if(!DBConnectionManager.getInstance().canConnect("portal")) {
				return null;
			}
			
			ConsultaData dataModulo = ConsultaTool.getInstance().getData("portal", sql0);
			if(dataModulo != null && dataModulo.next()) {
				idModulo = dataModulo.getInt("id_modulo");
			}
			else {
				if( EModulos.getThisModulo() == EModulos.nodefinido) {
					return null;
				}
				else {
					throw new RuntimeException("No es posible reconocer el contexto del parámetro, posiblemente esté mal escrito, revise la configuración [´MOD BUSCADO:"+modulo.getContexo()+"] ");	
				}
				
			}
			
			String sql = " SELECT id_param, id_modulo, nemo FROM eje_generico_param WHERE nemo = ? and id_modulo = ? ";
			
			
			Object[] params = {key, idModulo};
		
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
			if(data != null && data.next()) {
				pKey = new ParametroKey(data.getInt("id_param"), data.getString("nemo"), data.getInt("id_modulo"));
			}
			else {
				sql = "INSERT INTO eje_generico_param (id_modulo, nemo) VALUES (?, ?)";
				
				Object[] oInsert = {idModulo, key};
				ConsultaTool.getInstance().insert("portal", sql, oInsert);
				pKey = getParamKey(modulo, key);
				
				this.onNewParametro(modulo, key);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return pKey;
	}

	@Override
	public boolean addParam(int idCliente, ParametroKey key, ParametroValue value) {
		if(EModulos.getThisModulo() == EModulos.nodefinido) {
			return false;
		}
		
		
		ParametroValue pv = getValor(key.getNemo(), value.getKey());
		if(pv == null) {
			String sql = "INSERT INTO eje_generico_param_value (id_cliente,id_param,id_type,pos,valor,data_adic1,data_adic2,base64file,base64width,base64height,base64weigth)" ;
			sql+= " VALUES (?,?,?,?,?,?,?,?,?,?,?) ";
			
			Object[] param = {idCliente , key.getIdParam(), value.getIdType(), value.getPos(), value.getValue(), value.getDataAdic1(), value.getDataAdic2(), value.getBase64file(), value.getBase64width(), value.getBase64height(), value.getBase64weight()};
			try {
				ConsultaTool.getInstance().insert("portal", sql, param );
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			this.onNewParametroValue(idCliente, key, value);
		}
		else {
			String sql = "UPDATE eje_generico_param_value ";
			sql+= " SET valor=? " ;
			sql+= " WHERE data_adic1 = ? and id_cliente = ? and id_param = ? ";
			
			Object[] params = {value.getValue(), value.getDataAdic1(), idCliente, key.getIdParam()};
			
			try {
				ConsultaTool.getInstance().update("portal", sql, params);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			this.onEditParametroValue(idCliente, key, value);;
		}
		
		return true;
	}

	@Override
	public ParametroValue getKey(EModulos mods, String nemoParam, String valorBuscado) {
		List<ParametroValue> lista = ParametroLocator.getInstance().getValores(EModulos.getThisModulo(), nemoParam);
		String key = null;
		
		for(ParametroValue valor : lista) {
			if(valorBuscado.equals(valor.getValue())) {
				key = valor.getKey();
				break;
			}
		}
		
		if(key == null) {
			throw new RuntimeException("No fue posible encontrar un parámetro con el valor \""+valorBuscado+"\" en el modulo \""+mods.getContexo()+"\" parámetro \""+nemoParam+"\" ");
		}
		
		
		return getValor(mods, nemoParam, key);
	}

	@Override
	public boolean addParam(ParametroKey key, ParametroValue value) {
		addParam(getIDCliente() , key , value );
		
		return true;
	}

	@Override
	public List<ParametroValue> getValores(EModulos mods, int idCliente, String nemoParam) {
		throw new NotImplementedException();
	}

	@Override
	public boolean clear() {
		MappingFactory.getFactory(ParametroWithCache.class).clear();
		Cache.resetCache(Parametro.class);
		Cache.resetCache(ParametroWithCache.class);
		EModulos.clearCache();
		return true;
		 
	}

	@Override
	public void ifNotExistThenBuildParametro(EModulos modulo, String paramNemo, String valueKey, String value) throws PPMException {
		
		
		if(modulo == null) {
			throw new PPMBadParameterException(" @EModulo no puede ser null ");
		}
		else if(paramNemo == null) {
			throw new PPMBadParameterException(" @paramNemo no puede ser null ");
		}
		else if( valueKey == null ) {
			throw new PPMBadParameterException(" @valueKey no puede ser null ");
		}
		else if( value == null ) {
			throw new PPMBadParameterException(" @value no puede ser null ");
		}
		
		
		if(modulo == EModulos.getThisModulo() ) {
			/*
			 * 2018-04-04 Francisco
			 * Solo se creará para modulos WEB
			 * */
			ParametroKey key 		= ParametroLocator.getInstance().getParamKey(modulo, paramNemo);
			ParametroValue valor_1 	= ParametroValue.buildParametro(valueKey , value );
			
			if( getValor(modulo, paramNemo, valueKey) == null) {
				ParametroLocator.getInstance().addParam(ParametroLocator.getInstance().getIDCliente(), key, valor_1);
			}
			
		}
	}

	@Override
	public void ifNotExistThenBuildParametro(List<EModulos> modulos, String paramNemo, String valueKey, String valueDefault) throws PPMException {
		if(modulos == null) {
			throw new PPMBadParameterException(" @EModulos no puede ser null ");
		}
		
		for(EModulos e : modulos) {
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(e, paramNemo, valueKey, valueDefault);
		}
	}

	@Override
	public void setParametroListener(IParametroListener listener) {
		if(listener != null) {
			this.localListener = listener;
		}
		
	}
	



	@Override
	public boolean isRecognized() {
		return this.idClienteByContext != null && this.idModuloByContext != null && this.idClienteByContext != -1 && this.idModuloByContext != -1 ;
	}

	@Override
	public boolean deleteKey(EModulos modulo, String nemo, String key) {
		
		if(modulo == EModulos.getThisModulo()) {
			String sql = "DELETE FROM eje_generico_param_value WHERE id_cliente = ? and id_param = ? and data_adic1 = ? ";
			Integer idCliente = getIDCliente();
			Integer idModulo = ParametroLocator.getInstance().getIDModuloFromBD(modulo);
			Object[] params = {idCliente, ParametroLocator.getInstance().getIDParamFromBD(idModulo, nemo), key};
			try {
				boolean ok = ConsultaTool.getInstance().update("portal", sql, params) > 0;
				
				ParametroKey pkey = getParamKey(modulo, nemo);
				
				this.onDeleteParametroValue(idCliente, pkey, key);
				
				return ok;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				
			}
			
		}
		
		return false;
		
	}

	@Override
	public void ifNotExistThenBuildParametro(EModulos modulo, String paramNemo) throws PPMException {
		if(modulo == EModulos.getThisModulo() ) {
			/*
			 * 2018-04-04 Francisco
			 * Solo se creará para modulos WEB
			 * */
			ParametroKey key 		= ParametroLocator.getInstance().getParamKey(modulo, paramNemo);
		}
		
	}

	@Override
	public void onNewParametro(EModulos modulo, String key) {
		if(localListener != null) {
			localListener.onNewParametro(modulo, key);
		}
	}

	@Override
	public void onNewParametroValue(int idCliente, ParametroKey key, ParametroValue value) {
		if(localListener != null) {
			localListener.onEditParametroValue(idCliente, key, value);
		}
	}

	@Override
	public void onEditParametroValue(int idCliente, ParametroKey key, ParametroValue newValue) {
		if(localListener != null) {
			localListener.onEditParametroValue(idCliente, key, newValue);
		}
	}

	@Override
	public void onDeleteParametroValue(int idCliente, ParametroKey key, String valueKey) {
		
		if(localListener != null) {
			localListener.onDeleteParametroValue(idCliente, key, valueKey);
		}
		
	}

	public boolean existContext() {
		Context initContext;
		boolean ok = true;
		
		try {
			initContext = new InitialContext();
			Context c = (Context)initContext;
			return c.lookup("java:/comp/env") != null; 
		}
		catch (NoInitialContextException e) {
			ok = false;
		}
		catch (NamingException e) {
			ok = false;
		}
				
		return ok;
	}

	@Override
	public void setParam(EModulos modulo, String paramNemo, String paramKey, String value) {
		
		ParametroLocator.getInstance().deleteKey(modulo, paramNemo, paramKey);
		
		ParametroKey key 		= ParametroLocator.getInstance().getParamKey(modulo, paramNemo);
		ParametroValue valor_1 	= ParametroValue.buildParametro(paramKey , value );
 
		ParametroLocator.getInstance().addParam(ParametroLocator.getInstance().getIDCliente(), key, valor_1);
		 

	}


	@Override
	public List<EModulos> getModulos(boolean vigente) {
		List<EModulos> lista = new ArrayList<EModulos>();
		
		try {
			String sql = "select contexto from eje_generico_modulo where vigente = ? ";
			Object[] params = {vigente};
			ConsultaData dataModulo = ConsultaTool.getInstance().getData("portal", sql, params);
			while(dataModulo != null && dataModulo.next()) {
				try {
					String contexto = dataModulo.getString("contexto");
					if( contexto != null && contexto.length() > 1 ) {
						lista.add(EModulos.getModulo(contexto));
					}
					
				}
				catch(Exception e) {
					
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return lista;
	}
	
	@Override
	public List<EModulos> getModulosWorkflows(boolean vigente) {
		List<EModulos> lista = new ArrayList<EModulos>();
		
		try {
			String sql = "select contexto from eje_generico_modulo where vigente = ? ";
			Object[] params = {vigente};
			ConsultaData dataModulo = ConsultaTool.getInstance().getData("portal", sql, params);
			while(dataModulo != null && dataModulo.next()) {
				try {
					String contexto = dataModulo.getString("contexto");
					if( contexto != null && contexto.length() > 1 ) {
						EModulos modulo = EModulos.getModulo(contexto);
						if(modulo.isWorkflow()) {
							lista.add(modulo);	
						}
					}
				}
				catch(Exception e) {
					
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return lista;
	}

	@Override
	public List<String> getJndis(EModulos m) {
		 
		List<String> jndis = new ArrayList<String>();
		List<String> nemos = getNemoParams(m);
		for(String n : nemos) {
			String jndi = null;
			
			if(n != null && n.indexOf("conexion.") == 0) {
				jndi = n.substring("conexion.".length(), n.length());
			}
			
			if(jndi != null && !jndis.contains(jndi)) {
				jndis.add(jndi);
			}
		}
			
		return jndis;
	}

	@Override
	public List<String> getNemoParams(EModulos modulo) {
		String sql = "select nemo from eje_generico_param where id_modulo = ? ";
		
		List<String> nemos = new ArrayList<String>();
		try {
			Object[] params = {modulo.getId()};
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
			
			while(data != null && data.next()) {
				nemos.add(data.getString("nemo"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return nemos;
	}

	@Override
	public String getDatabaseName(EModulos modulo, String jndi) {
		
		String db = null;
		try {
			ParametroValue url = getValor(modulo, "conexion."+jndi, "url");
			ParametroValue driver = getValor(modulo, "conexion."+jndi, "drivers");
			if(url != null && url.getValue() != null && driver != null && driver.getValue() != null) {
				IDriverTool driverTool = DriverConnectionTool.getInstance(driver.getValue());
				db = driverTool.getDataBaseFromUrl(url.getValue());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return db;
	}

	@Override
	public Eje_generico_modulo getModuloDef(EModulos modulo) {
		int idModulo = getIDModuloFromBD(modulo);
		
		Eje_generico_modulo retorno = null;
		try {
			retorno = CtrGeneric.getInstance().getFromClass(Eje_generico_modulo.class, Wheres.where("id_modulo", "=", idModulo).build());
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retorno;
	}

	@Override
	public Map<String, ParametroValue> getMapParamValues(String paramNemo) throws DuplicateKeyException {
		return getMapParamValues(EModulos.getThisModulo(), paramNemo);
	}
	
	@Override
	public Map<String, ParametroValue> getMapParamValues(EModulos modulo, String paramNemo) throws DuplicateKeyException {
		Map<String, ParametroValue> mapRetorno = new HashMap<String, ParametroValue>();
		List<ParametroValue> values = ParametroLocator.getInstance().getValores(modulo, paramNemo);
		
		for(ParametroValue pv : values) {
			
			String methodName =   new StringBuilder().append("getKey").toString();
			 
			Object retorno = VoTool.getInstance().getReturnFromMethod(pv, methodName, new Class[] {}, new Object[] {});
			String retornoKey = ClaseConversor.getInstance().getObject(retorno, String.class);
			
			if(mapRetorno.get(retornoKey) != null) {
				throw new DuplicateKeyException();
			}
			
			mapRetorno.put(retornoKey, pv);
		}
		
		return mapRetorno;
	}
 
}
