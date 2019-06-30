package portal.com.eje.portal.solinf;

import java.sql.Connection;
import java.util.List;

import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.EModulos;

public interface ISolInf {
	
	public List<VoTipoDocumento> getTiposDeDocumento();
	public List<VoTipoDocumento> getTiposDeDocumento(int intIdSolicitud);
	public List<VoTipoDocumento> getTiposDeDocumento(EModulos modulo);
	
	public boolean putTiposDeDocumento(List<VoTipoDocumento> tiposdocto);
	
	public VoLote getLoteBySolicitud(VoSolicitud solicitud);
	public VoLote getLoteBySolicitud(Connection conn, VoSolicitud solicitud);
	
	public boolean putLote(IOClaseWeb io, Connection conn, VoLote lote);
	
	public boolean updLote(VoLote lote);
	public boolean updLote(Connection conn, VoLote lote);
	
	public List<VoSolicitud> getSolicitudes(Connection conn, VoLote lote);
	
	public boolean updSolicitud(IOClaseWeb io, VoSolicitud solicitud);
	public boolean updSolicitud(IOClaseWeb io, Connection conn, VoSolicitud solicitud);
	
}
