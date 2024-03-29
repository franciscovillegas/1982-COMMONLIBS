package portal.com.eje.serhumano.user.startsession;

import java.io.IOException;
import java.sql.Connection;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.user.Rut;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.UserMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.serhumano.user.jndimanager.JndiManagerPer;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.SendMail;
import portal.com.eje.tools.security.Base64Coder;
import portal.com.eje.tools.security.Config;
import portal.com.eje.tools.security.Encrypter;
import cl.ejedigital.tool.properties.PropertiesTools;
import cl.ejedigital.tool.validar.Validar;
import freemarker.template.SimpleHash;

public class StartSessionWebmatico extends StartSessionGlobal2016 {

    public StartSessionWebmatico(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

    @Override
	public void doPost() throws Exception {
		
		
		Connection conexion = super.getIoClaseWeb().getConnection("portal");
        ResourceBundle proper = ResourceBundle.getBundle("db");
        String usarDBexterna = proper.getString("winper.enable");
        Connection conexUser = null;
        if(usarDBexterna.equals("S")) {
        	conexUser = super.getIoClaseWeb().getConnection("winper");
        }
        String pRut = super.getIoClaseWeb().getParamString("rut",null);
        String pDig = super.getIoClaseWeb().getParamString("dig","0");
        String pClave = super.getIoClaseWeb().getParamString("clave",null);
        String modo = super.getIoClaseWeb().getParamString("modo",null);
        String pHtm = super.getIoClaseWeb().getParamString("htm","login/login.html");
        String pHtmFail = super.getIoClaseWeb().getParamString("htmf","login/loginadmin.html");
        
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
        	PropertiesTools prop = new PropertiesTools();
        	String conTerceraClave = prop.getString(ResourceBundle.getBundle("db"),"skewpassimage.active","true");
        	
        	if("true".equals(conTerceraClave)) {
	        	boolean pageOut = validaCodigo(super.getIoClaseWeb().getReq());
	        	if(!pageOut) {
	        		loginGet(super.getIoClaseWeb().getReq(),super.getIoClaseWeb().getResp(),"C&oacute;digo \"Captcha\" inv&aacute;lido.");
	        		return;
	        	}
        	}
        	
            user.getDatos(conexion, conexion, rut, pClave, app);

            if(!user.esValido()) {
            	if(usarDBexterna.equals("S")) {
            		OutMessage.OutMessagePrint("\n\nUsando DB externa");
            		user.findUser(conexion, conexUser, pRut, pClave);
            	}
            }
        }
        System.err.println(String.valueOf(String.valueOf((new StringBuilder("--------->Es valido??")).append(user.esValido()).append("  --->Template:--> ").append(pHtm))));
        
        UserMgr usermgr = new UserMgr(conexion);
        boolean userBloqueado = usermgr.userBloqueado(pRut);
        boolean userBloqueadoNoUso = usermgr.tieneNoUsada(pRut, Integer.parseInt( proper.getString("clave.tiemponouso")) );
        boolean esUserVigente = user.tieneApp("excepcion") || user.usuarioVigente(conexion, rut);
        
        
        if(user.esValido() && esUserVigente && !userBloqueado && !userBloqueadoNoUso) {
            SessionMgr.setTimeout();
            SessionMgr.guardarUsuario(super.getIoClaseWeb().getReq(), user);
            usermgr.resetAccesoFallidos(pRut);
            super.getIoClaseWeb().insTracking("/servlet/StartSession".intern(),"Ingreso Portal".intern(), null);
            
            System.out.println("GRABAR CLAVE 1");
          	user.grabaPass(conexion, rut.getRut(), pClave);
          	user.updCantIngresosMasUno(conexion);
          	super.getIoClaseWeb().getResp().sendRedirect(pHtm);
            return;
        } 
        else {
        	boolean envioAviso = false;
            SimpleHash modelRoot = new SimpleHash();
            
            if(!"0".equals(modo)) {
            	modelRoot.put("passlineValueEncoded", getPassLineEnconded(super.getIoClaseWeb().getReq()) );	
            }
           	
            if(userBloqueadoNoUso) {
        		envioAviso = usermgr.registraBloqueoNoUso(pRut,proper.getString("clave.mensajebloqueoEmail2"));
        	}
        	else {
        		usermgr.resetAccesoFallidosTiempo(pRut);
        		envioAviso = usermgr.registraAccesoFallido(pRut,usermgr.tieneAccesosFallidos(pRut),proper.getString("clave.mensajebloqueoEmail"));
        	}
            	
            if(envioAviso) {
            	SendMail sendBloqueoAcceso = new SendMail();
            	String tipobloqueo = userBloqueadoNoUso ? proper.getString("clave.mensajebloqueoEmail2") : proper.getString("clave.mensajebloqueoEmail");
                sendBloqueoAcceso.sendAvisoBloqueo(pRut,conexion,tipobloqueo);
                loginGet(super.getIoClaseWeb().getReq(),super.getIoClaseWeb().getResp(),proper.getString("clave.mensajebloqueo") + " " + tipobloqueo);
            }
            else if (usermgr.userBloqueado(pRut)) {
            	String tipobloqueo = userBloqueadoNoUso ? proper.getString("clave.mensajebloqueoEmail2") : proper.getString("clave.mensajebloqueoEmail");
            	loginGet(super.getIoClaseWeb().getReq(),super.getIoClaseWeb().getResp(),proper.getString("clave.mensajebloqueo2") + " " + tipobloqueo);
            }
            else if (user.getPassWord() != null && !user.getPassWord().equals(user.getPasswordBrowser())) {
            	loginGet(super.getIoClaseWeb().getReq(),super.getIoClaseWeb().getResp()," El Rut y la Clave secreta ingresada no coinciden.");
            }
            
            loginGet(super.getIoClaseWeb().getReq(),super.getIoClaseWeb().getResp()," ");
        }
        
        usermgr.close();
        
        super.getIoClaseWeb().freeConnection("portal", conexion);
       	super.getIoClaseWeb().freeConnection("winper", conexUser);
		
	}

	@Override
	public void doGet() throws Exception {
		//super.getIoClaseWeb().getServletContext().getServletContextName()
		String htm = super.getIoClaseWeb().getParamString("htm", "login/login.html");
		loginGet(super.getIoClaseWeb().getReq(),super.getIoClaseWeb().getResp(),"");
		
	}
}