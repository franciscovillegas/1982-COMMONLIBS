package portal.com.eje.frontcontroller.ioutils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWebLight;
import portal.com.eje.frontcontroller.error.MoreThanOneObjectException;
import portal.com.eje.frontcontroller.ioutils.sencha.toolsonrenderjson.ParamEncrypted;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.ClaseConversor;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.tools.ArrayFactory;
import portal.com.eje.tools.ConsultaDataEncrypter;
import portal.com.eje.tools.EnumTool;
import portal.com.eje.tools.security.IEncrypter;
 

public class IOUtilParam extends IOUtil {
	private final String SPLIT_COMA = "\\,";
	
	public static IOUtilParam getInstance() {
		return Util.getInstance(IOUtilParam.class);
	}
	/**
	 * 
	 * Obtiene el parámetro fecha en el formato deseado, siempre será en String por lo que se deberá entregar el formato de la fecha a obtener<br/>
	 * fecha_iniciofin: {27/06/2017} 
	 * @since 2017-06-22
	 * @author Pancho
	 * */
	public String getParamDateAsString(IIOClaseWebLight  io, String paramName, String formatIn, String formatOut, String defaultValue) {
		String value = io.getParamString(paramName, null);
		try {
			String retorno = Formatear.getInstance().toAnotherDate(value, formatIn, formatOut);
			return retorno;
		}
		catch(Exception e) {
			return defaultValue;
		}
	}
	
	
	public void printParams(IIOClaseWebLight  io, PrintStream out) {
		StringBuilder strParams = new StringBuilder();
		{
			Map<String,List<String>> params = io.getMapParams();
			Set<String> set = params.keySet();

			for(String s : set) {
				strParams.append(s).append(": {");
				List<String> values = params.get(s);
				boolean primero = true;
				for(String v: values) {
					if(primero) {
						primero = false;
					}
					else {
						strParams.append(",");
					}
					strParams.append(v);	
				}
				strParams.append("} ").append("\n");
			}
		}
		
		{
			Map<String, List<File>> map = io.getMapFiles();
			Set<String> sets = map.keySet();
			for(String s : sets) {
				strParams.append(s).append(": {");
				List<File> values = map.get(s);
				boolean primero = true;
				for(File v: values) {
					if(primero) {
						primero = false;
					}
					else {
						strParams.append(",");
					}
					try {
						strParams.append(v.getCanonicalPath());
					} catch (IOException e) {
						e.printStackTrace();
					}	
				}
				strParams.append("} ").append("\n");
			}
		}
		
		out.println(strParams.toString());
	}
	
	public void printParams(IIOClaseWebLight  io) {
		printParams(io, System.out);
	}
	
	public void printParams(IIOClaseWebLight  io, String paramName) {
		System.out.println(io.getParamString(paramName, null));
	}
	
	public int getParamNumEncrypted(IIOClaseWebLight  io, String nomParam, int defValue) {
		int finalValue = defValue;
		String value = io.getParamString(nomParam, null);
		try {
			finalValue = Validar.getInstance().validarInt( decryptValue((IOClaseWeb) io , value) , defValue );
		}
		catch(Exception e) {
			
		}
		return finalValue;
	}
	
	/**
	 * Get valor Encriptado
	 * @since 08-05-2018
	 * @author Pancho
	 * */
	public String getParamStringEncrypted(IIOClaseWebLight  io, String nomParam, String defValue) {
		String finalValue = defValue;
		String value = io.getParamString(nomParam, null);
		try {
			finalValue =  decryptValue((IOClaseWeb) io, value) ;
		}
		catch(Exception e) {
			finalValue = defValue;
		}
		
		if(finalValue == null) {
			finalValue = defValue;
		}
		return finalValue;
	}
	
	/**
	 * Genera un valor encryptado para ser transmitido por URL
	 * 
	 * */
	public String escapeEncryptValue(IOClaseWeb io, String toEncrypt) {
 
		try {
			String encrypted = encryptValue(io, toEncrypt);
			return URLEncoder.encode(encrypted, "UTF-8");
		}
		catch(Exception e) {
			
		}
		
		return null;
	}
	
	/**
	 * Genera un valor encriptado
	 * */
	public String encryptValue(IOClaseWeb io, String toEncrypt) {
		 
		try {
			IEncrypter enc = ParamEncrypted.getInstance().getEncrypter(io);
			String encrypted = enc.encrypt(toEncrypt);
			return encrypted;
		}
		catch(Exception e) {
			
		}
		
		return null;
	}
	
	/**
	 * Desencrypta valor
	 * */
	public String decryptValue(IOClaseWeb io, String toDesencrypt) {
		 
		try {
			IEncrypter enc = ParamEncrypted.getInstance().getEncrypter(io);
			String encrypted = enc.decrypt(toDesencrypt);
			return encrypted;
		}
		catch(Exception e) {
			
		}
		
		return null;
	}
	
