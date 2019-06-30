package cl.eje.view.sencha;

import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;
import cl.eje.helper.SenchaController;
import cl.eje.helper.SenchaTipoPeticion;


public class Conf extends AbsClaseWebInsegura {
	private static final String paquete = "cl.eje.view.sencha";
	
	public Conf(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doGet()  {
		SenchaController sc = new SenchaController(paquete, false);
		sc.doThings(SenchaTipoPeticion.doGet, super.getIoClaseWeb());
	}

	@Override
	public void doPost() throws Exception {
		SenchaController sc = new SenchaController(paquete, false);
		sc.doThings(SenchaTipoPeticion.doPost, super.getIoClaseWeb());
	}

	
	
}
