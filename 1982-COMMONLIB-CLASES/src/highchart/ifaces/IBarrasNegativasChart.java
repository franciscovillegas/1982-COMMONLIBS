package highchart.ifaces;

import portal.com.eje.frontcontroller.IOClaseWeb;
import cl.ejedigital.consultor.output.IDataOut;

public abstract class IBarrasNegativasChart extends AbsGraph {

	public IBarrasNegativasChart(IOClaseWeb cw) {
		super(cw);
	}

	public abstract int getRangeAbsolute();
	
	public abstract String getTitulo();
	
	public abstract String getSubTitulo();
	
	public abstract IDataOut getCategorias();
	
	public abstract IDataOut getSeries();
	
	
}
