package portal.com.eje.portal.correspondencia;

import java.io.File;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.Message.RecipientType;

import org.apache.log4j.Logger;

import cl.eje.mail.modulos.CorreoProcessBase;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.ifaces.ICorreoProcess;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.DBConnectionManager;
import cl.ejedigital.web.fileupload.FileService;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.frontcontroller.IIOClaseWebProcess;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.PPMException;
import portal.com.eje.portal.correspondencia.CorrespondenciaProgramacion.ECorrespondenciaProgramacionEstado;
import portal.com.eje.portal.correspondencia.CorrespondenciaProgramacion.ECorrespondenciaProgramacionTipo;
import portal.com.eje.portal.correspondencia.groupviewer.GroupViewer_sqlSearch;
import portal.com.eje.portal.correspondencia.groupviewer.IGroupViewer;
import portal.com.eje.portal.factory.Static;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroValue;
import portal.com.eje.tools.QuartzScheduler;
import portal.com.eje.vo.CorreoBuilderDefault;

public class CorrespondenciaLocator implements IIOClaseWebProcess {
	private Logger logger = Logger.getLogger(getClass());
	private Cronometro cro;
	private int secondToRefresh;
 
	
	public enum ECorrespondenciaVigencia {
		vigente(1),
		novigente(0);
		
		private int id;
		ECorrespondenciaVigencia(int id) {
			this.id = id;
		}
		
		public String toString() {
			return String.valueOf(this.id);
		}
	}

	public static CorrespondenciaLocator getInstance() {
		return Static.getInstance(CorrespondenciaLocator.class);
	}

	public CorrespondenciaLocator() { 
		try {

			ParametroValue pv = ParametroLocator.getInstance().getValor(EModulos.getThisModulo(), "conf.mail", "send.intervalo.en_segundos");
			if(pv != null) {
				secondToRefresh = Validar.getInstance().validarInt( pv.getValue() , 60 );
			}
			else {
				secondToRefresh = 60;
			}
			
		}
		catch(Exception e) {
			secondToRefresh = 60;
		}
		
		if(secondToRefresh < 60)  {
			secondToRefresh = 60;
		}
	}
	
	/**
	 * El IIOClaseWebLight se usa para determinar la conexión y adjuntar los archivos, Puede ser pasado como nulo, sin embargo, los archivos adjuntos no serán enviados<br/><br/>
	 * 
	 * <b>Corrección 2018-04-04 Francisco</b><br/>
	 * Cuando la ejecución es desde Consola ya no es necesario pasarle el IOClaseWeb, de todas formas los archivos serán adjuntados.
	 * 
	 * @deprecated Ya no necesario pasarle IIOClaseWebLight como parámetro, esto era en un inicio dado que era l única forma de reconocer la carpeta donde estaban los archivos adjuntos
	 * @author Pancho
	 * @since 19-10-2018
	 * */
	public Double addCorreo(IIOClaseWebLight io, CorrespondenciaBuilder cb) {
		Double ok = null;
		
		if(cb != null) {
			if(cb.getProgramacion().size() == 0) {
				logger.info("Al no tener programación se agrega NOW ");
				CorrespondenciaProgramacion programacion = new CorrespondenciaProgramacion(ECorrespondenciaProgramacionTipo.ejecucion_fechas, CorrespondenciaProgramacion.getNow());
				cb.addProgramacion(programacion);
			}
			ok = addCorreoToDefinition(io, cb);
		}
		
		return ok;
	}
	/**
	 * Niguno de los parámetros puede ser null <br/>
	 * Registra el correo a ser enviado con sus respectivos documentos adjuntos.
	 * 
	 * @author Pancho
	 * @since 19-10-2018
	 * */
	public Double addCorreoRemitente(EModulos remitente, CorrespondenciaBuilder cb) {
		Double ok = null;
		
		if(remitente != null && cb != null) {
			if(cb.getProgramacion().size() == 0) {
				logger.info("Al no tener programación se agrega NOW ");
				CorrespondenciaProgramacion programacion = new CorrespondenciaProgramacion(ECorrespondenciaProgramacionTipo.ejecucion_fechas, CorrespondenciaProgramacion.getNow());
				cb.addProgramacion(programacion);
			}
			ok = addCorreoToDefinitionCRemitente(remitente, cb);
		}
		
		return ok;
	}
	
	/**
	 * El IIOClaseWebLight se usa para determiniar la conexión y adjuntar los archivos, Puede ser pasado como nulo, sin embargo, los archivos adjuntos no serán enviados
	 * */
	public Double updCorreo(IIOClaseWebLight io, CorrespondenciaBuilder cb, Double idCorrespondencia) {
		Double ok = null;
		
		if(cb != null) {
			ok = addCorreoToDefinition(io, cb, idCorrespondencia);
		}
		
		return ok;
	}
	
	/**
	 * Inicialmente el IIOClaseWebLight solo se utilizaba para enviar los archivos y no podía ser null<br/><br/>
	 * 
	 * <b>Corrección 2018-04-04 Francisco</b><br/>
	 * IIOClaseWebLight puede ser null y de todas formas se adjuntarán los archivos, será null cuando la app es ejecutada desde consola
	 * 
	 * Solo debe ocuparse de forma interna
	 * 
	 * @deprecated
	 * @see #sendCorreosPendientes(IIOClaseWebLight)
	 * */
	public void sendCorreosPendientes(IIOClaseWebLight io) {
		sendCorreosPendientes(io, null);
	}
	
