package portal.com.eje.frontcontroller;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.consultor.ISenchaPage;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.web.datos.IDBConnectionManager;
import cl.ejedigital.web.datos.error.DuplicateKeyException;
import cl.ejedigital.web.frontcontroller.error.AlreadyReturnedException;
import cl.ejedigital.web.frontcontroller.io.IWebInput;
import cl.ejedigital.web.frontcontroller.io.WebInputsManager;
import cl.ejedigital.web.frontcontroller.io.WebInputsManagerSimulator;
import portal.com.eje.frontcontroller.ioutils.IOUtil;
import portal.com.eje.frontcontroller.ioutils.IOUtilFile;
import portal.com.eje.frontcontroller.ioutils.IOUtilFreemarker;
import portal.com.eje.frontcontroller.ioutils.IOUtilParam;
import portal.com.eje.frontcontroller.ioutils.IOUtilSencha;
import portal.com.eje.frontcontroller.ioutils.IOUtilXml;
import portal.com.eje.portal.EModulos;
import portal.com.eje.portal.factory.SingleFactory;
import portal.com.eje.portal.factory.SingleFactoryType;
import portal.com.eje.portal.pdf.HttpFlow;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;

public class IOClaseWebLight implements IIOClaseWebLight {
	protected MyHttpServlet myHttpServlet;
	protected HttpServletRequest req;
	protected HttpServletResponse resp;
	protected WebInputsManager				in;
	protected Usuario							user;
	protected Cronometro cro;
	protected EModulos modulo = EModulos.getThisModulo();
	protected Boolean returnSuccess;
	
