package portal.com.eje.tools.maptool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cl.ejedigital.consultor.DataFields;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.tools.ListUtils;
import portal.com.eje.tools.security.IEncrypter;

public class MapEncrypter {

	
	public static MapEncrypter getInstance() {
		return Util.getInstance(MapEncrypter.class);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void encrypt(Map map, Object[] toEncrypt, IEncrypter encrypter) {
		encrypt(map, ListUtils.getList(toEncrypt), encrypter);
	}

	public void encrypt(Map<Object,Object> map, List<Object> toEncrypt, IEncrypter encrypter) {
		if(map != null ) {
			List<String>  toAdd = new ArrayList<String>();
			
 			for(Entry<Object, Object> entry : map.entrySet()) {
				if(toEncrypt.contains(entry.getKey())) {
					entry.setValue(encrypter.encrypt(entry.getValue()));
					
					if(!toAdd.contains(entry.getKey())) {
						 toAdd.add( (String) entry.getKey());
					}
					//
				}
			}
 		 
		}
	}

//	protected String getNombreColumnaDefEncrypted( String nombreColumnaOriginal) {
//		String nColumna = new StringBuilder().append(nombreColumnaOriginal).append(SUFIJO_COLUMNENCRYPTED).toString();
//		return nColumna;
//	}
	
//	protected void addEncryptedColumnDef(Map<Object,Object> map, String nombreColumnaOriginal) {
//
//		if (map != null) {
//			String nColumna = getNombreColumnaDefEncrypted(nombreColumnaOriginal);
//			map.put(nColumna, Boolean.TRUE);
//		}
//	}
//	
//	protected void addEncryptedColumnDef(DataFields map, String nombreColumnaOriginal) {
//
//		if (map != null) {
//			String nColumna = getNombreColumnaDefEncrypted(nombreColumnaOriginal);
//			map.put(nColumna, true);
//		}
//	}
	
	public List<String> getEncryptedColumns(Map<String,Object> map, IEncrypter encripter) {
		List<String> lista = new ArrayList<>();
		if(map != null) {
			for(String originalName : map.keySet()) {
				if(encripter.isValueEncrypted((String) map.get(originalName))) {
					lista.add(originalName);
				}
			}
		}
		
		return lista;
	}
//
//	@SuppressWarnings("rawtypes")
//	protected boolean isEncrypted(Map map, String columnName) {
//		boolean encrypted = false;
//		if (map != null) {
//			String nColumna = getNombreColumnaDefEncrypted(columnName);
//			encrypted = map.containsKey(nColumna);
//		}
//
//		return encrypted;
//	}
}
