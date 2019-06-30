package highchart.ifaces;

import portal.com.eje.frontcontroller.IOClaseWeb;
import cl.ejedigital.consultor.output.IDataOut;

public abstract class ISeriesChart extends AbsGraph {

	public ISeriesChart(IOClaseWeb cw) {
		super(cw);
	}

	public abstract String getTitulo();
	
	public abstract String getSubTitulo();
	
	public abstract String getYLabel();
	
	public abstract IDataOut getXLabels();
	
	/**
	 * Los valores obligatorios son: <br/>
	 * name:<br/>
	 * data:<br/>
	 * Los opcionales </br>
	 * color: '#89A54E' </br>
     * type: 'spline', 'bar'
	 * */
	public abstract IDataOut getSeries();
	
	
}
