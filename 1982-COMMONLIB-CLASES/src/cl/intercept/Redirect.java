package cl.intercept;

import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;

public class Redirect extends AbsClaseWebInsegura {

	public Redirect(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		doGet();
	}

	@Override
	public void doGet() throws Exception {
		super.getIoClaseWeb().getResp().sendRedirect(super.getIoClaseWeb().getParamString("url", super.getIoClaseWeb().getReq().getContextPath()));
		
	}

}
