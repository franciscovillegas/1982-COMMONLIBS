package cl.ejedigital.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.tool.strings.MyString;
import cl.ejedigital.web.fmrender.FmRenderers;
import cl.ejedigital.web.freemaker.bootstrappaanel.PanelesTool;
import cl.ejedigital.web.freemaker.ifaces.IFreemakerSetHash;
import cl.ejedigital.web.freemaker.sethash.SetHashDefault;
import cl.ejedigital.web.frontcontroller.resobjects.ResourceHtml;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;
import portal.com.eje.applistener.ContextInfo;
import portal.com.eje.genericconf.util.FreemakerButtonTool;
import portal.com.eje.genericconf.util.FreemakerPageTool;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.vo.ClaseConversor;
import portal.com.eje.portal.vo.CtrGeneric;
import portal.com.eje.portal.vo.VoTool;
import portal.com.eje.portal.vo.annotations.TableReference;
import portal.com.eje.portal.vo.vo.Vo;
import portal.com.eje.serhumano.httpservlet.MyTemplateUbication;


public class FreemakerTool  {
	private Logger logger = Logger.getLogger(getClass());
	private SimpleDateFormat dFormat;
	public static final FreemakerButtonTool buttons = FreemakerButtonTool.getInstance();
	public static final FreemakerPageTool pages = FreemakerPageTool.getInstance();
	public static final PanelesTool bootstrapPaneles = PanelesTool.getInstance();
	
	public FreemakerTool() {
		dFormat = getNewDateFormat();
	}
	
	public static FreemakerTool getInstance() {
		return Weak.getInstance(FreemakerTool.class);
	}
	
	public FreemakerTool(SimpleDateFormat dFormat) {
		if(dFormat != null) {
			this.dFormat = dFormat;
		}
		else {
			dFormat = getNewDateFormat();
		}
	}
	
	public PanelesTool getBootstrapPanel() {
		return PanelesTool.getInstance();
	}
	
	public SimpleList getListData(ConsultaData data) {
		return getListData(data, new HashMap<String,String>());
	}
	
	/**
	 * Transforma en SimpleList todos los ciclos de ConsultaData, además, mapea el nombre de los campos según el Map ingresado
	 * 
	 * @since 14-marzo-2017
	 * */
	
	public SimpleList getListData(ConsultaData data, Map<String,String> mapping) {
		SimpleList simple = new SimpleList();
		
		if( data != null) {
			data.toStart();
			while(data.next()) {
				simple.add(getData(data, mapping));			
			}
		}
		
		return simple;
	}
	
	public SimpleList getListDataB(List<IFreemakerBuilder> fbuilder) {
		SimpleList lista = new SimpleList();
		
		if(fbuilder != null) {
			for(IFreemakerBuilder i : fbuilder) {
				lista.add(i.toSimpleHash());
			}
		}
		
		return lista;
	}
	
	public SimpleList getListData(List<Map<String,Object>> lista) {
		SimpleList simple = new SimpleList();
		
		for(Map<String,Object> map : lista) {
			simple.add(getData(map));		
			SimpleHash hash = new SimpleHash();
			
			Set<String> set = map.keySet();
			
			for(String  key : set ) {
				if(map.get(key) instanceof List) {
					hash.put(key, getListData((List)map.get(key)));
				}
				else {
					map.put(key, manipulateString(map.get(key)));
				}
			}
		}
		
		return simple;
	}
	
