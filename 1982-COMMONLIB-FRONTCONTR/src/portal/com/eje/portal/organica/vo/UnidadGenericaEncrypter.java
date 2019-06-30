package portal.com.eje.portal.organica.vo;

import java.util.List;
import java.util.Map.Entry;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.tools.ListUtils;
import portal.com.eje.tools.security.Encrypter;

public class UnidadGenericaEncrypter {

	public static UnidadGenericaEncrypter getInstance() {
		return Util.getInstance(UnidadGenericaEncrypter.class);
	}
 
	public void encrypt(IUnidadGenerica u, List<String> encryptFields) {
		if(u != null) {
			privateEncrypt(u.getRootNode(), encryptFields, null);	
		}
		
		
	}
	
	private void privateEncrypt(IUnidadGenerica u, List<String> encryptFields, String keyEncrypt) {
		if(encryptFields != null && u != null) {
			for( Entry<String,Object> entry : ((UnidadGenericaBase) u).map.entrySet()) {
				if(encryptFields.contains(entry.getKey())) {
					entry.setValue(Encrypter.getInstance().encrypt(String.valueOf(entry.getValue())));
				}
			}
			for(IUnidadGenerica hijo : u.getChilds()) {
				privateEncrypt(hijo, encryptFields, keyEncrypt);
			}
		}
	}
	
	 
	public void encrypt(IUnidadGenerica u, String[] encryptFields, String keyEncrypt) {
		privateEncrypt(u, ListUtils.getList(encryptFields), keyEncrypt);
		
	}
}
