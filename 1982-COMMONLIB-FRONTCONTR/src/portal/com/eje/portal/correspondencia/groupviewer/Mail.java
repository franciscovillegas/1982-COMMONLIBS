package portal.com.eje.portal.correspondencia.groupviewer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Message.RecipientType;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.correspondencia.CorrespondenciaBuilder;
import portal.com.eje.portal.correspondencia.CorrespondenciaLocator;
import portal.com.eje.portal.correspondencia.CorrespondenciaProgramacion;
import portal.com.eje.portal.correspondencia.CorrespondenciaProgramacion.ECorrespondenciaProgramacionTipo;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.tool.validar.Validar;

public class Mail {
 
		
	/**
	 * Si el retorno es diferente de null entonces todo se ejecutó correctamente. El double entregado corresponde al ID en BD de la correspondencia
	 * */
	public Double process(IOClaseWeb io) {
		CorrespondenciaBuilder cb = createMail(io);
		Double idCorrespondencia = io.getParamDouble("id_correspondencia", null);
		
		if(cb != null) {
			createProgramaciones(io, cb);
			
			if(idCorrespondencia == null) {
				idCorrespondencia = CorrespondenciaLocator.getInstance().addCorreo(io, cb);	
			}
			else {
				idCorrespondencia = CorrespondenciaLocator.getInstance().updCorreo(io, cb, idCorrespondencia);
			}
		}
		
		return idCorrespondencia;
	}
	private CorrespondenciaBuilder createProgramaciones(IOClaseWeb io, CorrespondenciaBuilder cb ) {
		ConsultaData programaciones = io.getParamConsultaData("programaciones");
		
		if(programaciones != null) {
			while(programaciones.next()) {
				if(String.valueOf(ECorrespondenciaProgramacionTipo.ejecucion_fechas).equals(programaciones.getForcedString("tipo")) ) {
					crearProgramaciones_fechas(io, cb, programaciones);
				}
				else if(String.valueOf(ECorrespondenciaProgramacionTipo.ejecucion_itera_dias_ano).equals(programaciones.getForcedString("tipo")) ) {
					System.out.println("ejecucion_itera_dias_ano");
				}
				else if(String.valueOf(ECorrespondenciaProgramacionTipo.ejecucion_itera_dias_mes).equals(programaciones.getForcedString("tipo")) ) {
					crearProgramaciones_mensuales(io, cb, programaciones);
				}
				else if(String.valueOf(ECorrespondenciaProgramacionTipo.ejecucion_itera_dias_semanas).equals(programaciones.getForcedString("tipo")) ) {
					crearProgramaciones_semanales(io, cb, programaciones);
				}
				else {
					System.out.println("No definido");
				}
			}
		}
		else {
			/* 
			 *2016-06-21 FV
			 * AL NO VENIR PROGRAMACIONES, NO SE TOME COMO RIGHT NOW!!! 
			 * 
			 * CorrespondenciaProgramacion programacion = new CorrespondenciaProgramacion(ECorrespondenciaProgramacionTipo.ejecucion_fechas, new Date());
			 * cb.addProgramacion(programacion);
			 */
			
		}
		
		return cb;
	}
	
	/**
	 * Genera una instancia de ejecución
	 * */
	private void crearProgramaciones_fechas(IOClaseWeb io, CorrespondenciaBuilder cb, ConsultaData programaciones) {
		String toExec = programaciones.getString("fec_to_execute") + " " + programaciones.getString("time_to_execute");
		Date toExecDate = Formatear.getInstance().toDate(toExec, "yyyy/MM/dd HH:mm:ss");
		
		CorrespondenciaProgramacion programacion = new CorrespondenciaProgramacion(ECorrespondenciaProgramacionTipo.ejecucion_fechas,  toExecDate );
		cb.addProgramacion(programacion);
	}
	
	/**
	 * Genera todas las instancias de ejecución que en un año, a partir de ahora, puedan ser representadas según la programación semanal
	 * */
	private void crearProgramaciones_semanales(IOClaseWeb io, CorrespondenciaBuilder cb, ConsultaData programaciones) {
		String toExec = "2000/01/01 " + programaciones.getString("time_to_execute");
		Date toExecDate = Formatear.getInstance().toDate(toExec, "yyyy/MM/dd HH:mm:ss");
		
		String moment = programaciones.getString("moment_to_execute_array");
		if(moment != null) {
			moment = moment.substring(1, moment.length() -1 );
			String[] daysToExcecuteStr = moment.split("\\,");
			List<Integer> daysToExecute = new ArrayList<Integer>();
			
			/* Transforma a Integer*/
			for(String d: daysToExcecuteStr) {
				if(d != null) {
					d = d.replaceAll("\"", "");
				}
				daysToExecute.add(Validar.getInstance().validarInt(d,-1));
			}
		
			CorrespondenciaProgramacion programacion = new CorrespondenciaProgramacion(ECorrespondenciaProgramacionTipo.ejecucion_itera_dias_semanas,  daysToExecute, toExecDate );
			cb.addProgramacion(programacion);

		}
	}
	
	
	private void crearProgramaciones_mensuales(IOClaseWeb io, CorrespondenciaBuilder cb, ConsultaData programaciones) {
		String toExec = "2000/01/01 " + programaciones.getString("time_to_execute");
		Date toExecDate = Formatear.getInstance().toDate(toExec, "yyyy/MM/dd HH:mm:ss");
		
		String moment = programaciones.getString("moment_to_execute_array");
		if(moment != null) {
			moment = moment.substring(1, moment.length() -1 );
			String[] daysToExcecuteStr = moment.split("\\,");
			List<Integer> daysToExecute = new ArrayList<Integer>();
			
			/* Transforma a Integer*/
			for(String d: daysToExcecuteStr) {
				if(d != null) {
					d = d.replaceAll("\"", "");
				}
				daysToExecute.add(Validar.getInstance().validarInt(d,-1));
			}
		
			CorrespondenciaProgramacion programacion = new CorrespondenciaProgramacion(ECorrespondenciaProgramacionTipo.ejecucion_itera_dias_mes,  daysToExecute, toExecDate );
			cb.addProgramacion(programacion);

		}
	}

	private CorrespondenciaBuilder createMail(IOClaseWeb io) {
		String to  = io.getParamString("dest_to", null);
		String cc  = io.getParamString("dest_cc", null);
		String bcc = io.getParamString("dest_bcc", null);
		String asunto = io.getParamString("dest_subject", null);
		String body   = io.getParamString("ckeditor_data", null);
		
		ConsultaData cTO = io.getParamConsultaData("consulta_to");
		ConsultaData cCC = io.getParamConsultaData("consulta_cc");
		ConsultaData cBCC= io.getParamConsultaData("consulta_bcc");
		
		List<IVoDestinatario> ld = new ArrayList<IVoDestinatario>();
		parseDestinatario(ld, cTO	, RecipientType.TO);
		parseDestinatario(ld, cCC	, RecipientType.CC);
		parseDestinatario(ld, cBCC	, RecipientType.BCC);
		
		return new CorrespondenciaBuilder( asunto, body, ld);
	}
	
	
	private void parseDestinatario(List<IVoDestinatario> ld, ConsultaData data, RecipientType rt) {
		if(data != null) {
			data.toStart();
			
			while(data != null && data.next()) {
				IVoDestinatario vo = new VoDestinatario(Validar.getInstance().validarInt( data.getForcedString("rut"), -1), data.getString("nombres"), data.getString("mail"), rt);
				ld.add(vo);
			}
		}
	}
	
}
