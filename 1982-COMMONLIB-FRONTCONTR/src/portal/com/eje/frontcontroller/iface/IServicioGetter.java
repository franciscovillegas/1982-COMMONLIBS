package portal.com.eje.frontcontroller.iface;

import portal.com.eje.frontcontroller.IOClaseWeb;

public interface IServicioGetter {

	/**
	 * Se ejecuta solo si ya se encuentra la sesi�n validada
	 * */
	public Object getServicio(IOClaseWeb io) throws Exception;
}