	/**
	 * Retorna un SimpleHash a partir del nodo de documento XML<br/>
	 * la ruta a los nodos es completa ej: <b>documento.encabezado.iddoc.folio</b> <br/>
	 * para un xml ej:<br/>
	 * <pre>
	 * 
	 * &lt;Documento ID="MiPE76219408-969"&gt; 
		   &lt;Encabezado&gt; 
		   &lt;IdDoc&gt; 
		   &lt;TipoDTE&gt;33&lt;/TipoDTE&gt; 
		   &lt;Folio&gt;5331&lt;/Folio&gt; 
		   &lt;FchEmis&gt;2018-02-26&lt;/FchEmis&gt; 
		   &lt;TpoTranCompra&gt;1&lt;/TpoTranCompra&gt; 
		   &lt;TpoTranVenta&gt;1&lt;/TpoTranVenta&gt; 
		   &lt;FmaPago&gt;2&lt;/FmaPago&gt; 
		   &lt;MntPagos&gt; 
			   &lt;FchPago&gt;2018-03-26&lt;/FchPago&gt; 
			   &lt;MntPago&gt;928200&lt;/MntPago&gt; 
		   &lt;/MntPagos&gt; 
		   &lt;/IdDoc&gt; 
		   &lt;/Encabezado&gt; 
	 * &lt;/Documento &gt; 
	 * </pre>
	 * 
	 * Si se encuentra una serie de tags del mismo nombre se reconocerá como SimpleList <br/>
	 * entonces se podrá usar un list en la vista para iterar los valores
	 * 
	 * 
	 * @author Pancho
	 * @since 23-05-2018
	 * */
	public SimpleHash getData(Node nodeDocumento, boolean allToLowerCase) {
		Map map = getMapsFromDocument(nodeDocumento, null, allToLowerCase);
		return toSimpleHash(map);
	}
	/**
	 * Retorna un SimpleHash a partir del nodo de documento XML<br/>
	 * la ruta a los nodos es completa ej: <b>documento.encabezado.iddoc.folio</b> <br/>
	 * para un xml ej:<br/>
	 * <pre>
	 * 
	 * &lt;Documento ID="MiPE76219408-969"&gt; 
		   &lt;Encabezado&gt; 
		   &lt;IdDoc&gt; 
		   &lt;TipoDTE&gt;33&lt;/TipoDTE&gt; 
		   &lt;Folio&gt;5331&lt;/Folio&gt; 
		   &lt;FchEmis&gt;2018-02-26&lt;/FchEmis&gt; 
		   &lt;TpoTranCompra&gt;1&lt;/TpoTranCompra&gt; 
		   &lt;TpoTranVenta&gt;1&lt;/TpoTranVenta&gt; 
		   &lt;FmaPago&gt;2&lt;/FmaPago&gt; 
		   &lt;MntPagos&gt; 
			   &lt;FchPago&gt;2018-03-26&lt;/FchPago&gt; 
			   &lt;MntPago&gt;928200&lt;/MntPago&gt; 
		   &lt;/MntPagos&gt; 
		   &lt;/IdDoc&gt; 
		   &lt;/Encabezado&gt; 
	 * &lt;/Documento &gt; 
	 * </pre>
	 * 
	 * Si se encuentra una serie de tags del mismo nombre se reconocerá como SimpleList <br/>
	 * entonces se podrá usar un list en la vista para iterar los valores
	 * 
	 * 
	 * @author Pancho
	 * @since 23-05-2018
	 * */
	public SimpleHash getData(Node nodeDocumento, List<Node> nodesToList, boolean allToLowerCase) {
		Map map = getMapsFromDocument(nodeDocumento, nodesToList, allToLowerCase);
		return toSimpleHash(map);
	}
	
	private SimpleHash toSimpleHash(Map map) {
		SimpleHash hash = new SimpleHash();
		
		Set<String> set = map.keySet();
		for(String s : set) {
			Object o = map.get(s);
			
			if(o instanceof String) {
				hash.put(s, (String) o);	
			}
			else if(o instanceof Map) {
				hash.put(s, toSimpleHash((Map) o));	
			}
			else if(o instanceof List) {
				hash.put(s, toSimpleList((List) o));	
			}
		}
		
		return hash;
	}
	
	private SimpleList toSimpleList(List lista) {
		SimpleList sList = new SimpleList();
		
		for(Object o : lista) {
			if(o instanceof String) {
				System.out.println("NO");
			}
			else if(o instanceof Map) {
				sList.add(toSimpleHash((Map) o));	
			}
			else if(o instanceof List) {
				System.out.println("NO 2");
			}
		}
		
		return sList;
	}
	