	public IOClaseWebLight(MyHttpServlet myHttpServlet,HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;
		this.myHttpServlet = myHttpServlet;
		this.cro = new Cronometro();
		cro.start();

		
		try {
			if(req != null && this.myHttpServlet != null ) {
				this.in =  new WebInputsManager(this.myHttpServlet, req,this.myHttpServlet.getServletContext(),"/temporal/filesUnicos");
			}
			else {
				this.in = new WebInputsManagerSimulator("/temporal/filesUnicos");
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Cronometro getCronometro() {
		return this.cro;
	}
	
	public IWebInput getWebInputsManager() {
		return this.in;
	}
	
	public boolean retFlow(HttpFlow texto) {
		 return in.retTexto(resp, texto != null ? texto.getFlow(): null);
	}
	
	public boolean retTexto(String texto) {
		return in.retTexto(resp, texto);
	}
	
	public boolean retJson(String json) {
		 return in.retJson(resp, json);
	}
	
	public boolean retTexto(Boolean texto) {
		 return retTexto(String.valueOf(texto));
	}


	public void retTexto(Throwable error) {
		StringBuilder str = new StringBuilder();
		for(StackTraceElement e:  error.getStackTrace()) {
			str.append(e.toString()).append("\n");
		}
		
		retTexto(str.toString());
		
		resp.setStatus(500);
	}
	
	public Usuario getUsuario() {
		if(user == null) {
			user = SessionMgr.rescatarUsuario(req);
		}

		return user;
	}
	
	/**
	 * Retorna true cuando el valor sea igual a "true" o "1" en todos los casos contrarios entregará false
	 * */
	public boolean getParamBoolean(String nomParam) {
		if(in != null) {
			String val = in.getParamString(nomParam,null);
			
			if(val != null) {
				if("true".equals(val.toLowerCase())) {
					return true;
				}
				else if("false".equals(val.toLowerCase())) {
					return false;
				}
				else if("1".equals(val.toLowerCase())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Reemplaza todos los parametros por el valor indicado
	 * */
	public void setExtraParam(String param, String value) {
		in.setExtraParam(param, value);
	}
	
	/**
	 * Agrega un valor al parametro de nombre
	 * @revision 25-05-2018 Pancho
	 * */
	public void addExtraParam(String param, String value) {
		if(getMapParams().get(param) == null) {
			List<String> valores = new ArrayList<String>();
			valores.add(value);
			getMapParams().put(param, valores);
		}
		else {
			getMapParams().get(param).clear();
			getMapParams().get(param).add(value);
		}
	}
	

	
	public String getParamString(String nomParam, String defValue) {
		if(in != null) {
			return in.getParamString(nomParam,defValue);	
		}
		else {
			return defValue;	
		}
		
	}
	
	/**
	 * Mal definida la recepción de parámetros 
	 * @deprecated
	 * */
	public Date getParamDate(String nomParam, String formatDate, String defValueInFormatDate) {
		String strFec = null;
		try {
			
			strFec = in.getParamString(nomParam, Formatear.getInstance().toAnotherDate("29990101","yyyyMMdd",formatDate) );
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			return Formatear.getInstance().toDate(strFec,formatDate);
		}
		catch (Exception e) {
			return Formatear.getInstance().toDate(defValueInFormatDate,formatDate);
		}
	}
	
	/**
	 * Obtiene una fecha como parámetro
	 * @since 18-05-2018
	 * @author Pancho
	 * */
	public Date getParamDate(String nomParam, String formatDate, Date defValueInFormatDate) {
		String strFec = null;
		try {
			if(in.getParamString(nomParam, null) == null) {
				return defValueInFormatDate;
			}
					
			strFec = in.getParamString(nomParam, Formatear.getInstance().toAnotherDate("29990101","yyyyMMdd",formatDate) );
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			strFec = strFec.substring(0, formatDate.length());
			return Formatear.getInstance().toDate(strFec,formatDate);
		}
		catch (Exception e) {
			return defValueInFormatDate;
		}
	}
	
	/**
	 * Funciona cuando el dato vienen varios valores con el mismo nombre
	 * 
	 * parametro=1,parametro=2,parametro=3,parametro=4
	 * @author Pancho
	 * */
	public List<String> getListParamString(String nomParam, String defValue) {
		return in.getListParamString(nomParam);
	}

	
	public int getParamNum(String nomParam, int defValue) {
		return in.getParamNum(nomParam,defValue);
	}
	
	public Integer getParamInteger(String string, Integer defaultInteger) {
		int cte = -67452312;
		int i = in.getParamNum(string, cte);
		
		if(i != cte) {
			return new Integer(i);
		}
		
		return defaultInteger;
	}
	
	public HttpServletRequest getReq() {
		return req;
	}
	
	public HttpServletResponse getResp() {
		return resp;
	}
	
	public ServletContext getServletContext() {
		return myHttpServlet.getServletContext();
	}

	public HashMap<String, List<String>> getMapParams() {
		return in.getMapParams();
	}
	
	public HashMap<String, String> getMapParamsSimple() throws DuplicateKeyException {
		HashMap<String, String> map = new HashMap<String, String>();
		
		for(Entry<String, List<String>> entry : getMapParams().entrySet()) {
			if( map.containsKey(entry.getKey()) || entry.getValue().size() > 1) {
				throw new DuplicateKeyException();
			}
			
			map.put(entry.getKey(), (entry.getValue().size() > 0 ? entry.getValue().get(0) : null));
		}
		
		return map;
	}

	public HashMap<String, List<File>> getMapFiles() {
		return in.getMapFiles();
	}
	
	public File getFile(String name) {
		if(in.getMapFiles() != null) {
			List<File> l = in.getMapFiles().get(name);
			
			if(l != null && l.size() > 0) {
				return l.get(0);
			}
			
		}
		return null;
	}
	
	@Override
	public boolean retJavascript(String texto) {
		return in.retJavascript(resp, texto);
		  
	}
	
	/**
	 * @revision 25-05-2018 Pancho
	 * */
	@Override
	public void printTime(Object clazz) {
		if(clazz != null) {
			System.out.println(clazz.toString() + " " + cro.getTimeHHMMSS_milli());	
		}
		else {
			System.out.println(cro.getTimeHHMMSS_milli());
		}
	}
	
	/**
	 * @deprecated
	 * */
	@Override
	public String getParamInXml() {
		return in.toXml();
	}
	
	/**
	 * @deprecated
	 * */
	@Override
	public void loadParamInXml(String xml) {
		this.in = WebInputsManager.fromXml(xml);	
	}
	
	
	/**
	 * @revision 25-05-2018 Pancho
	 * */
	@Override
	public IDBConnectionManager getConnMgr() {
		return myHttpServlet.getConnMgr();
	}


	@Override
	public MyHttpServlet getMy() {
		return this.myHttpServlet;
	}
	
	/**
	 * Se debe llamar a los utils directamente
	 * @deprecated
	 * */
	@Override
	public <T extends IOUtil> T getUtil(Class<T> clazz) {
		return SingleFactory.getFactory(SingleFactoryType.UTIL).getInstance(clazz);
	}
	

	@Override
	public IOUtilSencha getUtilSencha() {
		return IOUtilSencha.getInstance();
	}
	
	@Override
	public IOUtilFreemarker getUtilFreemarker() {
		return IOUtilFreemarker.getInstance();
	}
	
	@Override
	public IOUtilParam getUtilParam() {
		return IOUtilParam.getInstance();
	}

	public IOUtilFile getUtilFile() {
		return IOUtilFile.getInstance();
	}
	
	public IOUtilXml getUtilXml() {
		return IOUtilXml.getIntance();
	}
 

	@Override
	public ISenchaPage getSenchaPage() {
		return IOUtilSencha.getInstance().getSenchaPage(this);
	}
	
	@Override
	public IOClaseWebResponse getResponse() {
		if(in.getResponseText() != null) {
			return IOClaseWebTool.getInstance().fromText(in.getResponseText());	
		}
		
		return new IOClaseWebResponse();
	}
	
	@Override
	public void setReturnSuccess(boolean success) {
		if(this.returnSuccess != null) {
			throw new AlreadyReturnedException();
		}
		
		this.returnSuccess = success;
	}
	
	@Override
	public boolean getReturnedSuccess() {
		return returnSuccess != null && returnSuccess == true;
	}
}
