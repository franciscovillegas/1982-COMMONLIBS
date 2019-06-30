package portal.com.eje.serhumano.user;

import org.apache.commons.lang.WordUtils;

import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.IOClaseWeb;

public class UsuarioClave {

	public String getFlowToSend_envia(IOClaseWeb io) {
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("nombre"	, WordUtils.capitalizeFully(io.getUsuario().getName()));
		modelRoot.put("servidor", io.getUrlContext());
		modelRoot.put("url"		, io.getUrlContext());
		modelRoot.put("usuario"	, io.getUsuario().getLoginUsuario());
		modelRoot.put("clave"	, io.getUsuario().getPassWord());
		
		FreemakerTool tool = new FreemakerTool();
		String body = tool.templateProcess(io.getReq(), "mail/correoRecuperaClave.html", modelRoot);
		return body;
	}
	
	public String getFlowToSend_autoRecupera(IOClaseWeb io) {
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("nombre"	, WordUtils.capitalizeFully(io.getUsuario().getName()));
		modelRoot.put("servidor", io.getUrlContext());
		modelRoot.put("url"		, io.getUrlContext());
		modelRoot.put("usuario"	, io.getUsuario().getLoginUsuario());
		modelRoot.put("clave"	, io.getUsuario().getPassWord());
		modelRoot.put("auto"	, "true");
		
		FreemakerTool tool = new FreemakerTool();
		String body = tool.templateProcess(io.getReq(), "mail/correoRecuperaClave.html", modelRoot);
		return body;
	}
	
}
