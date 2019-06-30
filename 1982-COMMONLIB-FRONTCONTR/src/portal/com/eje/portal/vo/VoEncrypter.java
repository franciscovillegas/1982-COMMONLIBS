package portal.com.eje.portal.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.vo.iface.IVoValueChange;
import portal.com.eje.portal.vo.vo.Vo;
import portal.com.eje.tools.ClaseGenerica;
import portal.com.eje.tools.security.Encrypter;

public class VoEncrypter {
	private Encrypter enc;
	private ClaseGenerica cg;
	
	public VoEncrypter() {
		enc = Util.getInstance(Encrypter.class);
		cg  = Util.getInstance(ClaseGenerica.class);
	}
	
	public static VoEncrypter getInstance() {
		return Util.getInstance(VoEncrypter.class);
	}
	
 
	
	/**
	 * Encrypta los fields de un Vo
	 * 
	 * @author Pancho
	 * @since 15-06-2018
	 * 
	 * */
	public <T> T encrypt(Vo vo, Class<T> clazzNewObject, List<String> fieldsWithNoSet) {
		T newObject = null;
		
		if(vo != null && clazzNewObject != null) {
			Collection<Vo> colls = new ArrayList<Vo>();
			//T t = cg.getNewFromClass(clazzNewObject);
			//vo.copyTo((Vo) t);
			colls.add(vo);
			
			Collection<T> colls2 = encrypt(colls, clazzNewObject, fieldsWithNoSet);
			for(T t2 : colls2) {
				return t2;
			}
			
		}
		
		return null;
	}
	
	public Vo encrypt(Vo vo, final List<String> fieldsToEncrypt) {
		if(vo != null) {
			//List<String> fields = VoTool.getInstance().getSetsFieldsWithNoParameters(vo.getClass());
			
			VoTool.getInstance().copy(vo, vo,  new IVoValueChange() {
				
				@Override
				public Object changeAfterGet(String fieldName, Object get) {
					return get;
				}
				
				@Override
				public Object changeBeforeSetToCopy(String fieldName, Object before) {
					if(before instanceof String && fieldsToEncrypt.contains(fieldName)) {
						return enc.encrypt((String) before);
					}
					
					return before;
				}


			});
			 
		}
		return vo;
	}
	
	/**
	 * Encrypta los fields de un Vo
	 * 
	 * @author Pancho
	 * @since 15-06-2018
	 * 
	 * */
	public <T> Collection<T> encrypt(Collection<? extends Vo> vos, Class<T> clazzNewObject, List<String> fieldsWithNoSet) {

		Collection<T> newVos = null;
		if(vos!=null){
			
			if(fieldsWithNoSet==null) {
				fieldsWithNoSet = new ArrayList<String>();
				fieldsWithNoSet = VoTool.getInstance().getSetsFieldsWithNoParameters(clazzNewObject);
				
			}
			
			newVos = privateEncrypt(vos, clazzNewObject, fieldsWithNoSet);
		}
		
		return newVos;
	}
	
	/**
	 * Encrypta los fields de un Vo
	 * 
	 * @author Pancho
	 * @since 15-06-2018
	 * 
	 * */
	public <T> Collection<T> privateEncrypt(Collection<? extends Vo> vos, Class<T> clazzNewObject, final List<String> fieldsWithNoSet) {

		Collection<T> newVos = null;
		if(vos!=null){
			
			newVos = VoTool.getInstance().copy(vos, clazzNewObject, new IVoValueChange() {
				
				@Override
				public Object changeAfterGet(String fieldName, Object get) {
					return get;
				}
				
				@Override
				public Object changeBeforeSetToCopy(String fieldName, Object before) {
					if(before instanceof String && fieldsWithNoSet.contains(fieldName)) {
						return enc.encrypt((String) before);
					}
					
					return before;
				}


			});

		}
		
		return newVos;
	}
	
	 
	/**
	 * Encrypta los fields de un Vo
	 * 
	 * @author Pancho
	 * @since 15-06-2018
	 * 
	 * */
	public <T> T decrypt(Vo vo, Class<T> clazzNewObject, List<String> fieldsWithNoSet) {
		T newObject = null;
		
		if(vo != null && clazzNewObject != null) {
			Collection<Vo> colls = new ArrayList<Vo>();
			
			//T t = cg.getNewFromClass(clazzNewObject);
			//vo.copyTo((Vo) t);
			colls.add(vo);
			
			Collection<T> colls2 = decrypt(colls, clazzNewObject, fieldsWithNoSet);
			for(T t2 : colls2) {
				return t2;
			}
			
		}
		
		return null;
	}
	
	/**
	 * Desencrypta los fields de un Vo
	 * 
	 * @author Pancho
	 * @since 15-06-2018
	 * 
	 * */
	public <T> Collection<T> decrypt(Collection<? extends Vo> vos, Class<T> clazzNewObject, List<String> fieldsWithNoSet ) {

		Collection<T> newVos = null;
		if(vos!=null){
			
			if(fieldsWithNoSet==null) {
				fieldsWithNoSet = new ArrayList<String>();
				fieldsWithNoSet = VoTool.getInstance().getSetsFieldsWithNoParameters(clazzNewObject);
				
			}
			
			newVos = privateDecrypt(vos, clazzNewObject, fieldsWithNoSet);

		}
		
		return newVos;
	}
	
	/**
	 * Desencrypta los fields de un Vo
	 * 
	 * @author Pancho
	 * @since 15-06-2018
	 * 
	 * */
	
	public <T> Collection<T> privateDecrypt(Collection<? extends Vo> vos, Class<T> clazzNewObject, final List<String> fieldsWithNoSet ) {
		Collection<T> newVos = null;
		if(vos!=null){
			
			newVos = VoTool.getInstance().copy(vos, clazzNewObject, new IVoValueChange() {
				
				@Override
				public Object changeAfterGet(String fieldName, Object get) {
					if(get instanceof String && fieldsWithNoSet.contains(fieldName)) {
						return enc.decrypt((String) get);
					}
					
					return get;
				}
				
				@Override
				public Object changeBeforeSetToCopy(String fieldName, Object after) {
					return after;
				}


			});		

		}
		
		return newVos;
	}

	public Vo decrypt(Vo vo, final List<String> fieldsToDecrypt) {
		if(vo != null) {
			//List<String> fields = VoTool.getInstance().getSetsFieldsWithNoParameters(vo.getClass());
			
			VoTool.getInstance().copy(vo, vo,  new IVoValueChange() {
				
				@Override
				public Object changeAfterGet(String fieldName, Object get) {
					if(get instanceof String && fieldsToDecrypt.contains(fieldName)) {
						return enc.decrypt((String) get);
					}
					
					return get;
				}
				
				@Override
				public Object changeBeforeSetToCopy(String fieldName, Object after) {
					return after;
				}


			});
			 
		}
		return vo;
	}

	public <T> Collection<T> encrypt(Collection<? extends Vo> vos, Class<T> clase, String[] campos) {
		Collection<T> retorno = null;
		
		if(vos != null && clase != null && campos != null) {
			
			List<String> listaCampos = new ArrayList<String>();
			for(String c : campos) {
				listaCampos.add(c);
			}
			
			retorno =  encrypt(vos, clase, listaCampos);
		}
		
		return retorno;
	}


}
