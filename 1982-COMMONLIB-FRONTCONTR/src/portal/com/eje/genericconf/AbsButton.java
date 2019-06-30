package portal.com.eje.genericconf;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.CharSet;

import cl.eje.helper.AZonePage;
import cl.eje.helper.AZoneUtil;
import cl.eje.view.servlets.page.ServletPage;
import portal.com.eje.genericbuttons.enums.EnumGenericButtonGroup;
import portal.com.eje.genericbuttons.enums.EnumGenericButtonSubGroup;
import portal.com.eje.genericconf.ifaces.IButton;
import portal.com.eje.genericconf.ifaces.IViewIde;
import portal.com.eje.portal.administradorportal.IButtonGroup;
import portal.com.eje.portal.app.enums.EnumTypeAppIde;
import portal.com.eje.senchatool.ViewIdeTool;
import portal.com.eje.tools.sencha.SenchaIdeTool;

public class AbsButton extends AbsUrlAccionable implements IButton{
	private EnumTypeAppIde type;
	private int position;
 
	private String nombre;
	private String onCLick = null;
//	protected String tooltip = null;

	private IButtonGroup group;
	private IButtonGroup subGroup;
	private String ide;
	private boolean accesible;
	private String icon;
	private int startHeight;
	private int startWidth;
	private boolean disabled;
	private String paramsMapping;
	
	protected AbsButton() {
		setType(EnumTypeAppIde.JAVASCRIPT);
		setNombre("Botón");
		
		setPosition(0);
		setAccesible(privateIsAccesible());
		setSubGroup(EnumGenericButtonSubGroup.GRUPO1);
		setGroup(EnumGenericButtonGroup.BASEDEDATOS);
	}
	
	private boolean privateIsAccesible() {
		boolean retorno = false;
		
		
		switch (getType()) {
		case AZONEUTIL:
			retorno = AZoneUtil.getClassFromUrl(getAccionUrl()) != null;
			break;
		case SENCHA:
			retorno = SenchaIdeTool.getInstance().isAccesible(getIde());
			break;
		case JAVASCRIPT:
			retorno = true;
			break;
		case TEMPLATE:	
			retorno = false;
			break;
		default:
			break;
		} 
		
		return retorno;
	}
	
	@Override
	public EnumTypeAppIde getType() {
		return type;
	}
	public void setType(EnumTypeAppIde type) {
		this.type = type;
	}
	
	@Override
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	 
	@Override
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getOnCLick() {
		return onCLick;
	}
	public void setOnCLick(String onCLick) {
		this.onCLick = onCLick;
	}

	@Override
	public IButtonGroup getGroup() {
		return group;
	}
	public void setGroup(IButtonGroup group) {
		this.group = group;
	}
	
	@Override
	public String getIde() {
		return ide;
	}
	
	/**
	 * Se debe utilizar {@link #setIde(Class)}
	 * @deprecated
	 * @since 10-06-2019
	 * */
	public void setIde(String ide) {
		this.ide = ide;
	}
	
	public void setIde(Class<? extends IViewIde> ideClass) {
		this.ide = ViewIdeTool.getInstance().getIdeUrl(ideClass);
	}
	
	public void setIdePage(Class<? extends AZonePage> pageClass) {
		this.ide = "../../"+AZonePage.buildUrlFolderFormat(ServletPage.class, pageClass);
	}
	
	public void setIde(Class<? extends IViewIde> ideClass, String paramName, String valueParam) {
		Map<String,String> mapParams = new HashMap<>();
		mapParams.put(paramName, valueParam);
		setIde(ideClass, mapParams);
	}
	
	public void setIde(Class<? extends IViewIde> ideClass, Map<String,String> mapParams) {
		StringBuilder str = new StringBuilder();
		str.append(ViewIdeTool.getInstance().getIdeUrl(ideClass));
		str.append("?");
		boolean first = true;
		for(Entry<String,String> entry : mapParams.entrySet()) {
			if(!first) {
				str.append("&");
			}
			try {
				str.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8") );
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			first=false;
		}
		
		this.ide = str.toString();
	}
	
	@Override
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Override
	public int getStartHeight() {
		return startHeight;
	}
	public void setStartHeight(int startHeight) {
		this.startHeight = startHeight;
	}
	
	@Override
	public int getStartWidth() {
		return startWidth;
	}
	public void setStartWidth(int startWidth) {
		this.startWidth = startWidth;
	}
	
	@Override
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	@Override
	public IButtonGroup getSubGroup() {
		return subGroup;
	}
	
	@Override
	public void setSubGroup(IButtonGroup subGroup) {
		this.subGroup = subGroup;
	}
	
	@Override
	public boolean isAccesible() {
		return this.accesible;
	}

	private void setAccesible(boolean accesible) {
		this.accesible = accesible;
	}


 
	@Override
	public String toString() {
		return this.getNombre();
	}

}
