package portal.com.eje.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import portal.com.eje.tools.security.Encrypter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class UrlParamEncrypter {

	public UrlParamEncrypter() {
		
	}
	 
	public String escapeParameter(Map<String, String> params) throws UnsupportedEncodingException {
		JsonObject json = new JsonObject();
		
		if(params != null) {
			Set<String> set = params.keySet();
			for(String s : set) {
				json.addProperty(s, params.get(s));
			}
		}
		
		Encrypter enc = new Encrypter();
		String ecripted = enc.encrypt(json.toString());
		return URLEncoder.encode(ecripted, "UTF-8");
	}
	
	
	public Map<String,String> unescapeParameter(String paramEncrypted) throws UnsupportedEncodingException {
		try {
			Encrypter enc = new Encrypter();
			String decripted = enc.decrypt(paramEncrypted);
			return new Gson().fromJson(decripted, Map.class);	
		}
		catch(Exception e) {
			return null;
		}
				
	}
	
}