	public Double getParamDouble(IIOClaseWebLight  io, String nomParam, Double defValue) {
		String param = io.getParamString(nomParam, String.valueOf(defValue));
		Double valorToReturn = Validar.getInstance().validarDouble( param , Validar.getInstance().validarDouble(String.valueOf(defValue), -1));
		
		if(valorToReturn >= 0) {
			return valorToReturn;
		}
		else{
			return defValue;
		}
	}
	
	/*
	 *Retorna lo mismo que : new Gson().fromJson(getParamString(paramName, null), List.class);
	 * */
	
	public List<Map<String,String>> getParamListFromJavascript(IIOClaseWebLight  io, String paramName) {
		List<Map<String,String>> datos =  new ArrayList<Map<String,String>>();
		
		try {
			datos = new Gson().fromJson(io.getParamString(paramName, null), (new TypeToken<List<Map<String, String>>>(){}).getType() );
		}
		catch (Exception e) {
			e.getMessage();
		}
		
		return datos;
	}
	
	public Map<String,String> getParamMapFromJavascript(IIOClaseWebLight  io, String paramName) {
		Map<String,String> datos = new HashMap<String,String>();
		
		try {
			datos = new Gson().fromJson(io.getParamString(paramName, null), (new TypeToken<Map<String, Object>>(){}).getType());
		}
		catch (Exception e) {
			e.getMessage();
		}
		
		return datos;
	}

	public ConsultaData getParamConsultaData(IIOClaseWebLight  io, String nomParam) {
		String param = io.getParamString(nomParam, null);
		
		return ConsultaTool.getInstance().fromJson(param);
	}
	
	/**
	 * Retorna una lista de objetos tipo String.<br/>
	 * El string que viene bajo el nombre del parámetro indicado debe estar limpio, en donde, cada item de la lista está separado por coma. br/>
	 * 	<ul>
	 * 		<li>Ejempo: 1,2,3,4,5,6,7,8....,99999</li>
	 * 	</ul>
	 * */
	public List<String> getParamList(IIOClaseWebLight  io, String nomParam) {
		List<String> lista = new ArrayList<String>();
		String param = io.getParamString(nomParam, null);
		
		if(param != null) {
			/*
			if("[".equals(param.substring(0, 1)) && "]".equals(param.substring(param.length(), param.length() +1))) {
				param = param.substring(1, param.length() -1);
			}
			*/
			
			String[] obs = param.split(SPLIT_COMA);
			for(String o : obs) {
				lista.add(o);
			}
		}

		return lista;
	}
	
	/**
	 * Retorna un Objeto ArrayFactory conteniendo a todos los items del parámetro que están separados por coma.<br/>
	 * El string que viene bajo el nombre del parámetro indicado debe estar limpio, en donde, cada item de la lista está separado por coma. br/>
	 * 	<ul>
	 * 		<li>Ejempo: 1,2,3,4,5,6,7,8....,99999</li>
	 * 	</ul>
	 * */
	public ArrayFactory getParamArrayFactory(IIOClaseWebLight  io, String nomParam) {
		ArrayFactory f = new ArrayFactory();
		
		List<String> listParams = io.getUtil(IOUtilParam.class).getParamList(io, nomParam);
		for(String p: listParams) {
			f.add(p);
		}
			 
		return f;
	}
	
	public cl.ejedigital.tool.strings.ArrayFactory getParamArrayFactory2(IIOClaseWebLight  io, String nomParam) {
		cl.ejedigital.tool.strings.ArrayFactory f = new cl.ejedigital.tool.strings.ArrayFactory();
		
		List<String> listParams = io.getUtil(IOUtilParam.class).getParamList(io, nomParam);
		for(String p: listParams) {
			f.add(p);
		}
			 
		return f;
	}


	public cl.ejedigital.tool.strings.ArrayFactory getParamArrayFactory2(IOClaseWeb io, String nomParam, String nomField, Class<? extends Object> toConvert) {
		ConsultaData data = getParamConsultaData(io, nomParam);
		 cl.ejedigital.tool.strings.ArrayFactory lista = ConsultaTool.getInstance().getList(data, cl.ejedigital.tool.strings.ArrayFactory.class, nomField, toConvert);
		return lista;
	}

	public File getParamFile(IIOClaseWebLight  io, String paramName) {
		return io.getFile(paramName);
	}

	public boolean existParamFile(IIOClaseWebLight  io, String nomParam) {
		return io.getFile(nomParam) != null;
	}


	public String[] getParamArray(IIOClaseWebLight  io, String nomParam) {
		String param = io.getParamString(nomParam, null);
		 
		List<String> values = new Gson().fromJson(param, (new TypeToken<List<String>>(){}).getType() );
		
		if(values != null) {
			String[] f = new String[values.size()];
			values.toArray(f);
			return f;	
		}
		return new String[0];
		
	}