	/**
	 * Inicialmente el IIOClaseWebLight solo se utilizaba para enviar los archivos y no podía ser null<br/><br/>
	 * 
	 * <b>Corrección 2018-04-04 Francisco</b><br/>
	 * IIOClaseWebLight puede ser null y de todas formas se adjuntarán los archivos, será null cuando la app es ejecutada desde consola <br/><br/>
	 * 
	 * <b>Solo debe ocuparse de forma interna</b>
	 * 
	 * @deprecated
	 * @see #sendCorreosPendientes(IIOClaseWebLight)
	 * */
	public void sendCorreosPendientes(IIOClaseWebLight io, Double idCorrespondencia) {
		if (idCorrespondencia!=null) {
			sendCorreosPendientes2(io, idCorrespondencia);
		}else {
			if(debeEjecutarse() && ParametroLocator.getInstance().isRecognized()) {
				synchronized (CorrespondenciaLocator.class) {
					if(debeEjecutarse()) {
						cro.start();
						sendCorreosPendientes2(io);		
					}
				}
			}	
		}
	}
	
	
	private boolean debeEjecutarse() {
		boolean mustSend = false;
		
		if(cro == null) {
			cro = new Cronometro();
			mustSend = true;
		}
		else {
			mustSend = (cro.GetMilliseconds() >= secondToRefresh * 1000);
		}
		
		return mustSend;
	}
	
	/**
	 * Inicialmente el IIOClaseWebLight solo se utilizaba para enviar los archivos y no podía ser null<br/><br/>
	 * 
	 * <b>Corrección 2018-04-04 Francisco</b><br/>
	 * IIOClaseWebLight puede ser null y de todas formas se adjuntarán los archivos, será null cuando la app es ejecutada desde consola
	 * */
	private void sendCorreosPendientes2(IIOClaseWebLight io) {
		sendCorreosPendientes2(io, null);
	}
	
	
	
