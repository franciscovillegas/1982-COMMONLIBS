package cl.ejedigital.web.frontcontroller.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.frontcontroller.error.AlreadyReturnedException;

/**

 * @since 2015-06-23
 * @author Francisco
 * */
public class WebInputsManager implements IWebInput {
	private Logger logger = Logger.getLogger(getClass());
	private HashMap<String,List<String>> mapString;
	private HashMap<String, List<File>> mapFile;
	private IWebOutputText outText;
	private HttpServletRequest req;
	private ServletContext sc;
	protected String realPathToSave;
	private String internalReturn;
	
	protected WebInputsManager(HashMap<String,List<String>> mapString,HashMap<String, List<File>> mapFile)  {
		this.mapFile = mapFile;
		this.mapString = mapString;
		
	}
	
	public WebInputsManager(String realPathToSave) throws Exception {
		this(new HashMap<String,List<String>>(), new HashMap<String, List<File>>());
		
		this.realPathToSave = realPathToSave;
		
	}
	
	public WebInputsManager(IWebOutputText outText, HttpServletRequest req, ServletContext sc, String realPathToSave) throws Exception {
		this.outText = outText;
		this.realPathToSave = realPathToSave;
		
		this.clear();
		
		this.req = req;
		this.sc = sc;
		
		if(req!=null) {
			processParameter();
		}
		
//		Enumeration e = req.getHeaderNames();
//		while(e.hasMoreElements()) {
//
//			String k = String.valueOf(e.nextElement());
//			
//			System.out.println("##### " + k + "=" + String.valueOf(req.getHeader(k)));
//		}
	}
	
