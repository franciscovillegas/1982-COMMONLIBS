package cl.ejedigital.web.frontcontroller.io;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * @since 2015-06-23
 * @author Francisco
 * */
public interface IWebInput {

	public String getParamString(String nomParam, String defValue);
	
	public List<String> getListParamString(String nomParam);

	public int getParamNum(String nomParam, int defValue);
	
	public int getListParamNum(String nomParam);

	public HashMap<String, List<String>> getMapParams();

	public HashMap<String, List<File>> getMapFiles();
	
	public void addParam(String key, String value, boolean onlyOneValue);
	
	public void addFile(String key, File value, boolean onlyOneValue);
	
	public boolean retJavascript(HttpServletResponse resp, String texto);
	
	public boolean retJson(HttpServletResponse resp, String json);
	
	public boolean retTexto(HttpServletResponse resp, String texto);
	
	public String getResponseText();
}
