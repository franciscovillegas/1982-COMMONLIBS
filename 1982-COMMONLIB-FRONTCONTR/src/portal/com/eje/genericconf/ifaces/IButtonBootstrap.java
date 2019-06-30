package portal.com.eje.genericconf.ifaces;

import portal.com.eje.genericconf.enums.EnumTooltipPlacement;

public interface IButtonBootstrap {

	String getSpanCssClasses();

	void setSpanCssClasses(String spanCssClasses);

	String getHref();

	/**
	 * No tiene una ubicación específica, puede partir desde la raíz o de la ruta en la que nace el template, siempre será recomendada que la 
	 * ruta base el template sea 2 niveles por sobre la ruta del dominio <br/>
	 * */

	void setHref(String href);

	String getAcssClasses();

	void setAcssClasses(String acssClasses);

	String getIdLi();

	void setIdLi(String idLi);

	String getOnClick();

	void setOnClick(String onClick);

	boolean isTooltipHtml();

	void setTooltipHtml(boolean tooltipHtml);

	EnumTooltipPlacement getTooltipPlacement();

	void setTooltipPlacement(EnumTooltipPlacement tooltipPlacement);

}