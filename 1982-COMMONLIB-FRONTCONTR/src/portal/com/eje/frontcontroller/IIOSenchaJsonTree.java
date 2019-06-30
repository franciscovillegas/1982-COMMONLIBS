package portal.com.eje.frontcontroller;

import portal.com.eje.portal.organica.ifaces.IUnidadDependency;
import portal.com.eje.portal.organica.vo.IUnidadGenerica;
import portal.com.eje.portal.organica.vo.UnidadGenericaStyleDef;

public interface IIOSenchaJsonTree {

	public void retSenchaTree(IUnidadGenerica unidad, UnidadGenericaStyleDef defUnidad);
		
	public void retSenchaTreeAllCompanies(UnidadGenericaStyleDef defUnidad);
	
	public String getSenchaTreeAllCompanies(UnidadGenericaStyleDef defUnidad);
	
	public String getSenchaTreeAllCompanies(UnidadGenericaStyleDef defUnidad, boolean showUnitsHidden);
	
	public void retSenchaTreeAllCompanies();
	
	public void retSenchaTreeFromUnidad(Object[] unidades, UnidadGenericaStyleDef defUnidad);
	
	/**
	 * Sirve para crear nodos dependiente a unidades
	 * @since 07-07-2018
	 * @author Pancho
	 * */
	public void retSenchaTreeFromUnidad(Object[] unidades, UnidadGenericaStyleDef defUnidad, IUnidadDependency dependencia);
	
	public String getSenchaTreeFromUnidad(Object[] unidades, UnidadGenericaStyleDef defUnidad);
	
	public String getSenchaTreeFromUnidad(Object[] unidades, UnidadGenericaStyleDef defUnidad,IUnidadDependency dependencia);
	
	public void retSenchaTreeFromUnidad(Object[] unidades);
	
	public void senchaTreeClearCache();
	
	public void clearCache(Object[] unidades,UnidadGenericaStyleDef defUnidad);
	
	public boolean isUseCache();

	public void setUseCache(boolean useCache);
	
	public IIOSenchaJsonTreeNodeListener getNodeListener();

	public void setNodeListener(IIOSenchaJsonTreeNodeListener nodeListener);

}
