package portal.com.eje.frontcontroller.ioutils.sencha.toolsonrenderjson;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.ioutils.IOUtilParam;
import portal.com.eje.frontcontroller.ioutils.sencha.iface.IOutComponent;
import portal.com.eje.frontcontroller.ioutils.sencha.util.AbsOutComponent;
import portal.com.eje.tools.ConsultaDataEncrypter;
import portal.com.eje.tools.security.EncrypterDynamic;
import portal.com.eje.tools.security.IEncrypter;

/**
 * Crea campos encryptados por cada parámetro que está en la lista 
 * 
 * @author Pancho
 * @since 03-06-2019
 * */
public class ParamEncrypt extends AbsOutComponent {
	private final String paramName = "encrypt";
	
	
	
	@Override
	public void setUtilidad(ConsultaData dataToReturn, IOClaseWeb io) {
		String[] colsToEnc = IOUtilParam.getInstance().getParamArray(io, paramName);
		ConsultaDataEncrypter.getInstance().encrypt(dataToReturn, colsToEnc, false, ParamEncrypted.getInstance().getEncrypter(io) ); 
		
	}

}
