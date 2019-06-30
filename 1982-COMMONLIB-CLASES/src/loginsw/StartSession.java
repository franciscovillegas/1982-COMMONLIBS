package loginsw;

import java.sql.Connection;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;

import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.tool.validar.Validar;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import freemarker.template.SimpleHash;

import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.user.Rut;
import portal.com.eje.serhumano.user.UserMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.serhumano.user.jndimanager.JndiManagerPer;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.security.Base64Coder;
import portal.com.eje.tools.security.Config;
import portal.com.eje.tools.security.Encrypter;


public class StartSession extends AbsClaseWebInsegura {

	public StartSession(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		/*
		String rut = super.getIoClaseWeb().getParamString("rut",null);
		SimpleHash modelRoot = new SimpleHash();
		
		if(rut != null) {
			XStream xstream = new XStream(new DomDriver());
			modelRoot.put("xml", StringEscapeUtils.escapeJavaScript(xstream.toXML(getSession())));
		}
	        
		super.getIoClaseWeb().retTemplate("loginsw/loginSW.html",modelRoot);
		*/
		
		XStream xstream = new XStream(new DomDriver());
		super.getIoClaseWeb().retTexto( xstream.toXML(getSession()));
		
		
	}

	private VoSession getSession() {
		XStream xstream = new XStream(new DomDriver());
    	Encrypter enc = new Encrypter();

    	
		Connection conexion = super.getIoClaseWeb().getConnection("portal");
        ResourceBundle proper = ResourceBundle.getBundle("db");
        String usarDBexterna = proper.getString("winper.enable");
        Connection conexUser = null;
        if(usarDBexterna.equals("S")) {
        	conexUser = super.getIoClaseWeb().getConnection("winper");
        }
		
		String pRut = super.getIoClaseWeb().getParamString("rut",null);
        String pDig = super.getIoClaseWeb().getParamString("dig",null);
        String pClave = super.getIoClaseWeb().getParamString("clave",null);


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
        Usuario user = new Usuario(getIoClaseWeb().getReq().getSession(),new JndiManagerPer());
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
		boolean captchaValido = validaCodigo(); 

		VoSession session = new VoSession();
		
        if(captchaValido && user.esValido() && esUserVigente && !userBloqueado && !userBloqueadoNoUso) {
        	usermgr.resetAccesoFallidos(pRut);
        	
        	MyString m = new MyString();
        	
        	
        	session.setDateInit(Calendar.getInstance().getTime());
        	session.setId(String.valueOf(user.getRutIdInt()));
        	session.setId2(user.getRut().getDig());
        	session.setName(user.getName());
        	session.setTarget( enc.encrypt(m.getRandomString(20) + user.getPassWord() + m.getRandomString(20)));
        	session.setMessage("ok");
        	session.setValid(true);
        	

        }else {
        	
            if(userBloqueadoNoUso) {
        		usermgr.registraBloqueoNoUso(pRut,proper.getString("clave.mensajebloqueoEmail2"));
        	}
        	else {
        		usermgr.resetAccesoFallidosTiempo(pRut);
        		usermgr.registraAccesoFallido(pRut,usermgr.tieneAccesosFallidos(pRut),proper.getString("clave.mensajebloqueoEmail"));
        	}
        	
        	session.setDateInit(Calendar.getInstance().getTime());
        	session.setId(String.valueOf(user.getRutIdInt()));
        	session.setName("");
        	session.setTarget("");
        	
         	if( !captchaValido ) {
               	session.setMessage("Captcha Inválido");
        	}
        	else if( !esUserVigente ) {
        		session.setMessage("Usuario No Vigente");
        	}
        	else if( userBloqueado ) {
        		session.setMessage("Usuario bloqueado");
        	}
        	else if( userBloqueadoNoUso ) {
        		session.setMessage("Usuario caducado");
        	}
        	else if ( !user.esValido() ) {
        		session.setMessage("Password incorrecto");
        	}
        	else {
        		session.setMessage("Error desconocido");
        	}
        	
        	session.setValid(false);
        	
        }
        
        
        super.getIoClaseWeb().freeConnection("portal", conexion);
       	super.getIoClaseWeb().freeConnection("winper", conexUser);
       	return session;
	}

	public boolean validaCodigo() {
	    String passline 	   =  super.getIoClaseWeb().getParamString("passline","");
	    String passlineEncoded =  super.getIoClaseWeb().getParamString("passline_enc","");
    	boolean pageOut = false;
	    Encrypter stringEncrypter = new Encrypter();
	    boolean securityLettersPass = true;
	    try {
		    if ((passline!=null) || (passlineEncoded !=null)) {
		        String passlineDecoded = Base64Coder.decode(passlineEncoded);
		        String passlineCheck = stringEncrypter.decrypt(passlineDecoded);
		        //String passlineString = passlineCheck;
		        passlineCheck = passlineCheck.substring(0, Config.getPropertyInt(Config.MAX_NUMBER));
		        //String sessionId = passlineString.substring(passlineString.indexOf(".")+1, passlineString.length());
		        //if (!sessionId.equals(req.getSession().getId())) {
		        //	securityLettersPass = false;
		        //}
		        if (!passline.toUpperCase().equals(passlineCheck.toUpperCase())) {
		        	securityLettersPass = false;
		        }
		        if (securityLettersPass) {
		        	pageOut = true;
		        }
		    }
	    } catch (Exception e) {
	    	
	    }
    	return pageOut;
    }
	
}
