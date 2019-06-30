package portal.com.eje.portal.solinf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message.RecipientType;

import org.apache.commons.lang.StringEscapeUtils;

import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.correspondencia.CorrespondenciaBuilder;
import portal.com.eje.portal.correspondencia.CorrespondenciaLocator;
import portal.com.eje.portal.correspondencia.CorrespondenciaMailLocator;
import portal.com.eje.portal.correspondencia.CorrespondenciaProgramacion;
import portal.com.eje.portal.correspondencia.CorrespondenciaProgramacion.ECorrespondenciaProgramacionTipo;
import portal.com.eje.portal.trabajador.VoPersona;
import portal.com.eje.vo.CorreoBuilderDefault;

public class SolInfMail implements ISolInfMail {

	private static ISolInfMail instance;
	public static ISolInfMail getInstance() {
		if(instance == null) {
			synchronized (SolInfMail.class) {
				if(instance == null) {
					instance = new SolInfMail();
				}
			}
		}
		return instance;
	}
	
	@Override
	public boolean sendMailSolicitud(IOClaseWeb io, EEstado tipo, VoLote lote, VoSolicitud solicitud) {

		StringBuffer strMsg = getTemplate();
		StringBuffer strBody = new StringBuffer();

		StringBuffer strMsgInicio = new StringBuffer();
		StringBuffer strMsgTermino = new StringBuffer();
		
		VoPersona solicitado = new VoPersona(solicitud.getPersona().getId());
		
		List<IVoDestinatario> destinatarios = new ArrayList<IVoDestinatario>();
		
		String strMailPersona = CorrespondenciaMailLocator.getInstance().getCorreo(solicitud.getPersona().getId(), null);
		
		destinatarios.add(new VoDestinatario(solicitud.getPersona().getId(), solicitado.getNombre(), strMailPersona, RecipientType.TO));
		
		CorrespondenciaMailLocator.getInstance().updSiEsNecesario(destinatarios);
		
		strMsgInicio.append("<p style='margin-bottom: 20px'>").append(StringEscapeUtils.unescapeHtml(solicitado.getNombres())).append(", se ha generado la siguiente solicitud de informaci&oacute;n:</p>");
		
		strMsgTermino.append("<p style='margin-bottom: 0px'>Muy cordialmente,</p> \n");
		if (lote.getEmisor()!=null) {
			strMsgTermino.append("<p style='margin: 0px'>").append(StringEscapeUtils.unescapeHtml(lote.getEmisor())).append("</p> \n");
		}else{
			strMsgTermino.append("<p style='margin: 0px'>Sistema de Solicitu de Informaci&oacute;n</p> \n");
		}
		
		strBody.append(getDetalleSolicitud(lote, solicitud));
		
		String strMensaje = strMsg.toString().replace("@Inicio", strMsgInicio.toString()).replace("@Detalle", strBody.toString()).replace("@Termino", strMsgTermino.toString());
		
		ICorreoBuilder ic = new CorreoBuilderDefault("Solicitud de Información", strMensaje, destinatarios);
		CorrespondenciaBuilder cb = new CorrespondenciaBuilder(ic);
		//cb.addProgramacion(new CorrespondenciaProgramacion(ECorrespondenciaProgramacionTipo.ejecucion_fechas, CorrespondenciaProgramacion.getNow()));
		//CorrespondenciaLocator.getInstance().addCorreo(io, cb);
		
		CorrespondenciaLocator.getInstance().sendCorreosNow(cb);
		
		return true;
		
	}
	