	private Map getMapsFromDocument(Node node, List<Node> nodesToList, boolean allToLowerCase) {
		NodeList lista = node.getChildNodes();
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		{
						
			for(int i = 0; i < lista.getLength() ; i++) {
				Node hijo = lista.item(i);
				String key = hijo.getNodeName();
				if(allToLowerCase) {
					key = key.toLowerCase();
				}
				
				if(!"#text".equals(hijo.getNodeName())) {
					if(hijo.getChildNodes().getLength() == 1) {
						map.put(hijo.getNodeName().toLowerCase(), hijo.getTextContent());
					}
					else {
						if(nodesToList != null && nodesToList.indexOf(hijo) != -1) {
							/*SI ES UN NODO LISTADO*/
							List<Map> listaMap = new LinkedList<Map>();
							listaMap.add((Map)map.get(key));
							listaMap.add(getMapsFromDocument(hijo, nodesToList, allToLowerCase));
							
							map.put(key, listaMap);
						}
						else if(map.get(key) == null ) {
							/*SI EL NODO NO EXISTE*/
							map.put(key, getMapsFromDocument(hijo, nodesToList, allToLowerCase));	
						}
						else if(map.get(key) instanceof Map) {
							/*SI EL NODO YA EXISTE LO TRANSFORMA EN LIST*/
							List<Map> listaMap = new LinkedList<Map>();
							listaMap.add((Map)map.get(key));
							listaMap.add(getMapsFromDocument(hijo, nodesToList, allToLowerCase));
							
							map.put(key, listaMap);
						}
						else if(map.get(key) instanceof List) {
							/*SI YA ES UNA LISTA*/
							List listaMap = (List) map.get(key);
							listaMap.add(getMapsFromDocument(hijo, nodesToList, allToLowerCase));
							map.put(key, listaMap);
						}
					}
				}
			}
		}

		return map;
	}
	
	public SimpleHash getData(ConsultaData data) {
		return getData(data, null);
	}
	/**
	 * Transforma en SimpleHash un ciclo de consulta data, además, mapea el nombre de los campos según el Map ingresado
	 * 
	 * @since 14-marzo-2017
	 * */
	public SimpleHash getData(ConsultaData data, Map<String,String> mapping ) {
		SimpleHash hash = new SimpleHash();
		
		if(data != null && data.existData()) {
			Set<String> set = data.getActualData().keySet();
			
			for(String key : set) {
				String mapeo = key;
				if(mapping != null && mapping.get(key) != null) {
					mapeo = mapping.get(key);
				}
				
				Field fields = data.getActualData().get(key);
				
				if(fields.getObject() instanceof ConsultaData ){
					hash.put(mapeo,  getListData(  (ConsultaData) fields.getObject() ));
				}
				else {
					String string = (String) ClaseConversor.getInstance().getObject( fields.getObject() , String.class  );
					hash.put(mapeo,string );	
				}
				
			}
		}

		
		return hash;
	}
	
	public SimpleHash getData( Map<String,Object> map) {
		return getData(null,map);
	}
	public SimpleHash getData(SimpleHash modelRoot, Map<String,Object> map) {
		SimpleHash hash = null;
		
		if(modelRoot == null) {
			hash = new SimpleHash();
		}
		else {
			hash = modelRoot;
		}
		
		if(map != null && map.size() > 0) {
			Set<String> set = map.keySet();
			
			for(String key : set) {
				
				if(map.get(key) instanceof List) {
					hash.put(key, getListData((List)map.get(key)));
				}
				else {
					hash.put(key, manipulateString(map.get(key)));
				}
				
			}
		}

		
		return hash;
	}
	
	private SimpleDateFormat getNewDateFormat() {
		return new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	}
	
