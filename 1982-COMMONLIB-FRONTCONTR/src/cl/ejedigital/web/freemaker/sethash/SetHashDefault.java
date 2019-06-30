package cl.ejedigital.web.freemaker.sethash;

import cl.ejedigital.web.FreemakerTool;
import cl.ejedigital.web.fmrender.FmRenderers;
import cl.ejedigital.web.freemaker.ifaces.IFreemakerSetHash;
import freemarker.template.SimpleHash;
import portal.com.eje.portal.factory.Util;

public class SetHashDefault implements IFreemakerSetHash {

	private SetHashDefault() {
		
	}
	
	public static SetHashDefault getInstance() {
		return Util.getInstance(SetHashDefault.class);
	}
	
	@Override
	public <T> void setDataFromVo(SimpleHash modelRoot, T t, FmRenderers<T> rendes, int counter) {
		FreemakerTool.getInstance().setDataFromVo(modelRoot, t, rendes, counter);
		
	}

	@Override
	public <T> void setDataFromVo(SimpleHash modelRoot, T t) {
		setDataFromVo(modelRoot, t, null, 0);
		
	}
	
	
	

}
