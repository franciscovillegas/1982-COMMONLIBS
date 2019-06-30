package portal.com.eje.frontcontroller.iface;

import cl.ejedigital.tool.misc.Cronometro;
import portal.com.eje.frontcontroller.IOClaseWeb;

public abstract class AbsServicioCaller extends AbsServicio {
	public abstract boolean doCall(IOClaseWeb io, IServicioGetter servicioGetter, String tipo, IServicioListener servicioListener, Cronometro cro) throws Exception;
}
