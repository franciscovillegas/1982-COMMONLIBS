package cl.ejedigital.web.parametroconfig;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.reflect.ClaseConstructor;
import cl.ejedigital.tool.reflect.ClaseGenerica;
import cl.ejedigital.tool.singleton.Singleton;
import cl.ejedigital.web.datos.ConsultaTool;


public class ConfigParametroManager implements Singleton {
	private static ConfigParametroManager instance;
	
	private ConfigParametroManager() {

	}
	
	public static ConfigParametroManager getInstance() {
		if(instance == null) {
			synchronized(ConfigParametroManager.class) {
				if(instance == null) {
					instance = new ConfigParametroManager();
				}
			}
			
		}
		
		return instance;
	}
	
	public ConfigParametro getConfigParametro(ConfigParametroKey key) {
		final ConfigParametro p;
		
		try {
			p = getSoloConfigParametro( getGrupo(key.toString()),
								getOptions(key.toString()),
								getOptionType(key.toString()),
								key.toString());
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return p;
	}
	
	public Object getClaseConfigParametro(ConfigParametroKey key, Class[] definicion, Object[] ConfigParametros) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		ConfigParametro p = getConfigParametro(key);
 
		Object o = null;
		try {
			o = ClaseConstructor.getNew(p.getSelectedKey(),definicion,ConfigParametros);
		}
		catch (ClassNotFoundException e) {
			throw e;
		}
		catch (NoSuchMethodException e) {
			throw e;
		}
		catch (InstantiationException e) {
			throw e;
		}
		catch (IllegalAccessException e) {
			throw e;
		}
		catch (InvocationTargetException e) {
			throw e;
		}
		
		return o;
	}
	
	public ConsultaData getConfigParametros() throws SQLException {
		String strConsulta = " select id_parametro,def_option,selected_value,selected_key,e_on_change,id_grupo,id_tipo from eje_generico_prm_parametro ";
		String[] params = {};

		return ConsultaTool.getInstance().getData("portal",strConsulta,params);
	}
	
	private ConfigParametro getSoloConfigParametro(Grupo grupo, Options options, OptionType optionType,  String key) throws SQLException  {
		String sql = " select id_parametro,def_option,selected_value,selected_key,id_grupo, id_tipo from eje_generico_prm_parametro where id_parametro = ? ";
		String[] params = {key};

		ConsultaData data = ConsultaTool.getInstance().getData("portal",sql,params);
		ConfigParametro p = null;
		
		if(data.next()) {
			p =
				new ConfigParametro(grupo,options,optionType,
					data.getString("id_parametro"), String.valueOf(data.getInt("def_option")),
					data.getString("selected_value"),data.getString("selected_key"),
					data.getString("selected_key"),data.getInt("id_tipo"));
		}
		
		return p;
			
	}
	
	private Options getOptions(String keyConfigParametro) throws SQLException {
		String sql = " select op.id_corr,op.id_parametro,op.option_value,op.option_key from eje_generico_prm_option op where id_parametro = ? ";
		String[] params = {keyConfigParametro};
		
		ConsultaData data = ConsultaTool.getInstance().getData("portal",sql,params);
		Options options = new Options();
		Option o = null;
		
		while(data.next()) {
			o = new Option(data.getInt("id_corr"),data.getString("id_parametro"),
				data.getString("option_value"),data.getString("option_key"));
			options.add(o);
		}
		
		return options;
	
	}
	
	private Grupo getGrupo(String keyConfigParametro) throws SQLException {
		String sql = " select g.id_grupo,g.nombre from eje_generico_prm_grupo g inner join eje_generico_prm_parametro p on p.id_grupo = g.id_grupo where p.id_parametro = ? ";
		String[] params = {keyConfigParametro};
		
		ConsultaData data = ConsultaTool.getInstance().getData("portal",sql,params);
		
		Grupo g = null;
		
		if(data.next()) {
			g = new Grupo(String.valueOf(data.getInt("id_grupo")),data.getString("nombre"));
		}
		
		return g;
		
	}
	
	private OptionType getOptionType(String keyConfigParametro) throws SQLException {
		String sql = " select t.id_tipo, t.nombre from eje_generico_prm_option_type t inner join eje_generico_prm_parametro p on p.id_tipo = t.id_tipo where p.id_parametro = ?  ";
		String[] params = {keyConfigParametro};
		
		ConsultaData data = ConsultaTool.getInstance().getData("portal",sql,params);
		OptionType o = null;
		
		if(data.next()) {
			o = new OptionType(data.getInt("id_tipo"),data.getString("nombre"));
		}
		
		return o;
	}
	
}
