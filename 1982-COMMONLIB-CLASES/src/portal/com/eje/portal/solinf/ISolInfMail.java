package portal.com.eje.portal.solinf;

import portal.com.eje.frontcontroller.IOClaseWeb;

public interface ISolInfMail {
	
	public boolean sendMailSolicitud(IOClaseWeb io, EEstado tipo, VoLote lote, VoSolicitud solicitud);
	
	public boolean sendMailRespuesta(IOClaseWeb io, EEstado tipo, VoLote lote, VoSolicitud solicitud);

}
