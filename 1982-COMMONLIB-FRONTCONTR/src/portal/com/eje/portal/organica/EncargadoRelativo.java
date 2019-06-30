package portal.com.eje.portal.organica;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.strings.SqlBuilder;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.organica.ifaces.IEncargadoRelativo;

public class EncargadoRelativo implements IEncargadoRelativo {

	@Override
	public ConsultaData getTrabajadoresDependientes(int rut) {
		StringBuilder sqlFinal = new StringBuilder();
		
		StringBuilder sqlRuts = new StringBuilder();
		sqlRuts.append(" select t.rut,unid_id=t.unidad \n");
		sqlRuts.append(" from eje_ges_trabajador t \n");
		sqlRuts.append("	inner join dbo.getdescendientes(?) u on t.unidad = u.unidad \n");
		
		
		ConsultaData dataRuts = null;
		
		try {
			StringBuilder sqlUnidades = new StringBuilder();
			sqlUnidades.append(" select unidad from eje_ges_usuario_unidad where rut_usuario = ? and tipo = 'R' ");
			
			Object[] params = {rut};
			ConsultaData dataUnidad = ConsultaTool.getInstance().getData("portal", sqlUnidades.toString(), params);
			List<String> parametros = new ArrayList<String>();
			
			if(dataUnidad != null && dataUnidad.size() > 0) {
				while(dataUnidad.next()) {
					parametros.add(dataUnidad.getString("unidad"));
					
					if(!"".equals(sqlFinal.toString())) {
						sqlFinal.append("union \n");
					}
					sqlFinal.append(sqlRuts.toString());
				}
				
				dataRuts  = ConsultaTool.getInstance().getData("portal", sqlFinal.toString(), parametros.toArray());
			}
			else {
				parametros.add("-1");
				dataRuts  = ConsultaTool.getInstance().getData("portal", sqlRuts.toString(), parametros.toArray());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dataRuts;
	}
	
	/***/
	@Override
	public ConsultaData getUnidadesEncargadas(int intPersona, boolean incluyeUnidadesDescendientes) {
		
		StringBuilder strSQL = new StringBuilder();
		strSQL.append("select unid_id=unidad from eje_ges_usuario_unidad where rut_usuario = ? \n");

		ConsultaData data = null;
				
		try {
			Object[] params = {intPersona};
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params);
			
			if(incluyeUnidadesDescendientes && data != null && data.size() > 0) {
				StringBuilder sqlAll = new StringBuilder();
				List<String> listaParams = new ArrayList<String>();
				
				while(data.next()) {
					String unidad = data.getString("unid_id");
					
					if(!"".equals(sqlAll.toString())) {
						sqlAll.append("union \n");
					}
					sqlAll.append("SELECT unid_id=unidad FROM dbo.getdescendientes(?) \n");
					listaParams.add(unidad);
				}
				
				data = ConsultaTool.getInstance().getData("portal", sqlAll.toString(), listaParams.toArray());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	@Override
	public ConsultaData getJefaturas(String fromUnidad, boolean incluyeUnidadesDescendientes)  {
		SqlBuilder sql = new SqlBuilder();
		sql.appendLine(" select rut=u.rut_usuario, d.unidad ");
		sql.appendLine(" from dbo.GetDescendientes(?) d "); 
		sql.appendLine("	inner join eje_ges_usuario_unidad u on d.unidad = u.unidad ");
		
		List<Object> params = new LinkedList<Object>();
		params.add(fromUnidad == null? 0 : fromUnidad);
		
		if(!incluyeUnidadesDescendientes) {
			sql.appendLine("	d.unidad = ? ");	
			params.add(fromUnidad);
		}
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql,params.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
 
}
