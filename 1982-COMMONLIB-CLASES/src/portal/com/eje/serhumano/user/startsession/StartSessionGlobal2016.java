package portal.com.eje.serhumano.user.startsession;

import java.io.IOException;
import java.sql.Connection;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.properties.PropertiesTools;
import cl.ejedigital.tool.validar.Validar;
import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroValue;
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

public class StartSessionGlobal2016 extends AbsClaseWebInsegura {
	private String pHtmLogin;
	private String pHtmSuccess;
	
    public StartSessionGlobal2016(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		//pHtm = "macc/login/loginWF.html"; //para WF
		pHtmLogin 	= super.getIoClaseWeb().getParamString("htm_login"		,"login/login.html");			 
        pHtmSuccess = super.getIoClaseWeb().getParamString("htm_success"	,"../servlet/Tool?htm=user/usuario_frame.html"); //REDIRECT   
	}

    
    protected void loginGet(HttpServletRequest req, HttpServletResponse resp, String msgRespuesta) 
    	throws IOException, ServletException {
    	SimpleHash modelRoot = new SimpleHash();
    	
    	modelRoot.put("passlineValueEncoded", getPassLineEnconded(req) );
    	modelRoot.put("msgRespuesta", StringEscapeUtils.escapeJavaScript(msgRespuesta));
    	modelRoot = putUsuarioEstatico(modelRoot);
    	
    	try {
    		super.getIoClaseWeb().retTemplate(pHtmLogin, modelRoot);
    	}
    	catch(Exception e) {
    		System.err.println(e.getMessage());
    	}
    }
    
