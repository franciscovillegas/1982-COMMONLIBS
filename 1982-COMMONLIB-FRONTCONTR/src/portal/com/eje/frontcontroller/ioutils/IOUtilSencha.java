package portal.com.eje.frontcontroller.ioutils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.ConsultaDataPage;
import cl.ejedigital.consultor.ConsultaDataPageRenderer;
import cl.ejedigital.consultor.ConsultaDataPaged;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.ISenchaPage;
import cl.ejedigital.consultor.Row;
import cl.ejedigital.consultor.output.GsonObject;
import cl.ejedigital.consultor.output.JSarrayDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.consultor.output.ReservedWord;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.Order;
import cl.ejedigital.web.datos.Sort;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.ioutils.sencha.toolsonrenderjson.ParamEncrypted;
import portal.com.eje.frontcontroller.ioutils.sencha.toolsonrenderjson.TruncaYPagina;
import portal.com.eje.frontcontroller.ioutils.sencha.util.RetornoSenchaManipulator;
import portal.com.eje.frontcontroller.resobjects.EscapeSencha;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.CtrGeneric;
import portal.com.eje.portal.vo.VoEncrypter;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.portal.vo.util.Where;
import portal.com.eje.portal.vo.vo.Vo;
import portal.com.eje.tools.EnumTool;
 

public class IOUtilSencha extends IOUtil {
	
	public static IOUtilSencha getInstance() {
		return Util.getInstance(IOUtilSencha.class);
	}
	
	public boolean retSenchaJson(IIOClaseWebLight  io, ConsultaData data, ConsultaDataPageRenderer dataRenderer, boolean estado) {
		return this.retSenchaJson(io, data, dataRenderer,  estado, null);
	}
	
	public boolean retSenchaJson(IIOClaseWebLight  io,ConsultaData data, boolean estado) {
		return this.retSenchaJson(io, data, estado, null);
	}
	
	private boolean retSenchaJson(IIOClaseWebLight  io,ConsultaData data, boolean estado, String message) {
		return retSenchaJson(io, data, null, estado, message);
	}
	
	public boolean retSenchaJson(IIOClaseWebLight  io, ConsultaData data, ConsultaDataPageRenderer dataPageRenderer, boolean estado, String message) {
		return retSenchaJson(io, data, dataPageRenderer, estado, message, null);
	}
			
	public boolean retSenchaJson(IIOClaseWebLight  io, ConsultaData data, ConsultaDataPageRenderer dataPageRenderer, boolean estado, String message, String formatDate) {
		String outType 			= io.getParamString("outType", null); //FORMA ANTES DE 2017-FEB-27
		if(outType != null) {
			outType = outType.toLowerCase();
		}
		String outType2 = io.getParamString("return_type", "application/json").toLowerCase();
		
		String outFileName  = io.getParamString("outFileName", "archivo_PeopleManager");
		String callback = io.getParamString("callback", null);
		
		boolean ok = false;
		
		if("json".equals(outType) || "application/json".equals(outType2)) {
			try {

				Map<String,Object> map = TruncaYPagina.getInstance().truncaYPagina(data, (IOClaseWeb)io, dataPageRenderer);
				int countSize = (Integer) map.get("Integer");
				data = (ConsultaData)map.get("ConsultaData");
				RetornoSenchaManipulator.getInstance().setUtilidad(data, (IOClaseWeb)io);
				
				DataList result = new DataList();
				
				formatDate = io.getParamString("format_date", null);
				
				if(data != null) {
					DataFields fields = new DataFields();
					result.add(fields);
					fields.put("success"					, estado);
					fields.put("data"  						, getJSArray(data, formatDate));
					fields.put("columns"  					, new GsonObject(getColumnasDef(data)));
					fields.put("total"    					, new ReservedWord(String.valueOf(countSize)));
					fields.put("message"					, message != null ? message.replaceAll("\"", "'") : message);
					fields.put("context_path"				, io.getReq().getContextPath());
					fields.put("usuario_is_session_valid"	, io.getUsuario().esValido());
					fields.put("usuario"					, getJSArray(io.getUsuario().toConsultaData()) );
					fields.put("time_process"    			, "@@time_process@@");

				}
				
				JSonDataOut dataOutResponse = new JSonDataOut(result);
				dataOutResponse.setEscape(new EscapeSencha());
				
				String retorno = dataOutResponse.getListData();
				retorno = retorno.replaceAll("@@time_process@@", io.getCronometro().getTimeHHMMSS_milli());
				if(callback == null) {
					io.retTexto(retorno);	
				}
				else {
					io.retTexto(callback + " ("+retorno+")");
				}
					
				
				ok = true;
			}
			catch(Exception e) {
				e.printStackTrace();	
			}
		}
		else if("excel".equals(outType) || "application/excel".equals(outType2)) {
			data.toStart();
			ConsultaData dtaColumnas = io.getUtil(IOUtilParam.class).getParamConsultaData(io, "columnas");
			LinkedHashMap<String, String> columnas = new LinkedHashMap<String, String>();
			while (dtaColumnas!=null && dtaColumnas.next()) {
				String columna= dtaColumnas.getForcedString("columna");
				String text = null;
				if(dtaColumnas.existField("texto")) {
					text = dtaColumnas.getForcedString("texto");
				}
				else {
					text = columna;
				}
				
				if(columna != null && text != null) {
					columnas.put(columna,text );
				}
				
			}
			io.getUtil(IOUtilExcel.class).retExcel(io, data, outFileName + ".xls", columnas);
			ok = true;
		}
		
		io.setReturnSuccess(estado);
		
		return ok;
	}
	
