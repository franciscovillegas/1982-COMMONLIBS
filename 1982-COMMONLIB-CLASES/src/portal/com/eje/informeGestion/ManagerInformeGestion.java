package portal.com.eje.informeGestion;


import java.sql.Connection;

import portal.com.eje.serhumano.user.SessionMgr;
//import organica.com.eje.ges.usuario.Usuario;
import portal.com.eje.serhumano.user.Usuario;

//import portal.com.eje.datos.DBConnectionManager;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;
import freemarker.template.SimpleHash;



public class ManagerInformeGestion extends AbsClaseWeb {
	
//    protected DBConnectionManager connMgr;
	
	public ManagerInformeGestion(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		String htm	  = super.getIoClaseWeb().getParamString("htm","informegestion/organicaInformes.htm");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);

		
		if("show".equals(accion)) {

			Connection Conexion = super.getIoClaseWeb().getConnection("portal");
			
			if("organicaInformes".equals(thing)) {
				Usuario userPortal = super.getIoClaseWeb().getUsuario();				
				
				
				organica.com.eje.ges.usuario.Usuario user = new organica.com.eje.ges.usuario.Usuario();
				
				
				if (user.getDatos(Conexion, userPortal.getRut().getRutId().concat("-").concat(userPortal.getRut().getDig()), userPortal.getPassWord())) {
					SessionMgr.guardarUsuarioOrganica(super.getIoClaseWeb().getReq() , user);
					super.getIoClaseWeb().retTemplate(htm,modelRoot);		
				}
				else {
					
				}
				
				
//				user.getDatos(Conexion, user.getRut(), user.getPassWord());
				
				
			} 
			else {
				super.getIoClaseWeb().retTemplate(htm,modelRoot);		
			}
		
//	        connMgr.freeConnection("portal", Conexion);
			
		}
		
	}
}



