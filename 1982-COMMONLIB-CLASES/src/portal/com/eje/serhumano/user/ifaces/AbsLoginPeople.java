package portal.com.eje.serhumano.user.ifaces;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import cl.eje.helper.AZoneUtil;
import cl.eje.model.generic.portal.Eje_ges_trabajador;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.validar.Validar;
import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroValue;
import portal.com.eje.portal.vo.CtrGeneric;
import portal.com.eje.portal.vo.util.Wheres;
import portal.com.eje.serhumano.user.Rut;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.UserMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.serhumano.user.enums.EnumUserValidatorMethod;
import portal.com.eje.serhumano.user.jndimanager.JndiManagerPer;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.SendMail;
import portal.com.eje.tools.security.Base64Coder;
import portal.com.eje.tools.security.Config;
import portal.com.eje.tools.security.Encrypter;

public abstract class AbsLoginPeople extends AZoneUtil {
	protected Logger logger = Logger.getLogger(getClass());
	 
	
//    public inicia(IOClaseWeb io) {
//		//pHtm = "macc/login/loginWF.html"; //para WF
//		pHtmLogin 	= io.getParamString("htm_login"		,"login/login.html");			 
//        pHtmSuccess = io.getParamString("htm_success"	,"../servlet/Tool?htm=user/usuario_frame.html"); //REDIRECT
//        logger = Logger.getLogger(getClass());
//	}

	@Override
	public void get(IOClaseWeb io) throws Exception {
		loginGet(io, "", null);

	}

	protected abstract void returnPageLogin(IOClaseWeb io, String pHtmLogin, SimpleHash hash) throws Exception;

	protected abstract String getHtmlOnSuccess();
	/**
	 * Return  true si debe validar captcha
	 * */
	protected abstract boolean isValidaCaptcha();

	/**
	 * Si es por rut, se debe pasar, rut, dig, clave, passline_enc, paso y clock <br/>
	 * Si es por email, se le debe pasar email, clave, passline_enc, paso y clock <br/>
	 * Por defecto será BY_RUT
	 * 
	 * @author Pancho
	 * @since 02-05-2019
	 * */
	protected abstract EnumUserValidatorMethod getValidationMethod();
	
	
	
	protected void validaUsuario(Connection connPortal, Usuario user, Rut rut, String email, String clave, String app) {
		if(getValidationMethod() == EnumUserValidatorMethod.BY_RUT) {
			user.getDatos(connPortal, connPortal, rut, clave, app);	
		}
		else {
			user.getDatosByEmail(connPortal, connPortal, rut, email, clave, app);
		}
		
	}

	protected void loginGet(IOClaseWeb io, String msgRespuesta, Boolean logeoCorrecto) throws IOException, ServletException {
		boolean respuestaJSON = "application/json".equals(io.getParamString("return_type", ""));
		
	
		SimpleHash modelRoot = new SimpleHash();

		modelRoot.put("passlineValueEncoded", getPassLineEnconded(io.getReq()));
		modelRoot.put("msgRespuesta", StringEscapeUtils.escapeJavaScript(msgRespuesta));
		modelRoot = putUsuarioEstatico(modelRoot);

		
		
		try {
			if(!io.isTextResponded()) {
				if(respuestaJSON && logeoCorrecto != null) {
					io.retSenchaJson(msgRespuesta, logeoCorrecto);
				}
				else {
					returnPageLogin(io, getPageLogin(io), modelRoot);	
				}
					
			}
		} catch (Exception e) {
			io.getResp().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			e.printStackTrace();
		}
	}
	
	protected String getPageLogin(IOClaseWeb io) {
		String pHtmLogin 	= io.getParamString("htm_login"		,"login/login.html");	
		return pHtmLogin;
	}

	private String getPassLineEnconded(HttpServletRequest req) {
		Encrypter stringEncrypter = new Encrypter();
		String randomLetters = new String("");
		for (int i = 0; i < Config.getPropertyInt(Config.MAX_NUMBER); i++) {
			randomLetters += (char) (65 + (Math.random() * 24));
		}
		randomLetters = randomLetters.replaceAll("I", "X");
		randomLetters = randomLetters.replaceAll("Q", "Z");

		String passlineNormal = randomLetters + "." + req.getSession().getId();
		String passlineValueEncoded = stringEncrypter.encrypt(passlineNormal);
		passlineValueEncoded = Base64Coder.encode(passlineValueEncoded);
		return passlineValueEncoded;
	}

