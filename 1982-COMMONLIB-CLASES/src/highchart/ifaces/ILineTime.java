package highchart.ifaces;

import cl.ejedigital.consultor.output.IDataOut;
import portal.com.eje.frontcontroller.IOClaseWeb;

public abstract class ILineTime extends AbsGraph {

	public ILineTime(IOClaseWeb cw) {
		super(cw);
	}

	public abstract String getTitulo();
	
	public abstract String getYLabel();
	
	public abstract IDataOut getSeries();
	
}
