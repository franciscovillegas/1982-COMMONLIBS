package portal.com.eje.portal.organica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringEscapeUtils;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.organica.ctr.CtrGOrganica;
import portal.com.eje.portal.organica.ifaces.ICtrGOrganica;
import portal.com.eje.portal.trabajador.enums.EjeGesTrabajadorField;
import portal.com.eje.portal.vo.ClaseConversor;

/**
 * 
 * @author Pancho
 * */
public class OrganicaOriginal implements IOrganica {

	protected EOrganicaTipo tipo;
	
	public OrganicaOriginal() {
		this.tipo = EOrganicaTipo.porUnidades;
	}
	
	public OrganicaOriginal getInstance() {
		return Util.getInstance(OrganicaOriginal.class);
	}
	
	public ConsultaData getUnidadesDescendientes(String unidad) {
		String sql = "select distinct unid_id='',unidad from dbo.GetDescendientes('"+StringEscapeUtils.escapeSql(Validar.getInstance().validarDato(unidad,"A900"))+"')";
		ConsultaData data = null;

		try {
			data = ConsultaTool.getInstance().getData("portal", sql);
			if(data != null) {
				while(data.next()) {
					ConsultaData dataUnidad = getUnidad(data.getString("unidad"));
					
					data.getActualData().put("unid_id", data.getString("unidad") );
					if(dataUnidad != null && dataUnidad.next()) {
						data.getActualData().put("unid_desc", dataUnidad.getString("unid_desc"));
					}
				}

				data.toStart();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public ConsultaData getUnidadesAscendientes(String unidad) {
		List<DataFields> listaInvertida = new LinkedList<DataFields>();
		ConsultaData dataRetorno = null;
		
		/*ESTA UNIDAD*/
		ConsultaData thisData = getUnidad(unidad);
		if(thisData != null) {
			dataRetorno = new ConsultaData(thisData.getNombreColumnas());
			
			if(thisData.next()) {
				listaInvertida.add(thisData.getActualData());
			}
		}
		
		/*TODAS LAS UNIDADES PADRES*/
		ConsultaData data = getUnidadesPadres(unidad);
		if(data != null && data.next()) {
			if(data.getForcedString("unid_id") != null) {
				listaInvertida.add(data.getActualData());
				
				data = getUnidadesPadres(data.getForcedString("unid_id")); 
				while(data != null && data.next() && data.getForcedString("unid_id") != null) {
					listaInvertida.add(data.getActualData());
					data = getUnidadesPadres(data.getForcedString("unid_id")); 
				}
			}
		}
		
		/*INVIERTE SELECCIÓN, COSA DE TENER PRIMERO LAS UNIDADES SUPERIORES Y AL FINAL LA UNIDAD EN SI.*/
 
			
		for(int i = (listaInvertida.size()) -1 ; i>= 0; i--) {
			dataRetorno.add(listaInvertida.get(i));
		}
		 
		
		return dataRetorno;
	}
	
	public ConsultaData getTrabajadoresInUnidad(String unidad) {
		return getTrabajadores(0, unidad, null, false);
	}

	public ConsultaData getTrabajadoresInUnidad(String unidad, boolean omiteJefe) {
		return getTrabajadores(0, unidad, null, omiteJefe);
	}
	
	public ConsultaData getTrabajadoresInUnidad(String strUnidad, List<EjeGesTrabajadorField> fields) {
		ConsultaData data = null;
		
		StringBuilder strSQL = new StringBuilder();

		strSQL.append("select distinct dummy=null");
		
		if(fields.contains(EjeGesTrabajadorField.rut)) {
			strSQL.append(", id_persona=t.rut, t.rut");
		}
		
		if(fields.contains(EjeGesTrabajadorField.digito_ver)) {
			strSQL.append(", t.digito_ver");
		}
		
		if(fields.contains(EjeGesTrabajadorField.rut) && fields.contains(EjeGesTrabajadorField.digito_ver)) {
			strSQL.append(", nif=cast(rut as varchar) + '-' + digito_ver");
		}
		
		if(fields.contains(EjeGesTrabajadorField.nombre)) {
			strSQL.append(", nombre=rtrim(ltrim(t.nombre))");
		}
		
		if(fields.contains(EjeGesTrabajadorField.nombres)) {
			strSQL.append(", nombres=rtrim(ltrim(t.nombres))");
		}
		
		if(fields.contains(EjeGesTrabajadorField.ape_paterno)) {
			strSQL.append(", ape_paterno=rtrim(ltrim(t.ape_paterno))");
		}
		
		if(fields.contains(EjeGesTrabajadorField.ape_materno)) {
			strSQL.append(", ape_materno=rtrim(ltrim(t.ape_materno))");
		}
		
		if(fields.contains(EjeGesTrabajadorField.cargo)) {
			strSQL.append(", id_cargo=t.cargo, cargo=rtrim(ltrim(c.descrip))");
		}

		if(fields.contains(EjeGesTrabajadorField.fecha_ingreso)) {
			strSQL.append(", t.fecha_ingreso");
		}
		
		if(fields.contains(EjeGesTrabajadorField.unidad) || fields.contains(EjeGesTrabajadorField.unid_id)) {
			strSQL.append(", id_unidad=u.unid_id, unidad=u.unid_desc, es_encargado=convert(smallint,isnull(ue.estado,0))");
		}
		
		if(fields.contains(EjeGesTrabajadorField.wp_cod_empresa) || fields.contains(EjeGesTrabajadorField.empresa)) {
			strSQL.append(", id_empresa=t.empresa, empresa=e.descrip");
		}
		
		if(fields.contains(EjeGesTrabajadorField.ccosto) ) {
			strSQL.append(", id_centrocosto=t.ccosto, centrocosto=cc.descrip");
		}
		
		if(fields.contains(EjeGesTrabajadorField.forma_pago) ) {
			strSQL.append(", t.forma_pago, t.banco, t.cta_cte");
		}
		
		for(EjeGesTrabajadorField c: fields) {
			if(strSQL.toString().indexOf(c.name()) == -1) {
				strSQL.append(", t."+c.name());	
			}
		}
		
		strSQL.append(" \n");
		strSQL.append("from eje_ges_trabajador t \n");
		
		if(fields.contains(EjeGesTrabajadorField.unidad) || fields.contains(EjeGesTrabajadorField.unid_id)) {
			strSQL.append("left join eje_ges_jerarquia j on t.unidad=j.nodo_id \n");
			strSQL.append("left join eje_ges_unidades u on t.unidad=u.unid_id \n");
			strSQL.append("left join eje_ges_unidad_encargado ue on ue.unid_id = t.unidad and ue.rut_encargado = t.rut and ue.estado = 1 \n");
		}
		
		if(fields.contains(EjeGesTrabajadorField.cargo)) {
			strSQL.append("left join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa \n");
		}
		
		if(fields.contains(EjeGesTrabajadorField.wp_cod_empresa) || fields.contains(EjeGesTrabajadorField.empresa)) {
			strSQL.append("left join eje_ges_empresa e on t.empresa = e.empresa \n");
		}
		
		if(fields.contains(EjeGesTrabajadorField.ccosto) ) {
			strSQL.append("left join eje_ges_centro_costo cc on t.ccosto = cc.centro_costo and cc.vigente = 's' and cc.wp_cod_empresa = t.wp_cod_empresa and cc.wp_cod_planta = t.wp_cod_planta \n");
		}
		
		strSQL.append("where t.unidad='").append(strUnidad).append("' \n");
		strSQL.append("order by rtrim(ltrim(t.nombre))");

		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public ConsultaData getTrabajadoresDependientes(String unidad, boolean omiteEstaUnidad) {
		ConsultaData dataTrabajadores = null;
		
		if( unidad != null ) {
			ConsultaData data = getUnidadesDescendientes(unidad);
			while( data != null && data.next()) {
				boolean clausulaMismaUnidad = true;
				
				if( unidad.equals( data.getString("unidad") ) && omiteEstaUnidad ) {
					clausulaMismaUnidad = false;
				}
				
				if( clausulaMismaUnidad ) {
					if(dataTrabajadores==null) {
						dataTrabajadores = getTrabajadores(0, data.getString("unidad"), null, false);
					}
					else {
						dataTrabajadores.addAll(getTrabajadores(0, data.getString("unidad"), null, false));					}	
				}
			}	
		}
		
		return dataTrabajadores;
	}
	

	@Override
	public ConsultaData getTrabajadoresDependientes(int rut) {
		/*SOLO RETORNARÁ DATA SI LA PERSONA ES JEFE DE UNIDAD*/
		throw new NotImplementedException();
	}
	
	@Override
	public ConsultaData getTrabajadoresDependientes(String unidad) {
		return getTrabajadoresDependientes(unidad, false);
	}

	public ConsultaData getJefesDependientes(String unidad, boolean omiteEstaUnidad) {
		ConsultaData dataTrabajadores = null;
		
		if( unidad != null ) {
			ConsultaData dataUnidad = getUnidad(unidad);
			if(dataUnidad != null && dataUnidad.next()) {
				ConsultaData data = getUnidadesDescendientes(unidad);
				while( data != null && data.next()) {
					boolean clausulaMismaUnidad = true;
					
					if( unidad.equals( data.getString("unidad") ) && omiteEstaUnidad ) {
						clausulaMismaUnidad = false;
					}
					
					if( clausulaMismaUnidad ) {
						if(dataTrabajadores==null) {
							dataTrabajadores = getJefeUnidad(data.getString("unidad") );
						}
						else {
							dataTrabajadores.addAll( getJefeUnidad( data.getString("unidad") ) );
						}	
					}
				}			
			}
			else {
				throw new UnidadNotFoundException("El código de unidad no es válido:"+unidad);
			}
		}
		
		return dataTrabajadores;
	}
	/*
	 * 
	 * */
	
	public ConsultaData getTrabajadoresReporteadores(String unidad) {

		List<String> columnas = new ArrayList<String>();
		columnas.add("rut"); 
		columnas.add("nombre");
		columnas.add("nombres");
		columnas.add("cargo");
		columnas.add("fecha_ingreso");
		columnas.add("empresa");

		ConsultaData reportes = new ConsultaData(columnas);
		
		reportes.addAll(getTrabajadoresInUnidad( unidad , true ));
		ConsultaData data = getUnidadesHijas(unidad);
		
		while(data != null && data.next()) {
			ConsultaData dataJefe = getJefeUnidad(data.getString("unidad"));
			if(dataJefe == null || dataJefe.size() == 0) {
				reportes.addAll(getTrabajadoresReporteadores(data.getString("unidad")));
			}
			else {
				reportes.addAll(dataJefe);
			}
		}
		
		return reportes;
	}
	

	public ConsultaData getTrabajadoresReporteadores(int rut) {
		ConsultaData data = getUnidadFromRut(rut);
		
		while(data != null && data.next()) {
			String unidad = data.getString("unid_id");
			ConsultaData dataJefatura = getJefeUnidad(unidad);
			
			if( dataJefatura != null && dataJefatura.next() ) {
				if(rut == dataJefatura.getInt("rut")) {
					return getTrabajadoresReporteadores(unidad);
				}
			}
		}
		
		return null;
	}
	
	

	public ConsultaData getUnidadesHijas(String unidad) {
		StringBuilder strSQL = new StringBuilder();

		strSQL.append("select unidad=j.nodo_id, u.unid_empresa, u.unid_id, u.unid_desc, u.area, u.vigente, u.id_tipo, u.texto, \n") 
		.append("dotacion_real=t.cantidad, \n") 
		.append("tiene_encargado=(case when coalesce(e.unid_id,'')<>'' then 1 else 0 end) \n") 
		.append("from eje_ges_unidades u \n") 
		.append("inner join eje_ges_jerarquia j on u.unid_id = j.nodo_id \n") 
		.append("left join (select unidad, cantidad=count(rut) from eje_ges_trabajador group by unidad) t on t.unidad=u.unid_id \n") 
		.append("left join (select e.unid_id \n") 
		.append("	from eje_ges_unidad_encargado e \n") 
		.append("	inner join eje_ges_trabajador t on t.rut=e.rut_encargado \n") 
		.append("	where e.estado=1) e on e.unid_id=u.unid_id \n")
		.append("where j.nodo_padre = ? \n") 
		.append("group by j.nodo_id,u.unid_empresa, u.unid_id, u.unid_desc, u.area, u.vigente, u.id_tipo, u.texto, t.cantidad, e.unid_id \n");
		
		ConsultaData data = null;
		Object[] params = {unidad};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params );
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}


	public ConsultaData getJefeUnidad(String unidad) {
		/*
		StringBuilder sql = new StringBuilder();
		sql.append("	SELECT 
		sql.append(" 		INNER JOIN eje_ges_trabajador trabajador ON trabajador.rut   = encargado.rut_encargado and 	trabajador.unidad = encargado.unid_id 	\n");
		sql.append(" 		LEFT OUTER JOIN eje_ges_cargos     cargos	 ON cargos.cargo 	 = trabajador.cargo and cargos.empresa = trabajador.empresa			\n");
		sql.append(" 		LEFT OUTER JOIN eje_ges_empresa    empresa   ON empresa.empresa  = trabajador.empresa 										\n");
		sql.append(" 		LEFT OUTER JOIN eje_ges_unidades   unidades  ON trabajador.unidad  = unidades.unid_id										\n");
		sql.append(" 	WHERE encargado.unid_id = ? and encargado.estado = ? 																			\n");
		
		ConsultaData data = null;
		Object[] params = {unidad, EstadoJefaUnidad.vigente.getValue()};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params );
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
		
//		ConsultaData data = getTrabajadores(0, unidad, null, false);
//		return data;
		
		StringBuilder strSQL = new StringBuilder();
 
		strSQL.append("select t.rut ");
		strSQL.append("from eje_ges_unidad_encargado ue ");
		strSQL.append("left outer join eje_ges_trabajador t on ue.unid_id=t.unidad and ue.rut_encargado=t.rut and ue.estado=1 \n");																					
		strSQL.append("left outer join eje_ges_unidades u on u.unid_id=t.unidad  \n");																
		strSQL.append("WHERE ue.unid_id = ? and ue.estado = ? and t.rut is not null");
//		MMA 20180718:	No debe devolver un registro rut=null
		
		
		
		ConsultaData dataFinal = null;
		try {
			Object[] params = {unidad, EstadoJefaUnidad.vigente.getValue()};
			ConsultaData data  = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params );
			if(data!= null && data.next()) {
				dataFinal = getTrabajadores(data.getInt("rut"), null, null, false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dataFinal;
	}
	

	@Override
	public ConsultaData getJefeUnidad(List<String> unidad) {
		throw new NotImplementedException();
	}

	public ConsultaData getJefeResponsableDeLaUnidad(String unidad) {
		
		ConsultaData dataJefatura = getJefeUnidad(unidad);
		
		if(dataJefatura != null && dataJefatura.next()) {
			dataJefatura.toStart();
			return dataJefatura;
		}
		else {
			ConsultaData dataPadre =  getUnidadesPadres(unidad);
			
			while(dataPadre != null &&  dataPadre.next()) {
				dataJefatura = getJefeUnidad(dataPadre.getString("unid_id"));
				if(dataJefatura != null && dataJefatura.next()) {
					dataJefatura.getActualData().put("pertenece_a_esta_unidad", 0);
					break;
				}
				dataPadre = getUnidadesPadres(dataPadre.getString("unid_id"));
			}	
		}
		
		if(dataJefatura != null) {
			dataJefatura.toStart();
		}
		return dataJefatura;
	}

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


	public ConsultaData getUnidadesPadres(String unidad) {
		StringBuilder sql = new StringBuilder();
		sql.append("	select u2.* \n");
		sql.append("	from eje_ges_unidades u inner join eje_ges_jerarquia j on u.unid_id = j.nodo_id \n"); 
		sql.append("	left outer join eje_ges_unidades u2 on u2.unid_id = j.nodo_padre  \n"); 
		sql.append("	where j.nodo_id = ?										\n");
		
		ConsultaData data = null;
		Object[] params = { unidad };
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params );
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
		
	}

	public ConsultaData getUnidadRaiz() {
		StringBuilder sql = new StringBuilder();
		sql.append("	select u.unid_empresa, u.unid_id, u.unid_desc, u.area, u.vigente, u.id_tipo, u.texto, dotacion_real=count(distinct rut) \n");
		sql.append("	from eje_ges_unidades u \n");
		sql.append("	inner join eje_ges_jerarquia j on u.unid_id = j.nodo_id  \n");
		sql.append("	left join eje_ges_trabajador t on t.unidad = u.unid_id  \n"); 
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
	
	
	public ConsultaData getUnidad(String unidad) {
		StringBuilder sql = new StringBuilder();
		sql.append("	select u.unid_empresa, u.unid_id, u.unid_desc, u.area, u.vigente, u.id_tipo, u.texto, dotacion_real=count(distinct rut) \n");
		sql.append("	from eje_ges_unidades u ");
		sql.append("		inner join eje_ges_jerarquia j on u.unid_id = j.nodo_id  \n"); 
		sql.append("	    left outer join eje_ges_trabajador t on t.unidad = u.unid_id \n");
		sql.append("	where u.unid_id = ? \n");
		sql.append("    group by u.unid_empresa, u.unid_id, u.unid_desc, u.area, u.vigente, u.id_tipo, u.texto ");

		ConsultaData data = null;
		Object[] params = { unidad };
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params );
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	
	public ConsultaData getUnidadFromRut(int rut) {
		StringBuilder sql = new StringBuilder();
		sql.append("	select u.* \n");
		sql.append("	from eje_ges_unidades u inner join eje_ges_jerarquia j on u.unid_id = j.nodo_id  \n");
		sql.append("							inner join eje_ges_trabajador t on t.unidad = u.unid_id  \n"); 
		sql.append("	where t.rut = ? \n");
		
		ConsultaData data = null;
		Object[] params = { rut };
		
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(), params );
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

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
	public ConsultaData getUnidades() {
		return getUnidades(null);
	}

	@Override
	public ConsultaData getUnidades(String strFiltro) {
		StringBuilder strSQL = new StringBuilder();
		strSQL.append("select u.unid_empresa, u.unid_id, u.unid_desc, u.area, u.vigente, u.id_tipo, u.texto, dotacion_real=count(distinct rut) \n")
		.append("from eje_ges_unidades u \n")
		.append("inner join eje_ges_jerarquia j on u.unid_id = j.nodo_id \n")
		.append("left outer join eje_ges_trabajador t on t.unidad = u.unid_id \n");
		if (strFiltro!=null) {
			if (strFiltro.replaceAll(" ", "")!=""){
	    		strSQL.append("where u.unid_id+' '+u.unid_desc like '%").append(strFiltro.replaceAll(" ", "%")).append("%' ");
	    	}
		}
		strSQL.append("group by u.unid_empresa, u.unid_id, u.unid_desc, u.area, u.vigente, u.id_tipo, u.texto ");

		ConsultaData data = null;
		Object[] params = {};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params );
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	
	@Override
	public boolean isJefe(int rut) {
		ConsultaData dataMiUnidad = getUnidadFromRut(rut);
		
		if(dataMiUnidad != null && dataMiUnidad.next()) {
			ConsultaData dataJefe = getJefeUnidad(dataMiUnidad.getString("unid_id"));
			
			if( dataJefe != null && dataJefe.next() ) {
				return dataJefe.getInt("rut") == rut; 
			}
		}
		
		return false;
	}

	@Override
	public ConsultaData getTrabajadores() {
		return getTrabajadores(0, null, null, false);
	}

	@Override
	public ConsultaData getTrabajadores(int intIdPersona) {
		return getTrabajadores(intIdPersona, null, null, false);
	}

	@Override
	public ConsultaData getTrabajadores(String strFiltro) {
		return getTrabajadores(0, null, strFiltro, false);
	}
	
	private ConsultaData getTrabajadores(int intIdPersona, String strUnidad, String strFiltro, boolean omiteJefe) {

		StringBuilder strSQL = new StringBuilder();
		String strInicFiltro = "and";
	
		strSQL.append("select id_persona=t.rut,\n");
		strSQL.append("t.rut,\n");
		strSQL.append("nif=cast(t.rut as varchar)+'-'+t.digito_ver,\n");
		strSQL.append("nombre=ltrim(rtrim(t.nombre)),\n");
		strSQL.append("persona=ltrim(rtrim(t.nombre)), \n")	;															
		strSQL.append("ape_paterno=ltrim(rtrim(t.ape_paterno)),\n");
		strSQL.append("ape_materno=ltrim(rtrim(t.ape_materno)),\n");
		strSQL.append("nombres=ltrim(rtrim(t.nombres)),\n");
		strSQL.append("sexo=ltrim(rtrim(t.sexo)),\n");
		strSQL.append("email=ltrim(rtrim(t.e_mail)), \n");
		strSQL.append("id_empresa=t.empresa, \n");
		strSQL.append("empresa=ltrim(rtrim(e.descrip)), \n");
		strSQL.append("u.unid_id,\n");
		strSQL.append("id_unidad=t.unidad,\n");
		strSQL.append("unidad=ltrim(rtrim(u.unid_desc)), \n");
		strSQL.append("unid_desc=ltrim(rtrim(u.unid_desc)), \n");			
		strSQL.append("id_cargo=t.cargo,\n");
		strSQL.append("cargo=ltrim(rtrim(c.descrip)), \n");
		strSQL.append("t.fecha_nacim, \n");	
		strSQL.append("fecha_ingreso=convert(varchar(10),t.fecha_ingreso,120),");
		strSQL.append("fecha_ingreso_date = t.fecha_ingreso, \n");
		strSQL.append("t.wp_cod_planta,\n");
		strSQL.append("e.compania, \n");
		strSQL.append("t.mail,\n");
		strSQL.append("t.e_mail, \n");
		strSQL.append("t.telefono, \n");
		strSQL.append("t.fec_ter_cont,\n");
		strSQL.append("id_encargado=ue.rut_encargado, \n");
		strSQL.append("encargado=ltrim(rtrim(te.nombre)), \n");
		strSQL.append("es_encargado=(case t.rut when ue.rut_encargado then 1 else 0 end), \n");
		strSQL.append("pertenece_a_esta_unidad=1 \n");
		strSQL.append("from eje_ges_trabajador t \n");						
		strSQL.append("left join eje_ges_empresa e on e.empresa=t.empresa \n");																
		strSQL.append("left join eje_ges_unidades u on u.unid_id=t.unidad  \n");																
		strSQL.append("left join eje_ges_cargos c on c.cargo=t.cargo and c.empresa=t.empresa \n");																
		strSQL.append("left join eje_ges_unidad_encargado ue on ue.unid_id=t.unidad and ue.rut_encargado=t.rut and ue.estado=1 \n");
		strSQL.append("left join eje_ges_trabajador te on te.rut=ue.rut_encargado \n");
		if (intIdPersona!=0) {
			strSQL.append("where t.rut=? ");
		}else if (strUnidad!=null) {
			strSQL.append("where t.unidad=? ");	
			if (omiteJefe) {
				strSQL.append("and ue.rut_encargado is null\n");	
			}
		}else {
			strSQL.append("where t.rut<>? ");
		}

		if (strFiltro!=null) {
	    	if (strFiltro.replaceAll(" ", "")!=""){
	    		strSQL.append("and cast(t.rut as varchar)+' '+t.nombre+' '+c.descrip+' '+u.unid_desc like '%").append(strFiltro.replaceAll(" ", "%")).append("%' ");
	    	}
		}
		
		strSQL.append("order by (case t.rut when ue.rut_encargado then 1 else 0 end) desc, ltrim(rtrim(t.nombre)) ");
		
		ConsultaData data = null;
		ArrayList<Object> params = new ArrayList<Object>();
		if (intIdPersona!=0) {
			params.add(intIdPersona);
		}else if (strUnidad!=null) {
			params.add(strUnidad);
		}else{
			params.add(0);
		}
		
		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
		
	}

	@Override
	public ConsultaData getUnidadesEncargadas(int intPersona) {
		return getUnidadesEncargadas(intPersona, null);
	}

	@Override
	public ConsultaData getUnidadesEncargadas(int intPersona, Integer intVigente) {
		
		StringBuilder strSQL = new StringBuilder();
		
		Object[] objParam = new Object[] {};
		ArrayList<Object> params = new ArrayList<Object>(Arrays.asList(objParam));
		
		params.add(intPersona);
		
		strSQL.append("select unid_id, estado \n")
		.append("from eje_ges_unidad_encargado \n")
		.append("where rut_encargado=? \n");
		if (intVigente!=null) {
			strSQL.append("and estado=? \n");
			params.add(intVigente);
		}
		strSQL.append("order by unid_id \n");

		ConsultaData data = null;
				
		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params.toArray());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}

	@Override
	public ICtrGOrganica getCtrG() {
		return new CtrGOrganica(this);
	}

	@Override
	public ConsultaData getUnidadesDescendientes(Connection connPortal, String unidad, boolean incluyeNoVigentes) {
		throw new NotImplementedException();
	}

	@Override
	public ConsultaData getUnidadesAscendientes(Connection connPortal, String unidad, boolean incluyeNoVigentes) {
		throw new NotImplementedException();
	}

	@Override
	public ConsultaData getUnidad(Connection connPortal, List<String> listaDeUnidId) {
		throw new NotImplementedException();
	}

	@Override
	public ConsultaData getUnidadesHijas(String unidad, boolean incluyeNoVigentes) {
		throw new NotImplementedException();
	}




}

