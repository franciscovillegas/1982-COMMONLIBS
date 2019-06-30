package portal.com.eje.serhumano.user.startsession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.datos.Consulta;
import portal.com.eje.frontcontroller.AbsClaseWebInsegura;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.admin.CapManager;
import portal.com.eje.serhumano.user.Rut;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.serhumano.user.jndimanager.JndiManagerOgp;
import portal.com.eje.serhumano.user.jndimanager.JndiManagerPer;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.security.Base64Coder;
import portal.com.eje.tools.security.Config;
import portal.com.eje.tools.security.Encrypter;
import portal.com.eje.tools.servlet.GetParam;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

public class StartSessionOgp extends AbsClaseWebInsegura {

    public StartSessionOgp(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	public void doPost() throws Exception {
		boolean flagClave = false;
		ResourceBundle proper = ResourceBundle.getBundle("db");
		String usarDBexterna = proper.getString("bdglobal.enable");
		Connection conexUser = null;
		if (usarDBexterna.equals("S")) {
			conexUser = super.getIoClaseWeb().getConnection("bdglobal");
		}
		
		Connection conjndi = super.getIoClaseWeb().getConnection("PortalGateWay");

		String pRut = super.getIoClaseWeb().getParamString("rut",null);
        String pDig = super.getIoClaseWeb().getParamString("dig",null);
        String pClave = super.getIoClaseWeb().getParamString("clave",null);
        String modo = super.getIoClaseWeb().getParamString("modo",null);
		
		String pHtm = "login/login.html";
        pHtm = super.getIoClaseWeb().getParamString("htm",null);
		
		String app = "sh";
		
		boolean estado = super.getIoClaseWeb().getReq().isRequestedSessionIdValid();
		
		OutMessage.OutMessagePrint("--> sesion actual valida? - ".concat(String.valueOf(String.valueOf(estado))));
		if (estado) {
			super.getIoClaseWeb().getReq().getSession().invalidate();
		}
		Rut rut;
		try {
			rut = new Rut(pRut, pDig);
		} 
		catch (Exception e) {
			rut = null;
		}
		
		JndiManagerOgp jndiogpo = JndiManagerOgp.getInstance();
		Usuario user = new Usuario(getIoClaseWeb().getReq().getSession(), jndiogpo);
		Connection conexion = super.getIoClaseWeb().getConnection(user.getJndi(pRut).toString());
		
		if (rut != null) {
	
			user.getDatos(conexion, conexion, rut, pClave, app);
			if (user.esValido() && user.tieneApp("adm")) {
				OutMessage.OutMessagePrint("Usuario administrador o portal");
			}
			else if (usarDBexterna.equals("S")) {
				OutMessage.OutMessagePrint("\n\nUsando DB externa");
				//user.findUser(conexion, conexUser, conjndi, pRut, pClave);
				user.findUser(conexion, conexUser, pRut, pClave);
			}
			if (user.esValido()) {
				user.getDatos(conexion, conexion, rut, pClave, app);
			}

		}

		System.err.println(String.valueOf(String.valueOf((new StringBuilder("--------->Es valido??")).append(user.esValido()).append("  --->Template:--> ").append(pHtm))));
		//if (user.esValido() && user.usuarioVigente(conexion, rut, user.tieneApp("adm"))) {
		if (user.esValido() ) {
			
			SessionMgr.setTimeout();
			SessionMgr.guardarUsuario(super.getIoClaseWeb().getReq(), user);
			super.getIoClaseWeb().insTracking("/servlet/StartSession".intern(),"Ingreso Portal".intern(), null);
			
			System.out.println("GRABAR CLAVE 1");
			user.grabaPass(conexion, rut.getRut(), pClave);
			user.updCantIngresosMasUno(conexion);
			super.getIoClaseWeb().getResp().sendRedirect(pHtm);
			return;
			
		} 
		else {	
			loginGet(super.getIoClaseWeb().getReq(),super.getIoClaseWeb().getResp(),"login/loginadmin.html"," ");
			
		}
		super.getIoClaseWeb().freeConnection(user.getJndi(),conexion);
		super.getIoClaseWeb().freeConnection("PortalGateWay",conjndi);
		if (usarDBexterna.equals("S")) {
			super.getIoClaseWeb().freeConnection("bdglobal",conexUser);
		}
	}

	
	public void doGet() throws Exception {
		loginGet(super.getIoClaseWeb().getReq(),super.getIoClaseWeb().getResp(),"login/login.html","");
		
	}
	
	protected void loginGet(HttpServletRequest req, HttpServletResponse resp, String pHtm, String msgRespuesta)
			throws IOException, ServletException {
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
		resp.setHeader("Expires", "0");
		resp.setHeader("Pragma", "no-cache");		
		SimpleHash modelRoot = new SimpleHash();
    	modelRoot.put("passlineValueEncoded", getPassLineEnconded(req) );
    	modelRoot.put("msgRespuesta",msgRespuesta);
		try {
    		super.getIoClaseWeb().retTemplate(pHtm, modelRoot);
    	}
    	catch(Exception e) {
    		System.err.println(e.getMessage());
    	}
		out.flush();
		out.close();
	}
	
    private String getPassLineEnconded(HttpServletRequest req) {
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
 
    public String validaCodigo(HttpServletRequest req) {
	    String passline = req.getParameter("passline");
	    String passlineEncoded = req.getParameter("passline_enc");
    	String pageOut = null;
	    Encrypter stringEncrypter = new Encrypter();
	    boolean securityLettersPass = true;

	    if ((passline!=null) || (passlineEncoded !=null)) {
	        String passlineDecoded = Base64Coder.decode(passlineEncoded);
	        String passlineCheck = stringEncrypter.decrypt(passlineDecoded);
	        passlineCheck = passlineCheck.substring(0, Config.getPropertyInt(Config.MAX_NUMBER));
	        if (!passline.toUpperCase().equals(passlineCheck.toUpperCase())) {
	        	securityLettersPass = false;
	        }
	        if (securityLettersPass) {
	        	pageOut = "true";
	        }
	    }
    	return pageOut;
    }

}