	public WebInputsManager clone() {
		try {
			return new WebInputsManager(outText, req, sc, realPathToSave);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		return null;
	}
	
	public WebInputsManager(IWebOutputText outText, HttpServletRequest req, ServletContext sc) throws Exception {
		this(outText, req, sc, "");
	}
	
	
	private void processParameter() throws Exception {

		try {
			boolean isMultipart = ServletFileUpload.isMultipartContent(req);
			
			if(isMultipart) {
				conMultipart();
				sinMultipart();
			}
			else {
				sinMultipart();
			}
		    
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	private void conMultipart() throws Exception {
		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		List /* FileItem */ items = null;
		
		try {
			items = upload.parseRequest(req);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		 
		 
		
        Iterator iter = items.iterator();
        List<String> localListaString = null;
        List<File> localListaFile = null;
        
        while (iter.hasNext()) {
	        FileItem item = (FileItem) iter.next();
	        
	        if("apelada".equals(item.getFieldName())) {
	        	logger.trace("debug");
	        }
	        
 	        if (item.isFormField()) {
 	        	localListaString = mapString.get(item.getFieldName());
 	        	if(localListaString == null) {
 	        		localListaString = new ArrayList<String>();
 	        	}
 	        	
 	        	localListaString.add(item.getString("UTF-8"));
 	        	mapString.put(item.getFieldName(),localListaString);
	        }
 	        else {			 
	        	if (item.getName() != null && !"".equals(item.getName())) {
		    		
	        		File tmp = new File(item.getName());
	        		File tosave = new File(sc.getRealPath("/".concat(realPathToSave)) ,tmp.getName());
	        		
	        		try {
	        			item.write(tosave);
		    		}
	        		catch(IOException e) {
		        		if(!tosave.getParentFile().exists()){
		        			tosave.getParentFile().mkdirs();
		        		}
		        		item.write(tosave);
	        		}
	        		
		    		localListaFile = mapFile.get(item.getFieldName());
	 	        	if(localListaFile == null) {
	 	        		localListaFile = new ArrayList<File>();
	 	        	}
	 	        	
	 	        	localListaFile.add(tosave);
	 	        	mapFile.put(item.getFieldName(),localListaFile);
		    		
		    		
			    	String modulo="";
			    	long bytes = tosave.length();
			    	
	    		}
            }
	    }
	}
	
	public void setExtraParam(String param, String value) {
		if(param != null) {
			List<String> localListaString = mapString.get(param);
	     	if(localListaString == null) {
	     		localListaString = new ArrayList<String>();
	     	}
	     	
	     	localListaString.clear();
	     	localListaString.add(value);
	     	mapString.put(param,localListaString);
		}
	}
	
	public void addExtraParam(String param, String value) {
		
		if(param != null) {
			List<String> localListaString = mapString.get(param);
	     	if(localListaString == null) {
	     		localListaString = new ArrayList<String>();
	     	}
	     	
	     	localListaString.add(value);
	     	mapString.put(param,localListaString);
		}
	}
	
	private void sinMultipart() {
			Map tmp = (Map) req.getParameterMap();
			Set set = tmp.keySet();
			String name = null;
			List<String> lista = null;

			for(Iterator it = set.iterator(); it.hasNext();) {
				name = (String) it.next();
				lista = new ArrayList<String>();

				for(String valor : (String[]) tmp.get(name)) {
					lista.add(valor);
				}

				mapString.put(name,lista);
			}

	}
	
	public String getParamString(String nomParam, String defValue) {
		if(getMapParams().get(nomParam) != null) {
			return Validar.getInstance().validarDato(getMapParams().get(nomParam).get(0),defValue);
		}
		else {
			return defValue;
		}
	}
	
	public List<String> getListParamString(String nomParam) {
		return getMapParams().get(nomParam);
	}

	public int getParamNum(String nomParam, int defValue) {
		if(getMapParams().get(nomParam) != null) {
			return Validar.getInstance().validarInt(getMapParams().get(nomParam).get(0),defValue);
		}
		else {
			return defValue;
		}	
	}
	
	public int getListParamNum(String nomParam) {
		throw new NotImplementedException();
	}	 

	public HashMap<String, List<String>> getMapParams() {
		return mapString;
	}

	public HashMap<String, List<File>> getMapFiles() {
		return mapFile;
	}
	
	public void addParam(String key, String value, boolean onlyOneValue) {
		List<String> lista = this.mapString.get(key);
		if(onlyOneValue) {
			lista = new ArrayList<String>();
		}
		
		lista.add(value);
		this.mapString.put(key, lista);
	}
	
	public void addFile(String key, File value, boolean onlyOneValue) {
		List<File> lista = this.mapFile.get(key);
		if(onlyOneValue) {
			lista = new ArrayList<File>();
		}
		
		lista.add(value);
		this.mapFile.put(key, lista);
	}
	
	public void clear() {
		mapString = new HashMap<String,List<String>>();
		mapFile = new HashMap<String,List<File>>();
	}
	
	public String toXml() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mapString", mapString);
		map.put("mapFile", mapFile);
		
 		XStream xs = new XStream();
		return xs.toXML(map);
	}
	
	public static WebInputsManager fromXml(String xml) {
 		XStream xs = new XStream();
 		Map<String,Object> map = (Map<String,Object>)xs.fromXML(xml);
 
		WebInputsManager web = new WebInputsManager( (HashMap<String,List<String>>)map.get("mapString"),
													 (HashMap<String, List<File>>)map.get("mapFile") );
		return web;	
	}

	@Override
	public boolean retJavascript(HttpServletResponse resp, String texto) {
		if(this.outText != null) {
			this.internalReturn = texto;
			return this.outText.retJavascript(resp,texto);
		}
		else {
			if(this.internalReturn != null) {
				throw new AlreadyReturnedException();
			}
			
			this.internalReturn = texto;
			return true;
		}

	}

	@Override
	public boolean retJson(HttpServletResponse resp, String json) {
		if(this.outText != null) {
			this.internalReturn = json;
			return this.outText.retJson(resp,json);
		}
		else {
			if(this.internalReturn != null) {
				throw new AlreadyReturnedException();
			}
			
			this.internalReturn = json;
			return true;
		}
	}

	@Override
	public boolean retTexto(HttpServletResponse resp, String texto) {
		
		if(this.outText != null) {
			this.internalReturn = texto;
			return this.outText.retTexto(resp,texto);
		}
		else {
			if(this.internalReturn != null) {
				throw new AlreadyReturnedException();
			}
			
			this.internalReturn = texto;
			
			return true;
		}
	}

	@Override
	public String getResponseText() {
		return this.internalReturn;
	}
}