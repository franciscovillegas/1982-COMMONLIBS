package portal.com.eje.portal.organica.ctr;

import java.util.Collection;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.portal.organica.OrganicaLocator;
import portal.com.eje.portal.organica.enums.DimensionCampoRef;
import portal.com.eje.portal.organica.ifaces.ICtrGDimension;
import portal.com.eje.portal.organica.vo.VoDimension;
import portal.com.eje.portal.organica.vo.VoDimensionValor;
import portal.com.eje.portal.vo.VoTool;

public class CtrGDimension implements ICtrGDimension {

	@Override
	public Collection<VoDimension> getDimensiones() {
		ConsultaData data = OrganicaLocator.getInstanceDimension().getDimensiones();
		Collection<VoDimension> colls = VoTool.getInstance().buildVo(data, VoDimension.class);
		return colls;
	}

	@Override
	public VoDimension getDimension(int idDimension) {
		ConsultaData data = OrganicaLocator.getInstanceDimension().getDimension(idDimension);
		
		VoDimension vo = VoTool.getInstance().buildVoSimple(data, VoDimension.class);
		return vo;
	}

	@Override
	public VoDimension getDimension(DimensionCampoRef campoRef) {
		ConsultaData data = OrganicaLocator.getInstanceDimension().getDimension(campoRef);
		
		VoDimension vo = VoTool.getInstance().buildVoSimple(data, VoDimension.class);
		return vo;
	}
	
	@Override
	public Collection<VoDimensionValor> getValores(Integer dimensionID) {
		ConsultaData data = OrganicaLocator.getInstanceDimension().getValores(dimensionID);
		Collection<VoDimensionValor> vos = VoTool.getInstance().buildVo(data, VoDimensionValor.class);
		return vos;
	}
	
	@Override
	public Collection<VoDimensionValor> getValores(DimensionCampoRef campoRef) {
		ConsultaData data = OrganicaLocator.getInstanceDimension().getValores(campoRef);
		Collection<VoDimensionValor> vos = VoTool.getInstance().buildVo(data, VoDimensionValor.class);
		return vos;
	}
	
	@Override
	public Collection<VoDimensionValor> getValores(String unidId, Integer dimensionID) {
		ConsultaData data = OrganicaLocator.getInstanceDimension().getValores(unidId, dimensionID);
		Collection<VoDimensionValor> vos = VoTool.getInstance().buildVo(data, VoDimensionValor.class);
		return vos;
	}

	@Override
	public Collection<VoDimensionValor> getValores(String unidId, DimensionCampoRef campoRef) {
		ConsultaData data = OrganicaLocator.getInstanceDimension().getValores(unidId, campoRef);
		Collection<VoDimensionValor> vos = VoTool.getInstance().buildVo(data, VoDimensionValor.class);
		return vos;
	}

}
