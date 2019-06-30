package portal.com.eje.frontcontroller.ioutils.sencha.toolsonrenderjson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.IConsultaDataStartable;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.ioutils.IOUtilParam;
import portal.com.eje.frontcontroller.ioutils.sencha.util.AbsOutComponent;
import portal.com.eje.portal.factory.Util;
import portal.com.eje.portal.organica.vo.IUnidadGenerica;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.ConsultaDataEncrypter;
import portal.com.eje.tools.maptool.MapEncrypter;
import portal.com.eje.tools.security.EncrypterDynamic;
import portal.com.eje.tools.security.IEncrypter;

/**
 * Reemplaza los campos por los valores encryptados por cada parámetro que está en la lista 
 * 
 * @author Pancho
 * @since 03-06-2019
 * */
public class ParamEncrypted extends AbsOutComponent {
	public final String PARAM_ENCRYPTED = "encrypted";
	public final String PARAM_ENCRYPTED_SECRETKEY = "encrypted_key";

	
	public static ParamEncrypted getInstance() {
		return Util.getInstance(ParamEncrypted.class);
	}
	
	private String getSecretKey(Usuario u) {
		return u.getSessionInitDate();
	}
	
	public IEncrypter getEncrypter(IOClaseWeb io) {
		String secretKey =  getSecretKey(io.getUsuario());
		return EncrypterDynamic.getInstance(secretKey);
	}
	
	public IEncrypter getEncrypter(HttpServletRequest req) {
		Usuario u = SessionMgr.rescatarUsuario(req);
		String secretKey =  getSecretKey(u);
		return EncrypterDynamic.getInstance(secretKey);
	}
	
	@Override
	public void setUtilidad(ConsultaData dataToReturn, IOClaseWeb io) {
		String[] colsToEnc = IOUtilParam.getInstance().getParamArray(io, PARAM_ENCRYPTED);
		if(colsToEnc != null && colsToEnc.length > 0) {
			ConsultaDataEncrypter.getInstance().encrypt(dataToReturn, colsToEnc, true , getEncrypter(io));

		}
		
	}

	@Override
	public void setUtilidad(IUnidadGenerica unidadGenerica, IOClaseWeb io) {
		String[] colsToEnc = IOUtilParam.getInstance().getParamArray(io, PARAM_ENCRYPTED);
		
				
		if(unidadGenerica != null &&  io != null) {
			List<Map<?,?>> lista = unidadGenerica.getRootNode().getListMap();
			for(Map<?,?> map : lista) {
				
				MapEncrypter.getInstance().encrypt(map , colsToEnc, getEncrypter(io));	

			}
			
		}
 
	}
	
	public List<String> getEncryptedColumns(ConsultaData data, HttpServletRequest req) {
		List<String> encriptados = new ArrayList<>();
		if(data != null) {
			IEncrypter encripter = getEncrypter(req);
			data.each(new IConsultaDataStartable() {
				
				@Override
				public void each(ConsultaData data) {
					while(data.next()) {
						for(String cn : data.getActualData().keySet()) {
							if(encripter.isValueEncrypted(data.getString(cn))) {
								encriptados.add(cn);
							}
						}
					}
					
				}
			});
			
			
		}
		
		return encriptados;
	}
	 
	
}
