package portal.com.eje.serhumano.misdatos;

import java.sql.SQLException;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import cl.ejedigital.web.datos.ConsultaTool;
import freemarker.template.SimpleHash;




public class S_GrabaEmail extends AbsClaseWeb {


	public S_GrabaEmail(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		super.getIoClaseWeb().insTracking("Upd Correo".intern(), null);
		String accion = super.getIoClaseWeb().getParamString("accion", "");
		String thing  = super.getIoClaseWeb().getParamString("thing", "");
		
		Boolean ok = null;
		if("upd".equals(accion)) {
			if("correo".equals(thing)) {
				ok = updateCorreo();
				super.getIoClaseWeb().retTemplate("user/main.htm");
			}
		}
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("correo", super.getIoClaseWeb().getUsuario().getEmailFromTablaConfirmacion());
		modelRoot.put("msg"   , ok != null? ( ok ? "Relizado correctamenre" : "Inténtelo nuevamente") : "");
		super.getIoClaseWeb().retTemplate("html/user/main.htm", modelRoot);
	}

	@Override
	public void doGet() throws Exception {
		doPost();
		
	}
	
	private boolean updateCorreo() {
		String newMail   = super.getIoClaseWeb().getParamString("email", "noreply@peoplemanager.cl");
		String email_old = super.getIoClaseWeb().getUsuario().getEmailFromTablaConfirmacion();
		StringBuilder sql = new StringBuilder();
		Object[] params = null;
		
		if(email_old == null ){
			sql.append("INSERT INTO eje_ges_correo_confirmacion (rut,fecha,correo) VALUES ( ? , getdate(), ?) ");
			Object[] paramsLocal = {super.getIoClaseWeb().getUsuario().getRutId(), newMail};
			params = paramsLocal;
			
        }
        else{
        	  sql.append("UPDATE eje_ges_correo_confirmacion SET CORREO = ?, FECHA = GETDATE() WHERE RUT = ?  ");
        	  Object[] paramsLocal = { newMail, super.getIoClaseWeb().getUsuario().getRutId() };
  			  params = paramsLocal;
        }
		
		boolean ok = false;
		try {
			ok = ConsultaTool.getInstance().insert("portal", sql.toString(), params);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	
		super.getIoClaseWeb().getUsuario().reLoadDatos();
		
		return ok;
	}
		
}