	private String getColumnasDef(ConsultaData data) {
		List<Map> lista = new LinkedList<Map>();
		for(String col : data.getNombreColumnas()) {
			Map defCol = new HashMap();
			defCol.put("name", col);
			lista.add(defCol);
		}
		
		return new Gson().toJson(lista);
	}
	
	public ConsultaData getSort(IIOClaseWebLight  io) {
		List<String> nCols = new ArrayList<String>();
		nCols.add("direction");
		nCols.add("property");
		ConsultaData dataSort = new ConsultaData(nCols);
		
		{
			String sort   = io.getParamString("sort", null);
			
			if(  sort != null ) {
				List<Map> array = new ArrayList<Map>();
				
				if(sort != null && sort.indexOf("{") != -1 && sort.indexOf("}") != -1 ) {
					array = new Gson().fromJson(sort, (new TypeToken<List<Map<String, String>>>(){}).getType());	
				}
				else {
					Map map = new HashMap();
					map.put("direction", io.getParamString("dir", "ASC") );
					map.put("property", sort);
					array.add(map);
				}
				
				for(Map map : array) {
					DataFields fields = new DataFields();
					
					Set<String> sets = map.keySet();
					for(String s: sets) {
						fields.put(s, map.get(s) != null ? String.valueOf(map.get(s)).toLowerCase(): null);
					}
					dataSort.add(fields);
				}
			}
		}
		
		return dataSort;
	}
	
	
 
	JSarrayDataOut getJSArray(ConsultaData data) {
		return getJSArray(data, null);
	}
	
	JSarrayDataOut getJSArray(ConsultaData data, String formatDate) {
		JSarrayDataOut dataArray = null;
		
		JSonDataOut dataJs    = new JSonDataOut(data);
		dataJs.setEscape(new EscapeSencha());
		dataJs.setFormatDate(formatDate);
		
		DataList result2 = new DataList();
		DataFields fields2 = new DataFields();
		fields2.put("value", new Field(dataJs));
		result2.add(fields2);
		
		dataArray = new JSarrayDataOut(result2);
		return dataArray;
	}
	
	public boolean retSenchaJson(IIOClaseWebLight  io, DataList list, boolean estado) {
		List<String> cabecera = new ArrayList<String>();
		
		if(list != null && list.size() > 0) {
			Set<String> set = list.get(0).keySet();
			for(String s : set) {
				cabecera.add(s);
			}
		}
		else {
			cabecera.add("vacio");
		}
		
		ConsultaData data = new ConsultaData(cabecera);
		if(list != null && list.size() > 0) {
			for(DataFields d : list) {
				data.add(d);
			}
		}
		
		return retSenchaJson(io, data, estado);
	}
	
	public boolean retSenchaJson(IIOClaseWebLight  io, boolean valor) {
		
		List<String> lista = new ArrayList<String>();
		lista.add("ejemplo");
		ConsultaData data = new ConsultaData(lista);
		return retSenchaJson(io, data, valor);
	}
	
	public boolean retSenchaJson(IIOClaseWebLight  io, Boolean valor) {
		
		List<String> lista = new ArrayList<String>();
		lista.add("ejemplo");
		ConsultaData data = new ConsultaData(lista);
		return retSenchaJson(io, data, valor);
	}
	
