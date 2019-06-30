package portal.com.eje.portal.trabajador;

import portal.com.eje.portal.factory.SingleFactory;
import portal.com.eje.portal.factory.SingleFactoryType;
import portal.com.eje.portal.liquidacion.ILiquidacionTool;
import portal.com.eje.portal.liquidacion.LiquidacionTool;
import portal.com.eje.portal.organica.IDimension;
import portal.com.eje.portal.organica.IOrganica;
import portal.com.eje.portal.organica.OrganicaLocator;

public class Colaborador implements ITrabajador {

	public static ITrabajador getInstance() {
		return SingleFactory.getFactory(SingleFactoryType.UTIL).getInstance(Colaborador.class);
	}
	
	@Override
	public ITrabajadorData getManagerTrabajador() {
		return TrabajadorDataLocator.getInstance();
	}

	@Override
	public IManagerDatosPrevisionales getManagerPrevisionales() {
		return DatosPrevisionalesLocator.getInstance();
	}

	@Override
	public IOrganica getManagerOrganicaUnidades() {
		return OrganicaLocator.getInstance();
	}
 
	@Override
	public IDimension getManagerOrganicaDimension() {
		return OrganicaLocator.getInstanceDimension();
	}
 
	@Override
	public ILiquidacionTool getMngLiquidacion() {
		return LiquidacionTool.getInstance();
	}
	
}
