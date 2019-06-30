package portal.com.eje.portal.appcontext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.strings.SqlBuilder;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.appcontext.enums.EnumBeansConfigurationType;
import portal.com.eje.portal.factory.Util;

class BeansConfigurationRecognize {
	private Logger logger = Logger.getLogger(getClass());
	//evita que se registre mas de una vez el mismo tiempo el mismo masterBean
	private Map<String, Boolean> mapMasterBeans = new HashMap<String, Boolean>();
	
	public static BeansConfigurationRecognize getInstance() {
		return Util.getInstance(BeansConfigurationRecognize.class);
	}
	/**
	 * Retorna siempre un List<Bean> y dependiendo de EnumBeansConfigurationType retorna uno o más
	 * 
	 * @author Pancho
	 * @since 16-04-2019 
	 * */
	List<Object> getClaseImplementacion(List beansToRecognize, Class<?> masterBeanClass, String varName, EnumBeansConfigurationType type) {
		ConsultaData data = null;
		try {
			SqlBuilder sql = new SqlBuilder();
			List<Object> params = new LinkedList<Object>();
			
			sql.appendLine(" declare @setted varchar(150) ");
			sql.appendLine(" declare @id_type smallint ");
			sql.appendLine("  ");
			sql.appendLine("  set @id_type = (select id_type from eje_appconfig_element where clase = ? and var_name = ? and id_type = ?) ");
			sql.appendLine("  ");
			params.add(masterBeanClass.getCanonicalName());
			params.add(varName);
			params.add(type.getIdtype());
			
			sql.appendLine("  ");
			sql.appendLine(" if(@id_type = ").append(EnumBeansConfigurationType.SINGLE.getIdtypeStr()).append(")"); //single
			sql.appendLine(" 	begin ");
			sql.appendLine(" 		select top 1 i.clase ");
			sql.appendLine(" 		from eje_appconfig_element e ");
			sql.appendLine(" 			inner join eje_appconfig_impl i on e.id_clase = i.id_clase ");
			sql.appendLine(" 		where i.existe = 1 and i.setted = 1 and e.clase = ? and e.var_name = ? ");
			sql.appendLine(" 	end ");
			sql.appendLine(" ");
			sql.appendLine(" else if(@id_type = ").append(EnumBeansConfigurationType.MUTISELECT.getIdtypeStr()).append(")"); 
			sql.appendLine(" 	begin ");
			sql.appendLine(" 		select i.clase ");
			sql.appendLine(" 		from eje_appconfig_element e ");
			sql.appendLine(" 			inner join eje_appconfig_impl i on e.id_clase = i.id_clase ");
			sql.appendLine(" 		where i.existe = 1 and i.setted = 1 and e.clase = ? and e.var_name = ? ");
			sql.appendLine(" 	end ");
			sql.appendLine(" else ");
			sql.appendLine(" select clase=null ");
			
			params.add(masterBeanClass.getCanonicalName());
			params.add(varName);
			params.add(masterBeanClass.getCanonicalName());
			params.add(varName);
			
			data = ConsultaTool.getInstance().getData("portal", sql, params.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(type == EnumBeansConfigurationType.SINGLE) {
			String clase = ConsultaTool.getInstance().getFirstValue(data, "clase", String.class);
			Object retorno = null;
			for( Object t : beansToRecognize) {
				if(t.getClass().getCanonicalName().equals(clase)) {
					retorno = t.getClass().getCanonicalName();
					break;
				}
			}
				
			List<Object> cols = new ArrayList<Object>();
			cols.add(retorno);
			return cols;
		}
		else if(type == EnumBeansConfigurationType.MUTISELECT) {
			return ConsultaTool.getInstance().getList(data, ArrayList.class, "clase", String.class);
		}
		else {
			throw new NotImplementedException();
		}
		
	}
	
	private String getKey(Class<?> masterBean, String varName) {
		return masterBean.getCanonicalName().concat("_").concat(varName);
	}
	
	<T> void registraClasesDeConfiguracion(List<T> beansToRecognize, Class<?> masterBean, String varName, EnumBeansConfigurationType type) {
		String key = getKey(masterBean, varName);
		if(getInstance().mapMasterBeans.get(key)!= null) {
			return;
		}
		getInstance().mapMasterBeans.put(key, false);
		
		Cronometro cro = new Cronometro();
		cro.start();
		EModulos thisMod = EModulos.getThisModulo();
		
		if(beansToRecognize.size() == 0) {
			throw new IndexOutOfBoundsException( ("Debe haber más de 0 beans para a injectar la variable \"").concat(varName).concat("\" de (").concat(masterBean.getCanonicalName()).concat(")"));
		}
		SqlBuilder sql = new SqlBuilder();
		List<Object> params = new LinkedList<Object>();
		
		sql.appendLine(" declare @id_clase int ");
		
		sql.appendLine(" if(not exists(select 1 from eje_appconfig_element where emodulos = ? and clase = ? and var_name = ? )) ");
		sql.appendLine("	begin ");
		sql.appendLine("		insert into eje_appconfig_element (emodulos, maquina, fec_reconocimiento, clase, var_name, id_type) values (?,?,getDate(),?,?,?) ");
		sql.appendLine("		set @id_clase = (SELECT SCOPE_IDENTITY()) ");
		sql.appendLine("	end ");
		sql.appendLine("	else ");
		sql.appendLine("	begin ");
		sql.appendLine("		set @id_clase = (select id_clase from eje_appconfig_element where emodulos = ? and clase = ? and var_name = ? ) ");
		sql.appendLine("	end ");
		
		params.add(thisMod.toString());
		params.add(masterBean.getCanonicalName());
		params.add(varName);

		params.add(thisMod.toString());
		params.add(getMaquinaName());
		params.add(masterBean.getCanonicalName());
		params.add(varName);
		params.add(type.getIdtype());
		
		params.add(thisMod.toString());
		params.add(masterBean.getCanonicalName());
		params.add(varName);
		
		sql.appendLine(" update eje_appconfig_impl set existe = 0 where id_clase = @id_clase ");
		
		for( T t : beansToRecognize) {
			sql.appendLine(" if(not exists(select 1 from eje_appconfig_impl where id_clase = @id_clase and clase = ? )) ");
			sql.appendLine("	begin ");
			sql.appendLine("		insert into eje_appconfig_impl (id_clase, fec_reconocimiento, clase, existe, setted) values (@id_clase, getdate(), ?, 1, 0)");
			sql.appendLine("	end ");
			sql.appendLine(" else ");
			sql.appendLine("	begin ");
			sql.appendLine(" 		update eje_appconfig_impl set existe = 1 where id_clase = @id_clase and clase = ?  ");
			sql.appendLine("	end ");
			
			params.add(t.getClass().getCanonicalName());
			params.add(t.getClass().getCanonicalName());
			params.add(t.getClass().getCanonicalName());
		}
		
//		sql.appendLine(" declare @q_setted int");
//		sql.appendLine(" set @q_setted = isnull((select count(*) from eje_appconfig_impl where id_clase = @id_clase and setted = 1  ),0) ");
//
//		
//		sql.appendLine(" if(@q_setted == 0 ) ");
//		sql.appendLine(" 	begin ");
//		sql.appendLine(" 		update eje_appconfig_impl impl set setted = 1 ");
//		sql.appendLine(" 		where impl.id_clase = @id_clase and impl.setted = 0 and impl.existe = 1 ");
//		sql.appendLine(" 			and impl.id_corr in (select top 1 impl2.id_corr where impl2.id_clase = impl.id_clase existe = 1 and setted = 0) ");
//		sql.appendLine(" 	end ");
		
		try {
			ConsultaTool.getInstance().update("portal", sql, params.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//getInstance().mapMasterBeans.put(key, null);
		logger.debug("Configuración Reconocida "+masterBean.getCanonicalName()+" "+cro.getTimeHHMMSS_milli());
	}
	
	private String getMaquinaName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "No-reconocido";
		}
	}
}
