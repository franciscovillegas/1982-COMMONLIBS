package portal.com.eje.portal.organica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringEscapeUtils;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.strings.ArrayFactory;
import cl.ejedigital.tool.strings.SqlBuilder;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.error.DuplicateKeyException;
import cl.ejedigital.web.datos.map.ConsultaDataMap;
import portal.com.eje.portal.factory.Ctr;
import portal.com.eje.portal.organica.ctr.CtrGOrganica;
import portal.com.eje.portal.organica.ifaces.ICtrGOrganica;
import portal.com.eje.portal.trabajador.enums.EjeGesTrabajadorField;
import portal.com.eje.portal.vo.ClaseConversor;

/**
 * Debe estar todo en Organica, no deben haber dos clases con lógicas similares
 * 
 * */
public class FastestOrganica implements IOrganica {

	
	@Override
	public ConsultaData getJefeUnidad(String unidad) {
		List<String> unidades = new ArrayList<String>();
		unidades.add(unidad);
		
		return getJefeUnidad(unidades);		
	}
	
	@Override
	public ConsultaData getJefeUnidad(List<String> unidades) {
		ArrayFactory af = new ArrayFactory();
		af.addAll(unidades);
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select u.unid_id, t.rut ");
		sql.append(" from eje_ges_unidad_encargado eu ");
		sql.append("		inner join eje_ges_jerarquia j on eu.unid_id = j.nodo_id ");
		sql.append("		inner join eje_ges_unidades u on u.unid_id = eu.unid_id ");
		sql.append("			inner join eje_ges_trabajador t on t.rut = eu.rut_encargado ");
		sql.append(" where eu.unid_id in ").append(af.getArrayString());
		sql.append(" 		and eu.estado = 1 ");
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}


	@Override
	public ConsultaData getJefeResponsableDeLaUnidad(String unidad) {
		ConsultaData data = getUnidadesAscendientes(null, unidad, false);
		List<String> unidades = ConsultaTool.getInstance().getList(data, LinkedList.class, "unid_id");
		
		/*de abajo hacia arriba*/
		Collections.reverse(unidades);
		
		ConsultaData jefes = getJefeUnidad(unidades);
		ConsultaData retorno = new ConsultaData(jefes.getNombreColumnas());
		
		try {
			ConsultaDataMap<String> mapJefes = ConsultaTool.getInstance().getMap(jefes, "unid_id", String.class);
			
			if(jefes != null) {
				for(String u : unidades) { // de abajo hacia arriba
					ConsultaData busqueda = mapJefes.getConsultaData(u);
					if(busqueda != null && busqueda.next()) {
						retorno.addAll(busqueda);
						break;
					}
				}
			}
			
		} catch (DuplicateKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return retorno;
	}

	@Override
	public ConsultaData getJefeDelTrabajador(int rut) {
		ConsultaData data = getUnidadFromRut(rut);
		ConsultaData dataJefatura = null;
		
		if(data != null && data.next()) {
 
			dataJefatura = getJefeResponsableDeLaUnidad(data.getString("unid_id"));
			if(dataJefatura != null && dataJefatura.next()) {
				if(dataJefatura.getInt("rut") == rut) {
					
					ConsultaData dataPadre =  getUnidadesPadres(data.getString("unid_id"));
					if(dataPadre != null &&  dataPadre.next()) {
						dataJefatura = getJefeResponsableDeLaUnidad(dataPadre.getString("unid_id"));
					}
					else {
						//throw new JefaturaNotFoundException("No fue encontrado el jefe del rut >>"+rut);
					}
				}
			}
			
			if(dataJefatura != null){
				dataJefatura.toStart();
			}
			return dataJefatura;
		}
		return null;
	}
	
	@Override
	public ConsultaData getJefeDelTrabajador(String rut) {
		return getJefeDelTrabajador((Integer) ClaseConversor.getInstance().getObject(rut, Integer.class));
	}

	@Override
	public ConsultaData getUnidadesDescendientes(String unidad) {
		return getUnidadesDescendientes(null, unidad, false);		
	}

	@Override
	public ConsultaData getUnidadesAscendientes(String unidad) {
		return getUnidadesAscendientes(null, unidad, false);
	}

	@Override
	public ConsultaData getTrabajadoresInUnidad(String unidad) {
//		String sql = "select rut from eje_ges_trabajador where unidad = ? ";
//		
//		ConsultaData data = null;
//		try {
//			Object[] params = {unidad};
//			data = ConsultaTool.getInstance().getData("portal", sql, params);
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}	
//		return data;
		
		return getTrabajadoresInUnidad(unidad, false);
	}