    protected SimpleHash putUsuarioEstatico(SimpleHash modelRoot) {
    	
    	try {
    		ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), "login.credenciales", "after_success"	, "../servlet/Tool?htm=login/v2/index.html" );
    		ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), "login.credenciales", "rut"			, "" );
    		ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), "login.credenciales", "digito_ver"	, "" );
    		ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), "login.credenciales", "clave"			, "" );
    		
    		ParametroValue p1    = ParametroLocator.getInstance().getValor(EModulos.getThisModulo(), "login.credenciales", "rut");
    		ParametroValue p2    = ParametroLocator.getInstance().getValor(EModulos.getThisModulo(), "login.credenciales", "digito_ver");
    		ParametroValue clave = ParametroLocator.getInstance().getValor(EModulos.getThisModulo(), "login.credenciales", "clave");
    	
    		modelRoot.put("usuario_parte1"	, p1.getValue());
    		modelRoot.put("usuario_parte2"	, p2.getValue());
    		modelRoot.put("clave"			, clave.getValue());
    	}
    	catch(NullPointerException e) {
    		
    	}
    	catch(MissingResourceException e) {
    		
    	}
    	catch(Exception e) {
    		
    	}
    	
    	return modelRoot;
    }
    
    protected String getPassLineEnconded(HttpServletRequest req) {
        Encrypter stringEncrypter = new Encrypter();
        String randomLetters = new String("");
        for (int i = 0; i < Config.getPropertyInt(Config.MAX_NUMBER); i++) {
            randomLetters += (char) (65 + (Math.random() * 24));
        }
        randomLetters = randomLetters.replaceAll("I","X");
        randomLetters = randomLetters.replaceAll("Q","Z");

        String passlineNormal = randomLetters + "." + req.getSession().getId();
        String passlineValueEncoded = stringEncrypter.encrypt(passlineNormal);
        passlineValueEncoded  = Base64Coder.encode(passlineValueEncoded );
        return passlineValueEncoded;
    }
    
    public boolean validaCodigo(HttpServletRequest req) {
    	Validar val = new Validar();
	    String passline = val.validarDato(req.getParameter("passline"),"");
	    String passlineEncoded =  val.validarDato(req.getParameter("passline_enc"),"");
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

	@Override
	public void doPost() throws Exception {
		Cronometro cro = new Cronometro();
		cro.start();
		
		Connection conexion = super.getIoClaseWeb().getConnection("portal");
        ResourceBundle proper = ResourceBundle.getBundle("db");
        String usarDBexterna = proper.getString("winper.enable");
        Connection conexUser = null;
        
        if(usarDBexterna.equals("S")) {
        	conexUser = super.getIoClaseWeb().getConnection("winper");
        }
        
        String pRut = super.getIoClaseWeb().getParamString("rut",null);  //mail
        String pDig = super.getIoClaseWeb().getParamString("dig",null);  //siempre va a ser 1
        String pClave = super.getIoClaseWeb().getParamString("clave",null);
        String modo = super.getIoClaseWeb().getParamString("modo",null);
        
        ParametroValue pv = ParametroLocator.getInstance().getValor(EModulos.getThisModulo(), "login.credenciales", "after_success");
        
        String def = pv != null ? pv.getValue():  "";
        pHtmSuccess = super.getIoClaseWeb().getParamString("htm", def);
        if (pHtmSuccess.indexOf("?")>0) {
            pHtmSuccess = pHtmSuccess + "&hide_banner="+super.getIoClaseWeb().getParamString("hide_banner","false");
            pHtmSuccess = pHtmSuccess + "&hide_close="+super.getIoClaseWeb().getParamString("hide_close","false");
            pHtmSuccess = pHtmSuccess + "&prms="+super.getIoClaseWeb().getParamString("prms","{}");
        }
        
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
	        		loginGet(super.getIoClaseWeb().getReq(),super.getIoClaseWeb().getResp(), StringEscapeUtils.escapeHtml("Código \"Captcha\" inválido."));
	        		return;
	        	}
        	}
        	
            user.getDatos(conexion, conexion, rut, pClave, app);

            if(!user.esValido()) {
            	if(usarDBexterna.equals("S")) {
            		OutMessage.OutMessagePrint("\n\nUsando DB externa");
            		user.findUser(conexion, conexUser, pRut, pClave);
            	}
            }else{
            	pRut = user.getRutId();
                try {
                    rut = new Rut(pRut, pDig);
                }
                catch(Exception e) {
                    rut = null;
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
            super.getIoClaseWeb().insTracking("/servlet/StartSession".intern(),"Ingreso Portal".intern(), null);
            
            //System.out.println("GRABAR CLAVE 1");
          	user.grabaPass(conexion, rut.getRut(), pClave);
          	user.updCantIngresosMasUno(conexion);
          	
          	
          	if("application/json".equals(super.getIoClaseWeb().getParamString("return_type", ""))) {
          		super.getIoClaseWeb().retSenchaJson("Logeo correcto", true);
          	}
          	else {
          		//System.out.println("Accediendo a:"+pHtmSuccess);
          		super.getIoClaseWeb().getResp().sendRedirect(pHtmSuccess);	
          	}
          	
        } 
        else {
        	 if("application/json".equals(super.getIoClaseWeb().getParamString("return_type", ""))) {
        		super.getIoClaseWeb().retSenchaJson("Malo, malo, malo. Logeo incorrecto", false);
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
        	
            
            
        }
        
        usermgr.close();
        
        super.getIoClaseWeb().freeConnection("portal", conexion);
       	
        if(usarDBexterna.equals("S")) {
        	super.getIoClaseWeb().freeConnection("winper", conexUser);
        }
		
       	System.out.println(new StringBuilder()
       						.append("@@ [").append(user.getName()).append("]")
       						.append(" ").append(cro.getTimeHHMMSS_milli())
       						.append("-->Es valido??").append(user.esValido())
       						.append("  --->Template:--> ").append(pHtmSuccess));
	}

	@Override
	public void doGet() throws Exception {
		loginGet(super.getIoClaseWeb().getReq(),super.getIoClaseWeb().getResp(),"");
		
	}
}