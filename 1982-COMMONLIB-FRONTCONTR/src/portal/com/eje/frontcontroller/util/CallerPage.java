package portal.com.eje.frontcontroller.util;

import cl.eje.helper.AZoneUtil;
import cl.ejedigital.tool.misc.Cronometro;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.iface.AbsServicioCaller;
import portal.com.eje.frontcontroller.iface.IServicioGetter;
import portal.com.eje.frontcontroller.iface.IServicioListener;
import portal.com.eje.portal.factory.Util;

public class CallerPage extends AbsServicioCaller {

	public static CallerPage getInstance() {
		return Util.getInstance(CallerPage.class);
	}

	@Override
	public boolean doCall(IOClaseWeb io, IServicioGetter servicioGetter, String tipo, IServicioListener servicioListener, Cronometro cro) throws Exception {

		AZoneUtil objeto = (AZoneUtil)servicioGetter.getServicio(io);
		
		boolean retorno = DoExecPage.getInstance().doExec(io, objeto, tipo, servicioListener);

		return retorno;
	}
	


}
