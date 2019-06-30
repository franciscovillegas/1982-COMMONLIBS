package portal.com.eje.cap.parametros;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Collection;

import cl.eje.model.generic.cap.Eje_cap_parametro;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.cap.parametros.CapParametro.CapParametroDefaultValue;
import portal.com.eje.cap.parametros.CapParametro.CapParametroGrupo;
import portal.com.eje.cap.parametros.CapParametro.CapParametroSubgrupo;
import portal.com.eje.cap.parametros.ifaces.ICapParametroTool;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.CtrGeneric;
import portal.com.eje.portal.vo.util.Wheres;

public class CapParametroTool implements ICapParametroTool {
 	
	public static ICapParametroTool getInstance() {
	 	return Util.getInstance(CapParametroTool.class);
	}
	
	@Override
	public boolean addParametro(CapParametroGrupo grupo, CapParametroSubgrupo sGrupo, String param) {
		boolean ok = false;
		
		if(grupo != null && sGrupo != null && param != null) {
			String sql = " INSERT INTO eje_cap_parametro (id_grupo, id_subgrupo, value, vigente, fecha_creacion) ";
			sql+= " values ( ? , ? , ? , ? , getdate()) ";
			
			Object[] params = {grupo.getId(), sGrupo.getId(), param, 1};
			
			
			try {
				ConsultaTool.getInstance().insert("portal", sql, params);
				ok = true;
			} catch (SQLException e) {
				e.printStackTrace();
				ok = false;
			}
		}
		
		return ok;
	}
	
	@Override
	public Collection<Eje_cap_parametro> getParametros(CapParametroGrupo grupo, String value, boolean vigente) {

		Collection<Eje_cap_parametro> params = null;
		try {

			Wheres w = Wheres.where("id_grupo", "=", grupo.getId()).and("value", "=", value);

			if (vigente) {
				w.and("vigente", "=", 1);
			}

			params = CtrGeneric.getInstance().getAllFromClass(Eje_cap_parametro.class, w.build());
		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		}

		return params;
	}

	@Override
	public Integer getIdParametroVigente() {
		Collection<Eje_cap_parametro> params = getParametros(CapParametroGrupo.VIGENCIA_GENERICA, CapParametroDefaultValue.VIGENTE.getValue(), true);

		Integer id = null;
		if (params != null && params.size() > 0) {
			id = params.iterator().next().getId_parametro();
		}

		return id;
	}
	
	@Override
	public Integer getIdParametroNoVigente() {
		Collection<Eje_cap_parametro> params = getParametros(CapParametroGrupo.VIGENCIA_GENERICA, CapParametroDefaultValue.NO_VIGENTE.getValue(), true);

		Integer id = null;
		if (params != null && params.size() > 0) {
			id = params.iterator().next().getId_parametro();
		}

		return id;
	}

	@Override
	public Integer getIdParametro(CapParametroGrupo grupo, String nemo) throws SQLException {
		Integer retorno = null;
		
		if(grupo != null && nemo != null) {
			String sql = " select id_parametro from eje_cap_parametro where id_grupo = ? and nemotecnico = ? ";
			Object[] params = {grupo.getId(), nemo};
			
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params );
			
			if( data != null && data.next() ){
				retorno = data.getInt("id_parametro");
			}
		}
		
		return retorno;
	 
	}
	
	@Override
	public EEstadoAcciones getEstadoFromIdAccion(int idAccion) {
		
		EEstadoAcciones e = null;
		
		try {
			String sql = " select p.nemotecnico ";
			sql += " from eje_cap_acciones a ";
			sql += " 	inner join eje_cap_parametro p on p.id_parametro = a.estado ";
			sql += " where a.id_accion = ? ";
			
			Object[] params = {idAccion};
			
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params );
			Field[] campos = EEstadoAcciones.class.getDeclaredFields();
			
			if( data != null && data.next() ){
				for(Field f : campos) {
					
					Enum.valueOf(EEstadoAcciones.class, f.getName());
					
					if(e.getNemo().equals(data.getForcedString("nemotecnico"))) {
						break;
					}
				}				
			}
		} catch (SQLException error) {
			error.printStackTrace();
		}
		
		return e;
	}
	
}
