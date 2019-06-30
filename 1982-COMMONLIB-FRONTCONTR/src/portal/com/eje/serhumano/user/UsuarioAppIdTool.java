package portal.com.eje.serhumano.user;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataMode;
import cl.ejedigital.consultor.Row;
import cl.ejedigital.tool.strings.ArrayFactory;
import cl.ejedigital.tool.strings.SqlBuilder;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.Order;
import cl.ejedigital.web.datos.error.DuplicateKeyException;
import cl.ejedigital.web.datos.map.ConsultaDataMap;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.generico_appid.EnumAppId;
import portal.com.eje.serhumano.user.errors.AppIdsIsUsedException;
import portal.com.eje.tools.EnumTool;
import portal.com.eje.tools.deprecates.AssertWarn;

public class UsuarioAppIdTool {

	public static UsuarioAppIdTool getInstance() {
		return Util.getInstance(UsuarioAppIdTool.class);
	}

	@SuppressWarnings("unchecked")
	public Map<Integer, Map<EnumAppId, Boolean>> getAppIds() {
		return getAppIds(null, (List) null);
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, Map<EnumAppId, Boolean>> getAppIds(EnumAppId appid) {
		return getAppIds(appid, (List) null);
	}

	public  Map<EnumAppId, Boolean> getAppIds(Integer rut) {
		List<Integer> ruts = null;

		if (rut != null) {
			ruts = new ArrayList<>();
			ruts.add(rut);
		}

		return getAppIds(null, ruts).get(rut);
	}

	/**
	 * Ruts puede ser null
	 * 
	 * @author Pancho
	 * @since 17-05-2019
	 */
	public Map<Integer, Map<EnumAppId, Boolean>> getAppIds(EnumAppId appId, List<Integer> ruts) {

		Map<Integer, Map<EnumAppId, Boolean>> mapRuts = new HashMap<>();

		SqlBuilder sql = new SqlBuilder();
		sql.appendLine(" select m.rut, m.app_id ");
		sql.appendLine(" from ");
		sql.appendLine(" (select r.rut, app.app_id ");
		sql.appendLine(" from eje_generico_perfiles_webmatico p ");
		sql.appendLine(" 	inner join eje_generico_perfiles_rut_webmatico r on r.perfil = p.id ");
		sql.appendLine(" 	inner join eje_generico_webmatico_ejegesuserapp app on app.id_perfil = p.id ");
		sql.appendLine(" 	inner join eje_ges_usuario u on r.rut = u.login_usuario ");
		sql.appendLine(" 	inner join eje_ges_trabajador t on t.rut = r.rut ");
		sql.appendLine(" union ");
		sql.appendLine(" select t.rut, app.app_id ");
		sql.appendLine(" from eje_ges_user_app app ");
		sql.appendLine(" 	inner join eje_ges_usuario u on app.rut_usuario = u.login_usuario ");
		sql.appendLine(" 	inner join eje_ges_trabajador t on t.rut = app.rut_usuario) as m");
		sql.appendLine(" WHERE 1=1 ");
		
		List<Object> params = new ArrayList();
		if (ruts != null && ruts.size() > 0) {
			ArrayFactory af = new ArrayFactory();
			af.addAll(ruts);
			sql.appendLine(" and m.rut in ").append(af.getArrayInteger());
		}

		if(appId != null) {
			sql.appendLine(" and m.app_id = ? ");
			params.add(appId.name());
		}
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params.toArray());
			while (data != null && data.next()) {
				int rut = data.getInt("rut");
				String supuestoAppId = data.getString("app_id");
				EnumAppId enumAppId = EnumTool.getEnumByNameIngoreCase(EnumAppId.class, supuestoAppId, null);

				AssertWarn.isTrue(getClass(), enumAppId != null, "El app_id \"" + supuestoAppId + "\" no existe en EnumAppId");

				getAppsFromRut(mapRuts, rut).put(enumAppId, true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return mapRuts;
	}

	private Map<EnumAppId, Boolean> getAppsFromRut(Map<Integer, Map<EnumAppId, Boolean>> mapRuts, Integer rut) {
		if (mapRuts.get(rut) == null) {
			mapRuts.put(rut, new HashMap<EnumAppId, Boolean>());
		}
		return mapRuts.get(rut);
	}

	public Map<EnumAppId, Integer> getCountAsignaciones() throws SQLException {
		SqlBuilder sql = new SqlBuilder();
		sql.appendLine(" select x.app_id, q=SUM(x.q) ");
		sql.appendLine(" from( ");
		sql.appendLine(" 	Select app_id=upper(f.app_id), q=COUNT(*) ");
		sql.appendLine(" 	From eje_ges_user_app_func f ");
		sql.appendLine(" 	group by upper(f.app_id) ");
		sql.appendLine(" 	union ");
		sql.appendLine(" 	select distinct app_id=upper(app_id), q=COUNT(*) ");
		sql.appendLine(" 	from eje_ges_user_app ");
		sql.appendLine(" 	group by upper(app_id) ");
		sql.appendLine(" 	union ");
		sql.appendLine(" 	select distinct app_id=upper(app_id), q=COUNT(*) ");
		sql.appendLine(" 	from eje_generico_webmatico_ejegesuserapp ");
		sql.appendLine(" 	group by upper(app_id) ");

		sql.appendLine(" 	union ");
		sql.appendLine(" 	select distinct app_id=upper(app_id), q=COUNT(*) ");
		sql.appendLine(" 	from eje_ges_user_app_tree_app_id ");
		sql.appendLine(" 	group by upper(app_id) ");

		sql.appendLine(" 	) as x ");
		sql.appendLine(" group by x.app_id ");

		ConsultaData data = ConsultaTool.getInstance().getData("portal", sql.toString());
		Map<EnumAppId, Integer> map = new HashMap<EnumAppId, Integer>();

		if (data != null) {
			while (data != null && data.next()) {
				String app_id = data.getString("app_id");
				EnumAppId enumApp_id = EnumAppId.valueOf(app_id);

				if (enumApp_id != null) {
					map.put(enumApp_id, data.getInt("q"));
				}
			}
		}

		return map;
	}

	/**
	 * Retorna la lista completa de AppsIds existentes
	 * 
	 * @author Pancho
	 * @since 24-10-2018
	 */
	public ConsultaData getAppIds(boolean clearCache, boolean esconderAtributosDeprecados, String query) {
		List<EnumAppId> lista = EnumTool.getArrayList(EnumAppId.class);

		ConsultaData data = ConsultaTool.getInstance().newConsultaData(new String[] { "app_id", "descripcion", "deprecate" });
		for (EnumAppId app : lista) {

			boolean agregar = true;
			if (esconderAtributosDeprecados) {
				if (app.isDeprecate()) {
					agregar = false;
				}
			}

			if (agregar) {
				data.add(Row.column("app_id", app.toString()).add("descripcion", app.getDescripcion()).add("deprecate", app.isDeprecate()).build());
			}
		}

		if (!"".equals(query)) {
			data = ConsultaTool.getInstance().filtroPorColumnas(data, query, new String[] { "app_id", "descripcion" });
		}

		ConsultaTool.getInstance().sort(data, "app_id", Order.Ascendente);
		return data;
	}

	/**
	 * Retorna solo los app_id disponibles para perfilar
	 * 
	 * @author Pancho
	 * @since 24-10-2018
	 */
	public ConsultaData getAppsIdsDisponibilizadas(boolean clearCache, boolean esconderAtributosDeprecados, String query) throws SQLException, DuplicateKeyException {

		ConsultaData data = null;
		SqlBuilder sql = new SqlBuilder();

		sql.appendLine(" Select app_id=upper(f.app_id) ");
		sql.appendLine(" From eje_ges_user_app_func f "); // de las disponibilizadas
		sql.appendLine(" union ");
		sql.appendLine(" select distinct app_id=upper(app_id) ");
		sql.appendLine(" from eje_ges_user_app "); // de las asignaciones de atributos individuales
		sql.appendLine(" union ");
		sql.appendLine(" select distinct app_id=upper(app_id) ");
		sql.appendLine(" from eje_generico_webmatico_ejegesuserapp "); // de las asignaciones de atributos por perfiles

		ConsultaData dataDisponibilizadas = ConsultaTool.getInstance().getData("portal", sql);

		if (dataDisponibilizadas != null) {
			putDescripcion(dataDisponibilizadas);

			dataDisponibilizadas.setMode(ConsultaDataMode.CONVERSION);

			ConsultaDataMap<String> map = ConsultaTool.getInstance().getMapForce(dataDisponibilizadas, "app_id", String.class);

			List<EnumAppId> lista = EnumTool.getArrayList(EnumAppId.class);

			data = ConsultaTool.getInstance().newConsultaData(new String[] { "app_id" });
			for (EnumAppId app : lista) {
				if (map.getConsultaData(app.toString()) != null && map.getConsultaData(app.toString()).size() > 0) {
					boolean agregar = true;
					if (esconderAtributosDeprecados) {
						if (app.isDeprecate()) {
							agregar = false;
						}
					}

					if (agregar) {
						data.add(Row.column("app_id", app.toString()).add("descripcion", app.getDescripcion()).add("deprecate", app.isDeprecate()).build());
					}
				}
			}
		}

		if (!"".equals(query)) {
			data = ConsultaTool.getInstance().filtroPorColumnas(data, query, new String[] { "app_id", "descripcion" });
		}

		ConsultaTool.getInstance().sort(data, "app_id", Order.Ascendente);

		return data;
	}

	/**
	 * Elimina desde eje_ges_user_app, eje_ges_user_app_tree_app_id y
	 * eje_generico_webmatico_ejegesuserapp
	 * 
	 * @author Pancho
	 * @since 17-05-2019
	 */
	private void eliminaAppsIdDelSistema(EnumAppId appid) throws SQLException {
		SqlBuilder sql = new SqlBuilder();
		sql.appendLine(" delete from eje_ges_user_app where app_id = ? ");
		// sql.appendLine(" delete from eje_ges_user_app_func where app_id = ? ");
		sql.appendLine(" delete from eje_ges_user_app_tree_app_id where app_id = ? ");
		sql.appendLine(" delete from eje_generico_webmatico_ejegesuserapp where app_id = ? ");

		Object[] params = { appid.name(), appid.name(), appid.name() };
		ConsultaTool.getInstance().update("portal", sql, params);
	}

	/**
	 * Inserta la columna descripción en una ConsultaData que solo contiene la
	 * columna app_id
	 * 
	 * @author Pancho
	 * @since 30-11-2018
	 */
	private void putDescripcion(ConsultaData dataDisponibilizadas) {

		if (dataDisponibilizadas != null) {
			dataDisponibilizadas.getNombreColumnas().add("descripcion");

			int pos = dataDisponibilizadas.getPosition();
			dataDisponibilizadas.setMode(ConsultaDataMode.CONVERSION);

			while (dataDisponibilizadas != null && dataDisponibilizadas.next()) {
				String app_id = dataDisponibilizadas.getString("app_id");
				EnumAppId enumApp_id = EnumAppId.valueOf(app_id);

				if (enumApp_id != null) {
					dataDisponibilizadas.getActualData().put("descripcion", enumApp_id.getDescripcion());
				}

			}

			dataDisponibilizadas.setPosition(pos);
		}

	}

	/**
	 * Agrega app_ids a disponibles
	 */
	public boolean establecer(List<String> lista) throws SQLException {
		if (lista == null || lista.size() == 0) {
			return false;
		}

		List<EnumAppId> enums = EnumTool.getArrayList(EnumAppId.class);

		SqlBuilder sql = new SqlBuilder();
		sql.appendLine("if(not exists(select top 1 1 from eje_ges_user_app_func WHERE app_id = ? )) ");
		sql.appendLine("	BEGIN ");
		sql.appendLine("		INSERT INTO eje_ges_user_app_func (app_id, descripcion, orden, is_administrable) VALUES (?,?,1,1) ");
		sql.appendLine("	END");

		sql.appendLine("if(not exists(select top 1 1 from eje_ges_aplicacion WHERE app_id = ? )) ");
		sql.appendLine("	BEGIN ");
		sql.appendLine("		INSERT INTO eje_ges_aplicacion (app_id, app_desc, orden) VALUES (?,?,1) ");
		sql.appendLine("	END");

		List<Object> params = new ArrayList<Object>();
		boolean ok = true;
		for (EnumAppId app : enums) {
			if (lista.contains(app.toString())) {

				params.clear();
				params.add(app.getAppId());
				params.add(app.getAppId());
				params.add(Validar.getInstance().cutStringSinComillas(app.getDescripcion(), 100));
				params.add(app.getAppId());
				params.add(app.getAppId());
				params.add(Validar.getInstance().cutStringSinComillas(app.getDescripcion(), 70));

				ConsultaTool.getInstance().insert("portal", sql, params.toArray());
			}
		}

		return ok;
	}

	/**
	 * Quita app_ids de los disponibles
	 * 
	 * @throws SQLException
	 * @throws AppIdsIsUsedException
	 */
	public boolean quitar(List<String> lista, boolean forzar) throws SQLException, AppIdsIsUsedException {
		if (lista == null || lista.size() == 0) {
			return false;
		}

		Map<EnumAppId, Integer> mapExistentes = getCountAsignaciones();

		for (String app_id : lista) {
			EnumAppId enumAppId = EnumAppId.valueOf(app_id);

			if (enumAppId != null && mapExistentes.get(enumAppId) > 0) {
				if (forzar) {
					eliminaAppsIdDelSistema(enumAppId);
				} else {
					throw new AppIdsIsUsedException("El app_id " + app_id + " aún está asignada.");
				}

			} else {
				throw new NullPointerException("No existe el app_id " + app_id);
			}
		}

		List<EnumAppId> enums = EnumTool.getArrayList(EnumAppId.class);

		SqlBuilder sql = new SqlBuilder();
		List<Object> params = new ArrayList<Object>();

		for (EnumAppId app : enums) {
			if (lista.contains(app.toString())) {
				sql.appendLine("DELETE FROM eje_ges_user_app_func WHERE app_id = ? ");
				params.add(app.toString());
			}
		}

		ConsultaTool.getInstance().insert("portal", sql, params.toArray());

		return true;

	}

}
