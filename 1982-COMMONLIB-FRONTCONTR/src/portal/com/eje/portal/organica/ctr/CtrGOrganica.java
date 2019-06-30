package portal.com.eje.portal.organica.ctr;

import java.util.Collection;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.portal.organica.IOrganica;
import portal.com.eje.portal.organica.ifaces.ICtrGOrganica;
import portal.com.eje.portal.organica.vo.VoTrabajadorUnidad;
import portal.com.eje.portal.organica.vo.VoUnidad;
import portal.com.eje.portal.vo.VoTool;

public class CtrGOrganica implements ICtrGOrganica {
	private IOrganica org;
	
	public CtrGOrganica(IOrganica org) {
		this.org = org;
	}
	
	@Override
	public VoTrabajadorUnidad getJefeUnidad(String unidad) {
		VoTrabajadorUnidad vo = null;
		if(unidad != null) {
			ConsultaData dataJefaturaU = org.getJefeUnidad(unidad);
			vo = VoTool.getInstance().buildVoSimple(dataJefaturaU, VoTrabajadorUnidad.class);
		}
		
		return vo;
	}

	@Override
	public Collection<VoTrabajadorUnidad> getTrabajadoresDependientes(String unidad, boolean omiteEstaUnidad) {
		Collection<VoTrabajadorUnidad> vos = null;
		
		if(unidad != null) {
			ConsultaData dataTrabajadores = org.getTrabajadoresDependientes(unidad, omiteEstaUnidad);
			vos = VoTool.getInstance().buildVo(dataTrabajadores, VoTrabajadorUnidad.class);
		}
		
		return vos;
	}

	@Override
	public VoTrabajadorUnidad getTrabajadores(int rutIdInt) {
		VoTrabajadorUnidad vo = null;
		if(rutIdInt > 0) {
			ConsultaData dataTrabajador = org.getTrabajadores(rutIdInt);
			vo = VoTool.getInstance().buildVoSimple(dataTrabajador, VoTrabajadorUnidad.class);
		}
		return vo;
	}
	
	@Override
	public Collection<VoTrabajadorUnidad> getTrabajadores(String filtro) {
		Collection<VoTrabajadorUnidad> vos = null;
		if(filtro != null) {
			ConsultaData dataTrabajador = org.getTrabajadores(filtro);
			vos = VoTool.getInstance().buildVo(dataTrabajador, VoTrabajadorUnidad.class);
		}
		return vos;
	}

	@Override
	public Collection<VoUnidad> getUnidadesAscendientes(String unid_id) {
		Collection<VoUnidad> vos = null;
		if(unid_id != null) {
			ConsultaData dataUnidades = org.getUnidadesAscendientes(unid_id);
			vos = VoTool.getInstance().buildVo(dataUnidades, VoUnidad.class);
		}

		return vos;
	}

	@Override
	public VoTrabajadorUnidad getJefeDelTrabajador(int rutIdInt) {
		VoTrabajadorUnidad vo = null;
		if(rutIdInt > 0) {
			ConsultaData dataJefaturaU = org.getJefeDelTrabajador(rutIdInt);
			vo = VoTool.getInstance().buildVoSimple(dataJefaturaU, VoTrabajadorUnidad.class);
		}
		
		return vo;
	}

	@Override
	public VoUnidad getUnidadFromRut(int rutIdInt) {
		VoUnidad vo = null;
		if(rutIdInt > 0) {
			ConsultaData data = org.getUnidadFromRut(rutIdInt);
			vo = VoTool.getInstance().buildVoSimple(data, VoUnidad.class);
		}
		
		return vo;
	}

}
