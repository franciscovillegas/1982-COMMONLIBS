package highchart.ifaces;

import portal.com.eje.frontcontroller.IOClaseWeb;
import cl.ejedigital.consultor.output.IDataOut;

public abstract class IStackedBar extends AbsGraph {
	
	public IStackedBar(IOClaseWeb cw) {
		super(cw);
	}

	public abstract String getTitulo();
	
	public abstract String getSubTitulo();
	
	public abstract IDataOut getCategorias();
	
	public abstract IDataOut getXLabel();
	
	public abstract IDataOut getSeries();
	
	
}
