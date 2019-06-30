package portal.com.eje.portal.organica;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Ctr;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.organica.ctr.CtrGDimension;
import portal.com.eje.portal.organica.enums.DimensionCampoRef;
import portal.com.eje.portal.organica.ifaces.ICtrGDimension;
import portal.com.eje.portal.organica.ifaces.IDimensionUtil;

public class OrganicaPorDimension implements IDimension {

	protected EOrganicaTipo tipo;
	
	public OrganicaPorDimension() {
		this.tipo = EOrganicaTipo.porDimension;
	}

	/***
	 * @author Pancho
	 * @since 18-06-2018
	 * */
	@Override
	public ConsultaData getDimensionFromUnidad(String strIdUnidad) {

		return getPrivateDimensiones(true,strIdUnidad, null, null);
		
	}
	
	
	@Override
	public ConsultaData getDimensionFromRut(int rut) {
		
		String strUnidad = "";
		
		ConsultaData dtaUnidad = OrganicaLocator.getInstance().getUnidadFromRut(rut);
		while (dtaUnidad!=null && dtaUnidad.next()){
			strUnidad = dtaUnidad.getString("unidad");
		}

		
		return getDimensionFromUnidad(strUnidad);
		
	}

	private ConsultaData getPrivateDimensiones(boolean incluyeValores, String unidId, Integer dimensionId, DimensionCampoRef campoRefDimension) {
		StringBuilder strSQL = new StringBuilder();

		strSQL.append("select distinct d.dimension_id, d.dimension_desc, d.dimension_tipo, d.dimension_cardinalidad, d.camporef \n");
		
		if(incluyeValores) {
			strSQL.append(", di.ditem_id, di.ditem_desc, di.codref \n");	
		}
		
		strSQL.append("from eje_ges_ditem_unidad du \n");
		strSQL.append("inner join eje_ges_dimension_item di on di.ditem_id=du.ditem_id \n");
		strSQL.append("inner join eje_ges_dimension d on d.dimension_id=di.dimension_id \n");
		strSQL.append("where 1=1 \n");
		if(unidId!=null) {
			strSQL.append(" and du.unidad_id= ?  \n");	
		}
		if(dimensionId != null) {
			strSQL.append(" and di.dimension_id = ?  \n");	
		}
		if(campoRefDimension != null) {
			strSQL.append(" and d.camporef = ?  \n");
		}

		ConsultaData data = null;
		
		try {
			List<Object> params = new ArrayList<Object>();
			if(unidId!=null) {
				params.add(unidId);
			}
			if(dimensionId != null) {
				params.add(dimensionId);
			}
			if(campoRefDimension != null) {
				params.add(campoRefDimension.toString());
			}
			
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		return data;
	}
	
	@Override
	public ConsultaData getDimensiones() {
		return getPrivateDimensiones(false,null, null, null);
	}

	@Override
	public ConsultaData getDimension(Integer dimensionID) {
		return getPrivateDimensiones(false,null, dimensionID, null);
	}
	
	@Override
	public ConsultaData getDimension(DimensionCampoRef campoRef) {
		return getPrivateDimensiones(false,null, null, campoRef);
	}

	@Override
	public ConsultaData getDimensionFromValor(String ditemID) {
		throw new NotImplementedException();
	}

	@Override
	public ConsultaData getValores(Integer dimensionID) {
		return getPrivateDimensiones(true,null, dimensionID, null);
	}
	
	@Override
	public ConsultaData getValores(DimensionCampoRef campoRef) {
		return getPrivateDimensiones(true,null, null, campoRef);
	}
	
	@Override
	public ConsultaData getValores(String unidId,Integer dimensionID) {
		return getPrivateDimensiones(true,unidId, dimensionID, null);
	}
	
	@Override
	public ConsultaData getValores(String unidId, DimensionCampoRef campoRef) {
		return getPrivateDimensiones(true,unidId, null, campoRef);
	}

	@Override
	public ConsultaData getTrabajadoresInDimension(String dimensionID) {
		throw new NotImplementedException();
	}

	@Override
	public ConsultaData getTrabajadoresInValor(String ditemID) {
		throw new NotImplementedException();
	}

	@Override
	public ICtrGDimension getCtrG() {
		return Ctr.getInstance(CtrGDimension.class);
	}

	@Override
	public IDimensionUtil getUtil() {
		return Util.getInstance(DimensionUtil.class);
	}





	
}
