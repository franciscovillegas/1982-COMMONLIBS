package portal.com.eje.genericconf.util;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import portal.com.eje.genericconf.ifaces.IPage;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.tools.javabeans.JavaBeanTool;

public class FreemakerPageTool {
	private FreemakerTool tool = new FreemakerTool();
	
	public static FreemakerPageTool getInstance() {
		return Util.getInstance(FreemakerPageTool.class);
	}
	
	public void putHash(SimpleHash modelRoot, Class<? extends IPage> page) {
		if(modelRoot != null && page != null) {
			ConsultaData data = JavaBeanTool.getInstance().getData(page);
			if(data != null && data.next()) {
				modelRoot.put("page", tool.getData(data));	
			}
		}
	}
	
	public SimpleHash putHash( Class<? extends IPage> page) {
		SimpleHash modelRoot = new SimpleHash(); 
		putHash(modelRoot, page);
		return modelRoot;
	}
}
