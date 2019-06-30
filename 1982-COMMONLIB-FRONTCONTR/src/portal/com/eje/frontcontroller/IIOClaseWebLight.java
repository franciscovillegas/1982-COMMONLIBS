package portal.com.eje.frontcontroller;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ejedigital.consultor.ISenchaPage;
import cl.ejedigital.tool.misc.Cronometro;
import cl.ejedigital.web.datos.IDBConnectionManager;
import cl.ejedigital.web.frontcontroller.io.IWebInput;
import portal.com.eje.frontcontroller.ioutils.IOUtil;
import portal.com.eje.frontcontroller.ioutils.IOUtilFreemarker;
import portal.com.eje.frontcontroller.ioutils.IOUtilParam;
import portal.com.eje.frontcontroller.ioutils.IOUtilSencha;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.Usuario;

public interface IIOClaseWebLight {

	public Cronometro getCronometro();
	
	public IWebInput getWebInputsManager();
	
//	public boolean retTemplate(String templatePath);
	
	public boolean retTexto(String texto);
	
	public boolean retTexto(Boolean texto);
	
	public boolean retJson(String json);
	
	public Usuario getUsuario();	
	/**
	 * Retorna true cuando el valor sea igual a "true" o "1" en todos los casos contrarios entregará false
	 * */
	public boolean getParamBoolean(String nomParam);
	
	/**
	 * Reemplaza todos los parametros por el valor indicado
	 * */
	public void setExtraParam(String param, String value);
	
	/**
	 * Agrega un valor al parametro de nombre
	 * */
	public void addExtraParam(String param, String value);
	
	public String getParamString(String nomParam, String defValue);
	
	public Date getParamDate(String nomParam, String formatDate, String defValueInFormatDate);
	
	/**
	 * Obtiene una fecha como parámetro
	 * @since 18-05-2018
	 * @author Pancho
	 * */
	public Date getParamDate(String nomParam, String formatDate, Date defValueInFormatDate);
	
	 
	public List<String> getListParamString(String nomParam, String defValue);

	
	public int getParamNum(String nomParam, int defValue);
	
	public Integer getParamInteger(String string, Integer defaultInteger);
	
	public HttpServletRequest getReq();
	
	public HttpServletResponse getResp();
	
	public ServletContext getServletContext();

	public Map<String, List<String>> getMapParams();

	public Map<String, List<File>> getMapFiles();
	
	public File getFile(String name);
	
	public boolean retJavascript(String texto);
	
	public void printTime(Object clazz);
	
	/**
	 * @deprecated
	 * */
	public String getParamInXml();
	
	/**
	 * @deprecated
	 * */

	public void loadParamInXml(String xml);
	
	public IDBConnectionManager getConnMgr();
	
//	public boolean retTemplate(String templatePath, SimpleHash modelRoot);
	
	public MyHttpServlet getMy();
	
	public <T extends IOUtil> T getUtil(Class<T> clazz);
	
	public ISenchaPage getSenchaPage();
	
	public IOUtilSencha getUtilSencha();
	
	public IOUtilParam getUtilParam();
	
	public IOClaseWebResponse getResponse();
	
	public void setReturnSuccess(boolean success);
	
	public boolean getReturnedSuccess();

	public IOUtilFreemarker getUtilFreemarker();
 
}