	/*
	public SimpleList getListData(List<?> data) {
		SimpleList simple = new SimpleList();
		SimpleHash hash = null;
		Set set = null;
		
		
		if( data != null && data.size() > 0) {
			for(int i = 0 ; i < data.size() ; i++ ) {
				HashMap map = (HashMap)data.get(i);
				
				set = map.keySet();
				Object[] objects = set.toArray();
				hash = new SimpleHash();
				
				for( int o = 0 ; o < objects.length ; o++) {
					hash.put((String)objects[o],(String)map.get(objects[o]));
				}
				
				simple.add(hash);
			}
		}
		
		return simple;
	}	
	*/

	private String manipulateString(Field f) {
		if(f != null) {
			return manipulateString(f.getObject());
		}
		
		return null;
	}
	
	private String manipulateString(Object o) {

		if(o != null) {
			try {
				if(o instanceof Boolean) {
					return  String.valueOf((Boolean)o) ;
				}
				else if(o instanceof Byte) {
					return  String.valueOf((Byte)o) ;
				}
				else if(o instanceof Short) {
					return  String.valueOf((Short)o) ;
				}
				else if(o instanceof Integer) {
					return  String.valueOf((Integer)o) ;
				}
				else if(o instanceof Long) {
					return  String.valueOf((Long)o) ;
				}
				else if(o instanceof Float) {
					return  String.valueOf((Float)o) ;
				}
				else if(o instanceof Double) {
					return  String.valueOf((Double)o) ;
				}
				else if(o instanceof java.util.Date) {
					
					return  String.valueOf( dFormat.format((java.util.Date)o) ) ;
				}
				else if(o instanceof java.sql.Date) {
					return  String.valueOf(dFormat.format((java.sql.Date)o)) ;
				}
				else if(o instanceof java.sql.Time) {
					return  String.valueOf(dFormat.format((java.sql.Time)o)) ;
				}
				else if(o instanceof java.sql.Timestamp) {
					return  String.valueOf(dFormat.format((java.sql.Time)o)) ;
				}
				else if(o instanceof String) {
					return  String.valueOf((String)o) ;
				}
				else if(o instanceof Blob) {
					return  String.valueOf((Blob) o) ;
				}
//				else if(o instanceof BlobBuffer) {
//					return  String.valueOf((BlobBuffer) o) ;
//				}
//				else if(o instanceof ClobImpl) {
//					Clob data = (Clob) o;
//					   StringBuilder sb = new StringBuilder();
//					    try {
//					        Reader reader = data.getCharacterStream();
//					        BufferedReader br = new BufferedReader(reader);
//
//					        String line;
//					        while(null != (line = br.readLine())) {
//					            sb.append(line);
//					        }
//					        br.close();
//					    } catch (SQLException e) {
//					        // handle this exception
//					    } catch (IOException e) {
//					        // handle this exception
//					    }
//					    return sb.toString();
//				}
				else if(o instanceof BigDecimal) {
					return  String.valueOf((BigDecimal) o) ;
				}
				else if(o == null) {
					return null;
				}
				else {
					return "parseError";
				}
			}
			catch (Exception e) {
				return "parseError (".concat(e.toString()).concat(")");
			}
		}
		
		return null;
	}
		

	
	public String templateProcess(Template template, SimpleHash modelRoot) {
    	StringWriter stringWriter = new StringWriter();
       	PrintWriter print = new PrintWriter(stringWriter);        
        template.process(modelRoot, print);
        
        return stringWriter.toString();
	}
	
 
	public String templateProcessFromPath(HttpServletRequest req, String templatePath, SimpleHash modelRoot, boolean startFromTemplatePath) throws IOException, ServletException, SQLException {
		Template template = portal.com.eje.frontcontroller.resobjects.ResourceHtml.getInstance().getTemplate(MyTemplateUbication.SrcAndWebContent, req, templatePath, startFromTemplatePath);
		String html = templateProcess(template, modelRoot);
		
		return html;
	}
	