	public <T> Collection<T> getParamCollection(IIOClaseWebLight  io, String nameParam,Class<T> clazz) {
		ConsultaData data = getParamConsultaData(io, nameParam);
		
		Collection<T> collection = VoTool.getInstance().buildVo(data, clazz);
		return collection;
	}
	
	public <T>Collection<T> getParamCollectionEncrypted(IOClaseWeb io, String paramName, Class<T> clazz) {
		ConsultaData data = getParamConsultaDataEncrypted(io, paramName);
		
		Collection<T> collection = VoTool.getInstance().buildVo(data, clazz);
		return collection;
	}

	public String getParamsAsString(IIOClaseWebLight io) {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(os);
		printParams(io, out);

		return os.toString();

	}
	public <T> T getParamCollectionSingle(IOClaseWeb io, String nameParam, Class<T> clazz) {
		Collection<T> vos = getParamCollection(io, nameParam, clazz);
		T t = null;
		
		if(vos == null ){

		}
		else if(vos.size() == 1) {
			t = vos.iterator().next();
		}
		else if(vos.size() > 1) {
			throw new MoreThanOneObjectException("No vienen objet");
		}
		
		return t;
		
	}
	
	public <T> T getParamCollectionSingleEncrypted(IOClaseWeb io, String paramName, Class<T> clazz) {
		Collection<T> vos = getParamCollectionEncrypted(io, paramName, clazz);
		
		T t = null;
		if(vos != null && vos.size() > 0) {
			t = vos.iterator().next();
		}
		
		return t;
	}
	
	/**
	 * Convierte cualquier tipo de parametro en un Objecto Java
	 * 
	 * @author Pancho
	 * @since 29-03-2019
	 * */
	public <T> T getParamObject(IOClaseWebLight io, String nomParam, TypeToken<T> type) {
		String param = io.getParamString(nomParam, null);
		 
		try {
			T values = new Gson().fromJson(param, type.getType() );
			return values;
		}
		catch(Exception e) {
			
		}
		
		return null;
	}

	/**
	 * El parámetro debe venir en el formato: 1,2,3,4,5
	 * */
	public <T>List<T> getListParam(IOClaseWeb io, String nomParam, Class<T> claseTo) {
		List<T> retorno = new ArrayList<>();
		String[] lista = io.getParamString(nomParam, "").split(SPLIT_COMA);
		
		for(String s : lista) {
			retorno.add(ClaseConversor.getInstance().getObject(s, claseTo));
		}
		
		return  retorno;
	}
	
	public <T>List<T> getListParamStringAsEnumByToStringIngoreCase(IOClaseWeb io, String nomParam, Class<? extends Enum<?>> clase) {
		List<T> retorno = new ArrayList<>();
		
		if(io != null && nomParam != null && clase != null) {
			List<String> lista = io.getListParamString(nomParam, null);
			if(lista != null && lista.size() > 0) {
				for(String valor : lista) {
					T enu = EnumTool.getEnumByToStringIngoreCase(clase, valor);
					if(enu != null) {
						retorno.add(enu);
					}
				}
			}
		}
		
		return retorno;
	}
	
	public <T>List<T> getListParamEncrypted(IOClaseWeb io, String nomParam, Class<T> claseTo) {
		List<T> retorno = new ArrayList<>();
		String params = io.getParamString(nomParam, null);
		if(params != null) {
			String[] lista = params.split(SPLIT_COMA);
			
			IEncrypter e = ParamEncrypted.getInstance().getEncrypter(io);
			
			for(String s : lista) {
				
				retorno.add(ClaseConversor.getInstance().getObject(e.decrypt(s), claseTo));
			}
		}
		
		return  retorno;
	}
	
	public ConsultaData getParamConsultaDataEncrypted(IOClaseWeb io, String paramName) {
		ConsultaData data = getParamConsultaData(io, paramName);
		
		if(data != null ) {
			ConsultaDataEncrypter.getInstance().decrypt(data, ParamEncrypted.getInstance().getEncrypter(io));
			 
		}
		
		return data;
	}
	@SuppressWarnings("unchecked")
	public <T>List<T> getParamList2(IOClaseWeb io, String nomParam) {
		List<T> lista = (List<T>) getParamObject(io, nomParam, new TypeToken<List<String>>() {});
		List<T> retorno = new ArrayList<>();
		
		if(lista != null && lista.size() > 0) {
			retorno.addAll(lista);
		} else if(getParamObject(io, nomParam, new TypeToken<String>() {}) != null) {
			retorno.add( (T)getParamObject(io, nomParam, new TypeToken<String>() {}));
		}
		
		return retorno;
	}


}
