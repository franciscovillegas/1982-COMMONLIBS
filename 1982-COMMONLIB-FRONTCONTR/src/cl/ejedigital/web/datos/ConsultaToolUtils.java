package cl.ejedigital.web.datos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataPage;
import cl.ejedigital.consultor.ISenchaPage;
import cl.ejedigital.tool.singleton.Singleton;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.ClaseConversor;
 
 
public class ConsultaToolUtils implements Singleton {	
	
	public static ConsultaToolUtils getIntance() {
		 
		return Util.getInstance(ConsultaToolUtils.class);
	}
	
	/**
	 * Retorna la query y los parámetros a ejecutar <br/>
	 * Retorno StringSql: la query a ejecutar<br/>
	 * Retorno StringOriginal: la query original<br/>
	 * Retorno StringSqlCount: la query para obtener la cantidad máxima de registros<br/>
	 * Retorno Object[]: los parametros nuevos para ejecutar StringSql, puede que no sea necesario paginar así que los objetos no serán modificados<br/>
	 * Retorno Boolean: indica si fue posible obtener un método de paginado<br/>
	 * 
	 * @author Pancho
	 * @throws SQLException 
	 * @since 10-08-2018
	 * */
	Map<String,Object> getPaginaValues(Object connOrJndi, String sqlOriginal,  List<String> pkList, ISenchaPage page, Object[] params) throws SQLException {
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(connOrJndi != null && 
				(String.class.isAssignableFrom( connOrJndi.getClass() ) || Connection.class.isAssignableFrom( connOrJndi.getClass() ) ) 
				&& sqlOriginal != null && page != null && page.getLimit() != null  && pkList != null && pkList.size() > 0) {

			sqlOriginal = sqlOriginal.toLowerCase();
			
			boolean estaOrdenado = sqlOriginal.indexOf("order by") > 0;
			boolean tieneJoins = sqlOriginal.indexOf(" join ") > 0;
			boolean tieneSubConsultas = sqlOriginal.split(" from ").length > 2 ;
			boolean tieneHavings = sqlOriginal.indexOf(" having ") != -1;
			boolean tieneTops = sqlOriginal.indexOf(" top ") != -1;
			boolean tieneWhere = sqlOriginal.indexOf(" where ") != -1;
			
			if(tieneTops) {
				throw new SQLException("No se permite paginado con sentencias TOP ");
			}
			
			StringBuilder sql = new StringBuilder();
			 
			String selectTop = utilPutSelect(sqlOriginal, "SELECT TOP 100 PERCENT ");
					
			if( estaOrdenado // esta ordnado
					|| tieneJoins // tiene anidamientos
					|| tieneSubConsultas // tiene subconsultas
					|| tieneHavings ) { // tiene havings 
					
				sql.append(" SELECT TOP ").append(page.getLimit()).append(" *\n");
				sql.append(" FROM (SELECT TOP 100 PERCENT * \n");
				sql.append(" 	   FROM  \n");
				sql.append("			(").append(selectTop).append(") as superq0 \n");
				sql.append(" ").append(buildSort("superq0", page.getSorts())).append(" ) as superq \n");
				sql.append(" WHERE NOT ").append(utilGetFieldForSelectCompare("superq.", pkList)).append(" IN \n");
				sql.append(" 		(SELECT TOP ").append(page.getStart()).append(" ").append(utilGetFieldForSelectCompare("superq2.", pkList)).append(" FROM \n");
				sql.append(" 														(").append( selectTop).append(") as superq2 \n");
				sql.append(" 		").append(buildSort("superq2", page.getSorts())).append(") \n");
				sql.append(" ").append(buildSort( null, page.getSorts())).append("  \n");
			}
			else {
				String[] partes = sqlOriginal.split(" from ");
				//String campos = partes[0].substring(7, partes[0].length());
				
				StringBuilder subTop = new StringBuilder();
				subTop.append("SELECT TOP ").append(page.getStart()).append(" ").append(utilGetFieldForSelectCompare("", pkList)).append("\n");
				subTop.append(" FROM ").append(partes[1]).append("\n");
				subTop.append(" ").append(buildSort("", page.getSorts())).append("\n");
 
				sql.append(" SELECT TOP ").append(page.getLimit()).append(" * \n");
				sql.append(" FROM (").append(selectTop).append(") as superq0 \n");
				sql.append(" WHERE NOT ").append(utilGetFieldForSelectCompare("superq.", pkList)).append(" IN \n");
				sql.append(" 		( ").append(subTop.toString()).append("\n");
				sql.append(" 		) ");
				sql.append(buildSort("superq0", page.getSorts()));
			}
			
			params = utilDuplicaParametros(params);
			
			StringBuilder sqlCount = new StringBuilder();
			sqlCount.append("select total_count=count(1) from ("+selectTop+") as superq "); 
			
			String jndi    = ConsultaToolUtils.getIntance().getJndi(connOrJndi);
			String catalog = ConsultaToolUtils.getIntance().getCatalog(connOrJndi);
			
			map.put("StringCatalog", catalog);
			map.put("StringJndi", jndi);
			map.put("StringSql", sql.toString());
			map.put("StringOriginal", sqlOriginal);
			map.put("StringSqlCount", sqlCount.toString());
			map.put("Object[]", params);
			map.put("BooleanPagged", true);
			map.put("BooleanSort", page.getSorts() != null && page.getSorts().size() > 0);
		}
		else if(page != null && page.getSorts() != null)  {
			String selectTop = utilPutSelect(sqlOriginal, "SELECT TOP 100 PERCENT ");
			
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM (").append(selectTop).append(") as superq0 ").append(buildSort("superq0", page.getSorts()));
			
			String jndi    = ConsultaToolUtils.getIntance().getJndi(connOrJndi);
			String catalog = ConsultaToolUtils.getIntance().getCatalog(connOrJndi);
			
			map.put("StringCatalog", catalog);
			map.put("StringJndi", jndi);
			map.put("StringSql", sql.toString());
			map.put("StringOriginal", sqlOriginal);
			map.put("StringSqlCount", null);
			map.put("Object[]", params);
			map.put("BooleanPagged", false);
			map.put("BooleanSort", true);
		}
		else {
			String jndi    = ConsultaToolUtils.getIntance().getJndi(connOrJndi);
			String catalog = ConsultaToolUtils.getIntance().getCatalog(connOrJndi);
			
			map.put("StringCatalog", catalog);
			map.put("StringJndi", jndi);
			map.put("StringSql", sqlOriginal);
			map.put("StringOriginal", sqlOriginal);
			map.put("StringSqlCount", null);
			map.put("Object[]", params);
			map.put("BooleanPagged", false);
			map.put("BooleanSort", false);
		}
		
		return map;
	}
	
