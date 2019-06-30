package highchart.ifaces;

import portal.com.eje.frontcontroller.IOClaseWeb;
import cl.ejedigital.consultor.output.IDataOut;

public abstract class ISpiderWeb extends AbsGraph {

	public ISpiderWeb(IOClaseWeb cw) {
		super(cw);
	}

	public abstract String getTitulo();
	
	public abstract String getSubTitulo();
	
	public abstract IDataOut getCategorias();
	
	
	/**
	 * Los valores obligatorios son: <br/>
	 * name: 'Allocated Budget',
	    data: [43000, 19000, 60000, 35000, 17000, 10000],
	    pointPlacement: 'on'
	 * */
	public abstract IDataOut getSeries();
	
	
}
