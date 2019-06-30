package portal.com.eje.portal.perfapp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.gson.Gson;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.PPMException;
import portal.com.eje.portal.parametro.ParametroLocator;
import portal.com.eje.portal.parametro.ParametroValue;
import portal.com.eje.portal.trabajador.VoPersona;

public class PerfApp implements IPerfApp {

	private static IPerfApp instance;
	public static IPerfApp getInstance() {
		if(instance == null) {
			synchronized (PerfApp.class) {
				if(instance == null) {
					instance = new PerfApp();
				}
			}
		}
		return instance;
	}
	
	@Override
	public boolean getPerfAppActivo() {
		
		boolean bolActivo = false;
		
		try {
			ParametroLocator.getInstance().ifNotExistThenBuildParametro(EModulos.getThisModulo(), "perfapp", "activo", "False");
			ParametroValue pv = ParametroLocator.getInstance().getValor(EModulos.getThisModulo(), "perfapp", "activo");
			if (pv!=null) {
				bolActivo = "True".equals(pv.getValue());
			}
		} catch (PPMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bolActivo;
	}
	
	@Override
	public List<voZona> getZona() {
		return getZona(null);
	}

	@Override
	public List<voZona> getZona(VoPersona persona) {
		
		StringBuilder strSQL = new StringBuilder();

		List<voZona> zonas = new ArrayList<voZona>();
		
		if (persona==null) {
			strSQL.append("select id_pazona, codigo, nombre \n")
			.append("from eje_perfapp_zona \n");
		}else {
			strSQL.append("select distinct z.id_pazona, z.codigo, z.nombre \n")
			.append("from eje_perfapp_zona z \n")
			.append("inner join eje_perfapp_perfil_objeto o on o.id_pazona=z.id_pazona \n")
			.append("inner join eje_perfapp_perfil_persona pp on pp.id_paperfil=o.id_paperfil \n")
			.append("where pp.id_persona=").append(persona.getId()).append(" \n");
		}
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
			while (data!=null && data.next()) {
				voZona zona = new voZona(data.getInt("id_pazona"), data.getString("codigo"), data.getString("nombre"));
				if (persona!=null) {
					zona.setAplicaciones(getAplicacion(zona, persona));
					zona.setGrupos(getGrupo(zona, persona));
				}
				zonas.add(zona);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return zonas;
		
	}
	
	@Override
	public boolean updZona(voZona zona) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<voAplicacion> getAplicacion(voZona zona) {
		return getAplicacion(zona, null);
	}

	@Override
	public List<voAplicacion> getAplicacion(voZona zona, VoPersona persona) {
		
		StringBuilder strSQL = new StringBuilder();
		
		List<voAplicacion> aplicaicones = new ArrayList<voAplicacion>();
		
		if (persona==null) {
			strSQL.append("select a.id_paaplicacion, a.codigo, a.nombre \n")
			.append("from eje_perfapp_aplicacion a \n")
			.append("where a.id_pazona=3 and a.habilitado=1 \n");
		}else {
			strSQL.append("select distinct a.id_paaplicacion, a.codigo, a.nombre \n")
			.append("from eje_perfapp_aplicacion a \n")
			.append("inner join eje_perfapp_perfil_modulo m on m.id_paaplicacion=a.id_paaplicacion \n")
			.append("inner join eje_perfapp_perfil_persona pp on pp.id_paperfil=m.id_paperfil \n")
			.append("where m.id_pazona=").append(zona.getId()).append(" and pp.id_persona=").append(persona.getId()).append(" and a.habilitado=1 \n");
		}
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
			while (data!=null && data.next()) {
				voAplicacion aplicacion = new voAplicacion(data.getInt("id_paaplicacion"), zona, data.getString("codigo"), data.getString("nombre"));
				if (persona!=null) {
					aplicacion.setModulos(getModulo(aplicacion, persona));
				}
				aplicaicones.add(aplicacion);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return aplicaicones;
	}
	
	
	@Override
	public boolean updAplicacion(voAplicacion aplicacion) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<voGrupo> getGrupo(voZona zona) {
		return getGrupo(zona, null);
	}

	@Override
	public List<voGrupo> getGrupo(voZona zona, VoPersona persona) {
		
		StringBuilder strSQL = new StringBuilder();

		List<voGrupo> grupos = new ArrayList<voGrupo>();

		if (persona==null) {
			strSQL.append("select g.id_pagrupo, g.codigo, g.nombre \n")
			.append("from eje_perfapp_grupo g \n")
			.append("inner join eje_perfapp_zona_grupo zg on zg.id_pagrupo=g.id_pagrupo \n")
			.append("where zg.id_pazona=").append(zona.getId()).append(" \n");
		}else {
			strSQL.append("select distinct g.id_pagrupo, g.codigo, g.nombre \n")
			.append("from eje_perfapp_grupo g \n")
			.append("inner join eje_perfapp_perfil_objeto o on o.id_pagrupo=g.id_pagrupo \n")
			.append("inner join eje_perfapp_perfil_persona pp on pp.id_paperfil=o.id_paperfil \n")
			.append("where o.id_pazona=").append(zona.getId()).append(" and pp.id_persona=").append(persona.getId()).append(" \n");			
		}

		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
			while (data!=null && data.next()) {
				voGrupo grupo = new voGrupo(data.getInt("id_pagrupo"), zona, data.getString("codigo"), data.getString("nombre"));
				if (persona!=null) {
					grupo.setObjetos(getObjeto(grupo, persona));
				}
				grupos.add(grupo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return grupos;
		
	}
	
	
	@Override
	public boolean updGrupo(voGrupo grupo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<voModulo> getModulo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<voModulo> getModulo(voAplicacion aplicacion) {
		return getModulo(aplicacion, null);
	}

	@Override
	public List<voModulo> getModulo(voAplicacion aplicacion, VoPersona persona) {
	
		StringBuilder strSQL = new StringBuilder();
		
		List<voModulo> modulos = new ArrayList<voModulo>();
		
		if (persona==null) {
			strSQL.append("select m.id_pamodulo, m.codigo, m.nombre \n")
			.append("from eje_perfapp_modulo m \n")
			.append("inner join eje_perfapp_aplicacion a on a.id_paaplicacion=m.id_paaplicacion \n")
			.append("where a.id_pazona=").append(aplicacion.getZona().getId()).append(" and m.id_paaplicacion=").append(aplicacion.getId()).append(" and m.habilitado=1 \n");
		}else {
			strSQL.append("select distinct m.id_pamodulo, m.codigo, m.nombre \n")
			.append("from eje_perfapp_modulo m \n")
			.append("inner join eje_perfapp_perfil_modulo pm on pm.id_paaplicacion=m.id_paaplicacion and pm.id_pamodulo=m.id_pamodulo \n")
			.append("inner join eje_perfapp_perfil_persona pp on pp.id_paperfil=pm.id_paperfil \n")
			.append("where pm.id_pazona=").append(aplicacion.getZona().getId()).append(" and pm.id_paaplicacion=").append(aplicacion.getId()).append(" and pp.id_persona=").append(persona.getId()).append(" and m.habilitado=1 \n");
		}
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
			while (data!=null && data.next()) {
				voModulo modulo = new voModulo(data.getInt("id_pamodulo"), aplicacion, data.getString("codigo"), data.getString("nombre"), true);
				modulos.add(modulo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return modulos;
		
	}
	
	@Override
	public boolean updModulo(voModulo modulo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<voObjeto> getObjeto() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<voObjeto> getObjeto(voGrupo grupo) {
		return getObjeto(grupo, null);
	}

	@Override
	public List<voObjeto> getObjeto(voGrupo grupo, VoPersona persona){
		
		StringBuilder strSQL = new StringBuilder();

		List<voObjeto> objetos = new ArrayList<voObjeto>();

		if (persona==null) {
			strSQL.append("select o.id_paobjeto, o.codigo, o.nombre \n")
			.append("from eje_perfapp_objeto o \n")
			.append("inner join eje_perfapp_grupo_objeto gb on gb.id_paobjeto=o.id_paobjeto \n")
			.append("inner join eje_perfapp_zona_grupo zg on zg.id_pazonagrupo=gb.id_pazonagrupo \n")
			.append("where zg.id_pazona=").append(grupo.getZona().getId()).append(" and zg.id_pagrupo=").append(grupo.getId()).append(" \n");
		} else {
			strSQL.append("select distinct o.id_paobjeto, o.codigo, o.nombre \n")
			.append("from eje_perfapp_objeto o \n")
			.append("inner join eje_perfapp_perfil_objeto po on po.id_paobjeto=o.id_paobjeto \n")
			.append("inner join eje_perfapp_perfil_persona pp on pp.id_paperfil=po.id_paperfil \n")
			.append("where po.id_pazona=").append(grupo.getZona().getId()).append(" and po.id_pagrupo=").append(grupo.getId()).append(" and pp.id_persona=").append(persona.getId()).append(" \n");
		}
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
			while (data!=null && data.next()) {
				voObjeto objeto = new voObjeto(data.getInt("id_paobjeto"), grupo, data.getString("codigo"), data.getString("nombre"), true);
				objetos.add(objeto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return objetos;
		
	}
	
	
	@Override
	public boolean updObjeto(voObjeto objeto) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<voPerfil> getPerfil() {
		return getPerfil(false);
	}

	@Override
	public List<voPerfil> getPerfil(boolean bolConMatriz) {
	
		StringBuilder strSQL = new StringBuilder();
		
		List<voPerfil> perfiles = new ArrayList<voPerfil>();
		strSQL.append("select p.id_paperfil, p.nombre, pp.ctd_usuarios, p.vigencia_desde, p.vigencia_hasta, p.por_defecto \n")
		.append("from eje_perfapp_perfil p \n")
		.append("left join (select id_paperfil, ctd_usuarios=count(id_persona) from eje_perfapp_perfil_persona group by id_paperfil) pp on pp.id_paperfil=p.id_paperfil ")
		.append("order by p.nombre \n");
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
			while (data!=null && data.next()) {
				voPerfil perfil = new voPerfil(data.getInt("id_paperfil"), data.getString("nombre"), data.getInt("ctd_usuarios"), data.getDateJava("vigencia_desde"), data.getDateJava("vigencia_hasta"), (data.getInt("por_defecto"))==1);
				
				JSonDataOut dtapersonas = new JSonDataOut(getDtaPersonasDelPerfil(perfil));
				perfil.setJsonPersonas("[" + dtapersonas.getListData()+ "]");

				JSonDataOut dtauserapp = new JSonDataOut(getDtaUserApp(perfil, null));
				perfil.setJsonUserApp("[" + dtauserapp.getListData()+ "]");
				
				JSonDataOut dtaautoservicio = new JSonDataOut(getDtaAutoservicio(perfil));
				perfil.setJsonAutoservicio("[" + dtaautoservicio.getListData()+ "]");
				
				JSonDataOut dtaobjeto = new JSonDataOut(getDtaObjeto(perfil));
				perfil.setJsonObjeto("[" + dtaobjeto.getListData()+ "]");

				JSonDataOut dtamodulo = new JSonDataOut(getDtaModulos(perfil));
				perfil.setJsonModulo("[" + dtamodulo.getListData()+ "]");
				
				if (bolConMatriz) {
					List<voMatriz> matriz = getMatriz();
					for (voMatriz record: matriz) {
						record.setActivo(getMatrizActiva(data.getInt("id_paperfil"), record));
						perfil.setMatriz(matriz);
					}
				}
				perfiles.add(perfil);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return perfiles;
		
	}
	
	public voPerfil getPerfilPorDefecto() {
		
		StringBuilder strSQL = new StringBuilder();

		voPerfil perfil = null;
		
		strSQL.append("select id_paperfil  \n")
		.append("from eje_perfapp_perfil \n")
		.append("where por_defecto=1 \n");

		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
			while (data!=null && data.next()) {
				perfil = new voPerfil(data.getInt("id_paperfil"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return perfil;
		
	}
	
	private boolean getMatrizActiva(int id_perfil, voMatriz matriz) {
		
		StringBuilder strSQL = new StringBuilder();
		
		boolean bolActivo = false;
		
		strSQL.append("declare @id_perfil int, @id_zona int, @id_grupo int, @id_objeto int \n")
		
		.append("set @id_perfil = ? \n")
		.append("set @id_zona = ? \n")
		.append("set @id_grupo = ? \n")
		.append("set @id_objeto = ? \n\n")
		
		.append("select activo=sum(activo) \n")
		.append("from ( \n")
		.append("	select activo=count(*) \n")
		.append("	from eje_perfapp_perfil_objeto \n")
		.append("	where id_paperfil= @id_perfil and id_pazona=@id_zona and id_pagrupo=@id_grupo and id_paobjeto=@id_objeto \n")
		.append("	union select activo=count(*) \n")
		.append("	from eje_perfapp_perfil_modulo \n")
		.append("	where id_paperfil= @id_perfil and id_pazona=@id_zona and id_paaplicacion=@id_grupo and id_pamodulo=@id_objeto \n")
		.append(") x");
		
		Object[] params = {id_perfil, matriz.getZona().getId(), matriz.getGrupo().getId(), matriz.getObjeto().getId()};
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params);
			while (data!=null && data.next()) {
				bolActivo = (!"0".equals(data.getForcedString("activo")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bolActivo;
	}
	
	@Override
	public List<voPerfil> getPerfilObjeto() {

		List<voPerfil> perfiles = new ArrayList<voPerfil>();
		
		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select id_paperfil, id_pazona, id_pagrupo, id_paobjeto \n")
		.append("from eje_perfapp_perfil_objeto \n")
		.append("order by id_paperfil, id_pazona, id_pagrupo, id_paobjeto \n");
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
			while (data!=null && data.next()) {
				voZona zona = new voZona(data.getInt("id_pazona"), null, null);
				voGrupo grupo = new voGrupo(data.getInt("id_pagrupo"), null, null, null, null);
				voObjeto objeto = new voObjeto(data.getInt("id_paobjeto"), null, null, null, true);
				List<voMatriz> matriz = new ArrayList<voMatriz>();
				matriz.add(new voMatriz(zona, grupo, objeto));
				voPerfil perfil = new voPerfil(data.getInt("id_paperfil"), null, 0, null, null, false);
				perfil.setMatriz(matriz);
				perfiles.add(perfil);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return perfiles;
		
	}
	
	@Override
	public List<voPerfil> getPerfilesDePersona(VoPersona persona) {
		
		List<voPerfil> perfiles = new ArrayList<voPerfil>();
	
		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select pp.id_paperfil, p.nombre \n")
		.append("from eje_perfapp_perfil_persona pp \n")
		.append("inner join eje_perfapp_perfil p on p.id_paperfil=pp.id_paperfil \n")
		.append("where pp.id_persona=").append(persona.getId()).append(" \n")
		.append("order by p.nombre \n");
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
			while (data!=null && data.next()) {
				perfiles.add(new voPerfil(data.getInt("id_paperfil")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return perfiles;
	
	}
	
	@Override
	public boolean updPerfil(voPerfil perfil) {

		StringBuilder strSQL = new StringBuilder();
		
		boolean ok = true;
		
		List<VoPersona> personas = new ArrayList<VoPersona>();
		List<voAutoservicio> autoservicio = new ArrayList<voAutoservicio>();
		List<voUserApp> userapps = new ArrayList<voUserApp>();
		List<voMatriz> objetos = new ArrayList<voMatriz>();
		List<voMatriz> modulos = new ArrayList<voMatriz>();
		
		try {
			if (perfil.getId()==0) {
				strSQL.append("insert into eje_perfapp_perfil (nombre, vigencia_desde, vigencia_hasta, por_defecto) values(?,?,?,?,?)");
				Object[] params = {perfil.strGetNombre(), perfil.getVigenciaDesde(), perfil.getVigenciaHasta(), perfil.getPorDefecto()};
				double dblIdPerfil = ConsultaTool.getInstance().insertIdentity("portal", strSQL.toString(), params);
				perfil.setId((int)dblIdPerfil);
			}else {
				strSQL.append("update eje_perfapp_perfil set nombre=?, vigencia_desde=?, vigencia_hasta=?, por_defecto=? where id_paperfil=?");
				Object[] params = {perfil.strGetNombre(), perfil.getVigenciaDesde(), perfil.getVigenciaHasta(), perfil.getPorDefecto(), perfil.getId()};
				ConsultaTool.getInstance().update("portal", strSQL.toString(), params);
			}
			List<Map> dtaPersonas = new Gson().fromJson(StringEscapeUtils.unescapeJavaScript(perfil.getJsonPersonas()), List.class);
			for(Map persona : dtaPersonas) {
				personas.add(new VoPersona((int)Validar.getInstance().validarDouble(persona.get("id_persona").toString()), null, null, null));
			}
			ok = updPersonasDelPerfil(perfil, personas);
			
			if (ok) {
				List<Map> dtaAutoservicio = new Gson().fromJson(StringEscapeUtils.unescapeJavaScript(perfil.getJsonAutoservicio()), List.class);
				for(Map paautoservicio : dtaAutoservicio) {
					if (Boolean.parseBoolean(paautoservicio.get("activo").toString())) {
							autoservicio.add(new voAutoservicio((int)Validar.getInstance().validarDouble(paautoservicio.get("id_autoservicio").toString()), true));
					}
				}
				ok = updAutoserviciosDelPerfil(perfil, autoservicio);
			}
			if (ok) {
				List<Map> dtaUserApps = new Gson().fromJson(StringEscapeUtils.unescapeJavaScript(perfil.getJsonUserApp()), List.class);
				for(Map userapp : dtaUserApps) {
					if (Boolean.parseBoolean(userapp.get("activo").toString())) {
						userapps.add(new voUserApp((int)Validar.getInstance().validarDouble(userapp.get("id_userapp").toString()), "true".equals(userapp.get("activo").toString())));
					}
				}
				ok = updUserAppsDelPerfil(perfil, userapps);
			}
			if (ok) {
				List<Map> dtaObjetos = new Gson().fromJson(StringEscapeUtils.unescapeJavaScript(perfil.getJsonObjeto()), List.class);
				for(Map matriz : dtaObjetos) {
					voZona zona = new voZona((int)Validar.getInstance().validarDouble(matriz.get("id_zona").toString()));
					voGrupo grupo = new voGrupo((int)Validar.getInstance().validarDouble(matriz.get("id_grupo").toString()));
					voObjeto objeto = new voObjeto((int)Validar.getInstance().validarDouble(matriz.get("id_objeto").toString()), true);
					objetos.add(new voMatriz(zona, grupo, objeto));
				}
				
				ok = updObjetosDelPerfil(perfil, objetos);
				
			}
			if (ok) {
				List<Map> dtaMosulos = new Gson().fromJson(StringEscapeUtils.unescapeJavaScript(perfil.getJsonModulo()), List.class);
				for(Map matriz : dtaMosulos) {
					voZona zona = new voZona((int)Validar.getInstance().validarDouble(matriz.get("id_zona").toString()));
					voGrupo grupo = new voGrupo((int)Validar.getInstance().validarDouble(matriz.get("id_aplicacion").toString()));
					voObjeto objeto = new voObjeto((int)Validar.getInstance().validarDouble(matriz.get("id_modulo").toString()), true);
					modulos.add(new voMatriz(zona, grupo, objeto));
				}
				
				ok = updModulosDelPerfil(perfil, modulos);
				
			}
			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ok = false;
		}

		return ok;
	}

	@Override
	public boolean updPerfil(List<voPerfil> perfiles) {

		boolean ok = true;
		
		for(voPerfil perfil: perfiles) {
			if (ok) {
				ok = updPerfil(perfil);
			}
		}
		
		return ok;
	}

	@Override
	public List<VoPersona> getPersonasDelPerfil(voPerfil perfil) {

		List<VoPersona> personas = new ArrayList<VoPersona>();

		ConsultaData data = getDtaPersonasDelPerfil(perfil);
		while (data!=null && data.next()) {
			personas.add(new VoPersona(data.getInt("id_persona")));
		}
		
		return personas;
		
	}
	
	private ConsultaData getDtaPersonasDelPerfil(voPerfil perfil) {

		StringBuilder strSQL = new StringBuilder();
		
		ConsultaData data = null;

		strSQL.append("select pp.id_persona, nombre=ltrim(rtrim(t.nombre)), cargo=ltrim(rtrim(c.descrip)), unidad=ltrim(rtrim(u.unid_desc)) \n")
		.append("from eje_perfapp_perfil_persona pp \n")
		.append("inner join eje_ges_trabajador t on t.rut=pp.id_persona \n")
		.append("left join eje_ges_cargos c on c.empresa=t.empresa and c.cargo=t.cargo \n")
		.append("left join eje_ges_unidades u on u.unid_id=t.unidad \n")
		.append("where pp.id_paperfil=? \n")
		.append("order by t.nombre \n");
		
		Object[] params = {perfil.getId()};
		
		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
		
	}
	
	@Override
	public boolean updPersonasDelPerfil(voPerfil perfil, List<VoPersona> personas) {
		
		StringBuilder strSQL = new StringBuilder();
		
		boolean ok = false;
		
		strSQL.append("declare @id_perfil int \n\n")
		
		.append("set @id_perfil=").append(perfil.getId()).append(" \n\n")
		
		.append("declare @data table (id_persona int) \n");
		for (VoPersona persona: personas) {
			strSQL.append("insert into @data values (").append(persona.getId()).append(") \n");
		}
		strSQL.append("\n")
		.append("delete pp \n")
		.append("from eje_perfapp_perfil_persona pp \n")
		.append("left join @data t on t.id_persona=pp.id_persona \n")
		.append("where pp.id_paperfil=@id_perfil and t.id_persona is null \n\n")
		
		.append("insert into eje_perfapp_perfil_persona (id_paperfil, id_persona) \n")
		.append("select id_paperfil=@id_perfil, t.id_persona \n")
		.append("from @data t \n")
		.append("left join eje_perfapp_perfil_persona pp on pp.id_paperfil=@id_perfil and pp.id_persona=t.id_persona \n")
		.append("where pp.id_persona is null \n\n")
		
		.append("select resultado='ok' \n");
		
		try {
			ConsultaTool.getInstance().getData("portal", strSQL.toString());
			ok = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ok;
		
	}
	
	@Override
	public List<voUserApp> getUserApp() {
		return getUserApp(null);
	}
	
	@Override
	public List<voUserApp> getUserApp(voPerfil perfil) {
		return getUserApp(perfil, null);
	}
	
	@Override
	public List<voUserApp> getUserApp(voPerfil perfil, Boolean bolActivo){
		
		List<voUserApp> userapps = new ArrayList<voUserApp>();

		ConsultaData data = getDtaUserApp(perfil, bolActivo);
		while (data!=null && data.next()) {
			boolean bolActTmp = true;
			if (bolActivo==null) {
				bolActTmp = (data.getInt("activo")==1);
			}
			userapps.add(new voUserApp(data.getInt("id_userapp"), data.getString("codigo"), data.getString("nombre"), bolActTmp));
		}
		
		return userapps;
		
	}
	
	
	@Override
	public List<voUserApp> getUserAppsDePersona(VoPersona persona) {
		
		StringBuilder strSQL = new StringBuilder();
		
		List<voUserApp> userapps = new ArrayList<voUserApp>();
		List<voPerfil> perfiles = getPerfilesDePersona(persona);
		
		Object[] objParam = new Object[] {};
		ArrayList<Object> params = new ArrayList<Object>(Arrays.asList(objParam));
		
		strSQL.append("declare @userapp table (id_pauserapp int, codigo varchar(max), nombre varchar(max)) \n"); 
		
		if (perfiles.size()==0) {
			perfiles.add(getPerfilPorDefecto());
		}
		for (voPerfil perfil: perfiles) {
			for (voUserApp userapp: getUserApp(perfil, true)) {
				strSQL.append("insert into @userapp values (?,?,?) \n");
				params.add(userapp.getId());
				params.add(userapp.getCodigo());
				params.add(userapp.getNombre());
			}
		}

		strSQL.append("\n")
		.append("select distinct id_pauserapp, codigo, nombre from @userapp \n");

		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString(), params.toArray());
			while (data!=null && data.next()) {
				userapps.add(new voUserApp(data.getInt("id_pauserapp"), data.getForcedString("codigo"), data.getForcedString("nombre")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userapps;
		
	}
	
	private ConsultaData getDtaUserApp(voPerfil perfil, Boolean bolActivo) {
		
		StringBuilder strSQL = new StringBuilder();
		
		ConsultaData data = null;
		
		int intIdPerfil = 0;
		if (perfil!=null) {
			intIdPerfil = perfil.getId();
		}
		
		strSQL.append("select id_userapp=ua.id_pauserapp, ua.codigo, ua.nombre");
		if (bolActivo==null) {
			strSQL.append(", activo=coalesce(pua.activo,'false') \n");	
		}else {
			strSQL.append(" \n");
		}
		strSQL.append("from eje_perfapp_userapp ua \n");
		if (bolActivo==null) {
			strSQL.append("left join (select id_pauserapp, activo='true' from eje_perfapp_perfil_userapp where id_paperfil=").append(intIdPerfil).append(") pua on pua.id_pauserapp=ua.id_pauserapp \n");
		}else if (bolActivo){
			strSQL.append("inner join eje_perfapp_perfil_userapp pua on pua.id_pauserapp=ua.id_pauserapp \n")
			.append("where pua.id_paperfil=").append(intIdPerfil).append(" \n");
		}else if (!bolActivo) {
			strSQL.append("where pua.activo is null \n");
		}
		strSQL.append("order by ua.nombre \n");
		
		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
	@Override
	public boolean updUserAppsDelPerfil(voPerfil perfil, List<voUserApp> userapps) {

		StringBuilder strSQL = new StringBuilder();
		
		boolean ok = false;
		
		strSQL.append("declare @id_perfil int \n\n")
		
		.append("set @id_perfil=").append(perfil.getId()).append(" \n\n")
		
		.append("declare @data table (id_userapp int, activo smallint) \n");
		for (voUserApp userapp: userapps) {
			int intActivo = 0;
			if (userapp.getActivo()) {
				intActivo = 1;
			}
			strSQL.append("insert into @data values (").append(userapp.getId()).append(",").append(intActivo).append(") \n");
		}
		strSQL.append("\n")
		.append("delete pp \n")
		.append("from eje_perfapp_perfil_userapp pp \n")
		.append("inner join @data t on t.id_userapp=pp.id_pauserapp \n")
		.append("where pp.id_paperfil=@id_perfil and t.activo=0 \n\n")
		
		.append("delete pp \n")
		.append("from eje_perfapp_perfil_userapp pp \n")
		.append("left join @data t on t.id_userapp=pp.id_pauserapp \n")
		.append("where pp.id_paperfil=@id_perfil and t.id_userapp is null \n\n")
		
		.append("insert into eje_perfapp_perfil_userapp (id_paperfil, id_pauserapp) \n")
		.append("select id_paperfil=@id_perfil, t.id_userapp \n")
		.append("from @data t \n")
		.append("left join eje_perfapp_perfil_userapp pp on pp.id_paperfil=@id_perfil and pp.id_pauserapp=t.id_userapp \n")
		.append("where pp.id_pauserapp is null and t.activo=1 \n\n")
		
		.append("select resultado='ok' \n");
		
		try {
			ConsultaTool.getInstance().getData("portal", strSQL.toString());
			ok = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ok;
		
	}
	
	public boolean updObjetosDelPerfil(voPerfil perfil, List<voMatriz> objetos) {

		StringBuilder strSQL = new StringBuilder();
		
		boolean ok = false;
		
		strSQL.append("declare @id_perfil int \n\n")
		
		.append("set @id_perfil=").append(perfil.getId()).append(" \n\n")
		
		.append("declare @data table (id_perfil int, id_zona int, id_grupo int, id_objeto int) \n");
		for (voMatriz objeto: objetos) {
			strSQL.append("insert into @data values (")
			.append(perfil.getId()).append(", ")
			.append(objeto.getZona().getId()).append(", ")
			.append(objeto.getGrupo().getId()).append(", ")
			.append(objeto.getObjeto().getId()).append(") \n");
		}
		strSQL.append(" \n")
		.append("delete po \n")
		.append("from eje_perfapp_perfil_objeto po \n")
		.append("inner join @data t on t.id_perfil=po.id_paperfil and t.id_zona=po.id_pazona and t.id_grupo=po.id_pagrupo and t.id_objeto=po.id_paobjeto \n")
		.append("where t.id_perfil is null \n\n")
		
		.append("insert into eje_perfapp_perfil_objeto (id_paperfil, id_pazona, id_pagrupo, id_paobjeto) \n")
		.append("select t.id_perfil, t.id_zona, t.id_grupo, t.id_objeto \n")
		.append("from @data t \n")
		.append("left join eje_perfapp_perfil_objeto po on po.id_paperfil=t.id_perfil and po.id_pazona=t.id_zona and po.id_pagrupo=t.id_grupo and po.id_paobjeto=t.id_objeto \n")
		.append("where po.id_paperfil is null \n\n")

		.append("select resultado='ok' \n");
		
		try {
			ConsultaTool.getInstance().getData("portal", strSQL.toString());
			ok = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ok;
		
	}
	
	public boolean updModulosDelPerfil(voPerfil perfil, List<voMatriz> objetos) {

		StringBuilder strSQL = new StringBuilder();
		
		boolean ok = false;
		
		strSQL.append("declare @id_perfil int \n\n")
		
		.append("set @id_perfil=").append(perfil.getId()).append(" \n\n")
		
		.append("declare @data table (id_perfil int, id_zona int, id_aplicacion int, id_modulo int) \n");
		for (voMatriz objeto: objetos) {
			strSQL.append("insert into @data values (")
			.append(perfil.getId()).append(", ")
			.append(objeto.getZona().getId()).append(", ")
			.append(objeto.getGrupo().getId()).append(", ")
			.append(objeto.getObjeto().getId()).append(") \n");
		}
		strSQL.append(" \n")
		.append("delete pm \n")
		.append("from eje_perfapp_perfil_modulo pm \n")
		.append("inner join @data t on t.id_perfil=pm.id_paperfil and t.id_zona=pm.id_pazona and t.id_aplicacion=pm.id_paaplicacion and t.id_modulo=pm.id_pamodulo \n")
		.append("where t.id_perfil is null \n\n")
		
		.append("insert into eje_perfapp_perfil_modulo (id_paperfil, id_pazona, id_paaplicacion, id_pamodulo) \n")
		.append("select t.id_perfil, t.id_zona, t.id_aplicacion, t.id_modulo \n")
		.append("from @data t \n")
		.append("left join eje_perfapp_perfil_modulo pm on pm.id_paperfil=t.id_perfil and pm.id_pazona=t.id_zona and pm.id_paaplicacion=t.id_aplicacion and pm.id_pamodulo=t.id_modulo \n")
		.append("where pm.id_paperfil is null \n")

		.append("select resultado='ok' \n");
		
		try {
			ConsultaTool.getInstance().getData("portal", strSQL.toString());
			ok = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ok;
		
	}
	
	@Override
	public List<voAutoservicio> getAutoservicio() {
		return getAutoservicio(null);
	}
	
	@Override
	public List<voAutoservicio> getAutoservicio(voPerfil perfil) {

		List<voAutoservicio> autoservicio = new ArrayList<voAutoservicio>();

		ConsultaData data = getDtaAutoservicio(perfil);
		while (data!=null && data.next()) {
			autoservicio.add(new voAutoservicio(data.getInt("id_autoservicio"), data.getInt("orden"), data.getString("codigo"), data.getString("nombre"), data.getString("imagen"), data.getString("backgroundcolor"), data.getInt("height"), data.getInt("width"), data.getString("path"), (data.getInt("activo")==1)));
		}

		return autoservicio;
	
	}
	
	private ConsultaData getDtaAutoservicio(voPerfil perfil) {
		
		StringBuilder strSQL = new StringBuilder();
		
		ConsultaData data = null;
		
		int intIdPerfil = 0;
		if (perfil!=null) {
			intIdPerfil = perfil.getId();
		}
		
		strSQL.append("select id_autoservicio=a.id_paautoservicio, a.orden, a.codigo, a.nombre, a.imagen, a.backgroundcolor, a.height, a.width, a.path, \n")
		.append("activo=(case when a.codigo in ('foto','info') then 'true' else coalesce(pa.activo,'false') end) \n")
		.append("from eje_perfapp_autoservicio a \n")
		.append("left join (select id_paautoservicio, activo='true' from eje_perfapp_perfil_autoservicio where id_paperfil=").append(intIdPerfil).append(") pa on pa.id_paautoservicio=a.id_paautoservicio  \n")
		.append("order by a.orden \n");
		
		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
		
	}
	
	private ConsultaData getDtaObjeto(voPerfil perfil) {
		
		StringBuilder strSQL = new StringBuilder();
		
		ConsultaData data = null;
		
		int intIdPerfil = 0;
		if (perfil!=null) {
			intIdPerfil = perfil.getId();
		}
		
		strSQL.append("select id_zona=id_pazona, id_grupo=id_pagrupo, id_objeto=id_paobjeto \n")
		.append("from eje_perfapp_perfil_objeto \n")
		.append("where id_paperfil=").append(intIdPerfil).append(" \n\n")
		.append("order by id_pazona, id_pagrupo, id_paobjeto \n");
		
		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
		
	}
	
	private ConsultaData getDtaModulos(voPerfil perfil) {
		
		StringBuilder strSQL = new StringBuilder();
		
		ConsultaData data = null;
		
		int intIdPerfil = 0;
		if (perfil!=null) {
			intIdPerfil = perfil.getId();
		}
		
		strSQL.append("select id_zona=id_pazona, id_aplicacion=id_paaplicacion, id_modulo=id_pamodulo \n")
		.append("from eje_perfapp_perfil_modulo \n")
		.append("where id_paperfil=").append(intIdPerfil).append(" \n")
		.append("order by id_pazona, id_paaplicacion, id_pamodulo \n");
		
		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
		
	}
	
	@Override
	public boolean updAutoserviciosDelPerfil(voPerfil perfil, List<voAutoservicio> autoservicio) {

		StringBuilder strSQL = new StringBuilder();
		
		boolean ok = false;
		
		strSQL.append("declare @id_perfil int \n\n")
		
		.append("set @id_perfil=").append(perfil.getId()).append(" \n\n")
		
		.append("declare @data table (id_autoservicio int, activo smallint) \n");
		for (voAutoservicio autoserv: autoservicio) {
			int intActivo = 0;
			if (autoserv.getActivo()) {
				intActivo = 1;
			}
			strSQL.append("insert into @data values (").append(autoserv.getId()).append(",").append(intActivo).append(") \n");
		}
		strSQL.append("\n")
		.append("delete pp \n")
		.append("from eje_perfapp_perfil_autoservicio pp \n")
		.append("inner join @data t on t.id_autoservicio=pp.id_paautoservicio \n")
		.append("where pp.id_paperfil=@id_perfil and t.activo=0 \n\n")
		
		.append("delete pp \n")
		.append("from eje_perfapp_perfil_autoservicio pp \n")
		.append("left join @data t on t.id_autoservicio=pp.id_paautoservicio \n")
		.append("where pp.id_paperfil=@id_perfil and t.id_autoservicio is null \n\n")
		
		.append("insert into eje_perfapp_perfil_autoservicio (id_paperfil, id_paautoservicio) \n")
		.append("select id_paperfil=@id_perfil, t.id_autoservicio \n")
		.append("from @data t \n")
		.append("left join eje_perfapp_perfil_autoservicio pp on pp.id_paperfil=@id_perfil and pp.id_paautoservicio=t.id_autoservicio \n")
		.append("where pp.id_paautoservicio is null and t.activo=1 \n\n")
		
		.append("select resultado='ok' \n");
		
		try {
			ConsultaTool.getInstance().getData("portal", strSQL.toString());
			ok = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ok;
		
	}
	
	@Override
	public List<voMatriz> getMatriz() {

		StringBuilder strSQL = new StringBuilder();
		
		List<voMatriz> matriz = new ArrayList<voMatriz>();
	
		strSQL.append("select tipo=1, o.orden, zg.id_pazona, zona=z.nombre, zonacodigo=z.codigo, zg.id_pagrupo, grupo=g.nombre, grupocodigo=g.codigo, o.id_paobjeto, objeto=o.nombre, objetocodigo=o.codigo \n")
		.append("from eje_perfapp_objeto o \n")
		.append("inner join eje_perfapp_grupo_objeto gb on gb.id_paobjeto=o.id_paobjeto \n")
		.append("inner join eje_perfapp_zona_grupo zg on zg.id_pazonagrupo=gb.id_pazonagrupo \n")
		.append("inner join eje_perfapp_zona z on z.id_pazona=zg.id_pazona \n")
		.append("inner join eje_perfapp_grupo g on g.id_pagrupo=zg.id_pagrupo \n\n")
		
		.append("union select tipo=2, orden=0, a.id_pazona, zona=z.nombre, zonacodigo=z.codigo, id_pagrupo=a.id_paaplicacion, grupo=a.nombre, grupocodigo=a.codigo, id_paobjeto=m.id_pamodulo, objeto=m.nombre, objetocodigo=m.codigo \n")
		.append("from eje_perfapp_modulo m \n")
		.append("inner join eje_perfapp_aplicacion a on a.id_paaplicacion=m.id_paaplicacion \n")
		.append("inner join eje_perfapp_zona z on z.id_pazona=a.id_pazona \n")
		.append("where a.habilitado=1 and m.habilitado=1 \n\n")
		
		.append("order by tipo, id_pazona, id_pagrupo, orden, id_paobjeto \n");
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
			while (data!=null && data.next()) {
				voZona zona = new voZona(data.getInt("id_pazona"), data.getString("zonacodigo"), data.getString("zona"));
				voGrupo grupo = new voGrupo(data.getInt("id_pagrupo"), null, data.getString("grupocodigo"), data.getString("grupo"));
				voObjeto objeto = new voObjeto(data.getInt("id_paobjeto"), null, data.getString("objetocodigo"), data.getString("objeto"), true);
				matriz.add(new voMatriz(zona, grupo, objeto));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return matriz;
	
	}
	
	@Override
	public List<voMatriz> getMatriz(VoPersona persona) {

		List<voMatriz> matriz = new ArrayList<voMatriz>();
	
		List<voZona> zonas = getZona(persona);
		for(voZona zona: zonas) {
			zona.setGrupos(getGrupo(zona, persona));
			zona.setAplicaciones(getAplicacion(zona, persona));
			for(voGrupo grupo: zona.getGrupos()) {
				grupo.setZona(zona);
				grupo.setObjetos(getObjeto(grupo, persona));
			}
			for (voAplicacion aplicacion: zona.getAplicaciones()) {
				aplicacion.setZona(zona);
				aplicacion.setModulos(getModulo(aplicacion, persona));
			}
		}
		
		return matriz;
	}

}