	/**
	 * Un Connection puede tener distintos JNDIS dependiendo del Contexto<br/>
	 * @author Pancho
	 * @deprecated
	 * */
	private String getJndi(Object connOrJndi) {
		String jndi = null;
		
		if(connOrJndi != null) {
			if ( String.class.isAssignableFrom( connOrJndi.getClass() ) ) {
				jndi = (String) connOrJndi;
			}
			else if( Connection.class.isAssignableFrom( connOrJndi.getClass() )) {
 				jndi = ((DBConnectionManager)DBConnectionManager.getInstance()).getJndiFromConnection( (Connection) connOrJndi) ;
			}
		}
		return jndi;
	}
	
	private String getCatalog(Object connOrJndi) {
		String catalog = null;
		
		if(connOrJndi != null) {
			if( Connection.class.isAssignableFrom( connOrJndi.getClass() )) {
				try {
					catalog =  ((Connection) connOrJndi).getCatalog() ;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return catalog;
	}

	private String utilGetFieldForSelectCompare(String prefijo, List<String> pkList) {
		StringBuilder sql = new StringBuilder();
		
		if(pkList.size() > 1) {
			for(String pk : pkList) {
				if(sql.toString().length() > 0) {
					sql.append("+'_'+");
				}
				sql.append(" convert(varchar,").append("ltrim(rtrim(").append(prefijo).append(pk).append(")))");
			}
		}
		else {
			sql.append(pkList.get(0));
		}
		
		return sql.toString();
	}
	
	private String utilPutSelect(String originalSql, String selectDeclare) {
		if(originalSql != null && originalSql.length() >= 7) {
			originalSql = originalSql.trim();
			while(tieneParentesisInicial(originalSql)) {
				originalSql = quitaParentesis(originalSql);
			}
			
			StringBuilder sql = new StringBuilder();
			sql.append(selectDeclare).append(" ").append(originalSql.substring(7, originalSql.length()));
			originalSql = sql.toString();
		}
		
		return originalSql;
	}
	
	private boolean tieneParentesisInicial(String sql) {
		return sql != null && sql.length() > 2 && "(".equals(sql.substring(0, 1));
	}
	
	private String quitaParentesis(String sql) {
		if(sql != null && sql.length() > 2 && "(".equals(sql.substring(0, 1)) && ")".equals(sql.substring(sql.length()-1, sql.length()))) {
			return sql.substring(1, sql.length()-1);
		}
		
		return sql;
	}
	
	private Object[] utilDuplicaParametros(Object[] params) {
		List<Object> paramsToReturn = new ArrayList<Object>();
		
		for(int i = 0 ; i< 2; i++) {
			for(Object o : params) {
				paramsToReturn.add(o);
			}	
		}
		
		return paramsToReturn.toArray();
	}
	
	public String buildSort(String prefijo, ConsultaData sort) {
		try {
			StringBuilder sql = new StringBuilder();
			if(sort != null) {
				sort.toStart();
				while( sort.next()) {
					if(!sql.toString().equals("")) {
						sql.append(", ");
						
					}
					String property = getFieldForSort(prefijo, sort.getString("property"));
					
					if(sort.getObject("direction") instanceof Order) {
						Order o = ((Order) sort.getObject("direction"));
						if(o == Order.Ascendente) {
							sql.append(property).append(" ").append( "ASC"  )  ;
						}
						else {
							sql.append(property).append(" ").append( "DESC"  )  ;
						}
					}
					else if(sort.getObject("direction") instanceof String) {
						String direction = ((String)sort.getObject("direction"));
						if(direction != null) {
							if( direction.toLowerCase().indexOf("asc") != -1) {
								sql.append(property).append(" ").append(String.valueOf(sort.getObject("direction")).substring(0, 3));
							}
							else if( direction.toLowerCase().indexOf("desc") != -1) {
								sql.append(property).append(" ").append(String.valueOf(sort.getObject("direction")).substring(0, 4));
							}
						}

					}
					
				}	
			}
			
			if( sql != null && sql.toString().length() > 0) {
				return "ORDER BY "+sql.toString();
			}
			else {
				return sql.toString();	
			}
			
		}
		catch(Exception e) {
			return "";
		}
	}
	 
	private String getFieldForSort(String prefijo,String property) {
		StringBuilder str = new StringBuilder();
		
		if(prefijo != null && property != null && !prefijo.equals("")) {
			str.append(prefijo).append(".").append(property);
		}
		else if (property != null) {
			str.append(property);
		}
		
		return str.toString();
	}
	
	public ConsultaData getEmptySort() {
		
		List<String> sort = new ArrayList<String>();
		sort.add("direction");
		sort.add("property");
		ConsultaData data = new ConsultaData(sort);
		
		return data;
	}
	
	public ISenchaPage getBiggerConsultaDataPage() {
		return getBiggerConsultaDataPage(getEmptySort());
	}
	
	public ISenchaPage getBiggerConsultaDataPage(ConsultaData sort) {
		ISenchaPage page = new ConsultaDataPage(Sort.fromConsultaData(sort), null, null, 1);
		return page;
	}

	public Object[] getConcatenateParam(Object[] params, Object[] objectArray) {
		List<Object> list = new ArrayList<Object>();
		
		if(objectArray != null) {
			for(Object p : objectArray ) {
				list.add(p);
			}
		}

		if(objectArray != null) {
			for(Object p : params) {
				list.add(p);
			}	
		}
		
		return list.toArray();
	}

	/**
	 * A partir de un consultaData representado por un JSON, se le agregan una COLUMNA con un VALOR todos sus registros. <br/>
	 * Se retorna el ConsultaData en formato String
	 * 
	 * @author Pancho
	 * @since 27-03-2019
	 * 
	 * */
	public String setValueOnAllRecords(String flujoDeUnConsultaData, String paramKey, Object value) {
		if(flujoDeUnConsultaData != null && paramKey != null) {
			ConsultaData data = ConsultaTool.getInstance().fromJson(flujoDeUnConsultaData);
			
			while(data != null && data.next()) {
				data.getActualData().put(paramKey, ClaseConversor.getInstance().getObject(value, String.class));
			}
			
			flujoDeUnConsultaData = data.getJSON();
		}
		
		return flujoDeUnConsultaData;
	}
}
