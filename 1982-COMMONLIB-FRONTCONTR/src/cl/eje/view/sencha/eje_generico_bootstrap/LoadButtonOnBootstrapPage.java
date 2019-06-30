package cl.eje.view.sencha.eje_generico_bootstrap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cl.eje.bootstrap.ifaces.IPageResource;
import cl.eje.bootstrap.v3x.modo1.BootstrapTemplates;
import cl.eje.helper.AZonePage;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.genericconf.ifaces.IButton;
import portal.com.eje.genericconf.ifaces.IPage;
import portal.com.eje.portal.factory.Weak;

public class LoadButtonOnBootstrapPage extends AZonePage {
	private final String PARAM_CLASE = "clase";
	private final String AMP = "&";
	private final String EQUALS = "=";
	//private final IPageResource DEFAULT_PAGE_PROCESOR = BootstrapTemplates.getInstance().getIFrameLoaders().getDefault();
	
	@SuppressWarnings("unchecked")
	@Override
	public void get(IOClaseWeb io) throws Throwable {
 
		SimpleHash modelRoot = new SimpleHash();
	 
		String str = io.getParamString(PARAM_CLASE, null);
		Class<IButton> buttonClass = null;
		IButton button = null;
		if(str != null) {
			buttonClass =(Class<IButton>) Class.forName(str); 	
			button = Weak.getInstance(buttonClass);
			FreemakerTool.getInstance().setDataFromVo(modelRoot, button);
			
			setPageIfExist(modelRoot, button);
		}
		
		if(button != null) {
			IPageResource r = BootstrapTemplates.getInstance().getIFrameLoaders().getDefault();
			
			Map<String,String> params = new HashMap<>();
			params.put("r", String.valueOf(Math.random()));
			params.put(PARAM_CLASE, str);
			
			modelRoot.put("paramsAdicionales", paramsToUrl(params));
			
			io.retTemplate(r, modelRoot);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void setPageIfExist(SimpleHash modelRoot, IButton button) {
		if(button != null) {
			try {
				Class<? extends AZonePage> llamada = (Class<? extends AZonePage>) AZonePage.getClassFromUrlFolderFormat(button.getIde());
				if(llamada != null) {
					FreemakerTool.pages.putHash(modelRoot, (Class<? extends IPage>) llamada);
				}
			}
			catch(Exception e) {
				
			}
		}
		
	}
	
	private String paramsToUrl(Map<String,String> params) throws UnsupportedEncodingException {
		StringBuilder retorno = new StringBuilder();
		
		if(params != null) {
			for(String k : params.keySet()) {
				if(retorno.toString().length() >0) {
					retorno.append(AMP);
				}
				retorno.append(k).append(EQUALS).append(URLEncoder.encode(params.get(k),"UTF-8"));
			}
		}
		
		return retorno.toString();
	}
 
}