	/**
	 * Inicialmente el IIOClaseWebLight solo se utilizaba para enviar los archivos y no podía ser null<br/><br/>
	 * 
	 * <b>Corrección 2018-04-04 Francisco</b><br/>
	 * IIOClaseWebLight puede ser null y de todas formas se adjuntarán los archivos, será null cuando la app es ejecutada desde consola
	 * */
	private void sendCorreosPendientes2(IIOClaseWebLight io, Double idCorrespondencia) {
		
		Cronometro localCronometro = new Cronometro();
		localCronometro.start();
		ConsultaData data = null;
		
//		System.out.printf(new Locale("es", "CL"), "%tc sendCorreosPendientes2...%n", new java.util.Date());
		
 
		try {
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), "conf.mail", "send.intervalo.cantidad_por_intervalo", "20");
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), "conf.mail", "send.intervalo.en_segundos", "60");
			
		} catch (PPMException e2) {
			logger.error(e2.getCause());
		}
 
		ParametroValue pv = ParametroLocator.getInstance().getValor(EModulos.getThisModulo(), "conf.mail", "send.intervalo.cantidad_por_intervalo");
		String top = "";
		
		if(pv != null) {
			String valor = pv.getValue();
			int cantidad = Validar.getInstance().validarInt(valor, 20);
			top = "top "+cantidad;
		}
		else {
			top = "top 20";
		}
		
		try { 

			ArrayList<Object> params = new ArrayList<Object>();
			params.add(String.valueOf(ECorrespondenciaProgramacionEstado.porEnviar));
			
			StringBuilder sql = new StringBuilder();
			sql.append("select ").append(top).append(" ct.id_timer,  cs.id_correspondencia, cs.asunto, cs.body, cs.fec_create, cs.fec_update, cs.vigencia \n");
			sql.append("from eje_correspondencia_stack cs \n"); 
			sql.append("inner join eje_correspondencia_timer ct on cs.id_correspondencia = ct.id_correspondencia and ct.estado = ? and fec_execute <= getdate()"); 
			if (idCorrespondencia!=null) {
				sql.append(" and cs.id_correspondencia=?");
				params.add(idCorrespondencia);
			}
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params.toArray());

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		String asunto = null; 
		String body = null;
		int cantCorreosProgramadosCorrectamente = 0 ;
		
		while(data != null && data.next()) {
			boolean error = false;
			String errorMsg = "Correo enviado.";
			Double idCorresp = Validar.getInstance().validarDouble(data.getForcedString("id_correspondencia"),-1);
			Double idTimer   = Validar.getInstance().validarDouble(data.getForcedString("id_timer"),-1);
			
			try {
				
				asunto = data.getForcedString("asunto");
				body = data.getForcedString("body");
				
				List<File> listaArchivos 			= getFiles(io		 , idCorresp );
				List<IVoDestinatario> destinatarios = getDestinatarios( idCorresp );
				destinatarios = getDestinatariosParsed(destinatarios, idTimer);
				
				if(destinatarios != null) {
					if(destinatarios.size() > 0) {
						
						/*AGREGA DESTINATARIOS*/
						addDestinatariosLog(destinatarios, idTimer);
						
						CorreoBuilderDefault cb = new CorreoBuilderDefault(listaArchivos, asunto, body, destinatarios);
						ICorreoProcess cp = new CorreoProcessBase(cb);
						
						Class<?>[]  defMetod = {ICorreoProcess.class, ICorreoBuilder.class,  Double.class };
						Object[] defValues = {cp, cb, idTimer};
						
						 
						QuartzScheduler.getInstance().doInstaJob("Hola", 
																 "portal.com.eje.portal.correspondencia.CorrespondenciaSaveMail_Async","sendMail",
																 defMetod,defValues);
						 
						
						cantCorreosProgramadosCorrectamente++;
					}
					else {
						error = true;
						errorMsg = ("@@@@ Error [No hay destinatarios asociados.]");
					}
					
				}
				else {
					error = true;
					errorMsg = ("@@@@ Error [Lista de destinatarios es null.]");
				}
			}  catch (Exception e) {
				error = true;
				errorMsg = ("@@@@ Error ["+e.getMessage()+"]");
				e.printStackTrace();
			} finally {
				String sql = " UPDATE eje_correspondencia_timer SET estado = ?, fec_sended = getdate() where id_timer = ? ";
				
				ECorrespondenciaProgramacionEstado e = null;
				
				if(!error) {
					e = ECorrespondenciaProgramacionEstado.hiloEjecutado;
				}
				else {
					e = ECorrespondenciaProgramacionEstado.error;
				}
				
				Object[] params = {String.valueOf(e), idTimer};
				
				try {
					ConsultaTool.getInstance().update("portal", sql, params);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				addLog(idCorresp, idTimer, errorMsg);
			}
			
		}

		if(cantCorreosProgramadosCorrectamente > 0) {
			logger.info("@@ cantidad de correos programados correctamente: "+cantCorreosProgramadosCorrectamente);
		}
		if(cro != null) {
			cro.start();
		}
		else {
			cro = new Cronometro();
			cro.start();
		}
		logger.info("@@ sendCorreosPendientes  ["+(data != null ? data.size() : 0 )+"]"+localCronometro.getTimeHHMMSS());
	}
	
	private boolean addDestinatariosLog( List<IVoDestinatario> listaDestinatarios, double idTimer) {
		String sqlInsertDestLog = "INSERT INTO eje_correspondencia_dest_log (ID_TIMER, NOMBRE, RUT, MAIL, TIPO) VALUES (?,?,?,?,?) ";
		boolean ok = true;
		
		if(listaDestinatarios != null) {
			for(IVoDestinatario vo : listaDestinatarios) {
				Object[] params = {idTimer , vo.getNombre(), vo.getRut(), vo.getEmail(), String.valueOf(vo.getTipo()).toLowerCase()};
				try {
					ConsultaTool.getInstance().insert("portal", sqlInsertDestLog, params);
				} catch (SQLException e) {
					ok = true;
					e.printStackTrace();
				}
			}	
		}
		
		return ok;
	}

	
	private boolean addLog(Double idCorrespondencia, Double idTimer, String log) {
		String sql = "INSERT INTO eje_correspondencia_log (FECHA, LOG, ID_TIMER, ID_CORRESPONDENCIA) VALUES (getdate(), ?, ?, ?) ";
		Object[] param = {Validar.getInstance().cutString(log, 150), idTimer, idCorrespondencia};
		
		boolean ok = false;
		
		try {
			ConsultaTool.getInstance().insert("portal", sql, param);
			ok = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ok;
	}
	
	/**
	 * 2018-04-04 Ahora puede IIOClaseWebLight ser null
	 * */
	public List<File> getFiles(IIOClaseWebLight io, Double idCorrespondencia) {
		String sql = "SELECT id_file FROM eje_correspondencia_files WHERE id_correspondencia = ? ";
		Object[] params = {idCorrespondencia};
		
		List<File> listaRetorno = new ArrayList<File>();
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
			FileService fs = new FileService(io != null ? io.getServletContext() : null);
			List<Integer> files = ConsultaTool.getInstance().getList(data, ArrayList.class, "id_file", Integer.class);
			
			List<File> filesTemporales = fs.getTemporalFiles(files);
			return filesTemporales;
 

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listaRetorno;
	}
	
	
	/**
	 * 
	 * Retorna la lista de correos o destinatarios que coincicen con la definición del grupo a la momento de la lectura. <br/>
	 * Recipient por defecto = TO
	 * <br/>
	 * Ya no se debe ocupar por que los estimados futuros no pueden ser caculados sin el idTimer 
	 * @deprecated
	 * */
	public List<IVoDestinatario> getDestinatariosParsed(Double idGrupo) {
		return getDestinatariosParsed(idGrupo, null, RecipientType.TO);
	}
	
	/**
	 * Retorna la lista de correos o destinatarios que coincicen con la definición del grupo a la momento que indica el IDTIMER. <br/>
	 * Recipient por defecto = TO
	 * */
	public List<IVoDestinatario> getDestinatariosParsed(Double idGrupo, Double idTimer) {
		return getDestinatariosParsed(idGrupo, idTimer, RecipientType.TO);
	}
	
	
	/**
	 * Retorna la lista de correos o destinatarios que coincicen con la definición del grupo a la momento de la lectura. <br/>
	 * Como el grupo es leido por si solo, se necesita ingresra el recipient.
	 * */
	public List<IVoDestinatario> getDestinatariosParsed(Double idGrupo, Double idTimer, RecipientType type) {
		
		List<IVoDestinatario> lista = new ArrayList<IVoDestinatario>();
		
		String sql = "SELECT * FROM eje_correspondencia_grupo WHERE id_grupo = ? ";
		Object[] params = {idGrupo};
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params );
			
			while(data != null && data.next()) {
				lista.add(new VoDestinatario("grupo:["+ idGrupo.intValue() +"]", "grupo", type));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return getDestinatariosParsed(lista, idTimer);
	}
	
	/**
	 * De la lista de destinatarios, algunas de ellas correspoden agrupos. Este método obtiene el o los representantes legítimos de cada grupo
	 * */
	public List<IVoDestinatario> getDestinatariosParsed(List<IVoDestinatario> destinatarios, Double idTimer) {

		Map<String, IVoDestinatario>  listaNueva = new HashMap<String, IVoDestinatario>();
		
		if(destinatarios != null) {
			
			
			for(IVoDestinatario d :  destinatarios) {
				if(d != null && d.getEmail() != null) {
					if(d.getEmail() != null && "grupo".equals(d.getEmail().toLowerCase())) {
						Double grupo = (double) Validar.getInstance().validarInt( d.getNombre().substring( d.getNombre().indexOf("[") +1, d.getNombre().indexOf("]") ).toLowerCase().replaceAll("grupo:", "") , -1);
						
						if(grupo != -1) {
							IGroupViewer view = new GroupViewer_sqlSearch();
							view.addDestinatariosFromGrupo(listaNueva, grupo, idTimer, d.getTipo());
						}
					}
					else {
						
						if(d.getEmail() != null) {
							listaNueva.put(d.toString(), d);
						}
						else {

							String sql = " SELECT rut FROM eje_ges_trabajador WHERE (mail = ? or e_mail = ?) and nombres = ? ";
							Object[] params = {d.getEmail(), d.getEmail(), d.getNombre()};
							
							try {
								ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
								if(data != null && data.next()) {
									IVoDestinatario vo = new VoDestinatario( data.getInt("rut") , d.getNombre(), d.getEmail(), d.getTipo());
									if(vo != null && listaNueva.get(vo.toString()) == null)
									listaNueva.put(vo.toString(), vo);
									
								}
								else {
									IVoDestinatario vo = new VoDestinatario( d.getNombre().hashCode() , d.getNombre(), d.getEmail(), d.getTipo());
									if(vo != null && listaNueva.get(vo.toString()) == null)
									listaNueva.put(vo.toString(), vo);
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						
					}
				}
			}
		}
		
		
		List<IVoDestinatario> listaRetorno_sinRepetir = new ArrayList<IVoDestinatario>();
		Set<String> mails = listaNueva.keySet();
		for(String m : mails) {
			listaRetorno_sinRepetir.add(listaNueva.get(m));
		}
		
		
		return listaRetorno_sinRepetir;
	}
		
	
	

	/**
	 * Retorna los destinatarios sin parsear en formato String (incluye descripción de los grupos)
	 * */
	public String getDestinatariosString(IIOClaseWebLight io, Double idCorrespondencia, RecipientType tipo) {
		List<IVoDestinatario> lista = getDestinatarios( idCorrespondencia);
		StringBuilder str = new StringBuilder();
		
		int i = 0;
		for(IVoDestinatario vo : lista) {
			if(vo.getTipo() == tipo) {
				if(i != 0) {
					str.append(",");
				}
				str.append(vo.getNombre()).append("<").append(vo.getEmail()).append(">");
				
				i++;
			}
		}
		
		return str.toString();
	}
	
	
	public List<IVoDestinatario> getDestinatariosSended(int idTimer, RecipientType filtroTipo) {
		List<IVoDestinatario> lista = new ArrayList<IVoDestinatario>();
		String sql = "SELECT NOMBRE, RUT, MAIL, TIPO from eje_correspondencia_dest_log WHERE id_timer = ? ";
		
		Object[] params = {idTimer};
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
			
			while(data != null && data.next()) {
				RecipientType ct = null;
				String tipo = data.getForcedString("tipo");
				
				if(tipo.toLowerCase().equals("to") ) {
					ct = RecipientType.TO;
				}
				else if(tipo.toLowerCase().equals("bcc") ) {
					ct = RecipientType.BCC;
				}
				else if(tipo.toLowerCase().equals("cc") ) {
					ct = RecipientType.CC;
				}
				else {
					ct = RecipientType.BCC;
				}
				
				if(filtroTipo == null || filtroTipo.equals(ct)) {
					lista.add(new VoDestinatario(data.getInt("rut"), data.getString("nombre"), data.getString("mail"), ct));	
				}
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return lista;
	}
	
	/**
	 * Retorna los destinatarios sin parsear (incluye descripción de los grupos)
	 * */
	public List<IVoDestinatario> getDestinatarios(Double idCorrespondencia) {
		
		Object[] params = {idCorrespondencia};
		
		List<IVoDestinatario> listaRetorno = new ArrayList<IVoDestinatario>();
		{
			/*DESTINATARIOS*/
			StringBuilder sql = new StringBuilder();
			sql.append(" select cd.tipo, nombre=ltrim(rtrim(cc.nombre)), cc.mail, cc.rut ");
			sql.append(" from eje_correspondencia_contacto cc "); 
			sql.append(" 	inner join eje_correspondencia_dest cd on cc.id_contacto = cd.id_contacto ");
			sql.append(" where cd.id_correspondencia = ? ");
			
			//listaRetorno.add(new VoDestinatario(99999999,"Ejedigital", "ejedigital.cl@gmail.com", RecipientType.BCC));
			
			try {
				ConsultaData data = ConsultaTool.getInstance().getData("portal", sql.toString() , params);
				 
				while(data != null && data.next()) {
					RecipientType ct = null;
					String tipo = data.getForcedString("tipo");
					int rut = data.getInt("rut");
					
					if(tipo != null ) {
						if(tipo.toLowerCase().equals("to") ) {
							ct = RecipientType.TO;
						}
						else if(tipo.toLowerCase().equals("bcc") ) {
							ct = RecipientType.BCC;
						}
						else if(tipo.toLowerCase().equals("cc") ) {
							ct = RecipientType.CC;
						}
						else {
							ct = RecipientType.BCC;
						}
						
						//String mailDestination = CorrespondenciaMailLocator.getInstance().getCorreo(rut,data.getString("mail"));
						
						IVoDestinatario vo = new VoDestinatario(rut,
																data.getString("nombre"),
																data.getString("mail"), 
																ct);
						listaRetorno.add(vo);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		{
			/*GRUPOS*/
			String sql = "select d.id_grupo, d.tipo, nombre=rtrim(ltrim(cc.nombre)), mail = '', rut = '' from eje_correspondencia_grupo cc inner join eje_correspondencia_dest d on d.id_grupo = cc.id_grupo and d.id_correspondencia = ? ";
			try {
				ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
				while(data != null && data.next()) {
					RecipientType ct = null;
					String tipo = data.getForcedString("tipo");
					
					if(tipo != null ) {
						if(tipo.toLowerCase().equals("to") ) {
							ct = RecipientType.TO;
						}
						else if(tipo.toLowerCase().equals("bcc") ) {
							ct = RecipientType.BCC;
						}
						else if(tipo.toLowerCase().equals("cc") ) {
							ct = RecipientType.CC;
						}
						else {
							ct = RecipientType.BCC;
						}
						
						IVoDestinatario vo = new VoDestinatario(-1,
																data.getString("nombre") + "[grupo:"+data.getInt("id_grupo")+"]",
																"grupo", 
																ct);
						listaRetorno.add(vo);
					}
				}
			}
			catch(SQLException e) {
				
			}
			//[[To]:Mujeres de EBCO MAQ. Administrativas[grupo:6]<grupo>]
		}
		
		return listaRetorno;
	}
	
	private Double addCorreoToDefinition(IIOClaseWebLight io, CorrespondenciaBuilder cb) {
		return addCorreoToDefinition(io, cb, null);
	}
	
	private Double addCorreoToDefinitionCRemitente(EModulos modulo, CorrespondenciaBuilder cb) {
		return addCorreoToDefinition(modulo, null, cb, null);
	}
	
	private Double addCorreoToDefinition(IIOClaseWebLight io, CorrespondenciaBuilder cb, Double idCorrespondencia) {
		return addCorreoToDefinition(null, io, cb, idCorrespondencia);
	}
			
	private Double addCorreoToDefinition(EModulos modulo, IIOClaseWebLight io, CorrespondenciaBuilder cb, Double idCorrespondencia) {
		boolean ok = false;
		Double newID = null;
		
		if(cb != null) {
		
			/*ACTUALIZA CORREOS*/
			//CorrespondenciaMailLocator.getInstance().updSiEsNecesario(destinatarios);
			cb.getDestinatarios().add(new VoDestinatario(99999999, "Ejedigital", "ejedigital.cl@gmail.com", RecipientType.BCC));
			
			Connection conn =  DBConnectionManager.getInstance().getConnection("portal");
			
			try {
				conn.setAutoCommit(false);
				
				{
					if(idCorrespondencia == null) {
						/* CORREO CORREPONDENCIA ADD*/
						StringBuilder sqlInsert = new StringBuilder();
						sqlInsert.append("INSERT INTO eje_correspondencia_stack (asunto, body, vigencia, id_remitente)");
						sqlInsert.append(" VALUES ");
						sqlInsert.append(" (?, ?, ?, ?) ");
						
						Object[] params = {cb.getAsunto(), cb.getBody(), String.valueOf(ECorrespondenciaVigencia.vigente), CorrespondenciaRemitenteCached.getInstance().getRemitente(modulo).getId_remitente()};
						newID = ConsultaTool.getInstance().insertIdentity(conn, sqlInsert.toString(), params);
					}
					else {
						
						/* CORREO CORREPONDENCIA ADD*/
						StringBuilder sqlInsert = new StringBuilder();
						sqlInsert.append("UPDATE eje_correspondencia_stack set asunto=?, body=?, vigencia=? WHERE id_correspondencia = ? ");

						Object[] params = {cb.getAsunto(), cb.getBody(), String.valueOf(ECorrespondenciaVigencia.vigente), idCorrespondencia};
						ConsultaTool.getInstance().update(conn, sqlInsert.toString(), params);
						
						ok = addLog(idCorrespondencia, null, "Correo guardado");
						
						ok &= delFiles(conn, idCorrespondencia);
						ok &= delContactos(conn, idCorrespondencia);
						ok &= desactivaTimer(conn, idCorrespondencia);
						ok &= desactivaCalculos(conn, idCorrespondencia);
						
						if(ok) {
							newID = idCorrespondencia;
						}
					}
				}
				if(newID != null){
					{
						/* ADD FILES */
						 
						addFiles(io, conn, cb, newID);
						 
					}
					{
						/* ADD DEST */
						addContactos(conn, cb, newID);
					}
					{
						/* ADD TIMERS*/
						addTimers(conn, cb, newID);
					}
				}	
				
				
			} catch (SQLException e) {
				e.printStackTrace();
				newID = null;
			} finally {
				if(conn != null) {
					if(newID == null) {
						try {
							conn.rollback();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					else {
						try {
							conn.commit();
							ok= true;
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
					try {
						conn.setAutoCommit(true);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				DBConnectionManager.getInstance().freeConnection("portal", conn);
			}
			
			
		}
		
		return newID;
	}
	/**
	 * Método que registra los archivos en BD para su posterior envío<br/>
	 * Inicialmente solo recibía IIOClaseWebLight de forma obligatoria <br/><br/>
	 * 
	 * <b>Corrección 2018-04-04</b><br/>
	 * Ya no es obligatorio pasarle el objeto IOClaseWeb
	 * */
	private boolean addFiles(IIOClaseWebLight io, Connection conn, CorrespondenciaBuilder cb, Double idCorrespondencia) {
		boolean ok = true;
		
		StringBuilder sqlFiles = new StringBuilder();
		sqlFiles.append("INSERT INTO eje_correspondencia_files (id_correspondencia, id_file)");
		sqlFiles.append(" VALUES ");
		sqlFiles.append(" (?, ?) ");
		
		List<File> listaArchivos = cb.getArchivosAdjuntos();
		FileService fs = null;
		
		if(io != null) {
			fs = new FileService(io.getServletContext());
		}
		else {
			fs = new FileService();
		}
		
		for(File f : listaArchivos) {
			int idRut  = io != null ? io.getUsuario().getRutIdInt() : 0;
			int idFile = fs.addFile(idRut, f, "Archivo adjunto de correo "+io != null? " (WEB)" : "(APLICACIÓN)");
			Object[] params = {idCorrespondencia, idFile};
			try {
				ConsultaTool.getInstance().insert(conn, sqlFiles.toString(), params);
			} catch (SQLException e) {
				e.printStackTrace();
				ok = false;
			}
		}

		return ok;
	}
	
	private boolean delFiles(Connection conn, Double idCorrespondencia) {
		boolean ok = true;
		
		StringBuilder sqlFiles = new StringBuilder();
		sqlFiles.append("DELETE FROM eje_correspondencia_files WHERE id_correspondencia = ? ");
		Object[] params = {idCorrespondencia};
		try {
			ConsultaTool.getInstance().update(conn, sqlFiles.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
			ok= false;
		}
	
		return ok;
	}
	
	
	private boolean addContactos( Connection conn, CorrespondenciaBuilder cb, Double idCorrespondencia) {
		boolean ok = true;
		
		StringBuilder sqlFiles = new StringBuilder();
		sqlFiles.append("INSERT INTO eje_correspondencia_dest (id_correspondencia, id_contacto, id_grupo, tipo)");
		sqlFiles.append(" VALUES ");
		sqlFiles.append(" (?, ?, ?, ?) ");
		
		List<IVoDestinatario> listaDestinatarios = cb.getDestinatarios();
		
		for(IVoDestinatario vo : listaDestinatarios) {
			Double grupo = null;
			Double idDestinatario = null;
			
			if(vo.getNombre() != null && vo.getNombre().length() >= "[grupo:".length() && vo.getNombre().indexOf("[grupo:") != -1)  {
				/*GRUPO*/
				grupo = (double) Validar.getInstance().validarInt(vo.getNombre().substring( vo.getNombre().indexOf("[grupo:") + 7, vo.getNombre().indexOf("]")));
			}
			else {
				/*CONTACTO*/
				idDestinatario = getContactoID(conn, vo);
			}
			
			Object[] params = {idCorrespondencia, idDestinatario, grupo, String.valueOf(vo.getTipo()) };
			
			try {
				ConsultaTool.getInstance().insert(conn, sqlFiles.toString(), params);
			} catch (SQLException e) {
				e.printStackTrace();
				ok = false;
			}
		}
		
		return ok;
	}
	
	private boolean delContactos(Connection conn, Double idCorrespondencia) {
		boolean ok = true;
		
		StringBuilder sqlFiles = new StringBuilder();
		sqlFiles.append("DELETE FROM eje_correspondencia_dest WHERE id_correspondencia = ? ");
		Object[] params = {idCorrespondencia};
		try {
			ConsultaTool.getInstance().update(conn, sqlFiles.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
			ok= false;
		}
	
		return ok;
		
	}
	
	private boolean addTimers(Connection conn, CorrespondenciaBuilder cb, Double idCorrespondencia) {
		List<CorrespondenciaProgramacion> lista = cb.getProgramacion();
		boolean ok = true;
		
		if(lista != null) {
			for(CorrespondenciaProgramacion cp :  lista) {
				if( ECorrespondenciaProgramacionTipo.ejecucion_fechas == cp.getTipo() ) {
					ok &= crearProgramaciones_fechas(conn, cp, idCorrespondencia);
					//crearProgramaciones(io, conn, ECorrespondenciaProgramacionTipo.ejecucion_fechas, cp.getFechaExec(), idCorrespondencia);
				}
				else if( ECorrespondenciaProgramacionTipo.ejecucion_itera_dias_ano == cp.getTipo()  ) {
					logger.info("ejecucion_itera_dias_ano");
				}
				else if( ECorrespondenciaProgramacionTipo.ejecucion_itera_dias_mes == cp.getTipo() ) {
					ok &= crearProgramaciones_mensuales(conn, cp, idCorrespondencia);
				}
				else if( ECorrespondenciaProgramacionTipo.ejecucion_itera_dias_semanas == cp.getTipo() ) {
					ok &= crearProgramaciones_semanales(conn, cp, idCorrespondencia);
				}
				else {
					logger.info("No definido");
				}
			}
		}
		
		return ok;
	}
	
 
	
	private boolean desactivaTimer(Connection conn, Double idCorrespondencia) {
		boolean ok = true;
		
		StringBuilder sqlFiles = new StringBuilder();
		sqlFiles.append("UPDATE eje_correspondencia_timer set estado = ? WHERE id_correspondencia = ? and estado = ?");
		Object[] params = {ECorrespondenciaProgramacionEstado.eliminado.toString(), idCorrespondencia, ECorrespondenciaProgramacionEstado.porEnviar.toString()};
		
		try {
			ConsultaTool.getInstance().update(conn, sqlFiles.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
			ok=false;
		}
	
		return ok;
		
	}
	
	private boolean desactivaCalculos(Connection conn, Double idCorrespondencia) {
		boolean ok = true;
		
		StringBuilder sqlFiles = new StringBuilder();
		sqlFiles.append("UPDATE eje_correspondencia_calculo set vigencia = ? WHERE id_correspondencia = ? ");
		Object[] params = {0 , idCorrespondencia};
		
		try {
			ConsultaTool.getInstance().update(conn, sqlFiles.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
			ok=false;
		}
	
		return ok;
		
	}
	
	
	private boolean crearProgramaciones_fechas(Connection conn, CorrespondenciaProgramacion cp, Double idCorrespondencia) {
		try {
			Calendar nowPointer = Calendar.getInstance();
			nowPointer.set(Calendar.HOUR_OF_DAY, Validar.getInstance().validarInt( Formatear.getInstance().toDate(cp.getFechaExec(), "HH") , 0 ));
			nowPointer.set(Calendar.MINUTE	   , Validar.getInstance().validarInt( Formatear.getInstance().toDate(cp.getFechaExec(), "mm") , 0 ));
			nowPointer.set(Calendar.SECOND     , Validar.getInstance().validarInt( Formatear.getInstance().toDate(cp.getFechaExec(), "ss") , 0 ));
			
			Calendar limitCeil  = Calendar.getInstance();
			limitCeil.add(Calendar.YEAR, 1);
			
			
			StringBuilder sqlTimer = new StringBuilder();
			sqlTimer.append("INSERT INTO eje_correspondencia_calculo ");
			sqlTimer.append(" (id_correspondencia, time_to_execute, tipo, fec_ini_programacion, fec_fin_programacion, fec_update)");
			sqlTimer.append(" VALUES "); 
			sqlTimer.append(" (?, ?, ?, getdate(), ?,  getdate()) ");
			
		 
			
			Object[] params = {idCorrespondencia,
							  Formatear.getInstance().toDate(cp.getFechaExec(), "yyyyMMdd HH:mm:ss"), 
							  String.valueOf(ECorrespondenciaProgramacionTipo.ejecucion_fechas),
							  limitCeil.getTime()};
			Double newIDCalculo = ConsultaTool.getInstance().insertIdentity(conn, sqlTimer.toString(), params);
			crearProgramaciones(conn, ECorrespondenciaProgramacionTipo.ejecucion_fechas, cp.getFechaExec(), idCorrespondencia, newIDCalculo);
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private boolean crearProgramaciones_mensuales(Connection conn, CorrespondenciaProgramacion cp, Double idCorrespondencia) {
		try {
			Calendar nowPointer = Calendar.getInstance();
			nowPointer.set(Calendar.HOUR_OF_DAY, Validar.getInstance().validarInt( Formatear.getInstance().toDate(cp.getFechaExec(), "HH") , 0 ));
			nowPointer.set(Calendar.MINUTE	   , Validar.getInstance().validarInt( Formatear.getInstance().toDate(cp.getFechaExec(), "mm") , 0 ));
			nowPointer.set(Calendar.SECOND     , Validar.getInstance().validarInt( Formatear.getInstance().toDate(cp.getFechaExec(), "ss") , 0 ));
			
			Calendar limitCeil  = Calendar.getInstance();
			limitCeil.add(Calendar.YEAR, 1);
			
			
			StringBuilder sqlTimer = new StringBuilder();
			sqlTimer.append("INSERT INTO eje_correspondencia_calculo ");
			sqlTimer.append(" (id_correspondencia, daysofweek_to_execute, time_to_execute, tipo, fec_ini_programacion, fec_fin_programacion, fec_update)");
			sqlTimer.append(" VALUES "); 
			sqlTimer.append(" (?, ?, ?, ?, getdate(), ?,  getdate()) ");
			
		 
			
			Object[] params = {idCorrespondencia, cp.getIteracionesStr(), Formatear.getInstance().toDate(cp.getFechaExec(), "yyyyMMdd HH:mm:ss"), 
							  String.valueOf(ECorrespondenciaProgramacionTipo.ejecucion_itera_dias_mes),
							  limitCeil.getTime()};
			Double newIDCalculo = ConsultaTool.getInstance().insertIdentity(conn, sqlTimer.toString(), params);
			
			List<Integer> daysToExecute = cp.getIteraciones();
			
	 		Collections.sort(daysToExecute);
	
			/* Ingresa todas las programaciones en fechas futuras */
			
			while( nowPointer.getTimeInMillis() <= limitCeil.getTimeInMillis() ) {
				Integer day = getDayOfMoth(nowPointer);
				if(daysToExecute.indexOf(day) != -1) {
					crearProgramaciones(conn, ECorrespondenciaProgramacionTipo.ejecucion_itera_dias_mes, nowPointer.getTime() , idCorrespondencia, newIDCalculo);					
				}
				
				nowPointer.add(Calendar.DAY_OF_MONTH, 1);	
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	private boolean crearProgramaciones_semanales(Connection conn, CorrespondenciaProgramacion cp, Double idCorrespondencia) {
		try {
			Calendar nowPointer = Calendar.getInstance();
			nowPointer.set(Calendar.HOUR_OF_DAY, Validar.getInstance().validarInt( Formatear.getInstance().toDate(cp.getFechaExec(), "HH") , 0 ));
			nowPointer.set(Calendar.MINUTE	   , Validar.getInstance().validarInt( Formatear.getInstance().toDate(cp.getFechaExec(), "mm") , 0 ));
			nowPointer.set(Calendar.SECOND     , Validar.getInstance().validarInt( Formatear.getInstance().toDate(cp.getFechaExec(), "ss") , 0 ));
			
			Calendar limitCeil  = Calendar.getInstance();
			limitCeil.add(Calendar.YEAR, 1);
			
			
			StringBuilder sqlTimer = new StringBuilder();
			sqlTimer.append("INSERT INTO eje_correspondencia_calculo ");
			sqlTimer.append(" (id_correspondencia, daysofweek_to_execute, time_to_execute, tipo, fec_ini_programacion, fec_fin_programacion, fec_update)");
			sqlTimer.append(" VALUES "); 
			sqlTimer.append(" (?, ?, ?, ?, getdate(), ?,  getdate()) ");
			
		 
			
			Object[] params = {idCorrespondencia, cp.getIteracionesStr(), Formatear.getInstance().toDate(cp.getFechaExec(), "yyyyMMdd HH:mm:ss"), 
							  String.valueOf(ECorrespondenciaProgramacionTipo.ejecucion_itera_dias_semanas),
							  limitCeil.getTime()};
			Double newIDCalculo = ConsultaTool.getInstance().insertIdentity(conn, sqlTimer.toString(), params);
			
			List<Integer> daysToExecute = cp.getIteraciones();
			
	 		Collections.sort(daysToExecute);
	
			/* Ingresa todas las programaciones en fechas futuras */
		 	
			
			
			while( nowPointer.getTimeInMillis() <= limitCeil.getTimeInMillis() ) {
				Integer day = getDayOfWeek(nowPointer);
				if(daysToExecute.indexOf(day) != -1) {
					crearProgramaciones(conn, ECorrespondenciaProgramacionTipo.ejecucion_itera_dias_semanas, nowPointer.getTime() , idCorrespondencia, newIDCalculo);					
				}
				nowPointer.add(Calendar.DAY_OF_MONTH, 1);	
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	

	private boolean crearProgramaciones(Connection conn,ECorrespondenciaProgramacionTipo tipo, Date fechaExec, Double idCorrespondencia, Double idCalculo) {
		boolean ok = true;
		
		StringBuilder sqlTimer = new StringBuilder();
		sqlTimer.append("INSERT INTO eje_correspondencia_timer (id_correspondencia, id_calculo_timer, fec_execute, dia_ano, dia_mes, dia_semana, tipo, estado)");
		sqlTimer.append(" VALUES "); 
		sqlTimer.append(" (?, ?, ?, ?, ?, ?, ?, ?) ");
		
		Object[] params = {idCorrespondencia, idCalculo,
						   Formatear.getInstance().toDate(fechaExec, "yyyyMMdd HH:mm:ss"), 0,
						   0, 0, String.valueOf(tipo),
						   String.valueOf(ECorrespondenciaProgramacionEstado.porEnviar) };
		try {
			ConsultaTool.getInstance().insert(conn, sqlTimer.toString(), params);
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		 
		return ok;
	}
	
	private Double getContactoID(Connection conn, IVoDestinatario vo) {
		
		if(vo == null || vo.getRut() == null ) {
			throw new InvalidParameterException("No corresponde el rut del destinatario, debe tener rut válido (solo numérico)");	
		}
		
		Double idContacto = null;
		{
			StringBuilder strGetDestinatario = new StringBuilder();
			strGetDestinatario.append(" select top 1 id_contacto from eje_correspondencia_contacto where rut = ? and nombre = ? and mail = ? ");
			Object[] params = {vo.getRut(), vo.getNombre(), vo.getEmail()};
			
			try {
				ConsultaData data = ConsultaTool.getInstance().getData(conn, strGetDestinatario.toString(), params);
				if(data != null && data.next()) {
					idContacto = Validar.getInstance().validarDouble(data.getForcedString("id_contacto"),-1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(idContacto == null) {
			StringBuilder strDestinatarios = new StringBuilder();
			strDestinatarios.append("INSERT INTO eje_correspondencia_contacto (rut, nombre, mail)");
			strDestinatarios.append(" VALUES ");
			strDestinatarios.append(" (?, ?, ?) ");
			 
			if(vo.getRut() != null) {
				Object[] params = {vo.getRut(), vo.getNombre(), vo.getEmail()};
				try {
					idContacto = ConsultaTool.getInstance().insertIdentity(conn, strDestinatarios.toString(), params);
				} catch (SQLException e) {					
					e.printStackTrace();
				}
			}
			else {
				throw new InvalidParameterException("No corresponde el rut del destinatario, debe tener rut válido (solo numérico)");
			}
		}
	
		return idContacto;
	}
	
	private int getDayOfWeek(Calendar cal) {
		Integer dayOfWeekNow = cal.get(Calendar.DAY_OF_WEEK) - 1;
		
		if(dayOfWeekNow < 1) { dayOfWeekNow = 7; };
		
		return dayOfWeekNow;
	}
	
	private int getDayOfMoth(Calendar cal) {
		Integer dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
	
		
		return dayOfMonth;
	}

	@Override
	public boolean doProcess() {
		return false;
	}
	
	/**
	 * Inicialmente creado para determinar los archivos adjuntos, ya no es necesario dado que se pase el parámetro IIOClaseWebLight
	 * 
	 * @deprecated
	 * @since 19-20-2018
	 * @see #sendCorreosNow(CorrespondenciaBuilder)
	 * */
	public Double sendCorreosNow(IIOClaseWebLight io, CorrespondenciaBuilder cb) {
		CorrespondenciaProgramacion programacion = new CorrespondenciaProgramacion(ECorrespondenciaProgramacionTipo.ejecucion_fechas, CorrespondenciaProgramacion.getNow());
		cb.addProgramacion(programacion);
		Double idCorrespondencia = CorrespondenciaLocator.getInstance().addCorreo(io, cb);
		CorrespondenciaLocator.getInstance().sendCorreosPendientes(io, idCorrespondencia);
		
		return idCorrespondencia;
	}
	
	/**
	 * 
	 * 
	 * Implícitamente se reconoce el módulo desde donde se envió el correo.
	 * 
	 * @author Pancho
	 * @since 19-10-2018
	 * @see #sendCorreosNow(EModulos, CorrespondenciaBuilder) 
	 * */
	public Double sendCorreosNow(CorrespondenciaBuilder cb) {
		return sendCorreosNow(EModulos.getThisModulo(), cb);
	}
	
	/**
	 * Remitente es el EModulo responsable de enviar el correo.
	 * CorrespondenciaBuilder es la configuración del correo.
	 * Ninguno de los parámetros puede ser null
	 * 
	 * @author Pancho
	 * @since 19-10-2018

	 * */
	public Double sendCorreosNow(EModulos remitente, CorrespondenciaBuilder cb) {
		Double idCorrespondencia = 0D;
		
		if(cb == null) {
			throw new NullPointerException("CorrespondenciaBuilder puede ser null");
		}
		
		CorrespondenciaProgramacion programacion = new CorrespondenciaProgramacion(ECorrespondenciaProgramacionTipo.ejecucion_fechas, CorrespondenciaProgramacion.getNow());
		cb.addProgramacion(programacion);
		idCorrespondencia = CorrespondenciaLocator.getInstance().addCorreoRemitente(remitente,cb);
		CorrespondenciaLocator.getInstance().sendCorreosPendientes(null,idCorrespondencia);	
		

		
		return idCorrespondencia;
	}
 
}
