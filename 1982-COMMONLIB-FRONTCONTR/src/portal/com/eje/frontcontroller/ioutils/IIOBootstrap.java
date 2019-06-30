package portal.com.eje.frontcontroller.ioutils;

public interface IIOBootstrap {

	public IIOBootstrapNavigator navigator();
	
	public IIOBootstrapPaneles panel();
	
	public IIOBootstrapContainer container();
	
	public IIOBootstrapAlert alerta();
}