	protected SimpleHash putUsuarioEstatico(SimpleHash modelRoot) {

		try {
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), "login.credenciales", "after_success", "../servlet/Tool?htm=login/v2/index.html");
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), "login.credenciales", "rut", "");
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), "login.credenciales", "digito_ver", "");
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), "login.credenciales", "clave", "");

			ParametroValue p1 = ParametroLocator.getInstance().getValor(EModulos.getThisModulo(), "login.credenciales", "rut");
			ParametroValue p2 = ParametroLocator.getInstance().getValor(EModulos.getThisModulo(), "login.credenciales", "digito_ver");
			ParametroValue clave = ParametroLocator.getInstance().getValor(EModulos.getThisModulo(), "login.credenciales", "clave");

			modelRoot.put("usuario_parte1", p1.getValue());
			modelRoot.put("usuario_parte2", p2.getValue());
			modelRoot.put("clave", clave.getValue());
		} catch (NullPointerException e) {

		} catch (MissingResourceException e) {

		} catch (Exception e) {

		}

		return modelRoot;
	}

	private boolean validaCodigo(HttpServletRequest req) {
		Validar val = new Validar();
		String passline = val.validarDato(req.getParameter("passline"), "");
		String passlineEncoded = val.validarDato(req.getParameter("passline_enc"), "");
		boolean pageOut = false;
		Encrypter stringEncrypter = new Encrypter();
		boolean securityLettersPass = true;
		try {
			if ((passline != null) || (passlineEncoded != null)) {
				String passlineDecoded = Base64Coder.decode(passlineEncoded);
				String passlineCheck = stringEncrypter.decrypt(passlineDecoded);
				// String passlineString = passlineCheck;
				passlineCheck = passlineCheck.substring(0, Config.getPropertyInt(Config.MAX_NUMBER));
				// String sessionId = passlineString.substring(passlineString.indexOf(".")+1,
				// passlineString.length());
				// if (!sessionId.equals(req.getSession().getId())) {
				// securityLettersPass = false;
				// }
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
	public void add(IOClaseWeb io) throws Exception {
		Cronometro cro = new Cronometro();
		cro.start();
		
		

		Connection conexion = io.getConnection("portal");
		ResourceBundle proper = ResourceBundle.getBundle("db");
		String usarDBexterna = proper.getString("winper.enable");

		String pRut = io.getParamString("rut", null); // mail
		String pDig = io.getParamString("dig", null); // siempre va a ser 1
		String email = io.getParamString("email", null);
		if(email != null && getValidationMethod() == EnumUserValidatorMethod.BY_EMAIL) {
			Eje_ges_trabajador t = getTrabajadorByEmail(email);
			pRut = String.valueOf(t.getRut());
			pDig = t.getDigito_ver();
		}
		String pClave = io.getParamString("clave", null);
		String modo = io.getParamString("modo", null);

		ParametroValue pv = ParametroLocator.getInstance().getValor(EModulos.getThisModulo(), "login.credenciales", "after_success");

		String def = pv != null ? pv.getValue() : "";
		String pHtmSuccess = io.getParamString("htm", def);
		if (pHtmSuccess.indexOf("?") > 0) {
			pHtmSuccess = pHtmSuccess + "&hide_banner=" + io.getParamString("hide_banner", "false");
			pHtmSuccess = pHtmSuccess + "&hide_close=" + io.getParamString("hide_close", "false");
			pHtmSuccess = pHtmSuccess + "&prms=" + io.getParamString("prms", "{}");
		}

		String app = "sh";
		boolean estado = io.getReq().isRequestedSessionIdValid();
		OutMessage.OutMessagePrint("--> sesion actual valida? - ".concat(String.valueOf(String.valueOf(estado))));
		if (estado) {
			io.getReq().getSession().invalidate();
		}
		Rut rut;
		try {
			rut = new Rut(pRut, pDig);
		} catch (Exception e) {
			rut = null;
		}
		Usuario user = new Usuario(io.getReq().getSession(), new JndiManagerPer());
		if (rut != null) {
//			PropertiesTools prop = new PropertiesTools();
//			String conTerceraClave = prop.getString(ResourceBundle.getBundle("db"), "skewpassimage.active", "true");

			if (isValidaCaptcha()) {
				boolean pageOut = validaCodigo(io.getReq());
				if (!pageOut) {
					loginGet(io, StringEscapeUtils.escapeHtml("Código 'Captcha' inválido."), false);
					return;
				}
			}

			validaUsuario(conexion, user, rut, email, pClave, app);

			if (!user.esValido()) {
				if (usarDBexterna.equals("S")) {
					OutMessage.OutMessagePrint("\n\nUsando DB externa");
					//user.findUser(conexion, conexUser, pRut, pClave);
				}
			} else {
				pRut = user.getRutId();
				try {
					rut = new Rut(pRut, pDig);
				} catch (Exception e) {
					rut = null;
				}
			}
		}

		UserMgr usermgr = new UserMgr(conexion);
		boolean userBloqueado = usermgr.userBloqueado(pRut);
		boolean userBloqueadoNoUso = usermgr.tieneNoUsada(pRut, Integer.parseInt(proper.getString("clave.tiemponouso")));
		boolean esUserVigente = user.tieneApp("excepcion") || user.usuarioVigente(conexion, rut);

		if (user.esValido() && esUserVigente && !userBloqueado && !userBloqueadoNoUso) {
			SessionMgr.setTimeout();
			SessionMgr.guardarUsuario(io.getReq(), user);
			usermgr.resetAccesoFallidos(pRut);
			io.insTrackingModulo_Inseguro(user.getRutIdInt(), getClass(), "ingresa", "/servlet/StartSession".intern(), "Ingreso Portal".intern());

			// System.out.println("GRABAR CLAVE 1");
			user.grabaPass(conexion, rut.getRut(), pClave);
			user.updCantIngresosMasUno(conexion);

			boolean respuestaJSON = "application/json".equals(io.getParamString("return_type", ""));
			
			if (respuestaJSON) {
				io.retSenchaJson(getHtmlOnSuccess(), true);
			} else {
				// System.out.println("Accediendo a:"+pHtmSuccess);
				io.getResp().sendRedirect(getHtmlOnSuccess());
			}

		} else {
			 
				boolean envioAviso = false;
				String msgRetorno = null;
				SimpleHash modelRoot = new SimpleHash();

				if (!"0".equals(modo)) {
					modelRoot.put("passlineValueEncoded", getPassLineEnconded(io.getReq()));

				}

				if (userBloqueadoNoUso) {
					envioAviso = usermgr.registraBloqueoNoUso(pRut, proper.getString("clave.mensajebloqueoEmail2"));
				} else {
					usermgr.resetAccesoFallidosTiempo(pRut);
					envioAviso = usermgr.registraAccesoFallido(pRut, usermgr.tieneAccesosFallidos(pRut), proper.getString("clave.mensajebloqueoEmail"));
				}

				if (envioAviso) {
					SendMail sendBloqueoAcceso = new SendMail();
					String tipobloqueo = userBloqueadoNoUso ? proper.getString("clave.mensajebloqueoEmail2") : proper.getString("clave.mensajebloqueoEmail");
					sendBloqueoAcceso.sendAvisoBloqueo(pRut, conexion, tipobloqueo);
					msgRetorno =  proper.getString("clave.mensajebloqueo") + " " + tipobloqueo;
				} else if (usermgr.userBloqueado(pRut)) {
					String tipobloqueo = userBloqueadoNoUso ? proper.getString("clave.mensajebloqueoEmail2") : proper.getString("clave.mensajebloqueoEmail");
					msgRetorno = proper.getString("clave.mensajebloqueo2") + " " + tipobloqueo;
				} else if (user.getPassWord() != null && !user.getPassWord().equals(user.getPasswordBrowser())) {
					msgRetorno = " El Rut y la Clave secreta ingresada no coinciden.";
				}

				
			 
					loginGet(io, msgRetorno, false);	
				 
				
		 
		}

		usermgr.close();

		io.freeConnection("portal", conexion);

//		if (usarDBexterna.equals("S")) {
//			super.getIoClaseWeb().freeConnection("winper", conexUser);
//		}

		System.out.println(new StringBuilder().append("@@ [").append(user.getName()).append("]").append(" ").append(cro.getTimeHHMMSS_milli()).append("-->Es valido??").append(user.esValido()).append("  --->Template:--> ")
				.append(pHtmSuccess));
	}
	
	/**
	 * Nunca retornará null
	 * @author Pancho
	 * */
	private Eje_ges_trabajador getTrabajadorByEmail(String email) {
		Eje_ges_trabajador eje = null;
		try {
			eje = CtrGeneric.getInstance().getFromClass(Eje_ges_trabajador.class, Wheres.where("e_mail", "=", email).build());
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(eje == null) {
			eje = new Eje_ges_trabajador();
		}
		
		return eje;
	}

}
