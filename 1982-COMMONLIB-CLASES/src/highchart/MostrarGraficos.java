package highchart;

import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;

public class MostrarGraficos extends AbsClaseWebInsegura{

	public MostrarGraficos(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("entropost");
		
	}

	@Override
	public void doGet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("entroget");
	}

}
