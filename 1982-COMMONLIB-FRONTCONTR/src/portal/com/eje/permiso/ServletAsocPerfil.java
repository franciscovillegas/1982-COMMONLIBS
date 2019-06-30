package portal.com.eje.permiso;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;


public class ServletAsocPerfil extends AbsClaseWeb {

	public ServletAsocPerfil(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doGet() throws Exception {
		/*
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		String htm	  = super.getIoClaseWeb().getParamString("htm","permiso/AsocPerfil.htm");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		PerfilMngr perfil = new PerfilMngr();
		
		if("show".equals(accion)) {
			super.getIoClaseWeb().retTemplate(htm,modelRoot);			
		}
		else if("select".equals(accion)) {
			if("perfiles".equals(thing)) {
				super.getIoClaseWeb().retTexto(perfil.jQueryGetListPerfiles(super.getIoClaseWeb().getConnection("portal")));
			}
			else if("privilegios".equals(thing)) {
				super.getIoClaseWeb().retTexto(jQueryGetListPrivilegios(super.getIoClaseWeb().getParamString("idPerfil","-1")));
			}
			
			
		}
		else if("insert".equals(accion)) {
			if("perfil".equals(thing)) {
				super.getIoClaseWeb().retTexto(String.valueOf(insertPerfil(super.getIoClaseWeb().getParamString("perfilName","[error]"))));
			}
			else if ("privilegios".equals(thing)) {
				super.getIoClaseWeb().retTexto(String.valueOf(
						insertPrivilegios(
								super.getIoClaseWeb().getParamString("idPerfil","-1"),
								super.getIoClaseWeb().getParamString("vigentes",""),
								super.getIoClaseWeb().getParamString("heredables","")
								)));
			}
		}
		else if("update".equals(accion)) {
			
		}
		else if("delete".equals(accion)) {
			if("perfil".equals(thing)) {
				super.getIoClaseWeb().retTexto(String.valueOf(delPerfil(super.getIoClaseWeb().getParamString("idPerfil","-1"))));
			}
		}
		
		*/
	}

	
}
