package portal.com.eje.genericconf;

import portal.com.eje.genericconf.enums.EnumTooltipPlacement;
import portal.com.eje.genericconf.ifaces.IButtonBootstrap;

public class AbsButtonBootstrap extends AbsButtonProcesable implements IButtonBootstrap  {

	//private IPageResource iframeLoader = BootstrapTemplates.getInstance().getIFrameLoaders().getDefault();

	protected String spanCssClasses = "fa fa-bank fa-2x";
	protected String href = "javascript:;";
	protected String acssClasses = "hvr-bounce-to-right-sidebar-parent";
	protected String idLi;
	protected String onClick;
	private boolean tooltipHtml;
	private EnumTooltipPlacement tooltipPlacement;
	
	public AbsButtonBootstrap() {
		setAcssClasses(null);
		setSpanCssClasses(null);
		setTooltipHtml(true);
	}
//	public IPageResource getIframeLoader() {
//		return iframeLoader;
//	}
//
//	public void setIframeLoader(IPageResource iframeLoader) {
//		this.iframeLoader = iframeLoader;
//	}

	@Override
	public String getSpanCssClasses() {
		return spanCssClasses;
	}

	@Override
	public void setSpanCssClasses(String spanCssClasses) {
		this.spanCssClasses = spanCssClasses;
	}

	@Override
	public String getHref() {
		return href;
	}

	@Override
	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public String getAcssClasses() {
		return acssClasses;
	}

	@Override
	public void setAcssClasses(String acssClasses) {
		this.acssClasses = acssClasses;
	}

	@Override
	public String getIdLi() {
		return idLi;
	}

	@Override
	public void setIdLi(String idLi) {
		this.idLi = idLi;
	}

	@Override
	public String getOnClick() {
		return onClick;
	}

	@Override
	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	@Override
	public boolean isTooltipHtml() {
		return tooltipHtml;
	}

	@Override
	public void setTooltipHtml(boolean tooltipHtml) {
		this.tooltipHtml = tooltipHtml;
	}

	@Override
	public EnumTooltipPlacement getTooltipPlacement() {
		return tooltipPlacement;
	}

	@Override
	public void setTooltipPlacement(EnumTooltipPlacement tooltipPlacement) {
		this.tooltipPlacement = tooltipPlacement;
	}

 

	
	
}
