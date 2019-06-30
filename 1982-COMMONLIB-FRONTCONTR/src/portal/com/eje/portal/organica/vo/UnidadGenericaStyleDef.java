package portal.com.eje.portal.organica.vo;

public class UnidadGenericaStyleDef implements IUnidadGenericaStyling, IUnidadGenericaStyling2 {
	private String iconHoja;
	private String iconAgrupa;
	private String iconHojaSelected;
	private String iconAgrupaSelected;
	private IUnidadGenericaStyling styleListener;
	private IUnidadGenericaStyling2 styleListener2;
	
	public UnidadGenericaStyleDef() {
		styleListener = new StyleDefGenericListener();
		styleListener2 = null;
	}
	
	public String getIconHoja() {
		return iconHoja;
	}
	public UnidadGenericaStyleDef setIconHoja(String iconHoja) {
		this.iconHoja = iconHoja;
		return this;
	}
	public String getIconAgrupa() {
		return iconAgrupa;
	}
	public UnidadGenericaStyleDef setIconAgrupa(String iconAgrupa) {
		this.iconAgrupa = iconAgrupa;
		return this;
	}

	public String getIconHojaSelected() {
		return iconHojaSelected;
	}

	public UnidadGenericaStyleDef setIconHojaSelected(String iconHojaSelected) {
		this.iconHojaSelected = iconHojaSelected;
		return this;
	}

	public String getIconAgrupaSelected() {
		return iconAgrupaSelected;
	}

	public UnidadGenericaStyleDef setIconAgrupaSelected(String iconAgrupaSelected) {
		this.iconAgrupaSelected = iconAgrupaSelected;
		return this;
	}

	public void addListener(IUnidadGenericaStyling styleListener) {
		this.styleListener = styleListener;
	}
	
	public IUnidadGenericaStyling getListener() {
		return this.styleListener;
	}

	public void addListener(IUnidadGenericaStyling2 styleListener) {
		this.styleListener2 = styleListener;
	}
	
	@Override
	public String putIcon(String unidad, boolean isGroup, String icon) {
		if(this.styleListener != null) {
			return this.styleListener.putIcon(unidad, isGroup, icon);
		}
		return icon;
	}

	@Override
	public String putIcon2(IUnidadGenerica unidad, int nivel, boolean isGroup, String icon) {
		if(this.styleListener2 != null) {
			return this.styleListener2.putIcon2(unidad, nivel,isGroup, icon);
		}
		return icon;
	}
 

}
