package highchart.ifaces;

import portal.com.eje.frontcontroller.IOClaseWeb;

public class AbsGraph {
	protected IOClaseWeb cw;
	
	public AbsGraph(IOClaseWeb cw) {
		this.cw = cw;
	}
	
	public IOClaseWeb getIoClaseWeb() {
		return cw;
	}
}