	@Override
	public boolean sendMailRespuesta(IOClaseWeb io, EEstado tipo, VoLote lote, VoSolicitud solicitud) {

		StringBuffer strMsg = getTemplate();

		StringBuffer strMsgInicio = new StringBuffer();
		StringBuffer strMsgTermino = new StringBuffer();
		
		VoPersona solicitante = new VoPersona(lote.getSolicitante());
		
		List<IVoDestinatario> destinatarios = new ArrayList<IVoDestinatario>();
		
		String strMailPersona = CorrespondenciaMailLocator.getInstance().getCorreo(lote.getSolicitante(), null);
		
		destinatarios.add(new VoDestinatario(lote.getSolicitante(), solicitante.getNombre(), strMailPersona, RecipientType.TO));
		
		CorrespondenciaMailLocator.getInstance().updSiEsNecesario(destinatarios);
		
		strMsgInicio.append("<p style='margin-bottom: 20px'>").append(StringEscapeUtils.unescapeHtml(solicitante.getNombres())).append(", se ha respondido la solicitud de informaci&oacute;n N°").append(solicitud.getId()).append(" de ").append(solicitud.getPersona().getNombres()).append(", ");
		strMsgInicio.append("referente a ").append(StringEscapeUtils.unescapeHtml(lote.getDescripcion())).append(".</p>");
		
		strMsgTermino.append("<p style='margin-bottom: 0px'>Muy cordialmente,</p> \n");
		if (lote.getEmisor()!=null) {
			strMsgTermino.append("<p style='margin: 0px'>").append(StringEscapeUtils.unescapeHtml(lote.getEmisor())).append("</p> \n");
		}else{
			strMsgTermino.append("<p style='margin: 0px'>Sistema de Solicitu de Informaci&oacute;n</p> \n");
		}

		String strMensaje = strMsg.toString().replace("@Inicio", strMsgInicio.toString()).replace("@Detalle", "").replace("@Termino", strMsgTermino.toString());
		
		ICorreoBuilder ic = new CorreoBuilderDefault("Solicitud de Información - Respuesta", strMensaje, destinatarios);
		CorrespondenciaBuilder cb = new CorrespondenciaBuilder(ic);
		cb.addProgramacion(new CorrespondenciaProgramacion(ECorrespondenciaProgramacionTipo.ejecucion_fechas, CorrespondenciaProgramacion.getNow()));
		CorrespondenciaLocator.getInstance().addCorreo(io, cb);
		
		CorrespondenciaLocator.getInstance().sendCorreosPendientes(io);
		
		return true;
		
	}
	
	
	private StringBuffer getTemplate() {
		
		StringBuffer strMsg = new StringBuffer();
		
		strMsg.append("<!DOCTYPE html> \n")
		.append("<html> \n")
		.append("	<style> \n")
		.append("		table { \n")
		.append("			font-size: 12px; \n")
		.append("		} \n")
		.append("		.x-contenedor-bloque { \n")
		.append("			border: 1px solid #2e6f9a; \n")
		.append("			border-radius: 0.3em; \n")
		.append("			font-family: tahoma,arial,verdana,sans-serif; \n")
		.append("		} \n")
		.append("		.x-contenedor-titulo { \n")
		.append("			border-bottom: 1px solid #e46060; \n")
		.append("			border-top-left-radius: 0.3em; \n")
		.append("			border-top-right-radius: 0.3em; \n")
		.append("			background-color: #fbffd7; \n")
		.append("			vertical-align: middle; \n")
		.append("		} \n")
		.append("		.x-detalle ,td{ \n")
		.append("			text-align: center; \n")
		.append("			vertical-align: text-top; \n")
		.append("		} \n")
		.append("		.txt-l{ \n")
		.append("			text-align: left; \n")
		.append("		} \n")
		.append("	</style> \n")
		.append("	@Inicio \n")
		.append("	@Detalle \n")
		.append("	@Termino \n")
		.append("</html> \n");
		
		return strMsg;
	}
	
	private String getDetalleSolicitud(VoLote lote, VoSolicitud solicitud) {
		
		StringBuffer strMsg = new StringBuffer();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		strMsg.append("")
		.append("<table class='x-contenedor-bloque' style='width: 850px;'> \n") 
		.append("	<tr> \n") 
		.append("		<td> \n") 
		.append("			<table class='x-contenedor-titulo'> \n") 
		.append("				<tr> \n") 
		.append("					<td class='txt-l' colspan=5 ><strong>Ref: ").append(lote.getDescripcion()).append("</strong></td> \n")
		.append("				</tr> \n") 
		.append("				<tr> \n") 
		.append("					<td style='width: 60px;'><strong>Solicitud</strong></td> \n")
		.append("					<td style='width: 520px;' class='txt-l'><strong>Detalle</strong></td> \n")
		.append("					<td style='width: 170px;'><strong>Fecha Entrega</strong></td> \n")
		.append("					<td style='width: 100px;'><strong>Ctd. Doctos.</strong></td> \n")
		.append("				</tr> \n") 
		.append("			</table> \n") 
		.append("		</td> \n") 
		.append("	</tr> \n")
		.append("	<tr> \n") 
		.append("		<td> \n") 
		.append("			<table class='x-detalle'> \n")
		.append("				<tr> \n") 
		.append("					<td style='width: 60px;'>").append(solicitud.getId()).append("</td> \n")
		.append("					<td style='width: 520px;' class='txt-l'>").append(solicitud.getDetalle()).append("</td> \n")
		.append("					<td style='width: 170px;'>").append(sdf.format(solicitud.getFecha())).append("</td> \n")
		.append("					<td style='width: 100px;'>").append(solicitud.getTiposDocto().size()).append("</td> \n")
		.append("				</tr> \n")
		.append("			</table> \n") 
		.append("		</td> \n") 
		.append("	</tr> \n")
		.append("</table> \n");

		return strMsg.toString();
	}
	
}
