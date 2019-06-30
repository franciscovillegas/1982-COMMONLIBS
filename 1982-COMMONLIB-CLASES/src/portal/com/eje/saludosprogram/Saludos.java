package portal.com.eje.saludosprogram;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;


public class Saludos extends AbsClaseWeb {

	public Saludos(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doPost() throws Exception {
		
	}

	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","descanso");
		
		if("show".equals(accion)) {
			if("descanso".equals(thing)) {
				super.getIoClaseWeb().retTemplate("saludosprogram/descanso.html");
			}
			if("zona".equals(thing)) {
				String zona = super.getIoClaseWeb().getParamString("zona","1");
				super.getIoClaseWeb().retTemplate("saludosprogram/zona"+zona+".html");
			}
		}
	}

 

}
