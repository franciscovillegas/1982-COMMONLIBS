package portal.com.eje.portal.organica.ifaces;

import portal.com.eje.portal.organica.vo.IUnidadGenerica;
import portal.com.eje.portal.organica.vo.UnidadGenericaStyleDef;

public interface IUnidadDependency {
 
	public String putDependency(IUnidadGenerica unidad, UnidadGenericaStyleDef defUnidad, String unidId, boolean isGroup, String icon);
}
