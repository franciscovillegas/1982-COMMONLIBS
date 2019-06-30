package portal.com.eje.clave;

import javax.servlet.RequestDispatcher;

import portal.com.eje.clave.formas.RecuperarCajaChile;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;


public class SRecuperaClave extends AbsClaseWeb {

	public SRecuperaClave(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doGet() throws Exception {
		// TODO Auto-generated method stub
		
		RequestDispatcher rd = super.getIoClaseWeb().getReq().getRequestDispatcher("../servlet/EjeCore?claseweb=".concat(RecuperarCajaChile.class.getName()));
		rd.forward(super.getIoClaseWeb().getReq(),super.getIoClaseWeb().getResp());
		
	}
	
	

}
