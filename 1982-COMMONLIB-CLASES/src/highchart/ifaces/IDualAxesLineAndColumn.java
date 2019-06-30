package highchart.ifaces;

import portal.com.eje.frontcontroller.IOClaseWeb;
import cl.ejedigital.consultor.output.IDataOut;

public abstract class IDualAxesLineAndColumn extends AbsGraph {

	
	public IDualAxesLineAndColumn(IOClaseWeb cw) {
		super(cw);
	}

	public abstract String getTitulo();
	
	public abstract String getSubTitulo();
	
	public abstract IDataOut getCategorias();
	
	public abstract String getXLabel();
	
	public abstract String getYLabel();
	
	public abstract String getYLabelSecundario();
	
	public abstract IDataOut getSeries();
	
}
