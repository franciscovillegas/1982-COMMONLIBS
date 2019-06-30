package portal.com.eje.frontcontroller.ioutils;

import portal.com.eje.frontcontroller.IOClaseWeb;

public class IOBootstrap implements IIOBootstrap {

	private IOClaseWeb io;
	private IIOBootstrapNavigator navigator;
	private IIOBootstrapPaneles paneles;
	private IIOBootstrapContainer container;
	private IIOBootstrapAlert alerta;
	
	public IOBootstrap(IOClaseWeb io) {
		super();
		this.io = io;
	}
	
	public IIOBootstrapNavigator navigator() {
		if(navigator ==null) {
			navigator = new IOBootstrapNavigator(io);
		}
		
		return navigator;
	}
	
	public IIOBootstrapPaneles panel() {
		if(paneles ==null) {
			paneles = new IOBootstrapPaneles(io);
		}
		
		return paneles;
	}

	@Override
	public IIOBootstrapContainer container() {
		if(container ==null) {
			container = new IOBootstrapContainer(io);
		}
		
		return container;
	}

	@Override
	public IIOBootstrapAlert alerta() {
		if(alerta ==null) {
			alerta = new IOBootstrapAlerta(io);
		}
		
		return alerta;
	}

}
