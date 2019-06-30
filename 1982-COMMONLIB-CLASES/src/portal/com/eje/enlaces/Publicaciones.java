package portal.com.eje.enlaces;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;

public class Publicaciones extends AbsClaseWeb{

	public Publicaciones(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		// TODO Auto-generated method stub
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		// TODO Auto-generated method stub
		String tipo = super.getIoClaseWeb().getReq().getParameter("tipo");
		String url = super.getIoClaseWeb().getReq().getParameter("url");
		super.getIoClaseWeb().insTracking("/servlet/".concat(tipo).intern(),tipo.intern(), null);		
		super.getIoClaseWeb().getResp().sendRedirect(url);
	}

}