	@Override
	public ConsultaData getTrabajadoresInUnidad(String unidad, boolean omiteJefe) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT t.rut, es_jefe=eu.estado ");
		sql.append(" FROM eje_ges_trabajador t ");
		sql.append("  LEFT OUTER JOIN eje_ges_unidad_encargado eu ");
		sql.append(" 	on eu.unid_id = t.unidad and eu.estado = 1 and eu.rut_encargado = t.rut ");
		sql.append(" where t.unidad = ? ");
		
		if(omiteJefe) {
			sql.append(" and not eu.rut_encargado is null ");
		}
		
		ConsultaData data = null;
		try {
			Object[] params = {unidad};
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return data;
		
	}

	@Override
	public ConsultaData getTrabajadoresDependientes(String unidad, boolean omiteEstaUnidad) {
		ConsultaData data = null;
		
		try {
			if(!omiteEstaUnidad) {
				String sql ="select t.rut,unid_id=t.unidad from eje_ges_trabajador t inner join dbo.getdescendientes(?) u on t.unidad = u.unidad ";
				Object[] params = {unidad};
				data = ConsultaTool.getInstance().getData("portal", sql, params);	
			}
			else {
				String sql ="select t.rut,unid_id=t.unidad from eje_ges_trabajador t inner join dbo.getdescendientes(?) u on t.unidad = u.unidad and u.unidad <> ? ";
				Object[] params = {unidad, unidad};
				data = ConsultaTool.getInstance().getData("portal", sql, params);	
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	

	@Override
	public ConsultaData getTrabajadoresDependientes(int rut) {
		ConsultaData dataUnidad = getUnidadFromRut(rut);
		boolean esJefe = isJefe(rut);
		String unidad =ConsultaTool.getInstance().getFirstValue(dataUnidad, "unid_id", String.class);
		
		ConsultaData data = null;
		if(esJefe) {
			 data = getTrabajadoresDependientes(unidad, false);
		}

		return data;
	}
	
	@Override
	public ConsultaData getTrabajadoresDependientes(String unidad) {
		return getTrabajadoresDependientes(unidad, false);
	}
	

	@Override
	public ConsultaData getJefesDependientes(String unidad, boolean omiteEstaUnidad) {
		throw new NotImplementedException();
		
	}

	@Override
	public ConsultaData getTrabajadoresReporteadores(String unidad) {
throw new NotImplementedException();
		
	}

	@Override
	public ConsultaData getTrabajadoresReporteadores(int rut) {
throw new NotImplementedException();
		
	}

	@Override
	public ConsultaData getTrabajadores() {
throw new NotImplementedException();
		
	}

	@Override
	public ConsultaData getTrabajadores(int intIdPersona) {
throw new NotImplementedException();
		
	}

	@Override
	public ConsultaData getTrabajadores(String strFiltro) {
throw new NotImplementedException();
		
	}

	@Override
	public ConsultaData getUnidadesHijas(String unidad) {
		return getUnidadesHijas(unidad, false);
		
	}
	
	@Override
	public ConsultaData getUnidadesHijas(String unidad, boolean incluyeNoVigentes) {
		StringBuilder strSQL = new StringBuilder();

		strSQL.append("select unidad=j.nodo_id, u.unid_id \n") 
		.append("from eje_ges_unidades u \n") 
		.append("inner join eje_ges_jerarquia j on u.unid_id = j.nodo_id \n") 
		.append("left join (select unidad, cantidad=count(rut) from eje_ges_trabajador group by unidad) t on t.unidad=u.unid_id \n") 
		.append("left join (select e.unid_id \n") 
		.append("	from eje_ges_unidad_encargado e \n") 
		.append("	inner join eje_ges_trabajador t on t.rut=e.rut_encargado \n") 
		.append("	where e.estado=1) e on e.unid_id=u.unid_id \n")
		.append("where j.nodo_padre = ? ");
		
		if(!incluyeNoVigentes) {
			strSQL.append(" and u.vigente = 'S' \n"); 
		}
		
		strSQL.append("group by j.nodo_id,u.unid_empresa, u.unid_id, u.unid_desc, u.area, u.vigente, u.id_tipo, u.texto, t.cantidad, e.unid_id \n");
		
		ConsultaData data = null;
		Object[] params = {unidad};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params );
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}




	@Override
	public ConsultaData getUnidades() {
			throw new NotImplementedException();
		
	}

	@Override
	public ConsultaData getUnidades(String strFiltro) {
			throw new NotImplementedException();
		
	}

	@Override
	public ConsultaData getUnidadesPadres(String unidad) {
throw new NotImplementedException();
		
	}

	/**
	 * copiado de Organica
	 * @see Organica #getUnidadRaiz()
	 * @since 05-07-2018
	 * */
	
	@Override
	public ConsultaData getUnidadRaiz() { 
		StringBuilder sql = new StringBuilder();
		sql.append("	select  u.unid_id \n");
		sql.append("	from eje_ges_unidades u \n");
		sql.append("		inner join eje_ges_jerarquia j on u.unid_id = j.nodo_id  \n");
		sql.append("		left join eje_ges_trabajador t on t.unidad = u.unid_id  \n"); 
		sql.append("	where (j.nodo_padre is null) or (j.nodo_padre = '0') \n");
		sql.append("    group by u.unid_empresa, u.unid_id, u.unid_desc, u.area, u.vigente, u.id_tipo, u.texto \n");
		ConsultaData data = null;
		Object[] params = { };
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params );
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
		
	}

	/**
	 * copiado de Organica
	 * @see Organica #getUnidadesRaices()
	 * @since 05-07-2018
	 * */
	@Override
	public List<String> getUnidadesRaices() { 
		String sql = "select j.nodo_id from eje_ges_jerarquia j inner join eje_ges_unidades u on u.unid_id = j.nodo_id where j.nodo_padre = '0' or j.nodo_padre is null";
		List<String> lista = new ArrayList<String>();
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql);
			
			while(data != null && data.next()) {
				lista.add(data.getForcedString("nodo_id"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return lista;
		
	}

	@Override
	public ConsultaData getUnidad(String unidad) {
throw new NotImplementedException();
		
	}

	@Override
	public ConsultaData getUnidadFromRut(int rut) {
		String sql ="select unid_id=unidad from eje_ges_trabajador where rut = ? ";
		Object[] params = {rut};
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
			return data;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}

	@Override
	public ConsultaData getUnidadesEncargadas(int intPersona) {
		throw new NotImplementedException();
		
	}

	@Override
	public ConsultaData getUnidadesEncargadas(int intPersona, Integer intVigente) {
		throw new NotImplementedException();
		
	}

	@Override
	public boolean isJefe(int rut) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT t.rut ");
		sql.append(" FROM eje_ges_trabajador t ");
		sql.append("  inner JOIN eje_ges_unidad_encargado eu ");
		sql.append(" 	on eu.unid_id = t.unidad and eu.estado = 1 and eu.rut_encargado = t.rut ");
		sql.append(" where t.rut = ? ");
		
		ConsultaData data = null;
		try {
			Object[] params = {rut};
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return data != null && data.next();
	
	}

	@Override
	public ICtrGOrganica getCtrG() {
 
		return new CtrGOrganica(this);
	}

	@Override
	public ConsultaData getUnidadesDescendientes(Connection connPortal, String unidad, boolean incluyeNoVigentes) {
		if(unidad == null) {
			throw new NullPointerException();
		}
		
		ConsultaData data = null;
		try {
			SqlBuilder sql = new SqlBuilder();
			sql.appendLine(" SELECT distinct u.unid_id");
			sql.appendLine(" FROM dbo.GetDescendientes('"+StringEscapeUtils.escapeSql(Validar.getInstance().validarDato(unidad,"A900"))+"') as d");
			sql.appendLine(" 		inner join eje_ges_unidades u on d.unidad = u.unid_id ");
			
			if(!incluyeNoVigentes) {
				sql.appendLine(" where u.vigente = 'S' ");	
			}
			
			if(connPortal != null) {
				data = ConsultaTool.getInstance().getData(connPortal, sql);	
			}
			else {
				data = ConsultaTool.getInstance().getData("portal", sql);
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return data;
	}

	@Override
	public ConsultaData getUnidad(Connection connPortal, List<String> listaDeUnidId) {
		throw new NotImplementedException();
	}

	@Override
	public ConsultaData getUnidadesAscendientes(Connection connPortal, String unidad, boolean incluyeNoVigentes) {
		if(unidad == null) {
			throw new NullPointerException();
		}
		
		ConsultaData data = null;
		try {
			SqlBuilder sql = new SqlBuilder();
			sql.appendLine(" SELECT distinct u.unid_id,j.nodo_nivel");
			sql.appendLine(" FROM dbo.GetAscendentes('"+StringEscapeUtils.escapeSql(Validar.getInstance().validarDato(unidad,"A900"))+"') as d");
			sql.appendLine(" 		inner join eje_ges_unidades u on d.unidad = u.unid_id ");
			sql.appendLine(" 		inner join eje_ges_jerarquia j  on j.nodo_id = u.unid_id ");
			
			if(!incluyeNoVigentes) {
				sql.appendLine(" where u.vigente = 'S' ");	
			}
			
			sql.appendLine(" ORDER BY j.nodo_nivel ");
			
			if(connPortal != null) {
				data = ConsultaTool.getInstance().getData(connPortal, sql);	
			}
			else {
				data = ConsultaTool.getInstance().getData("portal", sql);
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		} 
		
		return data;
	}

	@Override
	public ConsultaData getTrabajadoresInUnidad(String strUnidad, List<EjeGesTrabajadorField> fields) {
		throw new NotImplementedException();
	}



}
