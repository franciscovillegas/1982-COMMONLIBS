package portal.com.eje.portal.trabajador;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.strings.ArrayFactory;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.sqlserver.SqlServerTool;
import portal.com.eje.portal.trabajador.enums.EjeGesTrabajadorHistoriaField;
import portal.com.eje.portal.trabajador.ifaces.ITrabajadorInfoPersonalHistoria;
 
/**
 *  
 * */
public class TrabajadorInfoPersonalHistoria implements ITrabajadorInfoPersonalHistoria  {
	
	private TrabajadorInfoPersonalHistoria() {

	}
		
	/**
	 * Retorn la data de un trabajador, por favor, no hacerla más pesada
	 * 
	 * */
	public ConsultaData getData(ArrayFactory ruts, List<EjeGesTrabajadorHistoriaField> fields, ConsultaData orderBy, Integer periodo) {
		ConsultaData data = null;
		
		//StringBuffer sql = new StringBuffer("select top ").append(strLimit).append(" rut, cast(rut as varchar) + '-' + digito_ver rutdv, ")
		StringBuilder sql = new StringBuilder();

		if(fields.contains(EjeGesTrabajadorHistoriaField.rut)) {
			appendSelect(sql,"rut \n");
			// //debe ser por medio de EjeGesTrabajadorField
			boolean existe = SqlServerTool.getInstance().existColumn("portal", "eje_ges_trabajador_historia", "id_tabtrabh");
			if(existe) {
				appendSelect(sql,"id_tabtrabh \n");
			}
		}
		
//		if(fields.contains(EjeGesTrabajadorHistoriaField.id_tabtrabh)) {
//			appendSelect(sql,"id_tabtrabh \n");
//		}
		
		if(fields.contains(EjeGesTrabajadorHistoriaField.digito_ver)) {
			appendSelect(sql,"digito_ver \n");
		}
		
		if(fields.contains(EjeGesTrabajadorHistoriaField.rut) && fields.contains(EjeGesTrabajadorHistoriaField.digito_ver)) {
			appendSelect(sql,"cast(rut as varchar) + '-' + digito_ver rutdv  \n");
		}
		
		if(fields.contains(EjeGesTrabajadorHistoriaField.nombre)) {
			appendSelect(sql,"rtrim(ltrim(nombre)) nombre \n");
		}
		
		if(fields.contains(EjeGesTrabajadorHistoriaField.nombres)) {
			appendSelect(sql,"rtrim(ltrim(nombres)) nombres \n");
		}
		
		if(fields.contains(EjeGesTrabajadorHistoriaField.ape_paterno)) {
			appendSelect(sql,"rtrim(ltrim(ape_paterno)) ape_paterno \n");
		}
		
		if(fields.contains(EjeGesTrabajadorHistoriaField.ape_materno)) {
			appendSelect(sql,"rtrim(ltrim(ape_materno)) ape_materno \n");
		}
		
		if(fields.contains(EjeGesTrabajadorHistoriaField.cargo)) {
			appendSelect(sql,"t.cargo");
			appendSelect(sql,"cargo_desc=rtrim(ltrim(c.descrip)) \n");
		}

		if(fields.contains(EjeGesTrabajadorHistoriaField.fecha_ingreso)) {
			appendSelect(sql,"fecha_ingreso \n");
		}
		
		if(fields.contains(EjeGesTrabajadorHistoriaField.unidad) || fields.contains(EjeGesTrabajadorHistoriaField.unid_id)) {
			appendSelect(sql,"u.unid_id \n");
			appendSelect(sql,"u.unid_desc unidad \n");
			appendSelect(sql,"is_jefe=convert(smallint,isnull(ue.estado,0))  \n");
		}
		
		if(fields.contains(EjeGesTrabajadorHistoriaField.wp_cod_empresa) || fields.contains(EjeGesTrabajadorHistoriaField.empresa)) {
			appendSelect(sql,"t.empresa \n");
			appendSelect(sql,"empresa_desc=e.descrip \n");
		}
		
		if(fields.contains(EjeGesTrabajadorHistoriaField.ccosto) ) {
			appendSelect(sql,"ccosto=t.ccosto\n");
			appendSelect(sql,"ccosto_desc=cc.descrip \n");
		}
		
		appendSelect(sql,"fecha_traspaso_inc \n");
		
		
		sql.append(" from eje_ges_trabajador_historia t \n");
		
		if(fields.contains(EjeGesTrabajadorHistoriaField.unidad) || fields.contains(EjeGesTrabajadorHistoriaField.unid_id)) {
			sql.append(" 	left outer join eje_ges_jerarquia j on t.unidad=j.nodo_id \n");
			sql.append(" 	left outer join eje_ges_unidades u on t.unidad=u.unid_id \n");
			sql.append(" 	left outer join eje_ges_unidad_encargado ue on ue.unid_id = t.unidad and ue.rut_encargado = t.rut and ue.estado = 1 \n");
		}
		
		if(fields.contains(EjeGesTrabajadorHistoriaField.cargo)) {
			sql.append(" 	left outer join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa \n");
		}
		
		if(fields.contains(EjeGesTrabajadorHistoriaField.wp_cod_empresa) || fields.contains(EjeGesTrabajadorHistoriaField.empresa)) {
			sql.append(" 	left outer join eje_ges_empresa e on t.empresa = e.empresa \n");
		}
		
		if(fields.contains(EjeGesTrabajadorHistoriaField.ccosto) ) {
			sql.append(" 	left outer join eje_ges_centro_costo cc on t.ccosto = cc.centro_costo and cc.vigente = 's' and cc.wp_cod_empresa = t.wp_cod_empresa and cc.wp_cod_planta = t.wp_cod_planta \n");
		}
		
		sql.append(" WHERE periodo = ").append(periodo);
		
		if(ruts != null  ) {
			sql.append(" and t.rut in ").append(ruts.getArrayInteger()).append("\n");	
		}

		
		if(orderBy != null && orderBy.size() > 0) {
			sql.append("order by ");
			boolean first = true;
			while(orderBy.next()) {
				if(first) {
					first = false;
				}else{
					sql.append(",");
				}
				sql.append(orderBy.getString("property")).append(orderBy.getString("direction") );
			}
		}

		
		
		try {
			
			data = ConsultaTool.getInstance().getData("portal","SELECT DISTINCT "+sql.toString());
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	private void appendSelect(StringBuilder sqlFinal, String sql) {
		if(sqlFinal != null && sql != null) {
			if(!"".equals(sqlFinal.toString())) {
				sqlFinal.append(",");
			}
			sqlFinal.append(sql);
		}
	}

	@Override
	public List<Integer> getPeriodos(ArrayFactory ruts) {
		String sql = "select distinct periodo from eje_ges_trabajador_historia where rut in "+ruts.getArrayInteger()+" order by periodo desc";
		
		List<Integer> lista = new ArrayList<Integer>();
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql);
			lista = ConsultaTool.getInstance().getList(data, ArrayList.class, "periodo", Integer.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista;
	}

	@Override
	public List<Integer> getPeriodos(int rut) {
		ArrayFactory ruts = new ArrayFactory();
		ruts.add(rut);
		return getPeriodos(ruts);
	}

	@Override
	public ConsultaData getData(int rut, List<EjeGesTrabajadorHistoriaField> fields, Integer periodo) {
		ArrayFactory ruts = new ArrayFactory();
		ruts.add(rut);
		
		return getData(ruts, fields, null, periodo);
	}

}
