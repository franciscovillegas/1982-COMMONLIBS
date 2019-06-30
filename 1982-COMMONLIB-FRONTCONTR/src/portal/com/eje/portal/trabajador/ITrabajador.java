package portal.com.eje.portal.trabajador;

import portal.com.eje.portal.liquidacion.ILiquidacionTool;
import portal.com.eje.portal.organica.IDimension;
import portal.com.eje.portal.organica.IOrganica;

public interface ITrabajador {

	public ITrabajadorData getManagerTrabajador();
	
	public IManagerDatosPrevisionales getManagerPrevisionales();
	
	public IOrganica getManagerOrganicaUnidades();
	
	public IDimension getManagerOrganicaDimension();
	
	public ILiquidacionTool getMngLiquidacion();
	
}