	public boolean retSenchaJson(IIOClaseWebLight  io, ConsultaData data) {
		return retSenchaJson(io, data, true);
	}
	
	public boolean retSenchaJson(IIOClaseWebLight  io, ConsultaData data, ConsultaDataPageRenderer pageRenderer) {
		return retSenchaJson(io, data, pageRenderer, true);
	}
	
	public boolean retSenchaJsonFromSelect(IIOClaseWebLight  io, String jndi, String sql) {
		return retSenchaJsonFromSelect(io, jndi, sql, null);
	}
	
	public boolean retSenchaJsonFromSelect(IIOClaseWebLight  io, String jndi, String sql, Object[] params) {
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData(jndi, sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retSenchaJson(io, data, true);
	}
	
	public boolean retSenchaJsonPaginadoFromSelect(IIOClaseWebLight io, String jndi, String sql, Object[] params, String[] pks) {
		ConsultaData data = null;
		try {
			List<String> listPk = new ArrayList<String>();
			for(String pk : pks ) {
				listPk.add(pk);
			}
			data = ConsultaTool.getInstance().getDataPagged(jndi, sql, params, io.getSenchaPage(), listPk);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return retSenchaJson(io, data, true);
	}
	
	/**
	 * Retorna true o false según corresponda, se tomará como false: <br/><br/>
	 * 
	 * a) La ejecución incorrecta de la query,<br/>
	 * b) 0 registros modificados.<br/>
	 * 
	 * */
	public boolean retSenchaJsonFromUpdate(IIOClaseWebLight  io, String jndi, String sql) {
		return retSenchaJsonFromUpdate(io, jndi, sql, null);
	}
	/**
	 * Retorna true o false según corresponda, se tomará como false: <br/><br/>
	 * 
	 * a) La ejecución incorrecta de la query,<br/>
	 * b) 0 registros modificados.<br/>
	 * 
	 * */
	public boolean retSenchaJsonFromUpdate(IIOClaseWebLight  io, String jndi, String sql, Object[] params) {
		boolean ok  = false;
		
		try {
			 ok = ConsultaTool.getInstance().update(jndi, sql, params) > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		retSenchaJson(io, ok);
		return ok;
	}
	
	/**
	 * Retorna true o false según corresponda, se tomará como false solo la ejecución incorrecta de la query,
	 * si en vez de Insert la consulta tiene valores select o updates solo entregará false cuando la sentencia no pueda
	 * ser ejecutada.
	 * */
	public boolean retSenchaJsonFromInsert(IIOClaseWebLight  io, String jndi, String sql) {
		return retSenchaJsonFromInsert(io, jndi, sql, null);
	}
	
	/**
	 * Retorna true o false según corresponda, se tomará como false solo la ejecución incorrecta de la query,
	 * si en vez de Insert la consulta tiene valores select o updates solo entregará false cuando la sentencia no pueda
	 * ser ejecutada.
	 * */
	
	public boolean retSenchaJsonFromInsert(IIOClaseWebLight  io, String jndi, String sql, Object[] params) {
		boolean ok = false;
		try {
			ConsultaTool.getInstance().insert(jndi, sql, params);
			ok = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		retSenchaJson(io, ok);
		return ok;
	}
	
	
	public boolean retSenchaJson(IIOClaseWebLight  io, boolean valor, String msgSuccess, String msgError) {
		if(valor) {
			return retSenchaJson(io, msgSuccess, valor);
		}
		else {
			return retSenchaJson(io, msgError, valor);
		}
	}
	
	/**
	 * El <b>valor</b> es el parámetro <b>success</b> en la respuesta
	 * */
	public boolean retSenchaJson(IIOClaseWebLight  io, String mensaje, boolean valor) {
		return retSenchaJson(io, mensaje, null ,valor);
	}
	
	/**
	 * Retorna un mensaje en el parámetro message
	 * */
	public boolean retSenchaJson(IIOClaseWebLight  io, String mensaje) {
		return retSenchaJson(io, mensaje, null ,true);
	}
	
	/**
	 * No sirve enviar más de una ConsultaData en el response, la estructura de parámetros no lo permite
	 * @author Pancho
	 * @since 11-10-2018
	 * @deprecated
	 * */
	public boolean retSenchaJson(IIOClaseWebLight  io, String mensaje, ConsultaData[] adicional, boolean valor) {
		List<String> columnas = new ArrayList<String>();
		columnas.add("msg");
		ConsultaData data = new ConsultaData(columnas);
		DataFields fields = new DataFields();
		fields.put("msg", mensaje);
		int pos = 0 ;
		
		if( adicional != null) {
			for(ConsultaData dataAdicional : adicional) {
				JSonDataOut dataJs    = new JSonDataOut(dataAdicional);
				
				DataList result2 = new DataList();
				DataFields fields2 = new DataFields();
				fields2.put("value", new Field(dataJs));
				result2.add(fields2);
				JSarrayDataOut dataArray = new JSarrayDataOut(result2);
				
				
				DataList result = new DataList();
				DataFields fieldsAdicional = new DataFields();
				result.add(fieldsAdicional);
				fields.put("success"				, new Field(valor));
				fields.put("dataAdicional"+(pos++)	, new Field(dataArray));
				fields.put("total"    				, new ReservedWord(String.valueOf(dataAdicional.size())));

			}
		}
		
		data.add(fields);
		
		return retSenchaJson(io, data, valor, mensaje);
	}
	
	public boolean retSenchaJson(IIOClaseWebLight  io, Map row, boolean valor) {
		if(row != null) {
			List<String> columnas = new ArrayList<String>();
			Set<String> set = row.keySet();
			DataFields fields = new DataFields();
			
			for(String s : set ) {
				columnas.add(s);
				fields.put(s, row.get(s).toString());
			}
			
			ConsultaData data = new ConsultaData(columnas);
			data.add(fields);
			
			return io.getUtil(IOUtilSencha.class).retSenchaJson(io, data, valor);
			
		}
		else {
			retSenchaJson(io, valor);
		}
		
		return true;
	}
	
	public JSarrayDataOut getJSArray(IIOClaseWebLight  io, ConsultaData data) {
		JSarrayDataOut dataArray = null;
		
		JSonDataOut dataJs    = new JSonDataOut(data);
		dataJs.setEscape(new EscapeSencha());
		
		DataList result2 = new DataList();
		DataFields fields2 = new DataFields();
		fields2.put("value", new Field(dataJs));
		result2.add(fields2);
		
		dataArray = new JSarrayDataOut(result2);
		return dataArray;
	}
	
	public ISenchaPage getSenchaPage(IIOClaseWebLight  io) {
		Integer limit = io.getParamNum("limit", -1);
		Integer start = io.getParamNum("start", -1);
		Integer page  = io.getParamNum("page", -1);
		
		if(limit < 0) {
			limit = null;
		}
		
		if( start < 0) {
			start = null;
		}
		
		if(page < 0) {
			page = null;
		}
		Sort sort = Sort.fromConsultaData(io.getUtil(IOUtilParam.class).getParamConsultaData(io,"sort"));
		ISenchaPage p = new ConsultaDataPage(sort, limit, start, page);
		return p;
	}
	
	public <T> boolean retSenchaJson(IIOClaseWebLight io,Collection<? extends Vo> collection) {
		return retSenchaJson(io, collection, null);
	}
	
	public boolean retSenchaJson(IIOClaseWebLight io, Collection<? extends Vo> collection, String formatDates) {
		if(collection != null) {
			ConsultaData data = VoTool.getInstance().buildConsultaData(collection);
			return retSenchaJson(io, data, null, true, null, formatDates);
			
		}
		
		return false;
		
	}

	
	public <T> boolean retSenchaJsonIncluyeRefs(IIOClaseWebLight io,Collection<? extends Vo> collection) {
		return retSenchaJsonIncluyeRefs(io, collection, null);
	}


	public boolean retSenchaJsonIncluyeRefs(IIOClaseWebLight io ,Collection<? extends Vo> collection, String formatDate) {
		if(collection != null) {
			ConsultaData data = VoTool.getInstance().buildConsultaData(collection, true);
			return retSenchaJson(io, data, null, true, null, formatDate);
		}
		
		return false;
		
	}

	
	public <T> boolean retSenchaJson(IIOClaseWebLight io, T vo) {
		if(vo != null) {
			Collection<Vo> collection = new ArrayList<Vo>();
			collection.add((Vo)vo);
			return retSenchaJson(io, collection);
		}
		return false;
	}
	
	public boolean retSenchaJson(IIOClaseWebLight io, Enum<?> e) {
		if(e != null) {
			DataFields df = EnumTool.getEnumFields(e);
			ConsultaData retorno = new ConsultaData(new ArrayList<String>(df.keySet()));
			retorno.add(df);
			
			retSenchaJson(io, retorno, true);
		}
		else {
			retSenchaJson(io, true);
		}
		
		return true;
	}


	public void retSenchaJsonFromVo(IIOClaseWebLight io, Class<? extends Vo> clase, List<Where> wheres) {
		CtrGeneric ctrGeneric = Util.getInstance(CtrGeneric.class);
		
		try {
			ConsultaData data = ctrGeneric.getDataFromClass(clase, wheres, io.getSenchaPage());
			
			retSenchaJson(io, data);
		} catch (NullPointerException e) {
			retSenchaJson(io, e);
			e.printStackTrace();
		} catch (SQLException e) {
			retSenchaJson(io, e);
			e.printStackTrace();
		}
	}
 	
	

	public boolean retSenchaJson(IIOClaseWebLight io, Map<String, Object> retorno) {
		return retSenchaJson(io, retorno, true);		
	}


	public boolean retSenchaJson(IIOClaseWebLight io, Throwable e) {
		if(io != null && e != null) {
			ConsultaData data = ConsultaTool.getInstance().newConsultaData(new String[] {"msg"});
			
			data.add(Row.column("msg", e.toString()).build());
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(os);
			
			e.printStackTrace(ps);
			
			for(String line :  os.toString().split(System.getProperty("line.separator")) ) {
				data.add(Row.column("msg", line.toString() ).build());
			}
			
			StringBuilder error = new StringBuilder();
			try {
				int indexOfPComa = e.toString().indexOf(":") != -1 ? e.toString().indexOf(":") : e.toString().length();
				String clase = e.toString().substring(0 , indexOfPComa);
				clase = clase.substring(clase.lastIndexOf(".")+1, clase.length());
				error.append(clase).append(":").append(e.getLocalizedMessage()).append("");
			}
			catch(Exception er) {
				er.printStackTrace();
			}
			retSenchaJson(io, data, false, error.toString());
		}
		else {
			throw new NullPointerException();
		}
		
		return true;
	}

	public boolean retSenchaJson(IOClaseWeb io, Collection<? extends Vo> collection, boolean encryptPrimaryAndForeignKeys) throws Exception  {
		if(encryptPrimaryAndForeignKeys && collection != null && collection.size() >0) {
			adddPrimaryKeyAndForeignKeyToBeEncrypted(io, collection.iterator().next().getClass());
		}
		
		return retSenchaJson(io, collection);
	}

	/**
	 * Agrega a la lista de campos a encryptar, todas las PK y FK del Vo
	 * @author Pancho
	 * @since 05-06-2019
	 * */
	private void adddPrimaryKeyAndForeignKeyToBeEncrypted(IOClaseWeb io, Class<? extends Vo> clase) throws Exception {
		
		if(clase != null) {
			List<String> keys = VoTool.getInstance().getPrimaryAndForeignkeys(clase);
			onReturnEncryptColumns(io, keys);
		}
		
	}
	
 
	/**
	 * Agrega a la lista de campos encriptados, aquellos que se van a encryptar
	 * @author Pancho
	 * @since 05-06-2019
	 * */
	public void onReturnEncryptColumns(IOClaseWeb io, List<String> columns) throws Exception {
		if(io != null && columns != null && columns.size() > 0) {
			List<String> encrypted = io.getParamList(ParamEncrypted.getInstance().PARAM_ENCRYPTED);
			if(encrypted == null) {
				encrypted = new ArrayList<>();
			}
			
			encrypted.addAll(columns);
			String json = new Gson().toJson(encrypted, (new TypeToken<List<String>>(){}).getType() );
			
			io.addExtraParam(ParamEncrypted.getInstance().PARAM_ENCRYPTED, json);
		}
	}
//
//	public void onReadDencryptColumns(IOClaseWeb io, List<String> cols) {
//		if(io != null && cols != null && cols.size() > 0) {
//			List<String> encrypted = io.getParamList(ParamEncrypted.getInstance().PARAM_ENCRYPTED);
//		}
//		
//	}

	public void onReturnEncryptColumns(IOClaseWeb io, Class<? extends Vo> voClass) throws Exception {
		onReturnEncryptColumns(io, VoTool.getInstance().getPrimaryAndForeignkeys(voClass));
		
	}

	public void onReturnEncryptColumn(IOClaseWeb io, String column) throws Exception {
		List<String> columns = new ArrayList<>();
		columns.add(column);
		onReturnEncryptColumns(io, columns);		
	}
	
	
}
