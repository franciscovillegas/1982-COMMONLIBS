package loginsw;

import java.sql.Connection;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;

import mis.ProxyDemo;

import cl.ejedigital.tool.strings.MyString;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.user.Rut;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.UserMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.serhumano.user.jndimanager.JndiManagerPer;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.security.Encrypter;


public class LoadSession extends AbsClaseWebInsegura {

	public LoadSession(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		try {
				 XStream xstream = new XStream(new DomDriver());
				 Encrypter enc = new Encrypter();
				 
				Connection conexion = super.getIoClaseWeb().getConnection("portal");
		        ResourceBundle proper = ResourceBundle.getBundle("db");
		        String usarDBexterna = proper.getString("winper.enable");
		        Connection conexUser = null;
		        if(usarDBexterna.equals("S")) {
		        	conexUser = super.getIoClaseWeb().getConnection("winper");
		        }
		        
		        
		        String datoResp = super.getIoClaseWeb().getParamString("data","");
		        VoSession vo = (VoSession) xstream.fromXML(datoResp);
				
				String pRut = super.getIoClaseWeb().getParamString("rut",vo.getId());
		        String pDig = super.getIoClaseWeb().getParamString("dig",vo.getId2());
		        String target = enc.decrypt(vo.getTarget());
		        String pClave = super.getIoClaseWeb().getParamString("clave", target.substring(20,target.length()- 20) );
		
		
		        String app = "sh";
		        boolean estado = super.getIoClaseWeb().getReq().isRequestedSessionIdValid();
		        OutMessage.OutMessagePrint("--> sesion actual valida? - ".concat(String.valueOf(String.valueOf(estado))));
		        if(estado) {
		        	super.getIoClaseWeb().getReq().getSession().invalidate();
		        }
		        Rut rut;
		        try {
		            rut = new Rut(pRut, pDig);
		        }
		        catch(Exception e) {
		            rut = null;
		        }
		        Usuario user = new Usuario(getIoClaseWeb().getReq().getSession(), new JndiManagerPer());
		        if(rut != null) {        	        	
		            user.getDatos(conexion, conexion, rut, pClave, app);
		
		            if(!user.esValido()) {
		            	if(usarDBexterna.equals("S")) {
		            		OutMessage.OutMessagePrint("\n\nUsando DB externa");
		            		user.findUser(conexion, conexUser, pRut, pClave);
		            	}
		            }
		        }
		        
		        UserMgr usermgr = new UserMgr(conexion);
		        boolean userBloqueado = usermgr.userBloqueado(pRut);
		        boolean userBloqueadoNoUso = usermgr.tieneNoUsada(pRut, Integer.parseInt( proper.getString("clave.tiemponouso")) );
		        boolean esUserVigente = user.tieneApp("excepcion") || user.usuarioVigente(conexion, rut);
				
		
		        
		        if(user.esValido() && esUserVigente && !userBloqueado && !userBloqueadoNoUso) {
		        	SessionMgr.setTimeout();
		            SessionMgr.guardarUsuario(super.getIoClaseWeb().getReq(), user);
		            usermgr.resetAccesoFallidos(pRut);
		            super.getIoClaseWeb().insTracking("/servlet/StartSession".intern(),"Ingreso f/LoadSession".intern(), null);
		            
		            System.out.println("GRABAR CLAVE 1");
		          	user.grabaPass(conexion, rut.getRut(), pClave);
		          	user.updCantIngresosMasUno(conexion);
		          	super.getIoClaseWeb().getResp().sendRedirect("../intranet/indice.html");
		        }
		        
		        super.getIoClaseWeb().freeConnection("portal", conexion);
		       	super.getIoClaseWeb().freeConnection("winper", conexUser);
		}
		catch(Exception e) {
			
		}
		finally {
			super.getIoClaseWeb().getResp().sendRedirect(super.getIoClaseWeb().getReq().getContextPath());
		}
	}

	

}
