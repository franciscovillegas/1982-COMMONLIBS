package portal.com.eje.frontcontroller.ioutils;

import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.EMessageHtmlLevel;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.msggenerico.MsgGenerico;
import portal.com.eje.portal.msggenerico.MsgGenericoVo;

public class IOUtilHtmlMessage extends IOUtil {
	
	public static IOUtilHtmlMessage getInstance() {
		return Util.getInstance(IOUtilHtmlMessage.class);
	}

	public void retPageWithMessage(IIOClaseWebLight io, EMessageHtmlLevel lvl, String message, String urlAfter) {
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("icon"		, lvl != null ? lvl.getIcon() :  EMessageHtmlLevel.info.getIcon() );
		modelRoot.put("message" 	, message );
		
		if(urlAfter != null) {
			modelRoot.put("urlafter"	, urlAfter );
		}
		
		((IOClaseWeb)io).retTemplate("iomessagehtml/ioMessage.html", modelRoot);
	}

	public void retMensajeGenerico(IOClaseWeb io, Throwable e) {
		
		StringBuilder str = new StringBuilder();
		
		str.append(e.toString());
		
		for(StackTraceElement s : e.getStackTrace()) {
			str.append(  "at " ).append(  s.toString() );
		}
		
		MsgGenericoVo vo = new MsgGenericoVo("Error", str.toString(), null);
		MsgGenerico.getInstance().returnMessage(io, vo);
		
	}

	public void retMensajeGenerico(IOClaseWeb io, String titulo, String mensaje) {
		MsgGenericoVo vo = new MsgGenericoVo(titulo, mensaje, null);
		MsgGenerico.getInstance().returnMessage(io, vo);
		
	}
}
