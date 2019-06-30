package portal.com.eje.portal.trabajador;

import java.sql.SQLException;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.strings.ArrayFactory;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.sqlserver.SqlServerTool;
import portal.com.eje.portal.trabajador.enums.EjeGesTrabajadorField;
import portal.com.eje.portal.trabajador.ifaces.ITrabajadorInfoPersonal;
import portal.com.eje.usuario.UsuarioImagenManager;
import portal.com.eje.usuario.VoUsaurioImagen;
 
/**
 *  
 * */
public class TrabajadorInfoPersonal implements ITrabajadorInfoPersonal  {
	
	private TrabajadorInfoPersonal() {

	}
		
	/**
	 * Retorn la data de un trabajador, por favor, no hacerla más pesada
	 * 
	 * */
	public ConsultaData getData(ArrayFactory ruts, List<EjeGesTrabajadorField> fields, String filtro, ConsultaData orderBy) {
		ConsultaData data = null;
		
		//StringBuffer sql = new StringBuffer("select top ").append(strLimit).append(" rut, cast(rut as varchar) + '-' + digito_ver rutdv, ")
		StringBuilder sql = new StringBuilder();

		if(fields.contains(EjeGesTrabajadorField.rut)) { 
			// Pancho 20181030: si se pide el rut, automáticamente se generan los otros campos relacionados a rut
			appendSelect(sql,"nif=convert(varchar(10),rut)+'-'+digito_ver \n");
			appendSelect(sql,"id_persona=rut \n");
			appendSelect(sql,"rutdv=cast(rut as varchar) + '-' + digito_ver \n");
			appendSelect(sql,"rut \n");
			//appendSelect(sql,"id_tabtrab \n"); -- debe ser por EjeGesTrabajadorField
				
			boolean existe = SqlServerTool.getInstance().existColumn("portal", "eje_ges_trabajador", "id_tabtrab");
			if(existe) {
				appendSelect(sql,"id_tabtrab \n");
			}
		}
		
//		if(fields.contains(EjeGesTrabajadorField.id_tabtrab)) {
//			appendSelect(sql,"id_tabtrab \n"); 
//		}
		
		if(fields.contains(EjeGesTrabajadorField.digito_ver)) {
			appendSelect(sql,"digito_ver \n");
		}
		
//		if(fields.contains(EjeGesTrabajadorField.rut) && fields.contains(EjeGesTrabajadorField.digito_ver)) {
//			appendSelect(sql,"cast(rut as varchar) + '-' + digito_ver rutdv  \n");
//		}
		
		if(fields.contains(EjeGesTrabajadorField.nombre)) {
			appendSelect(sql,"rtrim(ltrim(nombre)) nombre \n");
		}
		
		if(fields.contains(EjeGesTrabajadorField.nombres)) {
			appendSelect(sql,"rtrim(ltrim(nombres)) nombres \n");
		}
		
		if(fields.contains(EjeGesTrabajadorField.ape_paterno)) {
			appendSelect(sql,"rtrim(ltrim(ape_paterno)) ape_paterno \n");
		}
		
		if(fields.contains(EjeGesTrabajadorField.ape_materno)) {
			appendSelect(sql,"rtrim(ltrim(ape_materno)) ape_materno \n");
		}
		
		if(fields.contains(EjeGesTrabajadorField.cargo)) {
			appendSelect(sql,"t.cargo");
			appendSelect(sql,"cargo_desc=rtrim(ltrim(c.descrip)) \n");
		}

		if(fields.contains(EjeGesTrabajadorField.fecha_ingreso)) {
			appendSelect(sql,"fecha_ingreso \n");
		}
		
		if(fields.contains(EjeGesTrabajadorField.unidad) || fields.contains(EjeGesTrabajadorField.unid_id)) {
			appendSelect(sql,"u.unid_id \n");
			appendSelect(sql,"u.unid_desc unidad \n");
			appendSelect(sql,"is_jefe=convert(smallint,isnull(ue.estado,0))  \n");
		}
		
		if(fields.contains(EjeGesTrabajadorField.wp_cod_empresa) || fields.contains(EjeGesTrabajadorField.empresa)) {
			appendSelect(sql,"t.empresa \n");
			appendSelect(sql,"empresa_desc=e.descrip \n");
		}
		
		if(fields.contains(EjeGesTrabajadorField.ccosto) ) {
			appendSelect(sql,"ccosto=t.ccosto\n");
			appendSelect(sql,"ccosto_desc=cc.descrip \n");
		}
		
		if(fields.contains(EjeGesTrabajadorField.forma_pago) ) {
			appendSelect(sql,"t.forma_pago, t.banco, t.cta_cte \n");
		}
		
		if (fields.contains(EjeGesTrabajadorField.imagen_path)) {
			appendSelect(sql,"imagen_path=null \n");
		}
		
		for(EjeGesTrabajadorField c: fields) {
			if(sql.toString().indexOf(c.name()) == -1) {
				appendSelect(sql,"t."+c.name() + "\n");	
			}
		}
		
		appendSelect(sql," fecha_traspaso_inc = null \n");	
		
		sql.append(" from eje_ges_trabajador t \n");
		
		if(fields.contains(EjeGesTrabajadorField.unidad) || fields.contains(EjeGesTrabajadorField.unid_id)) {
			sql.append(" 	left outer join eje_ges_jerarquia j on t.unidad=j.nodo_id \n");
			sql.append(" 	left outer join eje_ges_unidades u on t.unidad=u.unid_id \n");
			sql.append(" 	left outer join eje_ges_unidad_encargado ue on ue.unid_id = t.unidad and ue.rut_encargado = t.rut and ue.estado = 1 \n");
		}
		
		if(fields.contains(EjeGesTrabajadorField.cargo)) {
			sql.append(" 	left outer join eje_ges_cargos c on t.cargo=c.cargo and t.wp_cod_empresa=c.empresa \n");
		}
		
		if(fields.contains(EjeGesTrabajadorField.wp_cod_empresa) || fields.contains(EjeGesTrabajadorField.empresa)) {
			sql.append(" 	left outer join eje_ges_empresa e on t.empresa = e.empresa \n");
		}
		
		if(fields.contains(EjeGesTrabajadorField.ccosto) ) {
			sql.append(" 	left outer join eje_ges_centro_costo cc on t.ccosto = cc.centro_costo and cc.vigente = 's' and cc.wp_cod_empresa = t.wp_cod_empresa and cc.wp_cod_planta = t.wp_cod_planta \n");
		}
		
		sql.append(" where 1=1 ");
		
		if(ruts != null  ) {
			sql.append(" and t.rut in ").append(ruts.getArrayInteger()).append("\n");	
		}
		
		
		if(filtro != null){
			filtro = filtro.replaceAll("  ", " ").replaceAll(" ", "%");//espacio blanco
			sql.append(" and (t.nombre like('%").append(filtro).append("%') \n");
			sql.append("     or convert(varchar(10),rut) like('%").append(filtro).append("%') \n");
			sql.append("     ) \n");
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
				sql.append(orderBy.getString("property")).append(" ").append(orderBy.getString("direction") );
			}
		}

		try {
			data = ConsultaTool.getInstance().getData("portal","select distinct "+sql.toString());
			if (fields.contains(EjeGesTrabajadorField.imagen_path)) {
				while (data!=null && data.next()) {
					UsuarioImagenManager ui = new UsuarioImagenManager();
					VoUsaurioImagen voImagen = ui.getImagen(data.getInt("id_persona"));
					data.getActualData().put("imagen_path", voImagen.getNameUnic());
				}
				data.toStart();
			}
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
	public ConsultaData getData(Integer rut, List<EjeGesTrabajadorField> fields) {
		ArrayFactory ruts = null;
		
		if(rut != null) {
			ruts = new ArrayFactory();
			ruts.add(rut);
		}
		
		return getData(ruts, fields , null, null);
	}

	@Override
	public ConsultaData getData(ArrayFactory ruts, List<EjeGesTrabajadorField> fields) {
		return getData(ruts, fields , null, null);
	}

	@Override
	public ConsultaData getData(ArrayFactory ruts, List<EjeGesTrabajadorField> fields, String filtro) {
		return getData(ruts, fields , filtro, null);
	}
}
