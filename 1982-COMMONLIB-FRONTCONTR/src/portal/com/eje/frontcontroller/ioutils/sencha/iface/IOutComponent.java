package portal.com.eje.frontcontroller.ioutils.sencha.iface;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.portal.organica.vo.IUnidadGenerica;

/**
 * 
 * Está hecho para agregar herramientas al retorno del senchaJson
 * @author Pancho
 * @since
 * 
 * 
 * */
public interface IOutComponent {

	public void setUtilidad(ConsultaData dataToReturn, IOClaseWeb io);
	
	public void setUtilidad(IUnidadGenerica unidadGenerica, IOClaseWeb io);
	
}
