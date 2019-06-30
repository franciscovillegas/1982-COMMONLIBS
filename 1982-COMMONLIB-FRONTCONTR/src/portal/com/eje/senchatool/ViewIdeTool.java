package portal.com.eje.senchatool;

import java.util.Calendar;

import cl.ejedigital.tool.misc.Formatear;
import portal.com.eje.error.IdeNotDefiniteException;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.genericconf.ifaces.IViewIde;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.senchatool.ifaces.IGridTool;
import portal.com.eje.senchatool.ifaces.IIdeKeyEncrypterTool;
import portal.com.eje.senchatool.ifaces.ITreeGridTool;
import portal.com.eje.tools.security.Encrypter;

public class ViewIdeTool {
	public static final ViewIdeKeyEncripterTool keyEncrypter= new ViewIdeKeyEncripterTool();
	public static final ITreeGridTool treeGrid = ViewIdeTreeGridTool.getInstance();
	public static final IGridTool grid = ViewIdeGridTool.getInstance();

	
	public static ViewIdeTool getInstance() {
		return Util.getInstance(ViewIdeTool.class);
	}
	
	public String getIdeUrl(Class<? extends IViewIde> classIde) {
		String retorno = null;
		if(classIde != null) {
			 retorno = Weak.getInstance(classIde).getIde();
			 
			 if(retorno == null) {
				 throw new IdeNotDefiniteException(classIde.getCanonicalName()+" no tiene definida el ide, el retorno de getIde de esta clase no debe ser null");
			 }
		}
		
		return retorno;
	}
	
	static class ViewIdeKeyEncripterTool implements IIdeKeyEncrypterTool{

	 
		public String getEncKey(IOClaseWeb io) {
			return Encrypter.getInstance().encrypt( Formatear.getInstance().toDate(Calendar.getInstance().getTime(), "yyyyMMdd HH:mm:ss.SSS") );
		}
	}
	
}
