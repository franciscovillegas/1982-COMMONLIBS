package portal.com.eje.tools.security;

import java.util.List;

abstract class AbsDefaultEncrypter implements IEncrypter {

	@Override
	public List<String> decrypt(List<String> lista) {
 
		return null;
	}

	@Override
	public abstract String decrypt(String str);

	@Override
	public String encrypt(Integer numero) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public abstract String encrypt(Object value);

	@Override
	public boolean isValueEncrypted(String value) {
		// TODO Auto-generated method stub
		return false;
	}

}
