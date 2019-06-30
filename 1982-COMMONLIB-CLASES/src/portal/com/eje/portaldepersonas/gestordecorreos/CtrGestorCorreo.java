package portal.com.eje.portaldepersonas.gestordecorreos;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ISenchaPage;
import portal.com.eje.portal.factory.Ctr;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.portaldepersonas.gestordecorreos.enums.EnumGestorCorreosBandejas;
import portal.com.eje.portaldepersonas.gestordecorreos.vo.CorreoFullVo;

public class CtrGestorCorreo {

	public static CtrGestorCorreo getCtr() {
		return Ctr.getInstance(CtrGestorCorreo.class);
	}
	
	public List<CorreoFullVo> getCorreos(int rut, EnumGestorCorreosBandejas bandeja, Integer rutRemitente, boolean encripta, boolean getContenidoMail, ISenchaPage page) throws SQLException {
		ConsultaData data = GestorCorreoManager.getInstance().getCorreos(rut, bandeja, null, rutRemitente, encripta, getContenidoMail, page);
		Collection<CorreoFullVo> vos = VoTool.getInstance().buildVo(data,  CorreoFullVo.class);
		
		return (List<CorreoFullVo>) vos;
	}

}
