package portal.com.eje.portal.correspondencia;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import cl.ejedigital.tool.correo.CorreoDispatcher;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.ifaces.ICorreoProcess;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.PPMException;
import portal.com.eje.portal.correspondencia.CorrespondenciaProgramacion.ECorrespondenciaProgramacionEstado;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroValue;

public class CorrespondenciaSaveMail_Async  {
	private Logger logger = Logger.getLogger(getClass());
	
	public CorrespondenciaSaveMail_Async() {
		//System.out.println("@@@@@  AnySaveMail_Async");
	}
	
	public void sendMail(ICorreoProcess cp, ICorreoBuilder icb, Double idTimer ) throws MessagingException {
		
		try {

			logger.info(  "%tc sendMail...%n".concat(Formatear.getInstance().toDate( Calendar.getInstance().getTime() , "dd/MM/yyyy HH:mm:ss")));
			
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), "conf.mail", "send.type"				, "smtp"); //smtp / stored_procedure
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), "conf.mail", "send.stored_procedure"	, " EXEC [vs2k8-micro-remu].btab_gen_mic.dbo.ENVIO_EMAIL '@mail','@html','@subject' " );
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), "conf.mail", "send_activado" , "true");
		 
		} catch (PPMException e2) {
			e2.printStackTrace();
		}
		
		ParametroValue pv = ParametroLocator.getInstance().getValor("conf.mail", "send_activado");
		
		
		boolean send = true;
		if(pv != null && !"true".equals(pv.getValue())) {
			send = false;
		}
		
	 
		ParametroValue pvType = ParametroLocator.getInstance().getValor("conf.mail", "send.type");
		boolean error = false;
		String errorString = null;
		
		List<File> files = icb.getArchivosAdjuntos();
		File tmpFolder = null;
		
		if(files != null && files.size() > 0 && files.get(0).isDirectory()) {
			tmpFolder = files.get(0); //FOLDER TMP
			files.remove(0);
		}
		
		try {
			if(send == true && "smtp".equals(pvType.getValue())) {
				sendBySmtp(cp);
			}
			else if(send == true && "stored_procedure".equals(pvType.getValue())) {
				sendBySP(icb);
			}
			else if(send != true) {
				error = true;
				String log = "@@@@ Envío omitido por la configuración [PARAM:conf.mail - VALUE:send_activado]";
				logger.error(log);
				errorString = log;
			}
			else {
				error = true;
			}
		}
		catch(SQLException e) {
			errorString = e.getMessage();
			error = true;
			logger.error("Error:"+errorString);
			
			e.printStackTrace();
		}
		catch(Exception e) {
			errorString = e.getMessage();
			error = true;
			logger.error("Error:"+errorString);
			
			e.printStackTrace();
		} finally {
			if(tmpFolder != null && tmpFolder.exists() && tmpFolder.isDirectory()) {
				try {
					FileUtils.deleteDirectory(tmpFolder);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		 
		
		String sql = " UPDATE eje_correspondencia_timer SET estado = ?, fec_sended = getdate() where id_timer = ? ";
		
		ECorrespondenciaProgramacionEstado e = null;
		
		if(!error) {
			e = ECorrespondenciaProgramacionEstado.enviado;
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
		
	}
	
	private void sendBySmtp(ICorreoProcess cp) throws MessagingException {
		CorreoDispatcher.getInstance().sendMail_catchError(cp);
	}	
	
	private void sendBySP(ICorreoBuilder icb) throws MessagingException, SQLException {
		ParametroValue pvSql = ParametroLocator.getInstance().getValor("conf.mail", "send.stored_procedure");
		String sql = pvSql.getValue();
		
		List<IVoDestinatario> lista = icb.getDestinatarios();

		sql = sql.replaceAll("@html", StringEscapeUtils.escapeSql(icb.getBody()));
		sql = sql.replaceAll("@subject",  StringEscapeUtils.escapeSql(icb.getAsunto()));		
		
		for(IVoDestinatario l : lista) {
			String sqlToSend = sql.replaceAll("@mail",  StringEscapeUtils.escapeSql(l.getEmail()));
			
			ConsultaTool.getInstance().update("portal", sqlToSend);
		}
	}
 
 
}
