package portal.com.eje.frontcontroller.ioutils.sencha.util;

import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.ioutils.sencha.iface.IOutComponent;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.organica.vo.IUnidadGenerica;
import portal.com.eje.tools.paquetefactory.PaqueteFactory;

public class RetornoSenchaManipulator implements IOutComponent {
	private final String paquete = "portal.com.eje.frontcontroller.ioutils.sencha.toolsonrenderjson";
	private final List<IOutComponent> manipuladores = PaqueteFactory.getInstance().getObjects(paquete, IOutComponent.class);
	
	public static RetornoSenchaManipulator getInstance() {
		return Util.getInstance(RetornoSenchaManipulator.class);
	}
	
	/**
	 * Establece o configura el retorno según las herramientas que existan
	 * */
	@Override
	public void setUtilidad(ConsultaData dataToReturn, IOClaseWeb io) {
		if(dataToReturn != null && io != null && manipuladores != null) {
			for(IOutComponent c: manipuladores) {
				c.setUtilidad(dataToReturn, io);
			}	
		}
	}

	@Override
	public void setUtilidad(IUnidadGenerica unidadGenerica, IOClaseWeb io) {
		if(unidadGenerica != null && io != null  && manipuladores != null) {
			for(IOutComponent c: manipuladores) {
				c.setUtilidad(unidadGenerica, io);
			}	
		}
		
	}
}
