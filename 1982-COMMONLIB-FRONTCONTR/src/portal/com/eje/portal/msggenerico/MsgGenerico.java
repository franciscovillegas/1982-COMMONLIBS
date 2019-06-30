package portal.com.eje.portal.msggenerico;

import java.io.IOException;
import java.net.URLEncoder;

import portal.com.eje.cache.Cache;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.tools.security.Encrypter;

/**
 * Retorna mensajes genéricos en BOOTSTRAP
 * 
 * @author Pancho
 * @since 21-11-2018
 */
public class MsgGenerico {
	private MsgGenericoVo mensageGenerico;

	public static MsgGenerico getInstance() {
		return Util.getInstance(MsgGenerico.class);
	}

	private synchronized String createMsg(MsgGenericoVo msg) {
		//changeNbyP(msg);
		this.mensageGenerico = msg;

		int hashCode = msg.hashCode();
		String id = new StringBuilder().append(System.currentTimeMillis()).append("_").append(hashCode).toString();

		rescueMsg(id);
		this.mensageGenerico=null;
		
		return id;
	}

	private void changeNbyP(MsgGenericoVo msg) {
		String[] partes = msg.getMsg().split("\\n");
		StringBuilder str = new StringBuilder();
		for (String p : partes) {
			str.append("<p>").append(p).append("</p>");
		}

		msg.setMsg(str.toString());
	}

	public MsgGenericoVo rescueMsg(String idMessage) {
		MsgGenericoVo retorno = null;
		try {
			retorno = Cache.tenSeconds(this, "rescueMsgIntern", new Class[] { String.class }, new Object[] { idMessage }, MsgGenericoVo.class);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retorno;
	}

	private MsgGenericoVo rescueMsgIntern(String idMessage) {
		return mensageGenerico;
	}

	public void returnMessage(IOClaseWeb io, MsgGenericoVo msg) {

		Encrypter enc = new Encrypter();
		String id = (createMsg(msg));

		try {
			id = URLEncoder.encode(enc.encrypt(id), "UTF-8");

			StringBuilder str = new StringBuilder();
			if (io.getReq().getRequestURL().indexOf("servlet/") > 0) {
				str.append("../view/ide/ejeb_generico_mensajegenerico?msg=" + id);

			} else {
				str.append("view/ide/ejeb_generico_mensajegenerico?msg=" + id);

			}

			io.getResp().sendRedirect(str.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
