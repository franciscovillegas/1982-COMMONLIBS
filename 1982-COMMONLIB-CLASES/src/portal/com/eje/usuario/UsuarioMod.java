package portal.com.eje.usuario;

import portal.com.eje.carpelect.mgr.ManagerTrabajador;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;

public class UsuarioMod  extends AbsClaseWeb {

	public UsuarioMod(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		String htm	  = super.getIoClaseWeb().getParamString("htm","permiso/AsocPerfil.htm");
		
		if("upd".equals(accion)) {
			if("correoValidado".equals(thing)) {
				ManagerTrabajador.getInstance().delConfirmacion(Integer.parseInt(super.getIoClaseWeb().getUsuario().getRutId()));
				ManagerTrabajador.getInstance().insertConfirmacion(Integer.parseInt(super.getIoClaseWeb().getUsuario().getRutId()), super.getIoClaseWeb().getParamString("correo",""));	
				
				super.getIoClaseWeb().retTexto("true");
			}
		}
		
	}

}
