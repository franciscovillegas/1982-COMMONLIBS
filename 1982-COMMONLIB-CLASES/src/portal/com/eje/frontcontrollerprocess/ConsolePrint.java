package portal.com.eje.frontcontrollerprocess;

import portal.com.eje.frontcontroller.IIOClaseWebProcess;
import portal.com.eje.frontcontroller.IOClaseWeb;

public class ConsolePrint implements IIOClaseWebProcess {

	@Override
	public boolean doProcess() {
		System.out.println("ESTE ES EL PRINT :D");
		
		return true;
	}

}