	public String templateProcessFromFlow(String templateFlow, SimpleHash modelRoot) throws IOException {
       	MyString my = new MyString();
       	String fileName = my.getRandomString(MyString.cadena_alfanumerica, 30);
 
       	File file = new File(ContextInfo.getInstance().getRealPath("temporal")+File.separator+fileName);
       	FileOutputStream output = new FileOutputStream(file);
       	IOUtils.write(templateFlow, output, "ISO-8859-1");
       	
       	String out = templateProcess(file, modelRoot);
       	FileUtils.deleteQuietly(file);
       	
        return out;
	}
	
	public String templateProcess(File file, SimpleHash modelRoot) throws IOException {
    	StringWriter stringWriter = new StringWriter();
       	PrintWriter print = new PrintWriter(stringWriter);        
        
       	Template template = new Template(file);
       
       	template.process(modelRoot, print);
        
        return stringWriter.toString();
	}

	public Object getListData(String[] order) {
		throw new NotImplementedException();
	}

	/**
	 * Se deprecó por ocupar un antigüo ResourceHtml
	 * @deprecated
	 * @since 11-10-2018
	 * @see #templateProcessFromBase(HttpServletRequest, String, SimpleHash)
	 * 
	 * */
	public String templateProcess(HttpServletRequest req, String templatePath, SimpleHash modelRoot) {
		ResourceHtml html = new ResourceHtml();
		try {
			Template template = html.getTemplate(templatePath);
			return templateProcess(template, modelRoot);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Es muy extraño lo que hace este método, creo que emntrega una defición de los vos existentes en el arreglo entregado, fue hecho para
	 * los correos, para entregar una definición de los datos existentes en un vo y sus subeelementos.
	 * 
	 * @author Pancho
	 * */
	public SimpleList getListDataFromVo(Collection<Vo> vos) {
		SimpleList lista = new SimpleList();
		
		for(Vo vo : vos) {
			SimpleHash hash = new SimpleHash();
			getDataFromVo(hash, vo);
			
			lista.add(hash);
		}
		
		return lista;
	}
	
	
	/**
	 * Crea una SimpleList a partir de una Colletios de Vo<br/>
	 * Puede rendearse los campos
	 * 
	 * @author Pancho
	 * @since 18-04-2019
	 * */
	public <T>SimpleList getListDataFromVo(Collection<T> vos, FmRenderers<T> renders) {
		return getListDataFromVo(vos, renders, null);
	}
	
	/**
	 * Crea una SimpleList a partir de una Colletios de Vo<br/>
	 * Puede rendearse los campos
	 * 
	 * @author Pancho
	 * @since 18-04-2019
	 * */
	public <T>SimpleList getListDataFromVo(Collection<T> vos, FmRenderers<T> renders, IFreemakerSetHash setMethod) {
		if(setMethod == null) {
			setMethod= SetHashDefault.getInstance();
		}
		
		SimpleList lista = new SimpleList();
		
		int q = -1;
		if(vos != null) {
			for(T vo : vos) {
				SimpleHash hash = new SimpleHash();
				setMethod.setDataFromVo(hash, vo, renders, ++q);
				
				lista.add(hash);
			}
		}
		
		
		return lista;
		
	}
	
	public <T>void setDataFromVo(SimpleHash modelRoot, T t) {
		setDataFromVo(modelRoot, t, null, 0);
	}
	
	/**
	 * establece los valores de un Vo (debe extender de Vo) dentro de SimleHash<br/>
	 * Puede rendearse <br/>
	 * Se le pasa el contador, en el caso que sea llamado desde un iterador, este contados solo se pasa a la instancia del IRender
	 * 
	 * @author Pancho
	 * @since 18-04-2019
	 * */
	public <T>void setDataFromVo(SimpleHash modelRoot, T t, FmRenderers<T> rendes, int counter) {
		Map<String,String> toPut = new HashMap<>();
		
		List<String> fields = VoTool.getInstance().getGetsFieldsWithNoParameters(t.getClass());
		for(String field : fields) {
			//los cambios que si estan en el vo
			Object o = VoTool.getInstance().getMethodValue_byField(t, field);
			
			if(rendes != null)  {
				if( rendes.getRender(field) != null ) {
					o = rendes.getRender(field).getRenderer().renderer((T) t, field, counter);
				}
				else {
					o = ClaseConversor.getInstance().getObject(o, String.class);
				}
			}
			
			String value = ClaseConversor.getInstance().getObject(o , String.class);
			
			toPut.put(field, value);
		}
		
		if(rendes != null)  {
			for(String field : rendes.getRenderers().keySet()) {
				//los campos que no estan en el vo
				if(!fields.contains(field)) {
					Object o = rendes.getRender(field).getRenderer().renderer((T) t, field, counter);
					toPut.put(field, (String)o);
				}
			}
		}
		
		modelRoot.put("correlativo", String.valueOf(counter));
		logger.debug("correlativo >>> "+ String.valueOf(counter));
		
		for(Entry<String, String> entry : toPut.entrySet()) {
			modelRoot.put(entry.getKey(), entry.getValue());
			logger.debug(entry.getKey()+" >>> "+entry.getValue());
		}
	}
	
	/**
	 * Requiere Table definition
	 * @author Pancho
	 * 
	 * */
	public <T>void getDataFromVo(SimpleHash modelRoot, Vo t) {
		Map<String, Map<String,Object>> vosReferenced = new HashMap<String, Map<String,Object>>();
		Map<Class<? extends Vo>,Collection<Vo>> allColsReferenced = new HashMap<Class<? extends Vo>,Collection<Vo>>();
		Map<String,Map> tableReference = CtrGeneric.getInstance().getTableReference(t);
		
		if(tableReference != null) {
			for(String keyField : tableReference.keySet()) {
				List<TableReference> listRef = (List<TableReference>)tableReference.get(keyField).get("TableReference[]");
				
				if(listRef.size() > 0) {
					TableReference ref = listRef.get(0);
					
					Method mGet = VoTool.getInstance().getGetMethod(t.getClass(), ref.field());
				
					if(tableReference.get(keyField) != null && mGet != null) {
						Object o = VoTool.getInstance().getReturnFromMethod(t, mGet);
						
						if(vosReferenced.get(keyField) == null) {
							vosReferenced.put(keyField,new HashMap<String, Object>());
						}
						
						if(o != null && Collection.class.isAssignableFrom(o.getClass())) {
							vosReferenced.get(keyField).put("Collection",(Collection<Vo>)o);
							vosReferenced.get(keyField).put("String","Collection");
						}
						else if(o != null && Vo.class.isAssignableFrom(o.getClass())) {
							Collection<Vo> col = new ArrayList<Vo>();
							col.add((Vo)o);
							vosReferenced.get(keyField).put("Collection", col);
							vosReferenced.get(keyField).put("String","Vo");
						}
					}
				}

			}	
		}

		
		List<Method> mGets = VoTool.getInstance().getGetsMethodsWithNoParameters(t.getClass());
		for(Method mGet : mGets ) {
			String field = VoTool.getInstance().getMetodNameWithoutSetOrGetOrIsLowerCase(mGet);
		
			Object retorno = VoTool.getInstance().getReturnFromMethod(t, mGet);
			String strRetorno = (String) ClaseConversor.getInstance().getObject(retorno, String.class);
			if(strRetorno != null) {
				strRetorno = strRetorno.trim();
			}
			modelRoot.put(field, strRetorno);
		}
			
		 
		for(String set : vosReferenced.keySet()) {
			List<Vo> col = (List<Vo>) vosReferenced.get(set).get("Collection");
			String type = (String) vosReferenced.get(set).get("String");
			
			if("Vo".equals(type) && col.size() == 1) {
				Vo vo = col.iterator().next();
				SimpleHash hash = new SimpleHash();
				modelRoot.put(set, hash);
				getDataFromVo(hash, vo);
			}
			else if("Collection".equals(type)) {
				SimpleList lista = new SimpleList();
				
				for(Vo vo : col) {
					SimpleHash hash = new SimpleHash();
					getDataFromVo(hash, vo);
					lista.add(hash);
				}
				
				modelRoot.put(set, lista);
			}
		}
	}
 

	
}
